package com.heaven7.ve.colorgap;

import com.heaven7.core.util.Logger;
import com.heaven7.java.base.util.Predicates;
import com.heaven7.java.base.util.SparseArray;
import com.heaven7.java.base.util.Throwables;
import com.heaven7.java.visitor.PredicateVisitor;
import com.heaven7.java.visitor.ResultVisitor;
import com.heaven7.java.visitor.collection.VisitServices;
import com.heaven7.utils.CollectionUtils;
import com.heaven7.utils.CommonUtils;
import com.heaven7.utils.TextUtils;
import com.heaven7.ve.MediaResourceItem;
import com.heaven7.ve.TimeTraveller;
import com.heaven7.ve.gap.ItemDelegate;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.heaven7.ve.colorgap.VEGapUtils.getAverMainFaceArea;
import static com.heaven7.ve.colorgap.VEGapUtils.getFileName;
import static com.heaven7.ve.colorgap.VEGapUtils.getShotType;

/**
 * the media part item, Equivalent to a video camera-shot
 * @author heaven7
 */
public class MediaPartItem implements ItemDelegate , CutItemDelegate{

    private static final Integer TAG_ID_BLACK = 20;
    private static final String TAG = "MediaPartItem";

    final public MetaInfo.ImageMeta imageMeta;
    final public MediaResourceItem item;
    final public TimeTraveller videoPart;

    private GapColorFilter.GapColorCondition mCondition;
    private boolean hold; //是否已经被占用
    /** 评分系统(“领域Tag得分”) */
    private float domainTagScore = -1;

    /** used for story */
    private int storyId = -1;
    /** selected or not in story */
    private boolean selectedInStory;
    /** 偏差镜头标志 */
    private boolean planed;

    /** the cause be deleted from story */
    private String cause = "";

    /**
     * create media part item.
     * @param imageMeta the image meta
     * @param item the media resource item
     * @param videoPart the video part. will auto set max duration.
     */
    public MediaPartItem(MetaInfo.ImageMeta imageMeta, MediaResourceItem item, TimeTraveller videoPart) {
        this.imageMeta = imageMeta;
        this.item = item;
        this.videoPart = videoPart;
        //set shot-key
        imageMeta.setShotKey(getFileName(item.getFilePath()) + "_" + videoPart.getStartTime() + "_" + videoPart.getEndTime());
        //set max duration
        videoPart.setMaxDuration(CommonUtils.timeToFrame(item.getDuration(), TimeUnit.MILLISECONDS));
        setRawTags();
    }

    public boolean isPlaned() {
        return planed;
    }
    public void setPlaned(boolean planed) {
        this.planed = planed;
        this.selectedInStory = planed;
    }
    public void setSelectedInStory(boolean selectedInStory, String cause) {
        this.selectedInStory = selectedInStory;
        if(!selectedInStory) {
            this.cause += cause + "\r\n";
        }
    }

    public void addDetail(String desc){
        this.cause += desc + "\r\n";
    }

    public String getDetail() {
        return cause;
    }
    public boolean isSelectedInStory() {
        return selectedInStory;
    }

    public int getStoryId() {
        return storyId;
    }
    public void setStoryId(int storyId) {
        this.storyId = storyId;
    }

    @Override
    public GapColorFilter.GapColorCondition getColorCondition() {
        if(mCondition == null){
            mCondition = MetaInfo.createColorCondition(imageMeta);
        }
        return mCondition;
    }
    public boolean isImage() {
        return item.isImage();
    }

    @Override
    public String toString() {
        return "MediaPartItem{" +
                "path =" + item.getFilePath() +
                ", part_time =" + videoPart.toString2() +
                ", selectedInStory =" + selectedInStory +
                ", planed =" + planed +
                ", tagScore =" + getDomainTagScore() +
                ", detail =" + getDetail() +
                '}';
    }

    @Override
    public String getVideoPath() {
        return item.getFilePath();
    }

    @Override
    public boolean isHold() {
        return hold;
    }
    @Override
    public void setHold(boolean hold) {
        this.hold = hold;
    }

    @Override
    public ItemDelegate copy() {
        MediaPartItem mpi = new MediaPartItem((MetaInfo.ImageMeta) imageMeta.copy(), item, (TimeTraveller) videoPart.copy());
        mpi.mCondition = this.mCondition;
        mpi.domainTagScore = this.domainTagScore;
        mpi.storyId = this.storyId;
        return mpi;
    }

    @Override
    public long getMaxDuration() {
        return CommonUtils.timeToFrame(item.getDuration(), TimeUnit.MILLISECONDS);
    }

    @Override
    public boolean isVideo() {
        return item.isVideo();
    }

