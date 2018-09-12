package com.heaven7.ve.colorgap;


import com.heaven7.core.util.Logger;
import com.heaven7.java.base.anno.Nullable;
import com.heaven7.java.base.util.Predicates;
import com.heaven7.java.base.util.Throwables;
import com.heaven7.java.visitor.FireIndexedVisitor;
import com.heaven7.java.visitor.FireVisitor;
import com.heaven7.java.visitor.ResultVisitor;
import com.heaven7.java.visitor.collection.VisitServices;
import com.heaven7.utils.ConcurrentUtils;
import com.heaven7.utils.Context;
import com.heaven7.utils.FileUtils;
import com.heaven7.ve.BaseMediaResourceItem;
import com.heaven7.ve.collect.CollectModule;
import com.heaven7.ve.colorgap.filter.MediaDirFilter;
import com.heaven7.ve.colorgap.filter.VideoTagFilter;
import com.heaven7.ve.colorgap.impl.AirShotFilterImpl;
import com.heaven7.ve.gap.GapManager;
import com.heaven7.ve.kingdom.Kingdom;
import com.heaven7.ve.template.VETemplate;
import com.heaven7.ve.test.ShotAssigner;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import static com.heaven7.ve.collect.ColorGapPerformanceCollector.*;
import static com.heaven7.ve.colorgap.DebugParam.FLAG_ASSIGN_BODY_COUNT;
import static com.heaven7.ve.colorgap.DebugParam.FLAG_ASSIGN_SHOT_CUTS;
import static com.heaven7.ve.colorgap.DebugParam.FLAG_ASSIGN_SHOT_TYPE;

/**
 * Created by heaven7 on 2018/3/15 0015.
 */

public class ColorGapManager extends BaseContextOwner{

    private static final String TAG = "ColorGapManager";

    private final MusicCutter musicCut;
    private final MusicShader musicShader;
    private final PlaidFiller filler;
    private final MediaAnalyser mediaAnalyser;
    private StoryLineShader mStoryShader;

    private IShotRecognizer mShotRecognizer;

    public ColorGapManager(Context context, MediaAnalyser mediaAnalyser, MusicCutter musicCut,
                           MusicShader musicShader, PlaidFiller filler) {
        super(context);
        Throwables.checkNull(context);
        this.mediaAnalyser = mediaAnalyser;
        this.musicCut = musicCut;
        this.musicShader = musicShader;
        this.filler = filler;
    }

    public void cancel(){
        mediaAnalyser.cancel();
    }

    /**
     * pre load data. like batch image data which is generate by AI.
     */
    public void preLoadData(ColorGapParam param){
        CollectModule module = getPerformanceCollector().startModule(MODULE_PRELOAD, TAG);
        mediaAnalyser.preLoadData(getContext(), param);
        module.end(TAG);
    }
    public void setShotRecognizer(IShotRecognizer mShotRecognizer) {
        this.mShotRecognizer = mShotRecognizer;
    }

    public void setStoryLineShader(StoryLineShader storyShader) {
        this.mStoryShader = storyShader;
    }

