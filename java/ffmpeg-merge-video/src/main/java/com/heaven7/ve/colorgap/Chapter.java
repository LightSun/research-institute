package com.heaven7.ve.colorgap;

import com.heaven7.core.util.Logger;
import com.heaven7.java.base.util.Predicates;
import com.heaven7.java.base.util.Throwables;
import com.heaven7.java.visitor.PredicateVisitor;
import com.heaven7.java.visitor.Visitors;
import com.heaven7.java.visitor.collection.VisitServices;
import com.heaven7.utils.CollectionUtils;
import com.heaven7.ve.colorgap.filter.ShotKeyFilter;
import com.heaven7.ve.gap.GapManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * the chapter.(a chapter correspond one logic-sentence)
 * Created by heaven7 on 2018/5/12 0012.
 */
public class Chapter {
    private static final String TAG = "Chapter";

    private final List<CutInfo.PlaidInfo> plaids;
    private final List<MediaPartItem> items;
    private final int chapterIndex;

    /**
     * the air plaids which comes from {@linkplain #plaids}
     */
    private List<CutInfo.PlaidInfo> airPlaids;
    private boolean air;

    private List<GapManager.GapItem> filledItems;
    private List<Story> mStories;

    public Chapter(List<CutInfo.PlaidInfo> plaids, List<MediaPartItem> items, int chapterIndex) {
        this.plaids = plaids;
        this.items = items;
        this.chapterIndex = chapterIndex;

        for (CutInfo.PlaidInfo plaid : plaids) {
            if (plaid.isAir()) {
                addAirPlaid(plaid);
            }
        }
    }
    /** in mills */
    public long getFilledEndTime(){
        GapManager.GapItem lastItem = filledItems.get(filledItems.size() - 1);
        MediaPartItem item = (MediaPartItem)lastItem.item;
        return item.imageMeta.getDate();
    }
    /** in mills */
    public long getFilledStartTime(){
        GapManager.GapItem first = filledItems.get(0);
        MediaPartItem item = (MediaPartItem)first.item;
        return item.imageMeta.getDate();
    }

    private void addAirPlaid(CutInfo.PlaidInfo plaid) {
        if (airPlaids == null) {
            airPlaids = new ArrayList<>();
        }
        airPlaids.add(plaid);
    }

    public boolean isAir() {
        return air;
    }
    public void setAir(boolean air) {
        this.air = air;
    }

    public int getChapterIndex() {
        return chapterIndex;
    }

