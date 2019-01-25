package com.vida.ai.third.baidu.temp;

import com.heaven7.java.base.util.Logger;
import com.heaven7.java.base.util.ResourceLoader;
import com.heaven7.java.visitor.collection.VisitServices;
import okhttp3.Call;
import okhttp3.Response;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author heaven7
 */
public class BatchUploadSampleTest {
    private static final String TAG = "BatchSampleTest";

    //3555,
    public static void main(String[] args) {
        //test1();
        //testAll();
        //testReload();
       // testEmptyCount();
        testUploadEmpty();
        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void testEmptyCount(){
        String file = "E:\\test\\batch_upload\\empty1.txt";
        List<String> lines = ResourceLoader.getDefault().loadFileAsStringLines(null, file);
        System.out.println("empty 1: count = " + lines.size());

        file = "E:\\test\\batch_upload\\empty2.txt";
        lines = ResourceLoader.getDefault().loadFileAsStringLines(null, file);
        System.out.println("empty 2: count = " + lines.size());
    }

    private static void testReload() {
        new BatchUpdateSample().reUpload();
    }

    private static void testAll() {
        int dataSetId = 25245;
        String dir1 = "E:\\test\\batch_upload\\baggage";
        String dir2 = "E:\\test\\batch_upload\\high-speed railway";
        String empty1 = "E:\\test\\batch_upload\\empty1.txt";
        String empty2 = "E:\\test\\batch_upload\\empty2.txt";
        String failed1 = "E:\\test\\batch_upload\\failed1.txt";
        String failed2 = "E:\\test\\batch_upload\\failed2.txt";
        new BatchUpdateSample().detectAll(dataSetId, dir1, empty1, failed1);
        new BatchUpdateSample().detectAll(dataSetId, dir2, empty2, failed2);
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
