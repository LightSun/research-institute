package com.heaven7.ve.colorgap.impl;

import com.heaven7.core.util.Logger;
import com.heaven7.java.visitor.FireIndexedVisitor;
import com.heaven7.java.visitor.PredicateVisitor;
import com.heaven7.java.visitor.collection.VisitServices;
import com.heaven7.utils.Context;
import com.heaven7.utils.FileUtils;
import com.heaven7.ve.collect.ColorGapPerformanceCollector;
import com.heaven7.ve.colorgap.*;
import com.heaven7.ve.colorgap.impl.filler.*;
import com.heaven7.ve.gap.GapManager;
import com.heaven7.ve.template.VETemplate;
import com.heaven7.ve.utils.Flags;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 *
 * Created by heaven7 on 2018/5/12 0012.
 */

public class StoryLineShaderImpl implements StoryLineShader {

    private static final String TAG = "StoryLineShaderImpl";
    private static final Comparator<MediaPartItem> TIME_COMPARATOR
            = (Comparator<MediaPartItem>) (o1, o2) -> Long.compare(o1.getDate(), o2.getDate());

    @Override
    public List<GapManager.GapItem> tintAndFill(ColorGapContext context, List<CutInfo.PlaidInfo> plaids, VETemplate template, List<MediaPartItem> items,
                                                PlaidFiller filler, AirShotFilter filter) {
        if(DEBUG) {
            Logger.d(TAG, "tintAndFill", ">>> start story shader...items.size = " + items.size());
        }
        //normal fill
        int lastSortRule = ShotSortDelegate.SHOT_SORT_RULE_UNKNOWN;
        List<Chapter> chapters;
        switch (template.getChapterFillType()){
            case VETemplate.FILL_TYPE_STORY_LINE:
                chapters = groupChapterForStoryLine(context, template, items);
                for (Chapter chapter : chapters){
                    lastSortRule = chapter.fillStoryLine(null, filler, filter, lastSortRule);
                }
                break;
            case VETemplate.FILL_TYPE_FILTER_THEN_OTHERS:
                //1, all chapter: fill matches, 2, all chapter fill left. 3 all chapter fill by reuse.
                //1, fill by match
                MatchStageFiller matcher = new MatchStageFiller();
                chapters = groupChapterForFilterThenOthers(context, template, items);
                for (Chapter chapter : chapters){
                    chapter.fillMatches(matcher);
                }
                //left plaids and items
                List<CutInfo.PlaidInfo> leftPlaids = new ArrayList<>();
                List<MediaPartItem> leftItems = new ArrayList<>(items);
                MaxScoreStageFiller maxScore = new MaxScoreStageFiller();
                for (Chapter chapter : chapters){
                    leftPlaids.addAll(chapter.getLeftPlaids());
                    leftItems.removeAll(chapter.getFilledMediaPartItems());
                }
                //2, has left plaids. max score gap.
                if(leftPlaids.size() > 0) {
                    BasePlaidFiller.GapCallbackImpl gapCallback = new BasePlaidFiller.GapCallbackImpl(context, leftPlaids, leftItems);
                    maxScore.fill(context, leftPlaids, leftItems, gapCallback);
                    List<GapManager.GapItem> gapItems = gapCallback.getFilledItems();
                    for (Chapter chapter : chapters) {
                        chapter.receiveGapItems(gapItems);
                    }
                }
                //left plaids
                leftPlaids.clear();
                for (Chapter chapter : chapters){
                    leftPlaids.addAll(chapter.getLeftPlaids());
                }
                //3, has left plaids , reuse item gap
                if(leftPlaids.size() > 0){
                    BasePlaidFiller.GapCallbackImpl gapCallback = new BasePlaidFiller.GapCallbackImpl(context, leftPlaids, items);
                    ReuseItemStageFiller reuse = new ReuseItemStageFiller();
                    reuse.fill(context, leftPlaids, items, gapCallback);
                    List<GapManager.GapItem> gapItems = gapCallback.getFilledItems();
                    //sore by chapter
                    for (Chapter chapter : chapters){
                        //add the last gap items.
                        chapter.receiveGapItems(gapItems);
                    }
                }
                //sore by chapter
                for (Chapter chapter : chapters){
                    //sort as origin order
                    chapter.sortByPlaid();
                    //sort by rule
                    lastSortRule = chapter.sortRules(lastSortRule);
                    //adjust time
                    chapter.adjustTime();
                }
                break;

            case VETemplate.FILL_TYPE_ALL_SHOTS_MAX_SCORE:
                /*
                  1,  chapter: 镜头类型,比例染色。
                  2， 整体color-gap
                  3,  chapter: 局部排序
                 */
                chapters = groupChapterAllShots(context, template, items);
                //1, shot-type filter
                int startRule = lastSortRule;
                for (Chapter chapter : chapters){
                    startRule = ShotSortDelegate.getNextSortRule(startRule);
                    Logger.d(TAG, "tintAndFill", "all_shots_max_score >>> shot-type filter. rule = "
                            + ShotSortDelegate.getRuleString(startRule));
                    chapter.setShortTypeFilter(startRule);
                }
                //2 . gap
                BasePlaidFiller bp_filler = new BasePlaidFiller(new NormalStageFiller(), false);
                List<GapManager.GapItem> gapItems = bp_filler.fillPlaids(context, plaids, items, null);
                //3, sort by shot-type
                for (Chapter chapter : chapters){
                    //receive gap items.
                    chapter.receiveGapItems(gapItems);
                    //chapter.sortByPlaid();
                    lastSortRule = chapter.sortRules(lastSortRule);
                    //adjust time
                    chapter.adjustTime();
                }
                break;

             default:
                 throw new UnsupportedOperationException("wrong chapter_fill_type = " + template.getChapterFillType());
        }
        if(DEBUG) {
            Logger.d(TAG, "tintAndFill", "chapters: " + chapters.size());
        }

        //build result
        List<GapManager.GapItem> result = new ArrayList<>();
        for(Chapter chapter : chapters){
            result.addAll(chapter.getFilledItems());
        }
        //mark effects
        //TODO new EffectMarkerImpl().markEffects(context, chapters, result);
        return result;
    }

