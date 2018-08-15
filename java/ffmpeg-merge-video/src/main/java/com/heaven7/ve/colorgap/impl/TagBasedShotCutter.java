package com.heaven7.ve.colorgap.impl;


import com.heaven7.core.util.Logger;
import com.heaven7.java.base.anno.Nullable;
import com.heaven7.java.base.util.Predicates;
import com.heaven7.java.visitor.FireVisitor;
import com.heaven7.java.visitor.PredicateVisitor;
import com.heaven7.java.visitor.collection.VisitServices;
import com.heaven7.java.visitor.util.Map;
import com.heaven7.utils.CollectionUtils;
import com.heaven7.utils.CommonUtils;
import com.heaven7.utils.Context;
import com.heaven7.ve.TimeTraveller;
import com.heaven7.ve.colorgap.*;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 基于Tag的视频切割器：通用Tag，人脸Tag等
 * Created by heaven7 on 2018/4/12 0012.
 */

public class TagBasedShotCutter extends VideoCutter {

    //move to VEGapUtils.
   // private static final float MAIN_FACE_AREA_RATE          = 2.0f ;        // 主人脸相对次要人脸的面积倍率
   // private static final float AVERAGE_AREA_DIFF_RATE       = 0.5f  ;       // 多人脸场景中，次要人脸相对平均人脸面积的倍率
    private static final int MIN_SHOT_BUFFER_LENGTH         = 6 ;           // FrameBuffer中生成Shot的最小Frame数
    private static final int MULTI_FACE_THRESHOLD           = 3 ;           // 多人脸下限为3个人脸
    private static final boolean MAX_DOMAIN_SCORE_SHOT_ONLY = true ;        // 一段segment是否只返回“domain score”最高的Item
    private static final boolean MAX_FACE_RECT_SCORE_SHOT_ONLY  = true ;    // 一段segment是否只返回“face rect score”最高的Item
    private static final boolean CUT_BY_TAG = false;
    private static final long DURATION_THRESOLD = CommonUtils.timeToFrame(15, TimeUnit.SECONDS); //人脸视频part大于15s 会用tag切割

    private static final String TAG = "TagBasedShotCutter";

    @Override
    public List<MediaPartItem> cut(Context context, List<CutInfo.PlaidInfo> musicInfos, List<MediaItem> items) {
        List<MediaPartItem> resultList = new ArrayList<>();

        for(MediaItem item : items){
            if(item.item.isImage()){
                resultList.add(item.asPart(context));
                continue;
            }
            //MAX_SHOT_ONLY参数只对3分钟以内的视频生效，超过的一定是长纪录片 (小于3min的一般是慢速镜头)
            if(!CUT_BY_TAG && item.imageMeta.containsFaces()){
                Logger.d(TAG, "cut", "As human content. path = " + item.item.getFilePath());
                //cut by face
                final List<MediaPartItem> faceItems = cutByFace(context, item);
                dump(faceItems, item, "cutByFace");
                if(MAX_FACE_RECT_SCORE_SHOT_ONLY && item.item.getDuration() < 180*1000){
                    List<MediaPartItem> faceItems2 = getMaxFaceRectsScoreShot(faceItems, item);
                    resultList.addAll(faceItems2);
                }else {
                    //cut more face shots.
                    List<MediaPartItem> unUsedShots = getUnUsedShots(context, item, faceItems);
                    if (!Predicates.isEmpty(unUsedShots)) {
                        VisitServices.from(unUsedShots).fire(new FireVisitor<MediaPartItem>() {
                            @Override
                            public Boolean visit(MediaPartItem item, Object param) {
                                List<MediaPartItem> newItems = cutByCommonTag(context, item, false);
                                newItems = VisitServices.from(newItems).filter(null,
                                        (item1, param1) -> item1.getFacePercent() >= 0.7f,
                                        null).getAsList();
                                if(!Predicates.isEmpty(newItems)){
                                    faceItems.addAll(newItems);
                                    Logger.d(TAG, "unused part ,use tag to cut. part = " + item + "\n ---> " + newItems);
                                }
                                return null;
                            }
                        });
                    }
                    //AESC
                    VisitServices.from(faceItems).sortService(new Comparator<MediaPartItem>() {
                        @Override
                        public int compare(MediaPartItem o1, MediaPartItem o2) {
                            return Long.compare(o1.videoPart.getStartTime(), o2.videoPart.getStartTime());
                        }
                    });
                    resultList.addAll(faceItems);
                }
            }else{
                Logger.d(TAG, "cut", "As common tag. path = " + item.item.getFilePath());
                //cut by common tag
                List<MediaPartItem> faceItems = cutByCommonTag(context, item, true);
                dump(faceItems, item, "cutByCommonTag");
                if(MAX_DOMAIN_SCORE_SHOT_ONLY && item.item.getDuration() < 180*1000){
                    MediaPartItem shot = getMaxDomainScoreShot(context, faceItems, item);
                    if(shot != null) {
                        resultList.add(shot);
                    }
                }else{
                    resultList.addAll(faceItems);
                }
            }
        }
        return resultList;
    }

