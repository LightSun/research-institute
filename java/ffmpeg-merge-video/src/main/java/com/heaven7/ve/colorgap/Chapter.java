package com.heaven7.ve.colorgap;

import com.heaven7.java.base.util.Logger;
import com.heaven7.java.base.util.Predicates;
import com.heaven7.java.base.util.Throwables;
import com.heaven7.java.visitor.FireIndexedVisitor;
import com.heaven7.java.visitor.PredicateVisitor;
import com.heaven7.java.visitor.ResultVisitor;
import com.heaven7.java.visitor.collection.VisitServices;
import com.heaven7.utils.CollectionUtils;
import com.heaven7.utils.Context;
import com.heaven7.utils.FileUtils;
import com.heaven7.ve.collect.ColorGapPerformanceCollector;
import com.heaven7.ve.colorgap.filter.ShotKeyFilter;
import com.heaven7.ve.colorgap.filter.ShotTypeFilter;
import com.heaven7.ve.colorgap.impl.ChapterColorGapPostProcessor;
import com.heaven7.ve.colorgap.impl.filler.BasePlaidFiller;
import com.heaven7.ve.colorgap.impl.filler.StageFiller;
import com.heaven7.ve.cross_os.IPlaidInfo;
import com.heaven7.ve.gap.GapManager;
import com.heaven7.ve.kingdom.Kingdom;

import java.io.File;
import java.util.*;

import static com.heaven7.ve.collect.ColorGapPerformanceCollector.MODULE_FILL_PLAID;

/**
 * the chapter.(a chapter correspond one logic-sentence)
 * Created by heaven7 on 2018/5/12 0012.
 */
public class Chapter extends BaseContextOwner{
    private static final String TAG = "Chapter";

    private static final int MIN_SHOT_COUNT_IN_STORY = 1;

    private final List<IPlaidInfo> plaids;
    private final List<MediaPartItem> items;
    private final int chapterIndex;

    /**
     * the air plaids which comes from {@linkplain #plaids}
     */
    private List<IPlaidInfo> airPlaids;
    private boolean air;

    private List<GapManager.GapItem> filledItems = new ArrayList<>();
    private List<Story> mStories;

    public Chapter(Context context, List<IPlaidInfo> plaids, List<MediaPartItem> items, int chapterIndex) {
        super(context);
        this.plaids = plaids;
        this.items = items;
        this.chapterIndex = chapterIndex;

        for (IPlaidInfo plaid : plaids) {
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

    private void addAirPlaid(IPlaidInfo plaid) {
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
    public IPlaidInfo getAirPlaid() {
        Throwables.checkEmpty(airPlaids);
        return airPlaids.get(0);
    }
    public void addFilledItem(GapManager.GapItem gapItem) {
        if(filledItems == null){
            filledItems = new ArrayList<>();
        }
        filledItems.add(gapItem);
    }
    public int getPlaidCount(){
        return plaids.size();
    }
    /** fill the plaids by normal */
    public int fill(PlaidFiller filler, int lastSortRule){
        //target shot sort rule
        int sortRule = ShotSortDelegate.getNextSortRule(lastSortRule);
        getPerformanceCollector().addMessage(MODULE_FILL_PLAID,
                TAG, "fill", "sort rule is " + ShotSortDelegate.getRuleString(sortRule));
        //set short type filter
        setShortTypeFilter(sortRule);

        ChapterColorGapPostProcessor postProcessor = new ChapterColorGapPostProcessor(sortRule);
        filledItems = filler.fillPlaids(getContext(), plaids, items, postProcessor);
        return postProcessor.getLastSortRule();
    }

    /**
     * fill the items based on 'story-line'
     * @return  the sort rule that this chapter used. */
    public int fillStoryLine(List<MediaPartItem> airShots, PlaidFiller filler, AirShotFilter airFilter, int lastSortRule) {
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
                if(shots.get(shots.size() - 1).getTotalScore() > 1f){
                    list_to_delete.addAll(shots.subList(0, shots.size() - 1));
                }else{
                    list_to_delete.addAll(shots);
                }
            }
        }
        Collections.sort(list_to_delete, (o1, o2) -> Float.compare(o1.getTotalScore(), o2.getTotalScore()));
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
                Collections.sort(allShots, (o1, o2) -> Float.compare(o1.getTotalScore(), o2.getTotalScore()));
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
            IPlaidInfo info = plaids.get(i);
            info.addColorFilter(new ShotKeyFilter(new ShotKeyFilter.ShotKeyCondition(
                    shots.get(i).imageMeta.getShotKey())));
        }
        //set short type filter
        //setShortTypeFilter(plaidCount, sortRule);

        //gap
       // ChapterColorGapPostProcessor postProcessor = new ChapterColorGapPostProcessor(sortRule);
        filledItems = filler.fillPlaids(getContext(), plaids, shots, null);
       // sortRule = postProcessor.getLastSortRule();

        //process air-plaids 空镜头只会替换偏差镜头.(按照顺序 - 不能破坏故事的结构)
      /*  if (!Predicates.isEmpty(airPlaids)) {
            List<GapManager.GapItem> filledBiasShots = getBiasGapItems(filledItems);
            for(int i = 0 , count = Math.min(airPlaids.size(), filledBiasShots.size()) ;
                    i < count ; i ++){
                //air-shot replace bias-shot
                GapManager.GapItem gapItem = filledBiasShots.get(i);
                MediaPartItem item = airFilter.filter((IPlaidInfo)gapItem.plaid,
                        (MediaPartItem) gapItem.item, airShots);
                if(item != null) {
                    MediaPartItem oldItem = (MediaPartItem) gapItem.setItemDelegate(item);
                    Story story = getStory(oldItem.getStoryId());
                    story.replaceShot(oldItem, item);
                    //airPlaids.get(i).setAir(false);
                }
            }
        }*/

        dump(stories, "at last");
        return lastSortRule;
    }

