package com.vida.ai.third.baidu.temp;

import com.heaven7.java.base.util.Logger;
import com.heaven7.java.visitor.FireVisitor;
import com.heaven7.java.visitor.PredicateVisitor;
import com.heaven7.java.visitor.ResultVisitor;
import com.heaven7.java.visitor.collection.VisitServices;
import com.heaven7.utils.FileUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import static com.vida.ai.third.baidu.temp.BatchUpdateSample.findImagePath;

/**
 * @author heaven7
 */
public class TestUtils {
    private static final String TAG = "TestUtils";
    private static final int MAX_SIZE = 4 * 1024 * 1024 ;
    private static final int MIN = 15;
    private static final int MAX = 4096;

    //48408. real is 48385. failed is 11256. empty is 10584
    public static void main(String[] args) {
       // testImageCount();
       // testEmptyCount();
       // testFailedCount();
        testLargeImageCount();
    }

    private static void testLargeImageCount() {
        String logFile = "E:\\test\\batch_upload\\large_images.txt";
        String dir = "E:\\test\\batch_upload\\pexels";
        File file = new File(dir);
        List<String> files = new ArrayList<>();
        FileUtils.getFiles(file, "jpg", files);
        FileUtils.getFiles(file, "jpeg", files);
        FileUtils.getFiles(file, "png", files);

        FileUtils.getFiles(file, "JPG", files);
        FileUtils.getFiles(file, "JPEG", files);
        FileUtils.getFiles(file, "PNG", files);

        final StringBuilder sb = new StringBuilder();
        VisitServices.from(files).fire(new FireVisitor<String>() {
            @Override
            public Boolean visit(String s, Object param) {
                if(isLargeImage(s)){
                    sb.append(s).append("\r\n");
                }
                return false;
            }
        });
        FileUtils.writeTo(logFile, sb.toString());
    }
    private static boolean isLargeImage(String imagePath){
        //要求base64编码后大小不超过4M，最短边至少15px，最长边最大4096px,支持jpg/png/bmp格式
        try {
            byte[] imgData = FileUtil.readFileByBytes(imagePath);
            byte[] bytes = Base64.getEncoder().encode(imgData);
            System.out.println("imagePath: " + imagePath + " ,base64 size = " + bytes.length);
            if(bytes.length > MAX_SIZE){
                System.err.println("imagePath: " + imagePath + " too large");
                return true;
            }
            BufferedImage image = ImageIO.read(new File(imagePath));
            int w = image.getWidth();
            int h = image.getHeight();
            int min = w > h ? h : w;
            int max = w > h ? w : h;
            if(min < MIN || max > MAX){
                System.err.println("imagePath: " + imagePath + ", w or h is not match.");
                return true;
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return false;
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