    /** 根据已经使用的item， 计算出剩余的part item(未被使用的, 人脸为主的) */
    private List<MediaPartItem> getUnUsedShots(Context context, MediaItem item, List<MediaPartItem> faceItems) {
        List<MediaPartItem> result = new ArrayList<>();
        //升序
        List<MediaPartItem> items = VisitServices.from(faceItems).sortService(new Comparator<MediaPartItem>() {
            @Override
            public int compare(MediaPartItem o1, MediaPartItem o2) {
                return Long.compare(o1.videoPart.getStartTime(), o2.videoPart.getStartTime());
            }
        }).getAsList();

        for(int i = 0 , j = i + 1 ; j < items.size() ; i ++, j ++){
            MediaPartItem item1 = items.get(i);
            MediaPartItem item2 = items.get(j);

            long startTime1 = item1.videoPart.getStartTime();
            long endTime1 = item1.videoPart.getEndTime();
            long startTime2 = item2.videoPart.getStartTime();
            long endTime2 = item2.videoPart.getEndTime();
            final long maxDuration = item1.videoPart.getMaxDuration();
            //first
            if(i == 0){
                //前面有未使用的
                if(startTime1 > 0 && CommonUtils.frameToTime(startTime1,
                        TimeUnit.SECONDS) >= MIN_SHOT_BUFFER_LENGTH){
                    //判断是否人脸为主
                    MediaPartItem partItem = new MediaPartItem(context, (MetaInfo.ImageMeta) item.getImageMeta().copy(),
                            item.getItem(), TimeTraveller.of(0, startTime1, maxDuration));
                    result.add(partItem);
                }
            }
            //last
            if(j == items.size() - 1){
                //末尾没使用的
                long delta = maxDuration - endTime2;
                if(CommonUtils.frameToTime(delta, TimeUnit.SECONDS) >= MIN_SHOT_BUFFER_LENGTH){
                    MediaPartItem partItem = new MediaPartItem(context,(MetaInfo.ImageMeta) item.getImageMeta().copy(),
                            item.getItem(), TimeTraveller.of(endTime2, maxDuration, maxDuration));
                    result.add(partItem);
                }
            }
            //between
            long delta = startTime2 - endTime1;
            if(CommonUtils.frameToTime(delta, TimeUnit.SECONDS) >= MIN_SHOT_BUFFER_LENGTH){
                MediaPartItem partItem = new MediaPartItem(context,(MetaInfo.ImageMeta) item.getImageMeta().copy(),
                        item.getItem(), TimeTraveller.of(endTime1, startTime2, maxDuration));
                result.add(partItem);
            }
        }
        return result;
    }

    /**
     *
     // 获取“人脸得分”最高的Items
     * @param faceItems the face items
     * @param item the item
     * @return the media part items
     */
    private List<MediaPartItem> getMaxFaceRectsScoreShot(List<MediaPartItem> faceItems, MediaItem item) {
        // 检查项：确保返回优质镜头
        List<MediaPartItem> results = new ArrayList<>();

        final long timeLimit = CommonUtils.timeToFrame(3, TimeUnit.SECONDS);
        //避免太短的镜头
        List<MediaPartItem> shots = VisitServices.from(faceItems).visitForQueryList((mpi, param) -> {
            //大于3秒
            return mpi.videoPart.getDuration() >= timeLimit;
        }, null);


        // 2. 只返回1单人脸，双人脸
        getMaxTagScoreShotOfFace(shots,1,  results);
        //双人脸
        getMaxTagScoreShotOfFace(shots,2,  results);
        // 如果没有单、双人脸，返回多人脸
        if(results.isEmpty()) {
            getMaxTagScoreShotOfFace(shots, 3, results);
        }
        return results;
    }
    private void getMaxTagScoreShotOfFace(List<MediaPartItem> shots, int mainFaceCount, List<MediaPartItem> out) {
        List<MediaPartItem> oneFaces = VisitServices.from(shots).visitForQueryList(new PredicateVisitor<MediaPartItem>() {
            @Override
            public Boolean visit(MediaPartItem mpi, Object param) {
                return mpi.imageMeta.getMainFaceCount() == mainFaceCount;
            }
        }, null);
        if(!oneFaces.isEmpty()) {
            VisitServices.from(oneFaces).sortService(
                    (o1, o2) -> Float.compare(o2.getDomainTagScore(), o1.getDomainTagScore()), true)
                    .headService(1).save(out);
        }
    }