    /** set short type filter */
    public void setShortTypeFilter(int sortRule) {
        final int plaidCount = getPlaidCount();
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
                    IPlaidInfo info = plaids.get(i);
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
                    IPlaidInfo info = plaids.get(i);
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
        VisitServices.from(plaids).fireWithIndex(new FireIndexedVisitor<IPlaidInfo>() {
            @Override
            public Void visit(Object param, IPlaidInfo plaidInfo, int index, int size) {
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
                return Float.compare(o2.getTotalScore(), o1.getTotalScore());
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
                return Float.compare(o2.getTotalScore(), o1.getTotalScore());
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

    //----------------------------------------------------------

    public List<IPlaidInfo> getLeftPlaids() {
        List<IPlaidInfo> filledPlaids = getFilledPlaids();
        return VisitServices.from(this.plaids).filter(new PredicateVisitor<IPlaidInfo>() {
            @Override
            public Boolean visit(IPlaidInfo info, Object param) {
                return !filledPlaids.contains(info);
            }
        }).getAsList();
    }
    public void fillMatches(StageFiller filler) {
        BasePlaidFiller.GapCallbackImpl impl = new BasePlaidFiller.GapCallbackImpl(getContext(), plaids, items);
        filler.fill(getContext(), plaids, items, impl);
        List<GapManager.GapItem> filledItems = impl.getFilledItems();
        this.filledItems.addAll(filledItems);
    }

    public void sortByPlaid(){
        VisitServices.from(filledItems).sortService(new Comparator<GapManager.GapItem>() {
            @Override
            public int compare(GapManager.GapItem o1, GapManager.GapItem o2) {
                int index1 = plaids.indexOf(o1.plaid);
                int index2 = plaids.indexOf(o2.plaid);
                return Integer.compare(index1, index2);
            }
        });
    }

    public void adjustTime(){
        VEGapUtils.adjustTime(getContext(), filledItems);
    }

    public int sortRules(int lastShotTypeRule){
        if(plaids.size() != filledItems.size()){
            throw new IllegalStateException("fill failed. plaid.size = " + plaids.size() + " ,filledItem.size = " + filledItems.size());
        }
        //debug filled plaid
        if(isDebug() && getContext().getTestType() == ColorGapContext.TEST_TYPE_LOCAL
                && !Predicates.isEmpty(getDebugParam().getOutputDir())){
            //log
            StringBuilder sb = new StringBuilder();
            sb.append("after fill before sort. chapter_index = ")
                    .append(getChapterIndex())
                    .append("\n");
            VisitServices.from(filledItems).fireWithIndex(new FireIndexedVisitor<GapManager.GapItem>() {
                @Override
                public Void visit(Object param, GapManager.GapItem gapItem, int index, int size) {
                    IPlaidInfo info = (IPlaidInfo) gapItem.plaid;
                    MediaPartItem item = (MediaPartItem) gapItem.item;
                    sb.append("plaid: ").append(info.getFilterString()).append("\n");
                    sb.append("item: ").append(item.toString()).append("\n");
                    sb.append("\r\n");
                    return null;
                }
            });
            FileUtils.writeTo(new File(getDebugParam().getOutputDir(),
                    "chapter__" + getChapterIndex() + ".txt"), sb.toString());
        }

        List<MediaPartItem> list = getFilledMediaPartItems();
        //sort by shot type.
        int rule = ShotSortDelegate.getNextSortRule(lastShotTypeRule);
        ChapterColorGapPostProcessor processor = new ChapterColorGapPostProcessor(rule);
        List<MediaPartItem> resultItems = processor.onPostProcess(getContext(), list);
        //handle after sort.
        VisitServices.from(filledItems).fireWithIndex(new FireIndexedVisitor<GapManager.GapItem>() {
            @Override
            public Void visit(Object param, GapManager.GapItem gapItem, int index, int size) {
                gapItem.item = resultItems.get(index);
                return null;
            }
        });
        getPerformanceCollector().addMessage(MODULE_FILL_PLAID, TAG, "sortRules", "input lastShotRule("
                        + ShotSortDelegate.getRuleString(lastShotTypeRule)
                        + ") ,output lastSortRule("
                        + ShotSortDelegate.getRuleString(processor.getLastSortRule())  +") ");
        return processor.getLastSortRule();
    }

    public void receiveGapItems(List<GapManager.GapItem> gapItems) {
        List<GapManager.GapItem> list = VisitServices.from(gapItems).filter(
                new PredicateVisitor<GapManager.GapItem>() {
            @Override
            public Boolean visit(GapManager.GapItem gapItem, Object param) {
                return plaids.contains(gapItem.plaid);
            }
        }).getAsList();
        this.filledItems.addAll(list);
    }

    public List<MediaPartItem> getFilledMediaPartItems() {
        return VisitServices.from(filledItems).map(
                new ResultVisitor<GapManager.GapItem, MediaPartItem>() {
                    @Override
                    public MediaPartItem visit(GapManager.GapItem gapItem, Object param) {
                        return (MediaPartItem) gapItem.item;
                    }
                }).getAsList();
    }
    public List<IPlaidInfo> getFilledPlaids() {
        return VisitServices.from(filledItems).map(
                new ResultVisitor<GapManager.GapItem, IPlaidInfo>() {
                    @Override
                    public IPlaidInfo visit(GapManager.GapItem gapItem, Object param) {
                        return (IPlaidInfo) gapItem.plaid;
                    }
                }).getAsList();
    }

}
