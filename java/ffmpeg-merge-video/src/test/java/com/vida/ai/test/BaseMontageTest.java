package com.vida.ai.test;

import com.heaven7.ve.colorgap.ColorGapContext;
import com.heaven7.ve.colorgap.impl.SimpleColorGapContext;

/**
 * @author heaven7
 */
public class BaseMontageTest {

    private static final SimpleColorGapContext sInitContext = new SimpleColorGapContext();

    static {
        init();
    }

    public static ColorGapContext copySystemResource(){
        SimpleColorGapContext context = new SimpleColorGapContext();
        sInitContext.copySystemResource(context);
        return context;
    }

    private static void init(){
        ColorGapContext.InitializeParam ip = new ColorGapContext.InitializeParam();
        ip.setTestType(SimpleColorGapContext.TEST_TYPE_LOCAL);

        sInitContext.setInitializeParam(ip);
        Launcher.launch(sInitContext);
    }
}