    public List<GapManager.GapItem> getFilledItems() {
        return filledItems != null? filledItems : Collections.emptyList();
    }
    public CutInfo.PlaidInfo getAirPlaid() {
        Throwables.checkEmpty(airPlaids);
        return airPlaids.get(0);
    }
    public void addFilledItem(GapManager.GapItem gapItem) {
        if(filledItems == null){
            filledItems = new ArrayList<>();
        }
        filledItems.add(gapItem);
    }
    public void fill(List<MediaPartItem> airShots, PlaidFiller filler, AirShotFilter airFilter) {
        if (air) {
            //for air chapter . process after all chapter fill. here just check plaids.size = 1
            if (plaids.size() != 1) {
                throw new IllegalStateException("for air sentence/chapter can only have one");
            }
            return;
        }
        final int plaidCount = plaids.size();

        //1, 聚合故事/计算出所有的故事
        List<Story> stories = groupStories(items);
        this.mStories = stories;
        dump(stories, "group stoyies");
        // 2. 对每个故事，根据基本规则进行镜头过滤
        for (Story story : stories) {
            story.filterByRules();
        }
        dump(stories, "story.filterByRules()");
        //3, 保证故事的框架完整性的前提下， 删除低分镜头
        List<MediaPartItem> list_to_delete = new ArrayList<>();
        for (Story story : stories) {
            List<MediaPartItem> shots = story.getShots(Visitors.truePredicateVisitor());
            //保留最大得分的镜头
            if (shots.size() > 1) {
                list_to_delete.addAll(shots.subList(0, shots.size() - 1));
            }
        }
        Collections.sort(list_to_delete, new Comparator<MediaPartItem>() {
            @Override
            public int compare(MediaPartItem o1, MediaPartItem o2) {
                return Float.compare(o1.getDomainTagScore(), o2.getDomainTagScore());
            }
        });
        //4, 音乐格子个数限制
        int shotCount = getTotalShotCount(stories);
        if (shotCount > plaidCount) {
            int toDeleteCount = shotCount - plaidCount;
            for (int i = 0; i < toDeleteCount; i++) {
                list_to_delete.get(i).setSelectedInStory(false);
                shotCount--;
            }
        }
        dump(stories, "delete min score shot");
        //5, 处理偏差镜头
        List<MediaPartItem> biasItems = VisitServices.from(items).visitForQueryList(
                new PredicateVisitor<MediaPartItem>() {
                    @Override
                    public Boolean visit(MediaPartItem partItem, Object param) {
                        return partItem.getStoryId() == -1;
                    }
                }, null);
        //TODO 如果没有偏差镜头， 而且镜头个数不够？ 用空镜头填充？
        if (shotCount == plaidCount) {
            replaceByBaisShots(stories, biasItems, 3);
            dump(stories, "after-process  replace by biasItems");
        } else if (shotCount < plaidCount) {
            insertByBaisShots(stories, biasItems, plaidCount);
            shotCount = getTotalShotCount(stories);
            if (shotCount < plaidCount) {
                //TODO 有可能还是不够---, 这个时候 重复插入还是怎么处理？
            }
            dump(stories, "after-process  insert by biasItems");
        }

        //5, 给 MediaPartItem 着色。（shot-key). default by gap
        // 给plaid 着色。
        List<MediaPartItem> shots = getAllShots(stories);
        final int size = Math.min(plaidCount, shots.size());
        for (int i = 0; i < size; i++) {
            CutInfo.PlaidInfo info = plaids.get(i);
            info.addColorFilter(new ShotKeyFilter(new ShotKeyFilter.ShotKeyCondition(
                    shots.get(i).imageMeta.getShotKey())));
        }
        //gap
        filledItems = filler.fillPlaids(plaids, shots);

        //process air-plaids 空镜头只会替换偏差镜头.(按照顺序 - 不能破坏故事的结构)
        if (!Predicates.isEmpty(airPlaids)) {
            List<GapManager.GapItem> filledBiasShots = getBiasGapItems(filledItems);
            for(int i = 0 , count = Math.min(airPlaids.size(), filledBiasShots.size()) ;
                    i < count ; i ++){
                //air-shot replace bias-shot
                GapManager.GapItem gapItem = filledBiasShots.get(i);
                MediaPartItem item = airFilter.filter((CutInfo.PlaidInfo)gapItem.plaid,
                        (MediaPartItem) gapItem.item, airShots);
                if(item != null) {
                    MediaPartItem oldItem = (MediaPartItem) gapItem.setItemDelegate(item);
                    Story story = getStory(oldItem.getStoryId());
                    story.replaceShot(oldItem, item);
                    //airPlaids.get(i).setAir(false);
                }
            }
        }

        dump(stories, "at last");
    }

    private Story getStory(int storyId) {
        for(Story story : mStories){
            if(story.getStoryId() == storyId){
                return story;
            }
        }
        return null;
    }

    //偏差镜头的 gap item
    private List<GapManager.GapItem> getBiasGapItems(List<GapManager.GapItem> filledItems) {
        return VisitServices.from(filledItems).visitForQueryList((gapItem, param) -> {
            MediaPartItem mpi = (MediaPartItem) gapItem.item;
            return mpi.isPlaned();
        }, null);
    }

    private void insertByBaisShots(List<Story> stories, List<MediaPartItem> biasItems, int musicSize) {
        // DESC
        Collections.sort(biasItems, new Comparator<MediaPartItem>() {
            @Override
            public int compare(MediaPartItem o1, MediaPartItem o2) {
                return Float.compare(o2.getDomainTagScore(), o1.getDomainTagScore());
            }
        });
        for (MediaPartItem shot : biasItems) {
            if (getTotalShotCount(stories) == musicSize) {
                break;
            }
            if (shot.isPlaned()) {
                continue;
            }
            Story tail = stories.get(stories.size() - 1);
            long ctime = shot.imageMeta.getDate();
            long endTime = tail.getEndTime();
            //insert last
            if (ctime >= endTime) {
                Logger.d(TAG, "insertByBaisShots", "insert at last.");
                shot.setPlaned(true);
                Story story = new Story(new ArrayList<>(Arrays.asList(shot)));
                stories.add(story);
                continue;
            }
            //insert between stories
            int i = 0, j = 1;
            while (j < stories.size()) {
                long time0 = stories.get(i).getEndTime();
                long time1 = stories.get(j).getStartTime();
                if (ctime >= time0 && ctime <= time1) {
                    shot.setPlaned(true);
                    Story story = new Story(new ArrayList<>(Arrays.asList(shot)));
                    stories.add(j, story);
                    break;
                }
                i += 1;
                j += 1;
            }
        }
    }

