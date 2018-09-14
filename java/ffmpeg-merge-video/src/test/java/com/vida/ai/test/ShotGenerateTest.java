package com.vida.ai.test;

import com.heaven7.java.visitor.ResultVisitor;
import com.heaven7.java.visitor.collection.VisitServices;
import com.heaven7.utils.FileUtils;
import com.heaven7.ve.BaseMediaResourceItem;
import com.heaven7.ve.colorgap.ColorGapContext;
import com.heaven7.ve.colorgap.impl.SimpleColorGapContext;
import com.heaven7.ve.test.TestUtils;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author heaven7
 */
public class ShotGenerateTest {

    private final ExecutorService mService = Executors.newFixedThreadPool(1);
    private final String dir = "F:\\videos\\ClothingWhite";
    private final ShotInfoGenerator generator = new ShotInfoGenerator(mService);

    static {
        Launcher.launch();
    }

    public static void main(String[] args) {
         new ShotGenerateTest().genShotInfoForImages();
    }

    public void genShotInfoForVideos(){
        List<String> files = FileUtils.getFiles(new File(dir), "mp4");
        generator.genShotForVideo(VisitServices.from(files).map(new ResultVisitor<String, BaseMediaResourceItem>() {
            @Override
            public BaseMediaResourceItem visit(String s, Object param) {
                return TestUtils.createVideoItem(s, 0);
            }
        }).getAsList(), new ShotInfoGenerator.Callback() {
            @Override
            public void onGenerateDone() {
                System.out.println("gen all shot done...");
            }
        });
    }

    public void genShotInfoForImages(){
        String dataDir = "D:\\Users\\Administrator\\AppData\\Local\\Temp\\media_files\\test\\images\\highlight";
        String[] images = {
                "D:\\Users\\Administrator\\AppData\\Local\\Temp\\media_files\\test\\images\\DSC_2542-1.jpg",
                "D:\\Users\\Administrator\\AppData\\Local\\Temp\\media_files\\test\\images\\DSC_2519.jpg",
                "D:\\Users\\Administrator\\AppData\\Local\\Temp\\media_files\\test\\images\\DSC_2507.jpg",
        };
        generator.genShotForImage(Arrays.asList(images), dataDir, new ShotInfoGenerator.Callback() {
            @Override
            public void onGenerateDone() {
                System.out.println("gen for images done ......");
            }
        });
    }

}
