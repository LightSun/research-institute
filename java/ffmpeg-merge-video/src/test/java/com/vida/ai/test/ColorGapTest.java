package com.vida.ai.test;

import com.heaven7.java.visitor.ResultVisitor;
import com.heaven7.java.visitor.collection.VisitServices;
import com.heaven7.utils.FileUtils;

import com.heaven7.ve.collect.ColorGapPerformanceCollector;
import com.heaven7.ve.colorgap.*;
import com.heaven7.ve.colorgap.impl.*;
import com.heaven7.ve.colorgap.impl.filler.BasePlaidFiller;
import com.heaven7.ve.configs.BootStrapData;
import com.heaven7.ve.cross_os.IMediaResourceItem;
import com.heaven7.ve.starter.KingdomStarter;
import com.heaven7.ve.test.SimpleFaceScanner;
import com.heaven7.ve.test.SimpleHighLightScanner;
import com.heaven7.ve.test.SimpleTagScanner;
import com.heaven7.ve.test.TestUtils;

import java.io.File;
import java.util.List;

/**
 * the color gap test for video
 * @author heaven7
 */
public class ColorGapTest {

    public static final String MUSIC_DIR = "E:\\tmp\\music_cut";

    static {
        Launcher.launch();
    }

    /**
     * 1, 关键点识别.
     * @param args
     */
    public static void main(String[] args) {
         String videoDir = "F:\\videos\\ClothingWhite";
         String outDir = "F:\\videos\\temp_works\\修改主体识别后";
        // String music = "E:\\tmp\\music_cut\\M6.mp3";
        // String videoDir = "I:\\guanguan\\ClothingWhite";
        // String outDir = "I:\\guanguan\\clothing_out";
        String music = MUSIC_DIR + File.separator + "M11.mp3"; //M6.M7
        List<String> files = FileUtils.getFiles(new File(videoDir), "mp4");
        List<IMediaResourceItem> list = VisitServices.from(files).map(new ResultVisitor<String, IMediaResourceItem>() {
            @Override
            public IMediaResourceItem visit(String s, Object param) {
                IMediaResourceItem item = TestUtils.createVideoItem(s);
                item.setWidth(1280);
                item.setHeight(720);
                return item;
            }
        }).getAsList();
        ColorGapTest cgt = new ColorGapTest();
        cgt.start(music,  list, outDir);
    }

    public void start(String musicPath, List<IMediaResourceItem> items, String outDir){
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
       /* dp.addFlags(FLAG_ASSIGN_SHOT_CUTS |
                FLAG_ASSIGN_SHOT_TYPE |
                FLAG_ASSIGN_FACE_COUNT |
                FLAG_ASSIGN_BODY_COUNT
        );
        String shots = ConfigUtil.loadResourcesAsString("table/test/shots.json");
        ShotsData data = new Gson().fromJson(shots, ShotsData.class);
        data.resolve();
        dp.setShotAssigner(data);*/

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
                .build());
       // cgm.fill(new String[]{musicPath}, null, items, new FFMpegFiller(outDir));
        cgm.fill(new String[]{musicPath}, null, items, new MediaSdkExeFiller(data.getMediaSdkDir(), outDir));
    }
}
