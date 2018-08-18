package com.heaven7.ve.collect;

import com.heaven7.java.visitor.util.SparseArray;
import com.heaven7.java.visitor.util.Throwables;
import com.vida.common.TimeRecorder;

/**
 * the performance collector of color-gap.
 * @author heaven7
 */
public class ColorGapPerformanceCollector {

    public static final String MODULE_PRELOAD       = "PreLoad";
    public static final String MODULE_ANALYSE_MEDIA = "AnalyseMedia";
    public static final String MODULE_MUSIC_SHADER  = "MusicShader";
    public static final String MODULE_CUT_VIDEO     = "CutVideo";
    public static final String MODULE_RECOGNIZE_SHOT= "RecognizeShot";
    public static final String MODULE_FILL_PLAID    = "FillPlaid";

    private final SparseArray<Collector> mModuleMap = new SparseArray<>();

    public ColorGapPerformanceCollector(PerformanceWriter mWriter) {
        Collector mPreload = new Collector(MODULE_PRELOAD, mWriter);
        Collector mAnalyseMedia = new Collector(MODULE_ANALYSE_MEDIA, mWriter);
        Collector mMusicShader = new Collector(MODULE_MUSIC_SHADER, mWriter);
        Collector mCutVideo = new Collector(MODULE_CUT_VIDEO, mWriter);
        Collector mRecogizeShot = new Collector(MODULE_RECOGNIZE_SHOT, mWriter);
        Collector mFillPlaid = new Collector(MODULE_FILL_PLAID, mWriter);
        mModuleMap.put(MODULE_PRELOAD.hashCode(), mPreload);
        mModuleMap.put(MODULE_ANALYSE_MEDIA.hashCode(), mAnalyseMedia);
        mModuleMap.put(MODULE_MUSIC_SHADER.hashCode(), mMusicShader);
        mModuleMap.put(MODULE_CUT_VIDEO.hashCode(), mCutVideo);
        mModuleMap.put(MODULE_RECOGNIZE_SHOT.hashCode(), mRecogizeShot);
        mModuleMap.put(MODULE_FILL_PLAID.hashCode(), mFillPlaid);
    }

    public CollectModule startModule(String module, String label){
        CollectModule cm = mModuleMap.get(module.hashCode());
        Throwables.checkNull(cm);
        cm.start(label);
        return cm;
    }
    public void endModule(String module, String label){
        CollectModule cm = mModuleMap.get(module.hashCode());
        Throwables.checkNull(cm);
        cm.end(label);
    }
    public void addMessage(String module, String tag, String method, String msg) {
        CollectModule cm = mModuleMap.get(module.hashCode());
        Throwables.checkNull(cm);
        cm.addMessage(tag, method, msg);
    }

    private static class Collector implements CollectModule {

        private final TimeRecorder mRecorder;
        private final PerformanceWriter mWriter;

        public Collector(String name, PerformanceWriter writer) {
            this.mRecorder = new TimeRecorder(name);
            this.mWriter = writer;
        }
        @Override
        public PerformanceWriter getWriter(){
            return mWriter;
        }

        @Override
        public void addMessage(String tag, String method, String msg) {
            mWriter.writeMessage(mRecorder.getName(), tag + "_" + method  ,msg);
        }

        @Override
        public void start(String label) {
            mRecorder.begin();
            mWriter.writeMessage(mRecorder.getName(), "{ START } label ---> " + label);
        }

        @Override
        public void end(String label) {
            mRecorder.end();
            mWriter.writeMessage(mRecorder.getName(), "TimeRecorder", mRecorder.toString());
            mWriter.writeMessage(mRecorder.getName(), "{ END } label ---> " + label);
        }
    }

}