    // 当故事填满音乐格子时，用偏差镜头替换原有的故事镜头
    //biasItems : 偏差镜头 或者空镜头（如果没有偏差镜头）
    private void replaceByBaisShots(List<Story> stories, List<MediaPartItem> biasItems, int maxReplaceCount) {
        // DESC
        Collections.sort(biasItems, new Comparator<MediaPartItem>() {
            @Override
            public int compare(MediaPartItem o1, MediaPartItem o2) {
                return Float.compare(o2.getDomainTagScore(), o1.getDomainTagScore());
            }
        });
        final int count = Math.min(maxReplaceCount, biasItems.size());
        for (int i = 0; i < count; i++) {
            MediaPartItem shot = biasItems.get(i);
            if (shot.isPlaned()) {
                continue;
            }
            Story headStory = stories.get(0);
            //try insert head
            long ctime = shot.imageMeta.getDate();
            long headTime = headStory.getStartTime();
            if (ctime < headTime) {
                Logger.d(TAG, "replaceByBaisShots", "replace head.");
                shot.setPlaned(true);
                headStory.replaceFirstShot(shot);
                continue;
            }
            //try insert tail
            Story tail = stories.get(stories.size() - 1);
            long tailTime = tail.getEndTime();
            if (ctime >= tailTime) {
                Logger.d(TAG, "replaceByBaisShots", "replace tail.");
                shot.setPlaned(true);
                tail.replaceLastShot(shot);
                continue;
            }
            //try insert between stories
            int k = 0, j = 1;
            while (j < stories.size()) {
                long time0 = stories.get(k).getEndTime();
                long time1 = stories.get(j).getStartTime();
                if (ctime >= time0 && ctime <= time1) {
                    Logger.d(TAG, "replaceByBaisShots", "replace at " + k);
                    shot.setPlaned(true);
                    stories.get(k).replaceLastShot(shot);
                    break;
                }
                k++;
                j++;
            }
        }
    }

    //将最少2个镜头聚类成故事
    private List<Story> groupStories(List<MediaPartItem> items) {
        List<MediaPartItem> shotBuffer = new ArrayList<>();
        Set<Integer> currentTagSet = new HashSet<>();
        List<Story> stories = new ArrayList<>();
        for (MediaPartItem shot : items) {
            List<List<Integer>> tagss = shot.imageMeta.getTags();
            if (Predicates.isEmpty(tagss)) {
                continue;
            }
            if (currentTagSet.size() > 0) {
                //如果tags 和 当前的tag set 相似度小于1/3. 则故事已找到。
                Set<Integer> intersectSet = CollectionUtils.intersection(currentTagSet, tagss.get(0));
                if (intersectSet.size() == 0
                        || (currentTagSet.size() > 0 && intersectSet.size() * 1f / currentTagSet.size() < 0.3f)) {
                    if (shotBuffer.size() >= 2) {
                        Story story = new Story(new ArrayList<>(shotBuffer));
                        stories.add(story);
                    }
                    shotBuffer.clear();
                    currentTagSet.clear();
                }
            }

            //update buffer and currentTagSet
            shotBuffer.add(shot);
            if (currentTagSet.isEmpty()) {
                currentTagSet = new HashSet<>(tagss.get(0));
            } else {
                currentTagSet = CollectionUtils.intersection(currentTagSet, tagss.get(0));
            }
        }

        //process Fragmentary shots
        if (shotBuffer.size() >= 2) {
            Story story = new Story(new ArrayList<>(shotBuffer));
            stories.add(story);
        }
        //set id
        for (int i = 0, size = stories.size(); i < size; i++) {
            stories.get(i).setStoryId(i);
        }
        return stories;
    }

    private void dump(List<Story> stories, String tag) {
        if(StoryLineShader.DEBUG) {
            for (Story story : stories) {
                Logger.d(TAG + "__" + chapterIndex, "dump", tag + " >>> " + story);
            }
        }
    }

    private static int getTotalShotCount(List<Story> stories) {
        int total = 0;
        for (Story story : stories) {
            total += story.getShots().size();
        }
        return total;
    }

    private static List<MediaPartItem> getAllShots(List<Story> stories) {
        List<MediaPartItem> shots = new ArrayList<>();
        for (Story story : stories) {
            shots.addAll(story.getShots());
        }
        return shots;
    }

}
