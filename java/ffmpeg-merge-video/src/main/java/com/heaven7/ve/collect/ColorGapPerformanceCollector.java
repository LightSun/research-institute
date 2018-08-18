package com.heaven7.ve.collect;

import com.heaven7.java.visitor.FireVisitor;
import com.heaven7.java.visitor.collection.VisitServices;
import com.heaven7.java.visitor.util.SparseArray;
import com.heaven7.java.visitor.util.Throwables;
import com.vida.common.TimeRecorder;

import java.util.Arrays;

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

    public ColorGapPerformanceCollector(){
        this(PerformanceWriter.LOG_WRITER);
    }
    public ColorGapPerformanceCollector(PerformanceWriter mWriter) {
        //all modules
        String[] modules = {
                MODULE_PRELOAD, MODULE_ANALYSE_MEDIA, MODULE_MUSIC_SHADER,
                MODULE_CUT_VIDEO, MODULE_RECOGNIZE_SHOT, MODULE_FILL_PLAID,
        };
        VisitServices.from(Arrays.asList(modules)).fire(new FireVisitor<String>() {
            @Override
            public Boolean visit(String s, Object param) {
                putModuleCollector(s, mWriter);
                return null;
            }
        });
    }

    private void putModuleCollector(String module, PerformanceWriter mWriter){
        mModuleMap.put(module.hashCode(), new Collector(module, mWriter));
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
