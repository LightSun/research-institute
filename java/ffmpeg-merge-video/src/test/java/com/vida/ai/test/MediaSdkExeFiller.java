package com.vida.ai.test;

import com.heaven7.core.util.Logger;
import com.heaven7.java.base.util.Predicates;
import com.heaven7.java.base.util.SparseArray;
import com.heaven7.java.base.util.Throwables;
import com.heaven7.java.visitor.FireIndexedVisitor;
import com.heaven7.java.visitor.FireVisitor;
import com.heaven7.java.visitor.collection.VisitServices;
import com.heaven7.utils.CmdHelper;
import com.heaven7.utils.CommonUtils;
import com.heaven7.utils.ConcurrentManager;
import com.heaven7.ve.SpecialEffect;
import com.heaven7.ve.TimeTraveller;
import com.heaven7.ve.TransitionInfo;
import com.heaven7.ve.colorgap.*;
import com.heaven7.ve.gap.GapManager;
import com.vida.ai.third.baidu.MediaSdkParam;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author heaven7
 */
public class MediaSdkExeFiller implements ColorGapManager.FillCallback  {

    private final String mediaSdkDir;
    private final String outDir;

    public MediaSdkExeFiller(String mediaSdkDir, String outDir) {
        Throwables.checkNull(mediaSdkDir);
        this.outDir = outDir;
        this.mediaSdkDir = mediaSdkDir;
    }

    @Override
    public void onFillFinished(ColorGapContext context, ColorGapManager.FillResult result) {
        if(result == null){
            Logger.w("MediaSdkExeFiller", "onFillFinished", "fill failed.");
        }else{
            String exePath = new File(mediaSdkDir, "mediasdk.exe").getAbsolutePath();
            final MediaSdkParam msp = new MediaSdkParam(exePath);
            msp.setOutputFile(new File(outDir, "merged.mp4").getAbsolutePath());
            //video .effect, transition.
            VisitServices.from(result.nodes).fireWithIndex(new FireIndexedVisitor<GapManager.GapItem>() {
                long lastFramesIndex = 0;
                @Override
                public Void visit(Object param, GapManager.GapItem gapItem, int index, int size) {
                    MediaPartItem partItem = (MediaPartItem) gapItem.item;
                    float fileBegin = CommonUtils.frameToTime(partItem.videoPart.getStartTime(), TimeUnit.SECONDS);
                    final long frames = partItem.videoPart.getDuration();
                    msp.addVideoParam(new MediaSdkParam.VideoParam.Builder()
                            .setVideoPath(partItem.getVideoPath())
                            .setStartTime(fileBegin)
                            .setStartFrame(lastFramesIndex)
                            .setEndFrame(frames)
                            .build());
                    lastFramesIndex += frames;
                    SparseArray<List<TimeTraveller>> effectMap = partItem.getMarkFlags().getAppliedEffectMap();
                    if(effectMap != null && effectMap.size() > 0){
                        List<TimeTraveller> travellers = effectMap.get(MarkFlags.TYPE_TRANSITION);
                        if(!Predicates.isEmpty(travellers)){
                            VisitServices.from(travellers).fire(new FireVisitor<TimeTraveller>() {
                                @Override
                                public Boolean visit(TimeTraveller tt, Object param) {
                                    TransitionInfo info = (TransitionInfo) tt;
                                    msp.addTransitionParam(new MediaSdkParam.TransitionParam(index, info.getType()));
                                    return null;
                                }
                            });
                        }
                        travellers = effectMap.get(MarkFlags.TYPE_SPECIAL_EFFECT);
                        if(!Predicates.isEmpty(travellers)){
                            VisitServices.from(travellers).fire(new FireVisitor<TimeTraveller>() {
                                @Override
                                public Boolean visit(TimeTraveller tt, Object param) {
                                    SpecialEffect se = (SpecialEffect) tt;
                                    msp.addEffectParam(new MediaSdkParam.EffectParam(index, se.getType()));
                                    return null;
                                }
                            });
                        }
                    }
                    if(index == size - 1){
                        //last
                        CutInfo.PlaidInfo pi = (CutInfo.PlaidInfo) gapItem.plaid;
                        msp.setAudioParam(new MediaSdkParam.AudioParam.Builder()
                                .setAudioPath(pi.getPath())
                                .setStartTime(0)
                                .setEndTime(CommonUtils.frameToTime(pi.getEndTime(), TimeUnit.SECONDS))
                                .build());
                    }
                    return null;
                }
            });
            //start cmd
            CmdHelper cmdHelper = new CmdHelper(msp.toCmds(false));
            System.out.println("start generate video >>> cmd = " + cmdHelper.getCmdActually());
            cmdHelper.execute(new CmdHelper.LogCallback(){
                @Override
                public void beforeStartCmd(CmdHelper helper, ProcessBuilder pb) {
                    pb.directory(new File(mediaSdkDir));
                    pb.inheritIO();
                }
            });
            System.out.println("generate video done <<< cmd = " + cmdHelper.getCmdActually());
        }
    }

}
