package com.heaven7.ve.colorgap;

import com.heaven7.core.util.Logger;
import com.heaven7.java.base.util.Predicates;
import com.heaven7.java.base.util.Throwables;
import com.heaven7.java.visitor.FireIndexedVisitor;
import com.heaven7.java.visitor.collection.VisitServices;
import com.heaven7.utils.CollectionUtils;
import com.heaven7.utils.Context;
import com.heaven7.ve.collect.ColorGapPerformanceCollector;
import com.heaven7.ve.colorgap.filter.ShotKeyFilter;
import com.heaven7.ve.colorgap.filter.ShotTypeFilter;
import com.heaven7.ve.colorgap.impl.ChapterColorGapPostProcessor;
import com.heaven7.ve.gap.GapManager;
import com.heaven7.ve.kingdom.Kingdom;

import java.util.*;

import static com.heaven7.ve.collect.ColorGapPerformanceCollector.MODULE_FILL_PLAID;

/**
 * the chapter.(a chapter correspond one logic-sentence)
 * Created by heaven7 on 2018/5/12 0012.
 */
public class Chapter extends BaseContextOwner{
    private static final String TAG = "Chapter";

    private static final int MIN_SHOT_COUNT_IN_STORY = 1;

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

    public Chapter(Context context, List<CutInfo.PlaidInfo> plaids, List<MediaPartItem> items, int chapterIndex) {
        super(context);
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
    /** @return  the sort rule that this chapter used. */
    public int fill(List<MediaPartItem> airShots, PlaidFiller filler, AirShotFilter airFilter, int lastSortRule) {
        if (air) {
            //for air chapter . process after all chapter fill. here just check plaids.size = 1
            if (plaids.size() != 1) {
                throw new IllegalStateException("for air sentence/chapter can only have one");
            }
            return ShotSortDelegate.SHOT_SORT_RULE_UNKNOWN;
        }
        final int plaidCount = plaids.size();

        //1, 聚合故事/计算出所有的故事
        List<Story> stories = groupStories(items);
        if(stories.isEmpty()){
            //sometimes when no story
            fillDirectly();
            return lastSortRule;
        }

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
            List<MediaPartItem> shots = story.getSortedShots();
            //保留最大得分的镜头
            if (shots.size() > 1) {
                if(shots.get(shots.size() - 1).getDomainTagScore() > 1f){
                    list_to_delete.addAll(shots.subList(0, shots.size() - 1));
                }else{
                    list_to_delete.addAll(shots);
                }
            }
        }
        Collections.sort(list_to_delete, (o1, o2) -> Float.compare(o1.getDomainTagScore(), o2.getDomainTagScore()));
        //4, 音乐格子个数限制
        int shotCount = getTotalShotCount(stories);
        if (shotCount > plaidCount) {
            int toDeleteCount = shotCount - plaidCount;
            for (int i = 0; i < toDeleteCount; i++) {
                if(list_to_delete.isEmpty()){
                    break;
                }
                list_to_delete.remove(0).setSelectedInStory(false, "音乐格子个数限制");
                shotCount--;
            }
            //not delete done, delete more
            if(shotCount > plaidCount) {
                List<MediaPartItem> allShots = getAllShots(stories);
                Collections.sort(allShots, (o1, o2) -> Float.compare(o1.getDomainTagScore(), o2.getDomainTagScore()));
                for(int count = shotCount - plaidCount, i = 0 ; i < count; i ++){
                    allShots.remove(0).setSelectedInStory(false, "音乐格子个数限制_delete_more");
                }
            }
        }
        dump(stories, "delete min score shot");
        // remove empty story
        stories.removeIf(Story::isEmpty);

        //5, 处理偏差镜头
       /* List<MediaPartItem> biasItems = VisitServices.from(items).visitForQueryList(
                (partItem, param) -> partItem.getStoryId() == -1, null);

        //TODO 如果没有偏差镜头， 而且镜头个数不够？ 用空镜头填充？
        if (shotCount == plaidCount) {
            int base = new Random().nextInt(2) == 1 ?  3 : 4;
            replaceByBaisShots(stories, biasItems,  stories.size() / base );
            dump(stories, "after-process  replace by biasItems");
        } else if (shotCount < plaidCount) {
            insertByBaisShots(stories, biasItems, plaidCount);
            shotCount = getTotalShotCount(stories);
            if (shotCount < plaidCount) {
                //TODO 有可能还是不够---, 这个时候 重复插入还是怎么处理？
            }
            dump(stories, "after-process  insert by biasItems");
        }*/
        //reset story id.(change by insert or replace or etc.)
        setAllStoryId(stories);

        //5, 给 MediaPartItem 着色。（shot-key). default by gap
        // 给plaid 着色。
        List<MediaPartItem> shots = getAllShots(stories);
        final int size = Math.min(plaidCount, shots.size());
        for (int i = 0; i < size; i++) {
            CutInfo.PlaidInfo info = plaids.get(i);
            info.addColorFilter(new ShotKeyFilter(new ShotKeyFilter.ShotKeyCondition(
                    shots.get(i).imageMeta.getShotKey())));
        }
        //target shot sort rule
        int sortRule = ShotSortDelegate.getNextSortRule(lastSortRule);
        getPerformanceCollector().addMessage(MODULE_FILL_PLAID,
                TAG, "fill", "sort rule is " + ShotSortDelegate.getRuleString(sortRule));
        //set short type filter
        setShortTypeFilter(plaidCount, sortRule);

        //gap
        ChapterColorGapPostProcessor postProcessor = new ChapterColorGapPostProcessor(this, sortRule);
        filledItems = filler.fillPlaids(getContext(), plaids, shots, postProcessor);
        sortRule = postProcessor.getLastSortRule();

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
        return sortRule;
    }