    /** 获取“domain score”最大的shotItem */
    private static @Nullable MediaPartItem getMaxDomainScoreShot(Context context,List<MediaPartItem> faceItems, MediaItem item) {
        if(Predicates.isEmpty(faceItems)){
            return item.asPart(context);
        }
        Collections.sort(faceItems, new Comparator<MediaPartItem>() {
            @Override
            public int compare(MediaPartItem o1, MediaPartItem o2) {
                return Float.compare(o2.getDomainTagScore(), o1.getDomainTagScore());
            }
        });
        MediaPartItem result = faceItems.get(0);
        // 检查项：确保返回优质镜头
        // 过滤：黑屏（shotItem.tags为空 or [20]）
        if(result.isBlackShot() || result.videoPart.getDuration() < CommonUtils.timeToFrame(3, TimeUnit.SECONDS)){
            return null;
        }
        return result;
    }

    /** 从一个segment中根据tag切出tag稳定的镜头 */
    private static List<MediaPartItem> cutByCommonTag(Context context, CutItemDelegate item, boolean ensureOne) {
        if(item.getImageMeta() == null){
            return Collections.emptyList();
        }
        final List<MediaPartItem> result = new ArrayList<>();
        CommonTagTraveller traveller = new CommonTagTraveller(context, item, result);
        traveller.cut();

        // 确保至少有一个
        if(ensureOne && result.isEmpty()){
            result.add(item.asPart(context));
        }

        return result;
    }

    /** 从frameBuffer中提取镜头：通过通用tags */
    private static MediaPartItem createShotByTag(Context context, List<FrameTags> frameBuffer, Set<Integer> tagSet, CutItemDelegate item) {
        if(frameBuffer.size() >= MIN_SHOT_BUFFER_LENGTH){
            FrameTags first = frameBuffer.get(0);
            TimeTraveller tt = new TimeTraveller();
            tt.setStartTime(CommonUtils.timeToFrame(first.getFrameIdx(), TimeUnit.SECONDS));
            tt.setEndTime(tt.getStartTime() + CommonUtils.timeToFrame(frameBuffer.size() - 1, TimeUnit.SECONDS));

            MediaPartItem shot = new MediaPartItem(context, (MetaInfo.ImageMeta) item.getImageMeta().copy(), item.getItem(), tt);
            shot.addDetail("createShotByTag");
            return shot;
        }
        return null;
    }

    /**  通过人脸（框）信息切割镜头 */
    private static List<MediaPartItem> cutByFace(Context context,MediaItem item) {
        List<MediaPartItem> list = new ArrayList<>();
        List<MediaPartItem> oneFaceShots = cutByFaceArea(context, item, 1);
        List<MediaPartItem> twoFaceShots = cutByFaceArea(context, item, 2);
        List<MediaPartItem> multiFaceShots = cutForMultiFaces(context, item);
       // List<MediaPartItem> noFaceShots = cutByFaceArea(item, 0);

        list.addAll(oneFaceShots);
        list.addAll(twoFaceShots);
        list.addAll(multiFaceShots);
       // list.addAll(noFaceShots);

        // 确保至少有一个
        if(list.isEmpty()){
            list.add(item.asPart(context));
        }
        // 确保排序
        Collections.sort(list, new Comparator<MediaPartItem>() {
            @Override
            public int compare(MediaPartItem o1, MediaPartItem o2) {
                return Long.compare(o1.getStartTime(), o2.getStartTime());
            }
        });
        return list;
    }

