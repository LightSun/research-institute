package com.vida.ai.test;

import com.heaven7.java.base.util.Logger;
import com.heaven7.java.base.util.Predicates;
import com.heaven7.java.base.util.SparseArray;
import com.heaven7.java.base.util.Throwables;
import com.heaven7.java.visitor.FireIndexedVisitor;
import com.heaven7.java.visitor.FireVisitor;
import com.heaven7.java.visitor.collection.VisitServices;
import com.heaven7.utils.CmdHelper;
import com.heaven7.utils.CommonUtils;
import com.heaven7.ve.colorgap.ColorGapContext;
import com.heaven7.ve.colorgap.ColorGapManager;
import com.heaven7.ve.colorgap.MarkFlags;
import com.heaven7.ve.colorgap.MediaPartItem;
import com.heaven7.ve.cross_os.IPlaidInfo;
import com.heaven7.ve.cross_os.ISpecialEffectInfo;
import com.heaven7.ve.cross_os.ITimeTraveller;
import com.heaven7.ve.cross_os.ITransitionInfo;
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
                    SparseArray<List<ITimeTraveller>> effectMap = partItem.getMarkFlags().getAppliedEffectMap();
                    if(effectMap != null && effectMap.size() > 0){
                        List<ITimeTraveller> travellers = effectMap.get(MarkFlags.TYPE_TRANSITION);
                        if(!Predicates.isEmpty(travellers)){
                            VisitServices.from(travellers).fire(new FireVisitor<ITimeTraveller>() {
                                @Override
                                public Boolean visit(ITimeTraveller tt, Object param) {
                                    ITransitionInfo info = (ITransitionInfo) tt;
                                    msp.addTransitionParam(new MediaSdkParam.TransitionParam(index, info.getType()));
                                    return null;
                                }
                            });
                        }
                        travellers = effectMap.get(MarkFlags.TYPE_SPECIAL_EFFECT);
                        if(!Predicates.isEmpty(travellers)){
                            VisitServices.from(travellers).fire(new FireVisitor<ITimeTraveller>() {
                                @Override
                                public Boolean visit(ITimeTraveller tt, Object param) {
                                    ISpecialEffectInfo se = (ISpecialEffectInfo) tt;
                                    msp.addEffectParam(new MediaSdkParam.EffectParam(index, se.getType()));
                                    return null;
                                }
                            });
                        }
                    }
                    if(index == size - 1){
                        //last
                        IPlaidInfo pi = (IPlaidInfo) gapItem.plaid;
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
            CmdHelper cmdHelper = new CmdHelper(msp.toCmds(true));
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