    /** set short type filter */
    private void setShortTypeFilter(int plaidCount, int sortRule) {
        //add filter of short types.
        ShortTypeParam shortTypeParam = ShortTypeParam.fromTotalCount(plaidCount);
        getPerformanceCollector().addMessage(MODULE_FILL_PLAID,
                TAG, "setShortTypeFilter", "ShortTypeParam is " + shortTypeParam);
        switch (sortRule){
            case ShotSortDelegate.SHOT_SORT_RULE_AESC:
            {
                int mediaumStart = shortTypeParam.getNearCount();
                int longStart = mediaumStart + shortTypeParam.getMediumCount();
                for (int i = 0; i < plaidCount; i++) {
                    CutInfo.PlaidInfo info = plaids.get(i);
                    if (i < mediaumStart) {
                        //near
                        info.addColorFilter(new ShotTypeFilter(new ShotTypeFilter.ShotTypeCondition(MetaInfo.SHOT_TYPE_CLOSE_UP)));
                    } else if (i < longStart) {
                        //medium
                        info.addColorFilter(new ShotTypeFilter(new ShotTypeFilter.ShotTypeCondition(MetaInfo.SHOT_TYPE_MEDIUM_SHOT)));
                    } else { //long shot
                        info.addColorFilter(new ShotTypeFilter(new ShotTypeFilter.ShotTypeCondition(MetaInfo.SHOT_TYPE_LONG_SHORT)));
                    }
                }
            }break;

            case ShotSortDelegate.SHOT_SORT_RULE_DESC:
            {   // 0-long-medium-near
                int mediaumStart = shortTypeParam.getLongCount();
                int nearStart = mediaumStart + shortTypeParam.getMediumCount();
                for (int i = 0; i < plaidCount; i++) {
                    CutInfo.PlaidInfo info = plaids.get(i);
                    if (i < mediaumStart) {
                        //long
                        info.addColorFilter(new ShotTypeFilter(new ShotTypeFilter.ShotTypeCondition(MetaInfo.SHOT_TYPE_LONG_SHORT)));
                    } else if (i < nearStart) {
                        //medium
                        info.addColorFilter(new ShotTypeFilter(new ShotTypeFilter.ShotTypeCondition(MetaInfo.SHOT_TYPE_MEDIUM_SHOT)));
                    } else { //near
                        info.addColorFilter(new ShotTypeFilter(new ShotTypeFilter.ShotTypeCondition(MetaInfo.SHOT_TYPE_CLOSE_UP)));
                    }
                }
            }break;

            default:
                throw new IllegalStateException(" wong shot sort rule = " + ShotSortDelegate.getRuleString(sortRule));
        }
    }

    private void fillDirectly() {
        if(filledItems == null){
            filledItems = new ArrayList<>();
        }
        List<MediaPartItem> newItems = truncateItems(items);
        VisitServices.from(plaids).fireWithIndex(new FireIndexedVisitor<CutInfo.PlaidInfo>() {
            @Override
            public Void visit(Object param, CutInfo.PlaidInfo plaidInfo, int index, int size) {
                filledItems.add(new GapManager.GapItem(plaids.get(index), newItems.get(index)));
                return null;
            }
        });
    }

