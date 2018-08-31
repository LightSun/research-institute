package com.heaven7.ve.colorgap;

import com.heaven7.utils.Context;
import com.heaven7.ve.anno.SystemResource;
import com.heaven7.ve.collect.ColorGapPerformanceCollector;
import com.heaven7.ve.colorgap.impl.VideoAnalyseHelper;
import com.heaven7.ve.colorgap.impl.montage.*;
import com.heaven7.ve.kingdom.Kingdom;

/**
 * @author heaven7
 */
public interface ColorGapContext extends Context {

    /** test type local */
    int TEST_TYPE_LOCAL        = 1;
    /** test type local server */
    int TEST_TYPE_LOCAL_SERVER = 2;
    /** test type server */
    int TEST_TYPE_SERVER       = 3;

    @SystemResource
    void setInitializeParam(InitializeParam ip);
    InitializeParam getInitializeParam();

    /**
     * get test type .default is {@linkplain #TEST_TYPE_SERVER}
     * @return the test type
     */
    int getTestType();

    Kingdom getKingdom();
    void setKingdom(Kingdom kingdom);

    void setColorGapPerformanceCollector(ColorGapPerformanceCollector collector);

    ColorGapPerformanceCollector getColorGapPerformanceCollector();

    /** may be null */
    MusicCutter getMusicCutter();
    @SystemResource
    void setMusicCutter(MusicCutter provider);

    /**
     * copy the system resource to target color gap context.
     * @param dst the dst color gap context.
     */
    void copySystemResource(ColorGapContext dst);

    static String getTestTypeString(int testType) {
        switch (testType){
            case ColorGapContext.TEST_TYPE_LOCAL_SERVER:
                return "TEST_TYPE_LOCAL_SERVER";
            case ColorGapContext.TEST_TYPE_SERVER:
                return "TEST_TYPE_SERVER";
            case ColorGapContext.TEST_TYPE_LOCAL:
                return "TEST_TYPE_LOCAL";
        }
        return null;
    }

    /**
     * the init param
     */
    class InitializeParam{

        private int testType = ColorGapContext.TEST_TYPE_SERVER;

        public int getTestType() {
            return testType;
        }
        public void setTestType(int testType) {
            this.testType = testType;
        }
    }
}
