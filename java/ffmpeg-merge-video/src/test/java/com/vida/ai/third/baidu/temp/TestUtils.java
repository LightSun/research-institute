package com.vida.ai.third.baidu.temp;

import com.heaven7.java.base.util.Logger;
import com.heaven7.java.visitor.FireVisitor;
import com.heaven7.java.visitor.PredicateVisitor;
import com.heaven7.java.visitor.ResultVisitor;
import com.heaven7.java.visitor.collection.VisitServices;
import com.heaven7.utils.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.vida.ai.third.baidu.temp.BatchUpdateSample.findImagePath;

/**
 * @author heaven7
 */
public class TestUtils {
    private static final String TAG = "TestUtils";

    //48408. real is 48385. failed is 11256. empty is 10584
    public static void main(String[] args) {
       // testImageCount();
       // testEmptyCount();
       // testFailedCount();
        testLargeImageCount();
    }

    private static void testLargeImageCount() {

    }

    private static void testFailedCount() {
        String dir = "E:\\test\\batch_upload";
        List<BatchUpdateSample.Task> all = new ArrayList<>();
        VisitServices.from(FileUtils.getFiles(new File(dir), "txt")).filter(new PredicateVisitor<String>() {
            @Override
            public Boolean visit(String s, Object param) {
                return s.endsWith("_failed.txt");
            }
        }).map(new ResultVisitor<String, List<BatchUpdateSample.Task>>() {
            @Override
            public List<BatchUpdateSample.Task> visit(String s, Object param) {
                return BatchUpdateSample.readTasks(s);
            }
        }).fire(new FireVisitor<List<BatchUpdateSample.Task>>() {
            @Override
            public Boolean visit(List<BatchUpdateSample.Task> tasks, Object param) {
                all.addAll(tasks);
                return null;
            }
        });
        System.out.println(all.size());
    }

    private static void testEmptyCount() {
        String dir = "E:\\test\\batch_upload";
        List<BatchUpdateSample.Task> all = new ArrayList<>();
        VisitServices.from(FileUtils.getFiles(new File(dir), "txt")).filter(new PredicateVisitor<String>() {
            @Override
            public Boolean visit(String s, Object param) {
                return s.endsWith("_empty.txt");
            }
        }).map(new ResultVisitor<String, List<BatchUpdateSample.Task>>() {
            @Override
            public List<BatchUpdateSample.Task> visit(String s, Object param) {
                return BatchUpdateSample.readTasks(s);
            }
        }).fire(new FireVisitor<List<BatchUpdateSample.Task>>() {
            @Override
            public Boolean visit(List<BatchUpdateSample.Task> tasks, Object param) {
                all.addAll(tasks);
                return null;
            }
        });
        System.out.println(all.size());
    }

    private static void testImageCount() {
       // String dir = "E:\\test\\batch_upload\\pexels";
        String dir = "F:\\work\\素材";
        List<String> files = FileUtils.getFiles(new File(dir), "json");
        List<BatchUpdateSample.Task> tasks = VisitServices.from(files).map(new ResultVisitor<String, BatchUpdateSample.Task>() {
            @Override
            public BatchUpdateSample.Task visit(String s, Object param) {
                //replace empty
                final String oldJson = s;
                String fileName = FileUtils.getFileName(s);
                String dir = FileUtils.getFileDir(s, 1, true);
                String imagePath = findImagePath(fileName, dir);
                if(imagePath == null){
                    //replace white char for file name
                    fileName = fileName.replace(" ", "%20");
                    imagePath = findImagePath(fileName, dir);
                    if(imagePath == null){
                        //can't find at last.
                        Logger.w(TAG, "detactAll", "can't find proper image for json = " + s);
                        return null;
                    }
                }

                BatchUpdateSample.Task task = new BatchUpdateSample.Task();
                task.jsonPath = s;
                task.imagePath = imagePath;
                return task;
            }
        }).getAsList();
        System.out.println(tasks.size());
    }
}