    /** 从一个segment中获取“多主人脸”( >=3 主人脸 )的镜头 */
    private static List<MediaPartItem> cutForMultiFaces(Context context,MediaItem item) {
        if(item.imageMeta == null){
            return Collections.emptyList();
        }
        List<MediaPartItem> result = new ArrayList<>();
        List<FrameItem> frameBuffer = new ArrayList<>();

        List<FrameFaceRects> rawFaceRects = item.imageMeta.getAllFaceRects();
        for(int i = 0, size = rawFaceRects.size() ; i < size ; i ++){
            FrameFaceRects faceRects = rawFaceRects.get(i);
            //跳出检测
            if(faceRects.getMainFaceCount() < MULTI_FACE_THRESHOLD){
                MediaPartItem shot = createShotByMultiFaces(context, frameBuffer, item);
                if(shot != null){
                    result.add(shot);
                }
                frameBuffer.clear();
              //  Logger.d(TAG, "cutForMultiFaces", "clear frame buffer");
            }else{
                frameBuffer.add(new FrameItem(i, faceRects.getSortedFrameAreas()));
             //   Logger.d(TAG, "cutForMultiFaces", "append idx = "+ i +" into frame buffer.");
            }
        }
        // 判断最后buffer中残留的frame能否构成一个镜头
        MediaPartItem shot = createShotByMultiFaces(context, frameBuffer, item);
        if(shot != null){
            result.add(shot);
        }
        frameBuffer.clear();
      //  Logger.d(TAG, "cutForMultiFaces", "clear frame buffer -- 2");
        return result;
    }

    /** 从frameBuffer中提取镜头：多人脸( >=3 ) */
    private static MediaPartItem createShotByMultiFaces(Context context,List<FrameItem> frameBuffer, MediaItem item) {
        if(frameBuffer.size() >= MIN_SHOT_BUFFER_LENGTH){
            FrameItem bf_item = frameBuffer.get(0);
            TimeTraveller tt = new TimeTraveller();
            tt.setStartTime(CommonUtils.timeToFrame(bf_item.id, TimeUnit.SECONDS));
            tt.setEndTime(CommonUtils.timeToFrame(bf_item.id + frameBuffer.size() - 1, TimeUnit.SECONDS));

            MediaPartItem shot = new MediaPartItem(context,(MetaInfo.ImageMeta) item.imageMeta.copy(), item.item, tt);
            shot.imageMeta.setMainFaceCount(MULTI_FACE_THRESHOLD);
            return shot;
        }
        return null;
    }

    /**
     * cut by the face area. 主人脸面积
     * @param item the media item
     * @param mainFaceCount the main face count
     * @return the media parts
     */
    private static List<MediaPartItem> cutByFaceArea(Context context,MediaItem item, int mainFaceCount) {
        List<FrameFaceRects> allFaceRects = item.imageMeta.getAllFaceRects();
        if(Predicates.isEmpty(allFaceRects)){
            return Collections.emptyList();
        }
        List<MediaPartItem> result = new ArrayList<>();
        List<FrameItem> frameBuffer = new ArrayList<>();
        for(FrameFaceRects ffr : allFaceRects){
            // 跳出条件检测：主人脸个数不对
            if(ffr.getMainFaceCount() != mainFaceCount){
                MediaPartItem shot = createShotByFace(context, frameBuffer, mainFaceCount, item);
                if(shot != null){
                    result.add(shot);
                }
                frameBuffer.clear();
              //  Logger.d(TAG, "cutByFaceArea", "");
            }else{
                frameBuffer.add(new FrameItem(ffr.getFrameIdx(), ffr.getSortedFrameAreas()));
            }
        }
        // 判断最后buffer中残留的frame能否构成一个镜头
        MediaPartItem shot = createShotByFace(context, frameBuffer, mainFaceCount, item);
        if(shot != null){
            result.add(shot);
        }
        frameBuffer.clear();
       // Logger.d(TAG, "cutByFaceArea", "");
        return result;
    }

