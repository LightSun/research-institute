package com.heaven7.ve.colorgap;


import com.heaven7.core.util.Logger;
import com.heaven7.java.base.util.Throwables;
import com.heaven7.java.visitor.PredicateVisitor;
import com.heaven7.java.visitor.Visitors;
import com.heaven7.java.visitor.collection.VisitServices;
import com.heaven7.utils.TextUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 1 video -> multi chapter.<br>
 * 1 chapter -> multi story.<br>
 * 1 story -> multi shots(MediaPartItem).<br>
 */
public class Story {
    private static final String TAG = "Story";

    private final List<MediaPartItem> items;
    private int storyId;

    public Story(List<MediaPartItem> items) {
        Throwables.checkEmpty(items);
        this.items = items;
    }

    public void setStoryId(int storyId) {
        this.storyId = storyId;
        for (MediaPartItem item : items) {
            item.setStoryId(storyId);
        }
    }
    public long getStartTime(){
        return filterAndSortShots(Visitors.truePredicateVisitor(),false)
                .get(0).imageMeta.getDate();
    }
    public long getEndTime(){
        List<MediaPartItem> list = filterAndSortShots(Visitors.truePredicateVisitor(), false);
        return list.get(list.size() - 1).imageMeta.getDate();
    }

    public void replaceLastShot(MediaPartItem shot) {
        List<MediaPartItem> shots = getShots(Visitors.truePredicateVisitor());
        int index = shots.size() - 1;
        MediaPartItem preShot = shots.get(shots.size() - 1);
        if(preShot.isPlaned()){
            throw new IllegalStateException("already planed by bias shot.");
        }
        items.set(index, shot);
        shot.setStoryId(getStoryId());
        Logger.d(TAG, "replaceLastShot", "index("+ index +") of story. old shot = "
                + preShot + " ,new shot = " + shot);
    }
    public void replaceFirstShot(MediaPartItem shot) {
        List<MediaPartItem> shots = getShots(Visitors.truePredicateVisitor());
        MediaPartItem preShot = shots.get(0);
        int index = 0;
        items.set(index, shot);
        shot.setStoryId(getStoryId());
        Logger.d(TAG, "replaceFirstShot", "index("+ index +") of story. old shot = "
                + preShot + " ,new shot = " + shot);
    }
    public boolean replaceShot(MediaPartItem oldItem, MediaPartItem newItem) {
        int index = items.indexOf(oldItem);
        if(index >=0 ){
            items.set(index, newItem);
            newItem.setStoryId(getStoryId());
            return true;
        }
        return false;
    }

    public List<MediaPartItem> getShots() {
        return getShots(Visitors.truePredicateVisitor());
    }

    public List<MediaPartItem> getShots(PredicateVisitor<MediaPartItem> visitor) {
        return filterAndSortShots(visitor, false);
    }
    public List<MediaPartItem> getItems() {
        return items;
    }

    public int getStoryId() {
        return storyId;
    }

    // 过滤规则：
    // 1. 得分过于低的: < 0.5（或者某个动态计算的值）
    // 2. 内容相似的画面不连续超过5个
    // 3. 镜头类型同级的镜头不连续超过3个；如果是非人脸类型，不得超过2个
    // 4. 两个相邻镜头：得分、镜头类型都相等，则高概率是重复镜头，只保留最后一个
    public void filterByRules() {
         //1, 过滤分数太低的
        VisitServices.from(items).visitForQueryList(new PredicateVisitor<MediaPartItem>() {
            @Override
            public Boolean visit(MediaPartItem partItem, Object param) {
                boolean selected = partItem.getDomainTagScore() >= 0.5f;
                partItem.setSelectedInStory(selected);
                return selected;
            }
        }, null);

        //2, 镜头类型相同的镜头 不连续超过3个， 非人脸类型，不得超过2个
        //   先处理镜头，再出路相似的画面。
        int[] shotTypes = { MetaInfo.SHOT_TYPE_BIG_LONG_SHORT,
                MetaInfo.SHOT_TYPE_LONG_SHORT,
                MetaInfo.SHOT_TYPE_MEDIUM_LONG_SHOT,
                MetaInfo.SHOT_TYPE_MEDIUM_SHOT,
                MetaInfo.SHOT_TYPE_CLOSE_UP,
                MetaInfo.SHOT_TYPE_MEDIUM_CLOSE_UP,
                MetaInfo.SHOT_TYPE_BIG_CLOSE_UP,
        };
        for(final int shotType : shotTypes){
            List<MediaPartItem> list = filterAndSortShots(new PredicateVisitor<MediaPartItem>() {
                @Override
                public Boolean visit(MediaPartItem partItem, Object param) {
                    if(TextUtils.isEmpty(partItem.imageMeta.getShotType())){
                        return false;
                    }
                    return MetaInfo.getShotTypeFrom(partItem.imageMeta.getShotType()) == shotType;
                }
            }, false);
            int limit = 3;
            if(list.size() > 0 && list.get(0).imageMeta.getMainFaceCount() < 1){
                limit = 2;
            }
            int size = list.size();
            if(size > limit){
                for(int i = 0 ; i < size - limit ; i++){
                    list.get(i).setSelectedInStory(false);
                }
            }
        }
        //3, 内容相似的画面不超过5个
        deleteShotsByLimit(Visitors.truePredicateVisitor(), 5);

        //4, 重复镜头. 得分，类型都相同，则只保留1个
        List<MediaPartItem> partItems = filterAndSortShots(Visitors.truePredicateVisitor(), false);
        int size = partItems.size();
        if(size > 1){
            int i = 0;
            int j = 1;
            while ( j < size){
                MediaPartItem item1 = partItems.get(i);
                MediaPartItem item2 = partItems.get(j);
                if(item1.getDomainTagScore() == item2.getDomainTagScore() &&
                       MetaInfo.getShotTypeFrom(item1.imageMeta.getShotType()) ==
                               MetaInfo.getShotTypeFrom(item2.imageMeta.getShotType())){
                    item1.setSelectedInStory(false);
                }
                i++;
                j++;
            }
        }
    }

    // AESC (up)
    private List<MediaPartItem> filterAndSortShots(PredicateVisitor<MediaPartItem> predicate, boolean includeUnselect){
        List<MediaPartItem> out = new ArrayList<>();
        VisitServices.from(items).subService(new PredicateVisitor<MediaPartItem>() {
            @Override
            public Boolean visit(MediaPartItem partItem, Object param) {
                if(!includeUnselect && !partItem.isSelectedInStory()){
                    return false;
                }
                return predicate.visit(partItem, param);
            }
        }).asListService().sortService(new Comparator<MediaPartItem>() {
            @Override
            public int compare(MediaPartItem o1, MediaPartItem o2) {
                return Float.compare(o1.getDomainTagScore(), o2.getDomainTagScore());
            }
        }).save(out);
        return out;
    }

    private void deleteShotsByLimit(PredicateVisitor<MediaPartItem> predicate, int limit){
        List<MediaPartItem> selectItems = filterAndSortShots(predicate, false);
        int size = selectItems.size();
        if(size > limit){
            for(int i = 0 ; i < size - limit ; i++){
                selectItems.get(i).setSelectedInStory(false);
            }
        }
    }

    @Override
    public String toString() {
        return "Story{" +
                "storyId=" + storyId +
                ", items=" + items +
                '}';
    }

}