    @Override
    public float getDomainTagScore() {
        if(domainTagScore >= 0 ){
            return domainTagScore;
        }
        // frame得分（形容词 + 名词）
        List<FrameTags> framesTags = getFramesTags();
        if(Predicates.isEmpty(framesTags)){
            return 0;
        }
/*
        if(item.getFilePath().endsWith("C0073.MP4") || item.getFilePath().endsWith("C0075.MP4")){
            System.out.println("------------");
        }*/

        float score = 0f;
        for(FrameTags ft : framesTags){
            //先对common tag进行分组， 并且只考虑top tags.(WeddingTagIte.description, score)
            Map<String, Float> tagDict = new HashMap<>();
            Set<Integer> tagSet = ft.getTopTagSet(3, 0.8f);
            if(Predicates.isEmpty(tagSet)){
                tagSet = ft.getTopTagSet(3, 0.5f);
            }
            for (int tagIdx : tagSet){
                Vocabulary.WeddingTagItem item = Vocabulary.getWeddingTagItem(tagIdx, Vocabulary.TYPE_WEDDING_ALL);
                if(item != null){
                    tagDict.put(item.getDesc(), (float) item.getScore());
                }
            }
            //然后对每组进行遍历，不重复计算同一个‘中文领域tag’ 对应的多个tag的值
            for (Float val : tagDict.values()){
                score += val;
            }
        }
        score = score / framesTags.size();

        //2. 增加人脸得分
        if(imageMeta != null){
            int faces = imageMeta.getMainFaceCount();
            if(faces == 1){
                score += 1.0f;
            }else if(faces == 2){
                score += 2f;
            }else if(faces > 2){
                score += 1.5f;
            }
        }
        // 3. 增加镜头类型得分
        if(imageMeta != null){
            String shotType = imageMeta.getShotType();
            if (!TextUtils.isEmpty(shotType)) {
                switch (MetaInfo.getShotTypeFrom(shotType)) {
                  case MetaInfo.SHOT_TYPE_CLOSE_UP:
                    score += 2f;
                    break;
                  case MetaInfo.SHOT_TYPE_MEDIUM_CLOSE_UP:
                    score += 1.5f;
                    break;
                  case MetaInfo.SHOT_TYPE_MEDIUM_SHOT:
                  case MetaInfo.SHOT_TYPE_MEDIUM_LONG_SHOT:
                    score += 0.5f;
                    break;
                }
            }
        }

        this.domainTagScore = score;
        return score;
    }

    public List<FrameTags> getFramesTags(){
        if(imageMeta == null){
            return null;
        }
        return imageMeta.getVideoTags(videoPart.getStartTime(), videoPart.getEndTime());
    }

    public boolean isBlackShot(){
        if(imageMeta != null){
            List<List<Integer>> tags = imageMeta.getTags();
            /*
             *  if tags.count == 0 || tags == [20]  return true
             */
            if(Predicates.isEmpty(tags)){
                return true;
            }else if(tags.size() == 1){
                List<Integer> list = tags.get(0);
                if(list.size() == 1 && list.contains(TAG_ID_BLACK)){
                    return true;
                }
            }
        }
        return false;
    }
    /** get start time (in mill-seconds) based on last modify time.  */
    public long getStartTime() {
        Throwables.checkNull(imageMeta);
        return imageMeta.getDate() + (long)CommonUtils.frameToTime(videoPart.getStartTime(), TimeUnit.MILLISECONDS);
    }
    /** get end time (in mill-seconds) based on last modify time. */
    public long getEndTime() {
        Throwables.checkNull(imageMeta);
        return imageMeta.getDate() + (long)CommonUtils.frameToTime(videoPart.getEndTime(), TimeUnit.MILLISECONDS);
    }
    /** set tags with main face and shot type */
    private void setRawTags() {
        if(imageMeta == null){
            return;
        }
        // video tags
        List<FrameTags> framesTags = getFramesTags();
        if(!Predicates.isEmpty(framesTags)){
            imageMeta.setRawVideoTags(framesTags);

            List<Integer> tags = calculateTags(framesTags, Vocabulary.TYPE_WEDDING_ALL);

           /* List<String> tags_str = new ArrayList<>();
            VisitServices.from(tags).transformToCollection(new ResultVisitor<Integer, String>() {
                @Override
                public String visit(Integer index, Object param) {
                    return Vocabulary.getTagStr(index);
                }
            }).save(tags_str);
            Logger.d(TAG , "setRawTags", "path = "+ item.getFilePath() + ",  main tags: " + tags_str);*/
            List<List<Integer>> tmp_tags = new ArrayList<>();
            tmp_tags.add(tags);
            imageMeta.setTags(tmp_tags);

            imageMeta.setNounTags(calculateTags(framesTags, Vocabulary.TYPE_WEDDING_NOUN));
            imageMeta.setDomainTags(calculateTags(framesTags, Vocabulary.TYPE_WEDDING_DOMAIN));
            imageMeta.setAdjTags(calculateTags(framesTags, Vocabulary.TYPE_WEDDING_ADJ));
        }

        //face tags
        List<FrameFaceRects> faceRectsList = imageMeta.getFaceRects(videoPart.getStartTime(), videoPart.getEndTime());
        if(!Predicates.isEmpty(faceRectsList)){
            imageMeta.setRawFaceRects(faceRectsList);
        }

        //face count and shot type
        calculateMainFaces();
        calculateShotType();
    }

