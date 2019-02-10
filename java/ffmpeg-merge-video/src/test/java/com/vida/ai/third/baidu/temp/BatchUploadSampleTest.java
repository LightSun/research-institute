package com.vida.ai.third.baidu.temp;

import com.heaven7.java.base.util.Logger;
import com.heaven7.java.base.util.ResourceLoader;
import com.heaven7.java.visitor.FireIndexedVisitor;
import com.heaven7.java.visitor.FireVisitor;
import com.heaven7.java.visitor.PredicateVisitor;
import com.heaven7.java.visitor.ResultVisitor;
import com.heaven7.java.visitor.collection.KeyValuePair;
import com.heaven7.java.visitor.collection.VisitServices;
import com.heaven7.utils.FileUtils;
import okhttp3.Call;
import okhttp3.Response;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.Semaphore;

/**
 * @author heaven7
 */
public class BatchUploadSampleTest {
    private static final String TAG = "BatchSampleTest";
    private static final BatchUpdateSample SAMPLE = new BatchUpdateSample();

    //3555,
    public static void main(String[] args) {
        //test1();
        // testAll2();
        //testReload();
       // testEmptyCount();
       // testUploadEmpty();
       // uploadAllEmpty();
        //uploadAllFailed();
        //testUploadFailedBug();
        testSingleEmpty();
        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public static void uploadAllFailed(){
        int datasetId = 25245;
        String logDir = "E:\\test\\log2";
        String dir = "E:\\test\\batch_upload";
        final Semaphore semaphore = new Semaphore(1);
        VisitServices.from(FileUtils.getFiles(new File(dir), "txt")).filter(new PredicateVisitor<String>() {
            @Override
            public Boolean visit(String s, Object param) {
                return s.endsWith("_failed.txt");
            }
        }).map(new ResultVisitor<String, KeyValuePair<String, List<BatchUpdateSample.Task>>>() {
            @Override
            public KeyValuePair<String, List<BatchUpdateSample.Task>> visit(String s, Object param) {
                return KeyValuePair.create(s, BatchUpdateSample.readTasks(s));
            }
        }).fire(new FireVisitor<KeyValuePair<String, List<BatchUpdateSample.Task>>>() {
            @Override
            public Boolean visit(KeyValuePair<String, List<BatchUpdateSample.Task>> pair, Object param) {
                if(pair.getValue().isEmpty()){
                    return false;
                }
                String prefix = FileUtils.getFileName(pair.getKey()).split("_")[0];
                try {
                    semaphore.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                fireTasks(datasetId, logDir, prefix, pair.getValue(), new Runnable() {
                    @Override
                    public void run() {
                        semaphore.release();
                    }
                });
                return null;
            }
        });
    }
    public static void testSingleEmpty(){
        int datasetId = 25378;
        String logDir = "E:\\test\\test_bug";
        final String failedFile = "E:\\test\\temp\\building2_empty.txt";
        List<BatchUpdateSample.Task> list = BatchUpdateSample.readTasks(failedFile);
        final Semaphore semaphore = new Semaphore(1);
        VisitServices.from(list).group(100, false)
                .fireWithIndex(new FireIndexedVisitor<List<BatchUpdateSample.Task>>() {
                    @Override
                    public Void visit(Object param, List<BatchUpdateSample.Task> tasks, int index, int size) {
                        if(tasks.isEmpty()){
                            return null;
                        }
                        String prefix = FileUtils.getFileName(failedFile).split("_")[0];
                        try {
                            semaphore.acquire();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        fireTasks(datasetId, logDir, prefix + "@" + index, tasks, null,  new Runnable() {
                            @Override
                            public void run() {
                                System.out.println("=================== end >>> " + tasks.get(tasks.size() - 1).imagePath);
                                semaphore.release();
                            }
                        });
                        return null;
                    }
        });
    }

    public static void uploadAllEmpty(){
        int datasetId = 25378;
        String logDir = "E:\\test\\log3";
        //String dir = "E:\\test\\batch_upload";
        String dir = "E:\\test\\temp";
        final Semaphore semaphore = new Semaphore(1);
        VisitServices.from(FileUtils.getFiles(new File(dir), "txt")).filter(new PredicateVisitor<String>() {
            @Override
            public Boolean visit(String s, Object param) {
                return s.endsWith("_empty.txt");
            }
        }).map(new ResultVisitor<String, KeyValuePair<String,List<BatchUpdateSample.Task>>>() {
            @Override
            public KeyValuePair<String,List<BatchUpdateSample.Task>> visit(String s, Object param) {
                List<BatchUpdateSample.Task> tasks = BatchUpdateSample.readTasks(s);
                if(tasks.isEmpty()){
                    return null;
                }
                return KeyValuePair.create(s, tasks);
            }
        }).fire(new FireVisitor<KeyValuePair<String, List<BatchUpdateSample.Task>>>() {
            @Override
            public Boolean visit(KeyValuePair<String, List<BatchUpdateSample.Task>> pair, Object param) {
                String prefix = FileUtils.getFileName(pair.getKey()).split("_")[0];
                try {
                    semaphore.acquire();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                fireTasks(datasetId, logDir, prefix, pair.getValue(), null,new Runnable() {
                    @Override
                    public void run() {
                        semaphore.release();
                    }
                });
                return null;
            }
        });
    }

    private static void testEmptyCount(){
        String file = "E:\\test\\batch_upload\\empty1.txt";
        List<String> lines = ResourceLoader.getDefault().loadFileAsStringLines(null, file);
        System.out.println("empty 1: count = " + lines.size());

        file = "E:\\test\\batch_upload\\empty2.txt";
        lines = ResourceLoader.getDefault().loadFileAsStringLines(null, file);
        System.out.println("empty 2: count = " + lines.size());
    }

    private static void fireTasks(int datasetId, String logDir, String prefix,
                                  List<BatchUpdateSample.Task> all, Runnable end) {
        fireTasks(datasetId, logDir, prefix, all, new UploadTaskVisitor.Callback(){},
                end);
    }
    private static void fireTasks(int datasetId, String logDir, String prefix,
                                  List<BatchUpdateSample.Task> all,
                                  UploadTaskVisitor.Callback callback, Runnable end) {

        String empty = logDir + File.separator + prefix + "_failed_lefts.txt";
        String failed = logDir + File.separator + prefix + "_failed_faileds.txt";
        String toolarge = logDir + File.separator + prefix + "_failed_toolarge.txt";
        UploadTaskVisitor visitor = new UploadTaskVisitor.Builder()
                .setCount(all.size())
                .setDatasetId(datasetId)
                .setEmptyLogFile(empty)
                .setFailedLogFile(failed)
                .setTooLargetFile(toolarge)
                .setParent(SAMPLE)
                .setTasksTag("fireTasks__" + prefix)
                .setCallback(callback)
                .setEndTask(end)
                .build();

        VisitServices.from(all).fire(visitor);
    }

    private static void testReload() {
        new BatchUpdateSample().reUpload();
    }

    private static void testAll2(){
        int dataSetId = 25245;
        BatchUpdateSample sample = new BatchUpdateSample();
        String logDir = "E:\\test\\batch_upload";
        //sign
        //String parentDir = "E:\\test\\batch_upload\\pexels";
        String parentDir = "F:\\work\\素材";
       /* String[] args = {
            "sign", "sky", "street", "taking photo", "train", "train2", "way",
                "背影", "城市","岛屿","道路","雕塑","海洋","黄昏","火车",

                "街道","潜水","天空","庭院","肖像","游乐园","招牌","自然风景"
        };*/
        /*String[] args = {
                "back", "building2", "car", "cat", "city or swteet", "diving", "dog",
                "food", "foot","foot2","hand","hand2","indoor","island","landscape",
                "luggage","ocean","other","other2","patio","Porthole","portrait"
        };*/
        String[] args = {
                "backup1",
               /* "backup2","backup3","backup4","backup5","backup6",
                "backup7","backup8","backup9","backup10","backup11","backup12",
                "backup13","backup14","backup15","backup16",
                "back","building2", "car","cat","city or swteet","diving",
                "dog", "food", "foot","foot2","hand","hand2","indoor",
                "island", "landscape", "luggage","ocean","other","other2","patio",
                "Porthole", "portrait", "sign","sky","street","taking photo",
                "train", "train2","way"*/
        };
        List<String> tasks = VisitServices.from(args).map(new ResultVisitor<String, String>() {
            @Override
            public String visit(String s, Object param) {
                return parentDir + File.separator + s;
            }
        }).getAsList();
        final Semaphore semaphore = new Semaphore(1);
        VisitServices.from(tasks).fireWithIndex(new FireIndexedVisitor<String>() {
            @Override
            public Void visit(Object param, String dir, int index, int size) {
                String empty = logDir + File.separator + args[index] + "_empty.txt";
                String failed = logDir + File.separator + args[index] + "_failed.txt";
                try {
                    semaphore.acquire();
                    sample.detectAll(dataSetId, dir, empty, failed, new Runnable() {
                        @Override
                        public void run() {
                            semaphore.release();
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }
        });
    }

    private static void testAll() {
        int dataSetId = 25245;
        /*String dir1 = "E:\\test\\batch_upload\\baggage";
        String dir2 = "E:\\test\\batch_upload\\high-speed railway";
        String empty1 = "E:\\test\\batch_upload\\empty1.txt";
        String empty2 = "E:\\test\\batch_upload\\empty2.txt";
        String failed1 = "E:\\test\\batch_upload\\failed1.txt";
        String failed2 = "E:\\test\\batch_upload\\failed2.txt";
        new BatchUpdateSample().detectAll(dataSetId, dir1, empty1, failed1);
        new BatchUpdateSample().detectAll(dataSetId, dir2, empty2, failed2);*/

        String dir3 = "E:\\test\\batch_upload\\pexels";
        String empty3 = "E:\\test\\batch_upload\\empty3.txt";
        String failed3 = "E:\\test\\batch_upload\\failed3.txt";
        new BatchUpdateSample().detectAll(dataSetId, dir3, empty3, failed3);
    }

    private static void testUploadEmpty(){
        int datasetId = 25378;
        String file1 = "E:\\test\\batch_upload\\empty1.txt";
        String file2 = "E:\\test\\batch_upload\\empty2.txt";
        List<BatchUpdateSample.Task> tasks1 = BatchUpdateSample.readTasks(file1);
        List<BatchUpdateSample.Task> tasks2 = BatchUpdateSample.readTasks(file2);
        tasks1.addAll(tasks2);
        BatchUpdateSample sample = new BatchUpdateSample();

        String empty = "E:\\test\\batch_upload\\empty1_2_lefts.txt";
        String failed = "E:\\test\\batch_upload\\empty1_2_faileds.txt";
        UploadTaskVisitor visitor = new UploadTaskVisitor.Builder()
                .setCount(tasks1.size())
                .setDatasetId(datasetId)
                .setEmptyLogFile(empty)
                .setFailedLogFile(failed)
                .setParent(sample)
                .setTasksTag("testUploadEmpty")
                .build();

        VisitServices.from(tasks1).fire(visitor);
    }

    public static void test1() {
        String dir = "D:\\Users\\WeChat Files\\studyheaven7\\Files\\归档(1)";
        String imagePath = dir + File.separator + "404366496_2ddef48404_b.jpg";
        String jsonPath = dir + File.separator + "404366496_2ddef48404_b.json";
        BatchUpdateSample sample = new BatchUpdateSample();
        try {
            sample.detectObject(imagePath, jsonPath, new BatchUpdateSample.Callback() {
                @Override
                public void onEmptyLabel(String image, String json) {
                    Logger.w(TAG, "onEmptyLabel", "empty json = " + json);
                }
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        Logger.d(TAG, "onResponse", "" + response.body().string());
                    }finally {
                        response.close();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
