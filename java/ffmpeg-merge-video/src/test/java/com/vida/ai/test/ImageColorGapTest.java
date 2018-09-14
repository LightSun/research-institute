package com.vida.ai.test;

import com.heaven7.java.image.ImageFactory;
import com.heaven7.java.visitor.ResultVisitor;
import com.heaven7.java.visitor.collection.VisitServices;
import com.heaven7.ve.BaseMediaResourceItem;
import com.heaven7.ve.collect.ColorGapPerformanceCollector;
import com.heaven7.ve.colorgap.*;
import com.heaven7.ve.colorgap.impl.MediaAnalyserImpl;
import com.heaven7.ve.colorgap.impl.MusicShaderImpl;
import com.heaven7.ve.colorgap.impl.SimpleShotRecognizer;
import com.heaven7.ve.colorgap.impl.StoryLineShaderImpl;
import com.heaven7.ve.colorgap.impl.filler.BasePlaidFiller;
import com.heaven7.ve.configs.BootStrapData;
import com.heaven7.ve.starter.KingdomStarter;
import com.heaven7.ve.test.SimpleFaceScanner;
import com.heaven7.ve.test.SimpleHighLightScanner;
import com.heaven7.ve.test.SimpleTagScanner;
import com.heaven7.ve.test.TestUtils;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * @author heaven7
 */
public class ImageColorGapTest {

    static {
        Launcher.launch();
    }

    public static void main(String[] args) {
        String music = BootStrapData.get(null).getMusicDir() + File.separator + "M11.mp3";
        String output = "F:\\videos\\temp_works\\only_images";
        String[] images = {
                "D:\\Users\\Administrator\\AppData\\Local\\Temp\\media_files\\test\\images\\DSC_2542-1.jpg",
                "D:\\Users\\Administrator\\AppData\\Local\\Temp\\media_files\\test\\images\\DSC_2519.jpg",
                "D:\\Users\\Administrator\\AppData\\Local\\Temp\\media_files\\test\\images\\DSC_2507.jpg",
        };

        new ImageColorGapTest().start(music, Arrays.asList(images), output);
    }

    public void start(String musicPath, List<String> images, String outDir){
        List<BaseMediaResourceItem> items = VisitServices.from(images).map(
                new ResultVisitor<String, BaseMediaResourceItem>() {
            @Override
            public BaseMediaResourceItem visit(String s, Object param) {
                BaseMediaResourceItem item = TestUtils.createImageItem(s);
                int[] wh = ImageFactory.getImageInitializer().getImageReader().readWidthHeight(s);
                item.setWidth(wh[0]);
                item.setHeight(wh[1]);
                return item;
            }
        }).getAsList();

        BootStrapData data = BootStrapData.get(null);
        //montage param
        MontageParam param = new MontageParam();
        param.setDuration(30);
        param.setEffectFileName("effect_dress");
        param.setTemplateFileName("template_dress");
        //debug param
        File shots_dir = new File(outDir, "shots_videos");
        shots_dir.mkdirs();
        DebugParam dp = new DebugParam();
        dp.setShotsDir(shots_dir.getAbsolutePath());
        dp.setOutputDir(outDir);
        dp.addFlags(data.getDebugFlags());
        dp.setFaceScanner(new SimpleFaceScanner(data.getFaceDataDirs(), data.getFaceDataImageDirs()));
        dp.setHighLightScanner(new SimpleHighLightScanner(data.getHighLightDataDirs(), data.getHighLightDataImageDirs()));
        dp.setTagScanner(new SimpleTagScanner(data.getTagDataDirs(), data.getTagDataImageDirs()));

        ColorGapContext context = Launcher.createColorGapContext();
        context.setKingdom(KingdomStarter.getKingdom(data.getKingdomType()));
        context.setColorGapPerformanceCollector(new ColorGapPerformanceCollector());
        context.setMontageParameter(param);
        context.setDebugParam(dp);
        //gap
        ColorGapManager cgm = new ColorGapManager(context, new MediaAnalyserImpl(),
                context.getMusicCutter(), new MusicShaderImpl(), new BasePlaidFiller());
        cgm.setStoryLineShader(new StoryLineShaderImpl());
        cgm.setShotRecognizer(new SimpleShotRecognizer());
        //for batch image .need this.
        cgm.preLoadData(new ColorGapParam.Builder()
               //.setResourceDataDir()
                .setBatchImageDataDir("D:\\Users\\Administrator\\AppData\\Local\\Temp\\media_files\\test\\images\\data2")
                .build());
        cgm.fill(new String[]{musicPath}, null, items, new MediaSdkExeFiller(data.getMediaSdkDir(), outDir));
    }
}