    /**
     * fill the music plaids by videos, may be parts of video. must called in sub-thread
     * @param musicPath the music paths
     * @param srcTemplate the src template which comes from {@linkplain com.heaven7.ve.kingdom.FileResourceManager},
     * @param items the media resource item.
     * @param callback the fill callback
     */
    //return the FillResult which contains video editor nodes and src template.
    public void fill(String[] musicPath, @Nullable VETemplate srcTemplate, List<BaseMediaResourceItem> items, FillCallback callback) {
        final ColorGapContext mContext = getContext();
        // ResourceInitializer.init(mContext);
        //the barrier help we do two tasks: analyse, tint.
        CyclicBarrier barrier = new CyclicBarrier(mediaAnalyser.getAsyncModuleCount() + 1);
        //analyse
        CollectModule module = getPerformanceCollector().startModule(MODULE_ANALYSE_MEDIA, TAG);
        List<MediaItem> mediaItems = mediaAnalyser.analyse(mContext, items, barrier);
        module.end(TAG);
        //cut music
        CollectModule musicModule = getPerformanceCollector().startModule(MODULE_CUT_MUSIC, TAG);
        CutInfo[] infoes = musicCut.cut(mContext, musicPath);
        List<CutInfo.PlaidInfo> plaids = new ArrayList<>();
        for (CutInfo info : infoes) {
            plaids.addAll(info.getPlaidInfos());
        }
        musicModule.addMessage(TAG, "fill", "plaid.size = " + plaids.size());
        musicModule.end(TAG);

        //convert template
        if(srcTemplate == null){
            //from kingdom config
            srcTemplate = getContext().getTemplate();
        }

        //tint
        module = getPerformanceCollector().startModule(MODULE_MUSIC_SHADER, TAG);
        VETemplate resultTemplate = musicShader.tint(mContext, srcTemplate, plaids, 0);
        module.end(TAG);
        try {
            Logger.d(TAG, "fill", "tint done");
            barrier.await();
            Logger.d(TAG, "fill", "start cut video");
            //all done. shut down service.
            ConcurrentUtils.shutDownNow();
            //cut video
            module = getPerformanceCollector().startModule(MODULE_CUT_VIDEO, TAG);
            final List<MediaPartItem> newItems = cutMediaShots(mediaItems, plaids);
            module.end(TAG);
            Logger.d(TAG, "fill", "after cut, item.size = " + newItems.size());

            //shot recognition.for 'GeLayLiYa' ,mShotRecognizer is disabled.
            if(!getKingdom().isGeLaiLiYa() && mShotRecognizer != null && !Predicates.isEmpty(newItems)){
                final VETemplate source_tem = srcTemplate;
                getPerformanceCollector().startModule(MODULE_RECOGNIZE_SHOT, TAG);
                //assign body count
                ShotAssigner shotAssigner = getDebugParam().getShotAssigner();
                if(getDebugParam().hasFlags(FLAG_ASSIGN_BODY_COUNT)){
                    for(MediaPartItem item : newItems){
                        int bodyCount = shotAssigner.assignBodyCount(item);
                        item.getImageMeta().setBodyCount(bodyCount);
                    }
                    getPerformanceCollector().addMessage(MODULE_RECOGNIZE_SHOT, "KeyPoint_Assign", "onRecognizeDone", newItems.toString());
                    processShotType(newItems, plaids, source_tem, resultTemplate, callback);
                }else {
                    mShotRecognizer.requestKeyPoint(newItems, new IShotRecognizer.Callback() {
                        @Override
                        public void onRecognizeDone(List<MediaPartItem> parts) {
                            getPerformanceCollector().addMessage(MODULE_RECOGNIZE_SHOT, "KeyPoint", "onRecognizeDone", parts.toString());
                            processShotType(newItems, plaids, source_tem, resultTemplate, callback);
                        }
                    });
                }
            }else {
                getPerformanceCollector().startModule(MODULE_FILL_PLAID, "fill");
                doFillPlaids(newItems, plaids, srcTemplate, resultTemplate, callback);
            }
        } catch (InterruptedException e) {
            //ignored e.printStackTrace();
            callback.onFillFinished(getContext(),null);
        } catch (BrokenBarrierException e) {
            throw new RuntimeException(e);
        }catch (RuntimeException e){
            Logger.w(TAG, "fill", Logger.toString(e));
            callback.onFillFinished(getContext(), null);
        }
    }

    private List<MediaPartItem> cutMediaShots(List<MediaItem> mediaItems, List<CutInfo.PlaidInfo> plaids) {
        List<MediaPartItem> newItems;
        //assign shot-cuts
        if(getDebugParam().hasFlags(FLAG_ASSIGN_SHOT_CUTS)){
            ShotAssigner shotAssigner = getDebugParam().getShotAssigner();
            assert shotAssigner != null;
            newItems = new ArrayList<>();
            VisitServices.from(mediaItems).map(new ResultVisitor<MediaItem, List<MediaPartItem>>() {
                @Override
                public List<MediaPartItem> visit(MediaItem mediaItem, Object param) {
                    return shotAssigner.assignShotCuts(getContext(), mediaItem);
                }
            }).fire(new FireVisitor<List<MediaPartItem>>() {
                @Override
                public Boolean visit(List<MediaPartItem> itemList, Object param) {
                    newItems.addAll(itemList);
                    return null;
                }
            });
        }else{
            newItems = VideoCutter.of(mediaItems).cut(getContext(), plaids, mediaItems);
        }
        return newItems;
    }

