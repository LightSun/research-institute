package com.vida.ai.test;

import com.google.gson.Gson;
import com.heaven7.java.visitor.ResultVisitor;
import com.heaven7.java.visitor.collection.VisitServices;
import com.heaven7.utils.ConfigUtil;
import com.heaven7.utils.FileUtils;
import com.heaven7.ve.BaseMediaResourceItem;
import com.heaven7.ve.collect.ColorGapPerformanceCollector;
import com.heaven7.ve.colorgap.*;
import com.heaven7.ve.colorgap.impl.*;
import com.heaven7.ve.colorgap.impl.filler.BasePlaidFiller;
import com.heaven7.ve.starter.KingdomStarter;
import com.heaven7.ve.test.ShotsData;
import com.heaven7.ve.test.TestUtils;

import java.io.File;
import java.util.List;

/**
 * @author heaven7
 */
public class ColorGapTest {

    public static final String MUSIC_DIR = "I:\\guanguan\\test_musics";
    private static final SimpleColorGapContext sInitContext = new SimpleColorGapContext();

    static {
        init();
    }

    public static void main(String[] args) {
        // String videoDir = "F:\\videos\\ClothingWhite";
        // String outDir = "F:\\videos\\temp_works\\ClothingWhite";
        // String music = "E:\\tmp\\music_cut\\M6.mp3";
        String videoDir = "I:\\guanguan\\ClothingWhite";
        String outDir = "I:\\guanguan\\clothing_out";
        String music = MUSIC_DIR + File.separator + "M6.mp3";
        List<String> files = FileUtils.getFiles(new File(videoDir), "mp4");
        List<BaseMediaResourceItem> list = VisitServices.from(files).map(new ResultVisitor<String, BaseMediaResourceItem>() {
            @Override
            public BaseMediaResourceItem visit(String s, Object param) {
                BaseMediaResourceItem item = TestUtils.createVideoItem(s);
                item.setWidth(1280);
                item.setHeight(720);
                return item;
            }
        }).getAsList();
        ColorGapTest cgt = new ColorGapTest();
        cgt.start(music,  list, outDir);
    }

    public void start(String musicPath, List<BaseMediaResourceItem> items, String outDir){
        //montage param
        MontageParam param = new MontageParam();
        param.setDuration(60);
        param.setEffectFileName("effect_dress");
        param.setTemplateFileName("template_dress");

        SimpleColorGapContext context = new SimpleColorGapContext();
        sInitContext.copySystemResource(context);
        context.setKingdom(KingdomStarter.getKingdom(KingdomStarter.TYPE_DRESS));
        context.setColorGapPerformanceCollector(new ColorGapPerformanceCollector());
        context.setMontageParameter(param);
        //gap
        ColorGapManager cgm = new ColorGapManager(context, new MediaAnalyserImpl(),
               context.getMusicCutter(), new MusicShaderImpl(), new BasePlaidFiller());
        cgm.setStoryLineShader(new StoryLineShaderImpl());
        cgm.setShotRecognizer(new SimpleShotRecognizer());
        //for batch image .need this.
        cgm.preLoadData(new ColorGapParam.Builder()
                //.setResourceDataDir()
                .build());
        cgm.fill(new String[]{musicPath}, null, items, new FFMpegFiller(outDir));
    }

    private static void init(){
        ColorGapContext.InitializeParam ip = new ColorGapContext.InitializeParam();
        ip.setTestType(SimpleColorGapContext.TEST_TYPE_LOCAL);
        ip.setTemplateDir("table");
        ip.setEffectDir("table");
        ip.setDebug(true);
       // ip.setDebugOutDir("F:\\videos\\temp_works\\ClothingWhite");
        ip.setDebugOutDir("I:\\guanguan\\clothing_out");
        ip.setEffectResourceDir("E:\\tmp\\服装demo_数据_ios\\texture");
        ip.setTransitionDelegate(new TransitionDelegate() {
            @Override
            public long getDuration(int transitionType) {
                return 30;
            }
        });

        ip.setFlags(ColorGapContext.FLAG_ASSIGN_SHOT_TYPE);
        String shots = ConfigUtil.loadResourcesAsString("table/test/shots.json");
        ShotsData data = new Gson().fromJson(shots, ShotsData.class);
        ip.setShotAssigner(data);

        sInitContext.setInitializeParam(ip);
        Launcher.launch(sInitContext);
    }
}
