package com.vida.ai.test;

import com.heaven7.ve.MediaResourceItem;
import com.heaven7.ve.collect.ColorGapPerformanceCollector;
import com.heaven7.ve.colorgap.ColorGapContext;
import com.heaven7.ve.colorgap.ColorGapManager;
import com.heaven7.ve.colorgap.ColorGapParam;
import com.heaven7.ve.colorgap.impl.*;
import com.heaven7.ve.starter.KingdomStarter;

import java.util.List;

/**
 * @author heaven7
 */
public class ColorGapTest {

    private static final SimpleColorGapContext sInitContext = new SimpleColorGapContext();

    static {
        init();
    }

    public static void main(String[] args) {

    }

    public void start(String musicPath, List<MediaResourceItem> items, String outDir){
        SimpleColorGapContext context = new SimpleColorGapContext();
        sInitContext.copySystemResource(context);
        context.setKingdom(KingdomStarter.getKingdom(KingdomStarter.TYPE_DRESS));
        context.setColorGapPerformanceCollector(new ColorGapPerformanceCollector());
        //gap
        ColorGapManager cgm = new ColorGapManager(context, new MediaAnalyserImpl(),
               context.getMusicCutter(), new MusicShaderImpl(), new PlaidFillerImpl());
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

        sInitContext.setInitializeParam(ip);
        Launcher.launch(sInitContext);
    }
}
