package com.vida.ai.test;

import com.heaven7.java.visitor.ResultVisitor;
import com.heaven7.java.visitor.collection.VisitServices;
import com.heaven7.utils.FileUtils;
import com.heaven7.ve.BaseMediaResourceItem;
import com.heaven7.ve.collect.ColorGapPerformanceCollector;
import com.heaven7.ve.colorgap.*;
import com.heaven7.ve.colorgap.impl.*;
import com.heaven7.ve.colorgap.impl.filler.BasePlaidFiller;
import com.heaven7.ve.starter.KingdomStarter;
import com.heaven7.ve.test.TestUtils;
import com.vida.ai.test.impl.SimpleFaceScanner;
import com.vida.ai.test.impl.SimpleHighLightScanner;

import java.io.File;
import java.util.List;

import static com.heaven7.ve.colorgap.DebugParam.FLAG_ASSIGN_FACE_SCANNER;
import static com.heaven7.ve.colorgap.DebugParam.FLAG_ASSIGN_HIGH_LIGHT_SCANNER;

/**
 * @author heaven7
 */
public class ColorGapTest {

   // public static final String MUSIC_DIR = "I:\\guanguan\\test_musics";
    public static final String MUSIC_DIR = "E:\\tmp\\music_cut";
    private static final SimpleColorGapContext sInitContext = new SimpleColorGapContext();

    private static final String FACE_DATA_DIR = "F:\\videos\\ClothingWhite\\faces1"; //TODO
    private static final String HIGHLIGHT_DATA_DIR = "F:\\videos\\ClothingWhite\\highlight";

    static {
        init();
    }

    /**
     * 1, 关键点识别.
     * @param args
     */
    public static void main(String[] args) {
         String videoDir = "F:\\videos\\ClothingWhite";
         String outDir = "F:\\videos\\temp_works\\人脸和高光对比\\all_self_face1";
        // String music = "E:\\tmp\\music_cut\\M6.mp3";
        // String videoDir = "I:\\guanguan\\ClothingWhite";
        // String outDir = "I:\\guanguan\\clothing_out";
        String music = MUSIC_DIR + File.separator + "M11.mp3"; //M6.M7
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
        param.setDuration(30);
        param.setEffectFileName("effect_dress");
        param.setTemplateFileName("template_dress");
        //debug param
        DebugParam dp = new DebugParam();
        dp.setOutputDir(outDir);
        dp.addFlags(FLAG_ASSIGN_FACE_SCANNER |
                FLAG_ASSIGN_HIGH_LIGHT_SCANNER
        );
        dp.setFaceScanner(new SimpleFaceScanner(FACE_DATA_DIR));
        dp.setHighLightScanner(new SimpleHighLightScanner(HIGHLIGHT_DATA_DIR));
       /* dp.addFlags(FLAG_ASSIGN_SHOT_CUTS |
                FLAG_ASSIGN_SHOT_TYPE |
                FLAG_ASSIGN_FACE_COUNT |
                FLAG_ASSIGN_BODY_COUNT
        );
        String shots = ConfigUtil.loadResourcesAsString("table/test/shots.json");
        ShotsData data = new Gson().fromJson(shots, ShotsData.class);
        data.resolve();
        dp.setShotAssigner(data);*/

        SimpleColorGapContext context = new SimpleColorGapContext();
        sInitContext.copySystemResource(context);
        context.setKingdom(KingdomStarter.getKingdom(KingdomStarter.TYPE_DRESS));
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
        cgm.fill(new String[]{musicPath}, null, items, new FFMpegFiller(outDir));
    }

    private static void init(){
        ColorGapContext.InitializeParam ip = new ColorGapContext.InitializeParam();
        ip.setTestType(SimpleColorGapContext.TEST_TYPE_LOCAL);
        ip.setTemplateDir("table");
        ip.setEffectDir("table");
        ip.setDebug(true);
        ip.setEffectResourceDir("E:\\tmp\\服装demo_数据_ios\\texture");
        ip.setTransitionDelegate(new TransitionDelegate() {
            @Override
            public long getDuration(int transitionType) {
                return 30;
            }
        });

        sInitContext.setInitializeParam(ip);
        Launcher.launch(sInitContext);
    }
}
