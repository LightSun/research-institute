package com.heaven7.ve.colorgap;

import com.heaven7.java.base.util.Predicates;
import com.heaven7.java.base.util.SparseArray;
import com.heaven7.java.base.util.Throwables;
import com.heaven7.java.visitor.PredicateVisitor;
import com.heaven7.java.visitor.ResultVisitor;
import com.heaven7.java.visitor.collection.VisitServices;
import com.heaven7.utils.CollectionUtils;
import com.heaven7.utils.CommonUtils;
import com.heaven7.ve.MediaResourceItem;
import com.heaven7.ve.TimeTraveller;
import com.heaven7.ve.gap.ItemDelegate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * the media part item, Equivalent to a video camera-shot
 * @author heaven7
 */
public class MediaPartItem implements ItemDelegate {

    private static final Integer TAG_ID_BLACK = 20;

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
        imageMeta.setShotKey(getFileName(item) + "_" + videoPart.getStartTime() + "_" + videoPart.getEndTime());
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
    public void setSelectedInStory(boolean selectedInStory) {
        this.selectedInStory = selectedInStory;
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
                ", part_time =" + videoPart +
                ", selectedInStory =" + selectedInStory +
                ", planed =" + planed +
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
        // TODO: 改成“or”
        List<FrameTags> framesTags = getFramesTags();
        if(framesTags == null){
            return 0;
        }
        float score = 0f;
        for(FrameTags ft : framesTags){
            //先对common tag进行分组， 并且只考虑top tags.(WeddingTagIte.description, score)
            Map<String, Float> tagDict = new HashMap<>();
            for (int tagIdx : ft.getTopTagSet()){
                //noun
                Vocabulary.WeddingTagItem item = Vocabulary.getNounWeedingTagItem(tagIdx);
                if(item != null){
                    tagDict.put(item.getDesc(), (float) item.getScore());
                }

                //adj
                item = Vocabulary.getAdjWeddingTagItem(tagIdx);
                if(item != null){
                    tagDict.put(item.getDesc(), (float) item.getScore());
                }
            }
            //然后对每组进行遍历，不重复计算同一个‘中文领域tag’ 对应的多个tag的值
            for (Float val : tagDict.values()){
                score += val;
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
    private void setRawTags() {
        if(imageMeta == null){
            return;
        }
        // video tags
        List<FrameTags> framesTags = getFramesTags();
        if(!Predicates.isEmpty(framesTags)){
            imageMeta.setRawVideoTags(framesTags);

            List<Integer> tags = calculateTags(framesTags);
            List<List<Integer>> tmp_tags = new ArrayList<>();
            tmp_tags.add(tags);
            imageMeta.setTags(tmp_tags);
        }

        //face tags
        List<FrameFaceRects> faceRectsList = imageMeta.getFaceRects(videoPart.getStartTime(), videoPart.getEndTime());
        if(!Predicates.isEmpty(faceRectsList)){
            imageMeta.setRawFaceRects(faceRectsList);
        }
    }

    // 根据镜头的rawVideoTags统计计算tags
    private List<Integer> calculateTags(List<FrameTags> rawVideoTags) {
        final int count = Integer.MAX_VALUE;
        final float minPossibility = 0.8f;
        SparseArray<List<Float>> dict = new SparseArray<>();
        for (FrameTags ft : rawVideoTags){
            List<Tag> topTags = ft.getTopTags(count, minPossibility);
            for(Tag tag : topTags){
                List<Float> value = dict.get(tag.getIndex());
                if(value == null){
                    value = new ArrayList<>();
                    dict.put(tag.getIndex(), value);
                }
                value.add(tag.getPossibility());
            }
        }
        // 统计dict中的tag概率
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
                        return tag.getPossibility() >= minPossibility;
                    }
                }).asListService().sortService(
                        (o1, o2) -> Float.compare(o2.getPossibility(), o1.getPossibility()), true)
               .transformToCollection(null, new ResultVisitor<Tag, Integer>() {
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

    // exclude .
    static String getFileName(MediaResourceItem item) {
        String path = item.getFilePath();
        int index = path.lastIndexOf("/");
        return path.substring(index + 1, path.lastIndexOf("."));
    }
}