    private List<MediaPartItem> truncateItems(List<MediaPartItem> items) {
        final int itemSize = items.size();
        final int plaidSize = plaids.size();
        if(itemSize > plaidSize){
            return items.subList(0, plaidSize);
        }else if(itemSize < plaidSize){
            int diff = plaidSize - itemSize;
            List<MediaPartItem> newItems = new ArrayList<>(items);
            for(int i = 0 ; i < diff ; i ++){
                newItems.add(items.get(itemSize -1));
            }
            return newItems;
        }
        return items;
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
       //TODO filter empty story. VisitServices.from(stories)
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
            //偏差镜头只可能出现在故事之间
          /*  Story tail = stories.get(stories.size() - 1);
            //insert last
            if(shot.canPutAfter(tail.getLastShot())){
                Logger.d(TAG, "insertByBaisShots", "insert at last.");
                shot.setPlaned(true);
                Story story = new Story(new ArrayList<>(Arrays.asList(shot)));
                stories.add(story);
                continue;
            }*/
            //insert between stories
            int i = 0, j = 1;
            while (j < stories.size()) {
                MediaPartItem preShot = stories.get(i).getLastShot();
                MediaPartItem nextShot = stories.get(j).getFirstShot();
                //left and right can't be bias shot.
                if(!preShot.isBiasShot() && !nextShot.isBiasShot()) {
                    if(shot.canPutAfter(preShot) && shot.canPutBefore(nextShot)){
                        shot.setPlaned(true);
                        Story story = new Story(new ArrayList<>(Arrays.asList(shot)));
                        stories.add(j, story);
                        break;
                    }
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
            //偏差镜头只可能出现在故事之间
          /*  Story headStory = stories.get(0);
            //try replace head
            if(shot.canPutBefore(headStory.getFirstShot())){
                Logger.d(TAG, "replaceByBaisShots", "replace head.");
                headStory.replaceFirstShot(shot);
                shot.setPlaned(true);
                continue;
            }

            //try replace tail
            Story tail = stories.get(stories.size() - 1);
            if(shot.canPutAfter(tail.getLastShot())){
                Logger.d(TAG, "replaceByBaisShots", "replace tail.");
                tail.replaceLastShot(shot);
                shot.setPlaned(true);
                continue;
            }*/
            //try replace between stories
            int k = 0, j = 1;
            while (j < stories.size()) {
                MediaPartItem preShot = stories.get(k).getLastShot();
                MediaPartItem nextShot = stories.get(j).getFirstShot();
                if(!preShot.isBiasShot() && !nextShot.isBiasShot()){
                    if(shot.canPutAfter(preShot) && shot.canPutBefore(nextShot)){
                        Logger.d(TAG, "replaceByBaisShots", "replace at " + k);
                        stories.get(k).replaceLastShot(shot);
                        shot.setPlaned(true);
                        break;
                    }
                }
                k++;
                j++;
            }
        }
    }

    //将最少2个镜头聚类成故事
    private List<Story> groupStories(List<MediaPartItem> items) {
        ColorGapPerformanceCollector collector = getPerformanceCollector();
        final Kingdom kingdom = getKingdom();

        List<MediaPartItem> shotBuffer = new ArrayList<>();
        Set<Integer> currentTagSet = new HashSet<>();
        List<Story> stories = new ArrayList<>();
        /*
         * 聚合故事条件： 来自同一个视频或者tag相似
         */
        for (MediaPartItem shot : items) {
            List<List<Integer>> tagss = shot.imageMeta.getTags();
            if (Predicates.isEmpty(tagss)) {
                collector.addMessage(MODULE_FILL_PLAID, TAG,
                        "groupStories", "shot has no tags. shot = " + shot);
                continue;
            }
            boolean handled = false;
            if(!shotBuffer.isEmpty()){
                MediaPartItem lastItem = shotBuffer.get(shotBuffer.size() - 1);
                if(lastItem.getItem().getFilePath().equals(shot.getItem().getFilePath())){
                    handled = true;
                }
            }
            if (!handled && currentTagSet.size() > 0) {
                //如果tags 和 当前的tag set 相似度小于1/3. 则故事已找到。
                Set<Integer> intersectSet = CollectionUtils.intersection(currentTagSet, tagss.get(0));
                int intersectCount = intersectSet.size();
                //主词，不参加故事分类
                for(Integer index : intersectSet){
                    if(kingdom.isSubjectTag(index)){
                        intersectCount --;
                    }
                }
                if (intersectCount == 0
                        || (currentTagSet.size() > 0 && intersectCount * 1f / currentTagSet.size() < 0.3f)) {
                    if (shotBuffer.size() >= MIN_SHOT_COUNT_IN_STORY) {
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
        if (shotBuffer.size() >= MIN_SHOT_COUNT_IN_STORY) {
            Story story = new Story(new ArrayList<>(shotBuffer));
            stories.add(story);
        }
        //set id
        setAllStoryId(stories);
        return stories;
    }

    private void setAllStoryId(List<Story> stories) {
        for (int i = 0, size = stories.size(); i < size; i++) {
            Story story = stories.get(i);
            story.setStoryId(i);
            story.setChapterIndex(chapterIndex);
        }
    }

    private void dump(List<Story> stories, String tag) {
        ColorGapPerformanceCollector collector = getPerformanceCollector();
        for (Story story : stories) {
            //Logger.d(TAG + "__" + chapterIndex, "dump", tag + " >>> " + story);
            collector.addMessage(MODULE_FILL_PLAID, TAG, "dump_"+ chapterIndex, tag + " >>> " + story);
        }
    }

    private static int getTotalShotCount(List<Story> stories) {
        int total = 0;
        for (Story story : stories) {
            total += story.getSortedShots().size();
        }
        return total;
    }

    private static List<MediaPartItem> getAllShots(List<Story> stories) {
        List<MediaPartItem> shots = new ArrayList<>();
        for (Story story : stories) {
            shots.addAll(story.getShots(false));
        }
        return shots;
    }

    public List<Story> getAllStory() {
        return mStories;
    }
}
