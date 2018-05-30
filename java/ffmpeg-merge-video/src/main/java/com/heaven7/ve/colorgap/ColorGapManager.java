package com.heaven7.ve.colorgap;


import com.heaven7.core.util.Logger;
import com.heaven7.java.base.anno.Nullable;
import com.heaven7.java.base.util.ArrayUtils;
import com.heaven7.java.base.util.Predicates;
import com.heaven7.ve.Context;
import com.heaven7.ve.MediaResourceItem;
import com.heaven7.ve.colorgap.filter.MediaDirFilter;
import com.heaven7.ve.colorgap.filter.VideoTagFilter;
import com.heaven7.ve.colorgap.impl.AirShotFilterImpl;
import com.heaven7.ve.gap.GapManager;
import com.heaven7.ve.template.VETemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * Created by heaven7 on 2018/3/15 0015.
 */

public class ColorGapManager {

    private static final String TAG = "ColorGapManager";
    // 音乐格子切点，染色
    // 原始视频切片
    // 染色匹配计算.(时长过滤)

    private final Context mContext;
    private final MusicCutter musicCut;
    private final MusicShader musicShader;
    private final PlaidFiller filler;
    private final MediaAnalyser mediaAnalyser;
    private StoryLineShader mStoryShader;

    private TemplateScriptProvider mProvider;

    public ColorGapManager(Context context, MediaAnalyser mediaAnalyser, MusicCutter musicCut,
                           MusicShader musicShader, PlaidFiller filler) {
        this.mContext = context;
        this.mediaAnalyser = mediaAnalyser;
        this.musicCut = musicCut;
        this.musicShader = musicShader;
        this.filler = filler;
    }

    public void cancel(){
        mediaAnalyser.cancel();
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
     * fill the music plaids by videos, may be parts of video
     * @param musicPath the music paths
     * @param srcTemplate the src template which comes from {@linkplain #createVETemplate(List)},
     * @param items the media resource item
     * @return the FillResult which contains video editor nodes and src template.
     */
    public FillResult fill(String[] musicPath, @Nullable VETemplate srcTemplate, List<MediaResourceItem> items) {
        ResourceInitializer.init(mContext);
        //the barrier help we do two tasks: analyse, tint.
        CyclicBarrier barrier = new CyclicBarrier(2);
        //analyse
        List<MediaItem> mediaItems = mediaAnalyser.analyse(mContext, items, barrier);
        //cut music
        CutInfo[] infoes = musicCut.cut(mContext, musicPath);
        List<CutInfo.PlaidInfo> plaids = new ArrayList<>();
        for (CutInfo info : infoes) {
            plaids.addAll(info.getPlaidInfos());
        }

        //convert template
        //final VETemplate template = mSourceTemplate == null ? createVETemplate(templateItems) : mSourceTemplate;
        if(srcTemplate == null && mProvider != null){
            srcTemplate = createVETemplate(mProvider.provideScript());
        }

        //tint
        VETemplate resultTemplate = musicShader.tint(mContext, srcTemplate, plaids, 0);
        try {
            Logger.d(TAG, "fill", "tint done");
            barrier.await();
            Logger.d(TAG, "fill", "start cut video");
            //cut video
            List<MediaPartItem> newItems = VideoCutter.of(mediaItems).cut(plaids, mediaItems);
            //process story to color filter(生成shot key, 过滤)
            List<GapManager.GapItem> gapItems = null;
            if(mStoryShader != null) {
                gapItems = mStoryShader.tintAndFill(plaids, resultTemplate, newItems, filler, new AirShotFilterImpl());
            }
            if(gapItems == null){
                //fill plaid
                gapItems = filler.fillPlaids(plaids, newItems);
            }
            return new FillResult(gapItems, srcTemplate , resultTemplate);
        } catch (InterruptedException e) {
            //ignored e.printStackTrace();
        } catch (BrokenBarrierException e) {
            throw new RuntimeException(e);
        }catch (RuntimeException e){
            Logger.w(TAG, "fill", Logger.toString(e));
            return null;
        }
        return null;
    }

    /**
     * create src template by script. return null if the items is empty.
     * @param items the items from script
     * @return the video editor template. null if the items is empty.
     */
    public static @Nullable VETemplate createVETemplate(List<RawScriptItem> items){
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
            // Logger.d("PlaidFillerImpl", "fillPlaids", "plaid index = " + infoes.indexOf(info));
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

    private static void populateTemplate(VETemplate template, List<RawScriptItem> items) {
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
                    int[] ids = Vocabulary.getTagIdsFromWeddingNounTag(tag);
                    if (ids == null) {
                        Logger.w("ColorGapManager", "populateTemplate", "getTagIdsFromWeddingNounTag() failed.tag = " + tag);
                        continue;
                    }
                    tags.add(ArrayUtils.toList(ids));
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
}