    // compute shot-type and judge if need request subject.
    private void processShotType(List<MediaPartItem> newItems, List<CutInfo.PlaidInfo> plaids,
                              @Nullable VETemplate srcTemplate, VETemplate resultTemplate,
                              FillCallback callback) {
        //do with shot type (may need subject items)
        final List<MediaPartItem> subjectItems = new ArrayList<>();
        if(getDebugParam().hasFlags(FLAG_ASSIGN_SHOT_TYPE)){
            //assign shot type
            ShotAssigner assigner = getDebugParam().getShotAssigner();
            for (MediaPartItem item : newItems){
                item.imageMeta.setShotType(assigner.assignShotType(item));
                item.computeScore();
            }
        }else {
            for (MediaPartItem partItem : newItems) {
                int shotType = mShotRecognizer.getShotType(partItem);
                if (shotType == MetaInfo.SHOT_TYPE_NONE) {
                    subjectItems.add(partItem);
                } else {
                    partItem.imageMeta.setShotType(MetaInfo.getShotTypeString(shotType));
                    //recompute score
                    partItem.computeScore();
                }
            }
        }
        //shot category
        for (MediaPartItem partItem : newItems) {
            int shotCategory = mShotRecognizer.recognizeShotCategory(partItem);
            partItem.imageMeta.setShotCategory(shotCategory);
            getPerformanceCollector().addMessage(MODULE_RECOGNIZE_SHOT, "setShotCategory", "processShotType",
                    partItem.toString() + " ,shot_category = " + ShotRecognition.getShotCategoryString(shotCategory));
        }

        //start subject recognize.
        if(Predicates.isEmpty(subjectItems)){
            getPerformanceCollector().endModule(MODULE_RECOGNIZE_SHOT, "processShotType");
            getPerformanceCollector().startModule(MODULE_FILL_PLAID, "processShotType");
            doFillPlaids(newItems, plaids, srcTemplate, resultTemplate, callback);
        }else {
            final VETemplate source_tem = srcTemplate;
            mShotRecognizer.requestSubject(subjectItems, new IShotRecognizer.Callback() {
                @Override
                public void onRecognizeDone(List<MediaPartItem> parts) {
                    //at last. if no shot-type. default is medium-shot
                    VEGapUtils.setDefaultShotType(parts);
                    //process story to color filter(gen shot key, filter)
                    getPerformanceCollector().addMessage(MODULE_RECOGNIZE_SHOT, "RecognizeSubject", "onRecognizeDone",
                            parts.toString());
                    getPerformanceCollector().endModule(MODULE_RECOGNIZE_SHOT, "onRecognizeDone");
                    getPerformanceCollector().startModule(MODULE_FILL_PLAID, "onRecognizeDone");
                    doFillPlaids(newItems, plaids, source_tem, resultTemplate, callback);
                }
            });
        }
    }

    private void doFillPlaids(List<MediaPartItem> newItems, List<CutInfo.PlaidInfo> plaids,
                              @Nullable VETemplate srcTemplate, VETemplate resultTemplate,
                              FillCallback callback) {
        //all is ensure, write debug info for local debug.
        if(getContext().getInitializeParam().getTestType() == ColorGapContext.TEST_TYPE_LOCAL
                && isDebug()){
            StringBuilder sb = new StringBuilder();
            VisitServices.from(newItems).fireWithIndex(new FireIndexedVisitor<MediaPartItem>() {
                @Override
                public Void visit(Object param, MediaPartItem item, int index, int size) {
                    sb.append(item.toString()).append("\r\n\n");
                    return null;
                }
            });
            FileUtils.writeTo(new File(getDebugParam().getOutputDir(),
                    "media_part_detail.txt"), sb.toString());
        }

        //process story to color filter(gen shot key, filter)
        List<GapManager.GapItem> gapItems = null;
        if (mStoryShader != null) {
            gapItems = mStoryShader.tintAndFill(getContext(), plaids, resultTemplate, newItems, filler, new AirShotFilterImpl());
        }
        if (gapItems == null) {
            //fill plaid
            gapItems = filler.fillPlaids(getContext(), plaids, newItems ,null);
        }
        getPerformanceCollector().endModule(MODULE_FILL_PLAID, "doFillPlaids");
        callback.onFillFinished(getContext(), new FillResult(gapItems, srcTemplate, resultTemplate));
    }

    public static class FillResult{
        public List<GapManager.GapItem> nodes;
        public VETemplate srcTemplate;
        public VETemplate resultTemplate;

        public FillResult(List<GapManager.GapItem> nodes, VETemplate srcTemplate , VETemplate resultTemplate) {
            this.nodes = nodes;
            this.srcTemplate = srcTemplate;
            this.resultTemplate = resultTemplate;
        }
    }

    /**
     * the callback
     */
    public interface FillCallback{
        /**
         * called on fill finished
         * @param result the fill result, null means failed.
         */
        void onFillFinished(ColorGapContext context, FillResult result);
    }
}
