package com.vida.ai.test;

import com.heaven7.java.visitor.ResultVisitor;
import com.heaven7.java.visitor.collection.VisitServices;
import com.heaven7.utils.FileUtils;
import com.heaven7.ve.MediaResourceItem;
import com.heaven7.ve.colorgap.ColorGapContext;
import com.heaven7.ve.colorgap.impl.SimpleColorGapContext;
import com.heaven7.ve.test.TestUtils;

import java.io.File;
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
        ColorGapContext.InitializeParam ip = new ColorGapContext.InitializeParam();
        ip.setTestType(ColorGapContext.TEST_TYPE_SERVER);
        SimpleColorGapContext context = new SimpleColorGapContext();
        context.setInitializeParam(ip);
        Launcher.launch(context);
    }

    public static void main(String[] args) {
         new ShotGenerateTest().start();
    }

    public void start(){
        List<String> files = FileUtils.getFiles(new File(dir), "mp4");
        generator.genShotForVideo(VisitServices.from(files).map(new ResultVisitor<String, MediaResourceItem>() {
            @Override
            public MediaResourceItem visit(String s, Object param) {
                return TestUtils.createVideoItem(s, 0);
            }
        }).getAsList(), new ShotInfoGenerator.Callback() {
            @Override
            public void onGenerateDone() {
                System.out.println("gen all shot done...");
            }
        });
    }

}