    private List<Chapter> groupChapterForFilterThenOthers(Context context, VETemplate template, List<MediaPartItem> items) {
        //filter shot-category
        List<Chapter> result = new ArrayList<>();
        VisitServices.from(template.getLogicSentences()).fireWithIndex(
                new FireIndexedVisitor<VETemplate.LogicSentence>() {
                    @Override
                    public Void visit(Object param, VETemplate.LogicSentence ls, int index, int size) {
                        List<MediaPartItem> matchedItems = VisitServices.from(items).filter(
                                new PredicateVisitor<MediaPartItem>() {
                                    @Override
                                    public Boolean visit(MediaPartItem item, Object param) {
                                        return Flags.hasFlags(ls.getShotCategoryFlags(), item.getImageMeta().getShotCategory());
                                    }
                                }).getAsList();
                        Chapter chapter = new Chapter(context, ls.getPlaids(), matchedItems, index);
                        result.add(chapter);
                        return null;
                    }
                });
        return result;
    }

    /** all chapter by use all items. */
    private List<Chapter> groupChapterAllShots(Context context, VETemplate template, List<MediaPartItem> items) {
        List<Chapter> result = new ArrayList<>();
        VisitServices.from(template.getLogicSentences()).fireWithIndex(
                new FireIndexedVisitor<VETemplate.LogicSentence>() {
            @Override
            public Void visit(Object param, VETemplate.LogicSentence ls, int index, int size) {
                result.add(new Chapter(context, ls.getPlaids(), items, index));
                return null;
            }
        });
        return result;
    }

    private List<Chapter> groupChapterForStoryLine(Context context, VETemplate template, List<MediaPartItem> items) {
        ColorGapPerformanceCollector collector = VEGapUtils.asColorGapContext(context).getColorGapPerformanceCollector();
        List<Chapter> chapters = new ArrayList<>();
        List<VETemplate.LogicSentence> sentences = template.getLogicSentences();
        //only one dir (often for c user.)
        if(sentences.size() == 1){
            //only one
            collector.addMessage(ColorGapPerformanceCollector.MODULE_FILL_PLAID, TAG, "groupChapterForStoryLine", "sentences.size() == 1");
            items.sort(TIME_COMPARATOR);
            chapters.add(new Chapter(context, sentences.get(0).getPlaids(), items, 0));
            return chapters;
        }
        collector.addMessage(ColorGapPerformanceCollector.MODULE_FILL_PLAID, TAG, "groupChapterForStoryLine", "sentences.size() != 1(for RawScript)");

        //Division chapter base dir
        for(int i = 0 , size = sentences.size() ; i < size ; i ++){
            VETemplate.LogicSentence ls = sentences.get(i);
            List<MediaPartItem> filterItems = VisitServices.from(items)
                    .visitForQueryList(
                    new PredicateVisitor<MediaPartItem>() {
                @Override
                public Boolean visit(MediaPartItem partItem, Object param) {
                    String dir = FileUtils.getFileDir(partItem.item.getFilePath(), 1, false);
                    return dir != null && dir.equals(ls.getDir());
                }
            }, null);
            filterItems.sort(TIME_COMPARATOR);
            if(filterItems.isEmpty()){
                throw new IllegalStateException("RawScript error. dir = " + ls.getDir());
            }
            chapters.add(new Chapter(context, ls.getPlaids(), filterItems, i));
        }
        return chapters;
    }
}
