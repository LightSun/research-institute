package com.heaven7.ve.colorgap;


import com.heaven7.core.util.Logger;
import com.heaven7.java.base.anno.Nullable;
import com.heaven7.java.base.util.Predicates;
import com.heaven7.java.base.util.Throwables;
import com.heaven7.java.visitor.FireIndexedVisitor;
import com.heaven7.java.visitor.collection.VisitServices;
import com.heaven7.utils.ConcurrentUtils;
import com.heaven7.utils.Context;
import com.heaven7.utils.FileUtils;
import com.heaven7.ve.MediaResourceItem;
import com.heaven7.ve.collect.CollectModule;
import com.heaven7.ve.colorgap.filter.MediaDirFilter;
import com.heaven7.ve.colorgap.filter.VideoTagFilter;
import com.heaven7.ve.colorgap.impl.AirShotFilterImpl;
import com.heaven7.ve.gap.GapManager;
import com.heaven7.ve.kingdom.Kingdom;
import com.heaven7.ve.template.VETemplate;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import static com.heaven7.ve.collect.ColorGapPerformanceCollector.*;

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

    private TemplateScriptProvider mProvider;
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
    /**
     * set the template script provider
     * @param provider the template script provider
     */
    public void setTemplateScriptProvider(TemplateScriptProvider provider){
        this.mProvider = provider;
    }

    public void setStoryLineShader(StoryLineShader storyShader) {
        this.mStoryShader = storyShader;
    }

    /**
     * fill the music plaids by videos, may be parts of video. must called in sub-thread
     * @param musicPath the music paths
     * @param srcTemplate the src template which comes from {@linkplain #createVETemplate(List)},
     * @param items the media resource item.
     * @param callback the fill callback
     */
    //return the FillResult which contains video editor nodes and src template.
    public void fill(String[] musicPath, @Nullable VETemplate srcTemplate, List<MediaResourceItem> items, FillCallback callback) {
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
        //create template
        if(srcTemplate == null && mProvider != null){
            srcTemplate = createVETemplate(mProvider.provideScript());
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
            final List<MediaPartItem> newItems = VideoCutter.of(mediaItems).cut(mContext, plaids, mediaItems);
            module.end(TAG);
            Logger.d(TAG, "fill", "after cut, item.size = " + newItems.size());
            //shot recognition.for 'GeLayLiYa' ,mShotRecognizer is disabled.
            if(!getKingdom().isGeLaiLiYa() && mShotRecognizer != null && !Predicates.isEmpty(newItems)){
                final VETemplate source_tem = srcTemplate;
                getPerformanceCollector().startModule(MODULE_RECOGNIZE_SHOT, TAG);
                mShotRecognizer.requestKeyPoint(newItems, new IShotRecognizer.Callback() {
                    @Override
                    public void onRecognizeDone(List<MediaPartItem> parts) {
                        getPerformanceCollector().addMessage(MODULE_RECOGNIZE_SHOT, "KeyPoint", "onRecognizeDone", parts.toString());
                        processShotType(newItems, plaids, source_tem, resultTemplate, callback);
                    }
                });
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

    // compute shot-type and judge if need request subject.
    private void processShotType(List<MediaPartItem> newItems, List<CutInfo.PlaidInfo> plaids,
                              @Nullable VETemplate srcTemplate, VETemplate resultTemplate,
                              FillCallback callback) {
        //do with shot type (need subject items)
        List<MediaPartItem> subjectItems = new ArrayList<>();
        for(MediaPartItem partItem : newItems){
            int shotType = mShotRecognizer.getShotType(partItem);
            if(shotType == MetaInfo.SHOT_TYPE_NONE){
                subjectItems.add(partItem);
            }else{
                partItem.imageMeta.setShotType(MetaInfo.getShotTypeString(shotType));
                //recompute score
                partItem.computeScore();
            }
            int shotCategory = mShotRecognizer.recognizeShotCategory(partItem);
            partItem.imageMeta.setShotCategory(shotCategory);
            getPerformanceCollector().addMessage(MODULE_RECOGNIZE_SHOT, "setShotCategory", "processShotType",
                    partItem.toString() + " ,shot_category = " +ShotRecognition.getShotCategoryString(shotCategory));
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
                && getContext().getInitializeParam().isDebug()){
            StringBuilder sb = new StringBuilder();
            VisitServices.from(newItems).fireWithIndex(new FireIndexedVisitor<MediaPartItem>() {
                @Override
                public Void visit(Object param, MediaPartItem item, int index, int size) {
                    sb.append(item.toString()).append("\r\n\n");
                    return null;
                }
            });
            FileUtils.writeTo(new File(getContext().getInitializeParam().getDebugOutDir(),
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

    /**
     * create src template by script. return null if the items is empty.
     * @param items the items from script
     * @return the video editor template. null if the items is empty.
     */
    public @Nullable VETemplate createVETemplate(List<RawScriptItem> items){
        if(Predicates.isEmpty(items)){
            return null;
        }
        VETemplate template = new VETemplate();
        populateTemplate(template, items);
        return template;
    }

    /*private static List<VideoEditorNode> convertFilledItems(List<GapManager.GapItem> filledItems){
        //convert to our want items
        List<VideoEditorNode> nodes = new ArrayList<>();
        for(GapManager.GapItem gapItem : filledItems){

            VideoEditorNode ve_node = new VideoEditorNode();
            CutInfo.PlaidInfo info = (CutInfo.PlaidInfo) gapItem.plaid;
            MediaPartItem mpi = (MediaPartItem) gapItem.item;
            //audio
            AudioTrack at = new AudioTrack();
            at.setPath(info.getPath());
            at.setStartTime(info.getStartTime());
            at.setEndTime(info.getEndTime());
            at.setMaxDuration(info.getMaxDuration());
            ve_node.setAudioTrack(at);
            //video
            ImageInfo pt = mpi.isImage() ? new ImageInfo() : new VideoTrack();
            pt.setOriginWidth(mpi.item.getWidth());
            pt.setOriginHeight(mpi.item.getHeight());
            pt.setPath(mpi.item.getFilePath());
            pt.setMaxDuration(CommonUtils.timeToFrame(mpi.item.getDuration(), TimeUnit.MILLISECONDS));

            pt.setStartTime(mpi.videoPart.getStartTime());
            pt.setEndTime(mpi.videoPart.getEndTime());
            // Logger.w(TAG, "fill", "mp.getStartTime() = " + mp.getStartTime());

            if(mpi.isVideo()){
                //video
                if (mpi.videoPart.getDuration() != info.getDuration()) {
                    throw new IllegalStateException("time should adjust. by call GapCallbackImpl.adjustTimes()");
                }
            }else if(!mpi.isImage()){
                throw new UnsupportedOperationException();
            }
            //sync effect. transition. filter
            if(!Predicates.isEmpty(info.getEffects())){
                pt.setEffects(new ArrayList<>(info.getEffects()));
            }
            if(info.getTransitionInfo() != null){
                pt.setTransitionInfo(info.getTransitionInfo());
            }
            if(info.getFilter() != null){
                pt.setFilterInfo(info.getFilter());
            }

            ve_node.addFrameTrack(pt);
            nodes.add(ve_node);
        }
        return nodes;
    }*/

    private void populateTemplate(VETemplate template, List<RawScriptItem> items) {
        Kingdom kingdom = getKingdom();
        for (RawScriptItem item : items) {
            //转场镜头/空镜头的个数. 末尾开始，倒过来均分
            int airShotCount = item.getAirShotCount();
            final int size = item.getPercentage() - airShotCount > 0 ? 1 : 0;

            VETemplate.LogicSentence ls = new VETemplate.LogicSentence();
            ls.setDir(item.getEvent());

            for (int i = 0; i < size; i++) {
                CutInfo.PlaidInfo info = new CutInfo.PlaidInfo();
                ls.addPlaidInfo(info);
            }
            template.addLogicSentence(ls);

            //1, tint dir
            MediaDirFilter.MediaDirCondition condition = new MediaDirFilter.MediaDirCondition();
            condition.addTag(item.getEvent());
            ls.addColorFilter(new MediaDirFilter(condition));
            //2, tint shot
            if (airShotCount > 0) {
                //the air sentence.
                VETemplate.LogicSentence ls_air = new VETemplate.LogicSentence();

                CutInfo.PlaidInfo info = new CutInfo.PlaidInfo();
                ls_air.addPlaidInfo(info);
                ls_air.setAir(true);
                template.addLogicSentence(ls_air);

                //other shot
                if (airShotCount > 1) {
                    // int leftAirShotCount = airShotCount - 1;
                    //被空镜头分成airShotCount份, 平均每份
                    int everyPart = size / airShotCount + 1;
                    //air shot index. step = everyPart - 1
                    for (int i = everyPart; i < size; i += everyPart - 1) {
                        CutInfo.PlaidInfo plaid = ls.getPlaids().get(i);
                        plaid.setAir(true);
                    }
                }
            }
            //3, tint first shot
            if (!Predicates.isEmpty(item.getFirstShotTags())) {
                List<List<Integer>> tags = new ArrayList<>();
                for (String tag : item.getFirstShotTags()) {
                    List<Integer> ids = kingdom.getTagIdsAsNoun(tag);
                    if (ids == null) {
                        Logger.w("ColorGapManager", "populateTemplate", "getTagIdsFromWeddingNounTag() failed.tag = " + tag);
                        continue;
                    }
                    tags.add(ids);
                }
                if (tags.size() > 0) {
                    ls.addColorFilter(new VideoTagFilter(new VideoTagFilter.VideoTagCondition(tags)));
                }
            }
            // 4. necessary shot
        }
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