    /** 从frameBuffer中提取镜头：通过通用tags  */
    private static MediaPartItem createShotByFace(Context context,List<FrameItem> frameBuffer, int mainFaceCount, MediaItem item) {
        if(frameBuffer.size() >= MIN_SHOT_BUFFER_LENGTH){
            // 注意：用ffmpeg切的视频帧，pic-001其实代表第0秒，因此此处要-1
            FrameItem bf_item = frameBuffer.get(0);
            TimeTraveller tt = new TimeTraveller();
            tt.setStartTime(CommonUtils.timeToFrame(bf_item.id, TimeUnit.SECONDS));
            tt.setEndTime(CommonUtils.timeToFrame(bf_item.id + frameBuffer.size() - 1, TimeUnit.SECONDS));

            MediaPartItem mpi = new MediaPartItem(context,(MetaInfo.ImageMeta) item.imageMeta.copy(), item.item, tt);
            return mpi;
        }
        return null;
    }

    private static void dump(List<MediaPartItem> items, CutItemDelegate item, String tag) {
        StringBuilder sb = new StringBuilder();
        sb.append(tag)
                .append(" success, for path = ")
                .append(item.getItem().getFilePath())
                .append(" ;parts = ");
        for(MediaPartItem mpi : items){
            float start = CommonUtils.frameToTime(mpi.videoPart.getStartTime(), TimeUnit.SECONDS);
            float end = CommonUtils.frameToTime(mpi.videoPart.getEndTime(), TimeUnit.SECONDS);
            sb.append(String.format(Locale.getDefault(),"( start , end ) = ( %.1f, %.1f ),\n", start, end));
        }
        Logger.i(TAG, "dump", sb.toString());
    }

    private static class CommonTagTraveller extends BaseContextOwner implements Map.MapTravelCallback<Integer, VideoDataLoadUtils.FrameData>{

        private final CutItemDelegate item;
        private final List<MediaPartItem> result;
        List<FrameTags> frameBuffer = new ArrayList<>();
        Set<Integer> currentTagSet = new HashSet<>();

        public CommonTagTraveller(Context context,CutItemDelegate item, List<MediaPartItem> outItems) {
            super(context);
            this.item = item;
            this.result = outItems;
        }

        /** cut by frame tags */
        public void cut() {
            List<FrameTags> tags = item.getVideoTags();
            VisitServices.from(tags).fire(new FireVisitor<FrameTags>() {
                @Override
                public Boolean visit(FrameTags frameTags, Object param) {
                    travelFrameTags(frameTags);
                    return null;
                }
            });
            //item.imageMeta.travelAllFrameDatas(this);
            processRetainShot();
        }

        @Override
        public void onTravel(Integer key, VideoDataLoadUtils.FrameData value) {

            FrameTags ft = value.getTag();
            if(ft == null){
                //sparse array bug
                return;
            }
            travelFrameTags(ft);
        }

        private void travelFrameTags(FrameTags ft) {
            Set<Integer> frameTagSet = ft.getTopTagSet(getContext());
            // 跳出条件检测（新tags与currentTagSet的相似度小于1/2）
            if(currentTagSet.size() > 0){
                int intersectionCount = CollectionUtils.intersection(currentTagSet, frameTagSet).size();
                if(intersectionCount == 0 || intersectionCount * 1f / currentTagSet.size() < 0.5f){
                    MediaPartItem shot = createShotByTag(getContext(), frameBuffer, currentTagSet, item);
                    if(shot != null){
                        result.add(shot);
                    }
                    frameBuffer.clear();
                    currentTagSet = new HashSet<>();
                  //  Logger.d(TAG, "cutByCommonTag", "clear frame buffer");
                }
            }

            // 更新buffer检查条件：frameTags非空
            if(frameTagSet.size() == 0){
                return;
            }
            // 更新buffer和currentTagSet
            frameBuffer.add(ft);
            if(currentTagSet.isEmpty()){
                currentTagSet = frameTagSet;
            }else{
                currentTagSet = CollectionUtils.intersection(currentTagSet, ft.getTopTagSet(getContext()));
            }
            Logger.d(TAG, "cutByCommonTag", "append idx = "+ ft.getFrameIdx() +
                    ", tags = "+ frameTagSet +" into buffer, currentTagSet = ("+ currentTagSet +")");
        }

        /** 处理残留镜头 */
        private void processRetainShot(){
            // 判断最后buffer中残留的frame能否构成一个镜头
            MediaPartItem shot = createShotByTag(getContext(), frameBuffer, currentTagSet, item);
            if(shot != null){
                result.add(shot);
            }
            frameBuffer.clear();
           // Logger.d(TAG, "cutByCommonTag", "clear frame buffer -- 2");
        }
    }

}