    // 根据rawtags计算镜头类型
    // 1. 人脸优先
    // 2. 再尝试vidoe tags
    private void calculateShotType() {
        if(imageMeta == null){
            return;
        }
        //1, face
        if(!Predicates.isEmpty(imageMeta.getRawFaceRects()) && imageMeta.getMainFaceCount() > 0){
            List<FrameFaceRects> faceRects = imageMeta.getRawFaceRects();
            List<FrameItem> fis = new ArrayList<>();
            for(int i = 0 , size = faceRects.size() ; i < size ; i ++){
                FrameFaceRects frameFaceRects = faceRects.get(i);
                if(!frameFaceRects.hasRect()){
                    fis.add(new FrameItem(i, Collections.emptyList()));
                }else {
                    List<Float> areas = new ArrayList<>();
                    //面积降序
                    VisitServices.from(frameFaceRects.getRects()).map(null,
                            new Comparator<Float>() {
                                @Override
                                public int compare(Float o1, Float o2) {
                                    return Float.compare(o2, o1);
                                }
                            },
                            (faceRect, param) -> faceRect.getWidth() * faceRect.getHeight()
                    ).save(areas);
                    fis.add(new FrameItem(i, areas));
                }
            }
            float averMainFaceArea = getAverMainFaceArea(fis, imageMeta.getMainFaceCount());
            String shotType = getShotType(averMainFaceArea);
            if(shotType != null){
                imageMeta.setShotType(shotType);
            }
        }
        //2, video tags. 分三类进行计算（noun, domain, adj），noun计3分，其余计1分，返回最终得分最高的镜头类型
        if(!Predicates.isEmpty(imageMeta.getTags())){
            Map<String, Float>  shotTypeDict = new HashMap<>();
            List<Integer> shotTypeTags = new ArrayList<>();
            shotTypeTags.addAll(imageMeta.getNounTags());
            shotTypeTags.addAll(imageMeta.getDomainTags());
            shotTypeTags.addAll(imageMeta.getAdjTags());
            for(int tagIdx : shotTypeTags){
                Vocabulary.WeddingTagItem item = Vocabulary.getWeddingTagItem(tagIdx, Vocabulary.TYPE_WEDDING_ALL);
                if(item == null){
                    continue;
                }
                String st1 = item.getShotType1();
                if(!TextUtils.isEmpty(st1)){
                    Float val = shotTypeDict.get(st1);
                    if(val != null){
                        shotTypeDict.put(st1, val + item.getShotTypeScore());
                    }else{
                        shotTypeDict.put(st1, item.getShotTypeScore());
                    }
                }

                String st2 = item.getShotType2();
                if(!TextUtils.isEmpty(st2)){
                    Float val = shotTypeDict.get(st2);
                    if(val != null){
                        shotTypeDict.put(st2, val + item.getShotTypeScore());
                    }else{
                        shotTypeDict.put(st2, item.getShotTypeScore());
                    }
                }
            }
            //dump shotTypeDict
            //shot type
            if(shotTypeDict.size() > 0){
                String shotType = null;
                float maxVal = -1f;
                for (Map.Entry<String, Float> en : shotTypeDict.entrySet()){
                    Float value = en.getValue();
                    if(value > maxVal){
                        maxVal = value;
                        shotType =  en.getKey();
                    }
                }
                imageMeta.setShotType(shotType);
            }
        }
    }

