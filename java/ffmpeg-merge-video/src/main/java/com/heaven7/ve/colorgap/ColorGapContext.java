package com.heaven7.ve.colorgap;

import com.heaven7.utils.Context;
import com.heaven7.ve.anno.SystemResource;
import com.heaven7.ve.collect.ColorGapPerformanceCollector;
import com.heaven7.ve.kingdom.FileResourceManager;
import com.heaven7.ve.kingdom.Kingdom;
import com.heaven7.ve.template.VETemplate;
import com.heaven7.ve.utils.SharedThreadPool;

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

    MediaResourceConfiguration getMediaResourceConfiguration();
    void setMediaResourceConfiguration(MediaResourceConfiguration mrc);

    void setDebugParam(DebugParam param);
    /** call this you should check if you open the debug mode by set {@linkplain InitializeParam#setDebug(boolean)} true. */
    DebugParam getDebugParam();

    void setMontageParameter(MontageParam param);
    MontageParam getMontageParameter();

    FileResourceManager getFileResourceManager();
    VETemplate getTemplate();

    SharedThreadPool getSharedThreadPool();
    @SystemResource
    void setSharedThreadPool(SharedThreadPool pool);
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
        /** the template file dir of json-data */
        private String templateDir;
        /** the effect file dir of json-data */
        private String effectDir;
        /** the effect file dir of resource */
        private String effectResourceDir;
        /** the debug flag will effect {@linkplain ColorGapContext#getDebugParam()}. if debug is false. getDebugParam will return a default. */
        private boolean debug;
        private TransitionDelegate transitionDelegate;

        public TransitionDelegate getTransitionDelegate() {
            return transitionDelegate;
        }
        public void setTransitionDelegate(TransitionDelegate transitionDelegate) {
            this.transitionDelegate = transitionDelegate;
        }

        public String getEffectResourceDir() {
            return effectResourceDir;
        }
        public void setEffectResourceDir(String effectResourceDir) {
            this.effectResourceDir = effectResourceDir;
        }

        public boolean isDebug() {
            return debug;
        }
        public void setDebug(boolean debug) {
            this.debug = debug;
        }

        public int getTestType() {
            return testType;
        }
        public void setTestType(int testType) {
            this.testType = testType;
        }

        public String getTemplateDir() {
            return templateDir;
        }
        public void setTemplateDir(String templateDir) {
            this.templateDir = templateDir;
        }

        public String getEffectDir() {
            return effectDir;
        }
        public void setEffectDir(String effectDir) {
            this.effectDir = effectDir;
        }
    }
}