    // 根据rawRects计算主人脸个数
    private void calculateMainFaces() {
        if(imageMeta == null){
            return;
        }
        if(Predicates.isEmpty(imageMeta.getRawFaceRects())){
            return;
        }
        List<FrameFaceRects> ffrs = VisitServices.from(imageMeta.getRawFaceRects()).visitForQueryList(new PredicateVisitor<FrameFaceRects>() {
            @Override
            public Boolean visit(FrameFaceRects ffr, Object param) {
                return ffr.hasRect();
            }
        }, null);
        //遍历rawFaceRects，计算主人脸个数
        int total = 0;
        int maxRects = -1;
        int maxMainFaces = -1;
        for(FrameFaceRects ffr: ffrs){
            int rectsCount = ffr.getRectsCount();
            if(rectsCount > maxRects){
                maxRects = rectsCount;
            }
            //main face
            int mainFaceCount = ffr.getMainFaceCount();
            if(mainFaceCount > maxMainFaces){
                maxMainFaces = mainFaceCount;
            }
            total += mainFaceCount;
        }
        float mainFaces = total * 1f/ ffrs.size();
        // 双人脸判断（最大主人脸个数为2，倾向将mainFace设置为2）
        if(maxMainFaces == 2 && (int)mainFaces == 1){
            mainFaces = 2;
        }
        // 多人脸判断（若1<mainFace<3，切shot中的maxFace>3，则将mainFace提升到3
        if(maxRects > 3 && mainFaces > 1 && mainFaces < 3){
            mainFaces = 3;
        }
        imageMeta.setMainFaceCount((int)mainFaces);
    }
    private List<Integer> calculateTags(List<FrameTags> rawVideoTags,int vocabularyType) {
        return calculateTags(rawVideoTags, 3, 0.7f, vocabularyType);
    }
    // 根据镜头的rawVideoTags统计计算tags. 得到对应的tag-index 数组
    private List<Integer> calculateTags(List<FrameTags> rawVideoTags, int count,
                                        float minPossibility, int vocabularyType) {
        //map: tag_index - > possibility
        //统计 tag 出现的概率(整个镜头中)。 -- 指定tag个数， min 概率
        SparseArray<List<Float>> dict = new SparseArray<>();
        for (FrameTags ft : rawVideoTags){
            List<Tag> topTags = ft.getTopTags(count, minPossibility, vocabularyType);
            for(Tag tag : topTags){
                List<Float> value = dict.get(tag.getIndex());
                if(value == null){
                    value = new ArrayList<>();
                    dict.put(tag.getIndex(), value);
                }
                value.add(tag.getPossibility());
            }
        }
        // 统计dict中的tag概率 (平均)
        List<Tag> tags = new ArrayList<>();
        for(int i = 0 , size = dict.size() ; i < size ; i ++){
            int tagIdx = dict.keyAt(i);
            List<Float> values = dict.valueAt(i);
            //compute average
            float possibility = CollectionUtils.sum(values) / values.size();
            tags.add(new Tag(tagIdx, possibility));
        }
        //build tag idx
        List<Integer> result = new ArrayList<>();
        VisitServices.from(tags).subService(new PredicateVisitor<Tag>() {
            @Override
            public Boolean visit(Tag tag, Object param) {
                return tag.getPossibility() >= minPossibility;//平均概率 大于限定
            }
        }).asListService().sortService(
                (o1, o2) -> Float.compare(o2.getPossibility(), o1.getPossibility()), true)
                .map(null, new ResultVisitor<Tag, Integer>() {
                    @Override
                    public Integer visit(Tag tag, Object param) {
                        return tag.getIndex();
                    }
                }).save(result);
        if(tags.size() <= count){
            return result;
        }else{
            return result.subList(0, count);
        }
    }

    public long getDate() {
        return imageMeta.getDate();
    }

    public boolean isBiasShot() {
        return isPlaned();
    }

    @Override
    public MetaInfo.ImageMeta getImageMeta() {
        return imageMeta;
    }
    @Override
    public MediaPartItem asPart() {
        return (MediaPartItem) copy();
    }
    @Override
    public List<FrameTags> getVideoTags() {
        return imageMeta.getVideoTags(videoPart);
    }
    @Override
    public MediaResourceItem getItem() {
        return item;
    }
    /*** for debug */
    public String getFullPath() {
        return item.getFilePath() + " ," + videoPart.toString2();
    }
    public float getFacePercent() {
        List<FrameFaceRects> rects = imageMeta.getFaceRects(videoPart);
        if(Predicates.isEmpty(rects)){
            return 0f;
        }
        List<FrameFaceRects> tempList = new ArrayList<>();
        VisitServices.from(rects).visitForQueryList((ffr, param) -> ffr.hasRect(), tempList);
        return  tempList.size() * 1f / rects.size();
    }

    /** indicate current media part can put after target or not. */
    public boolean canPutAfter(MediaPartItem target) {
        long shotTime;
        long targetTime;
        if(getVideoPath().equals(target.getVideoPath())){
            shotTime = getStartTime();
            targetTime = target.getEndTime();
        }else{
            shotTime = getDate();
            targetTime = target.getDate();
        }
        return shotTime >= targetTime;
    }
    /** indicate current media part can put after target or not. */
    public boolean canPutBefore(MediaPartItem target) {
        long shotTime;
        long targetTime;
        if(getVideoPath().equals(target.getVideoPath())){
            shotTime = getEndTime();
            targetTime = target.getStartTime();
        }else{
            shotTime = getDate();
            targetTime = target.getDate();
        }
        return shotTime <= targetTime;
    }
}