package com.heaven7.ve.colorgap.impl;

import com.heaven7.java.visitor.PileVisitor;
import com.heaven7.java.visitor.ResultVisitor;
import com.heaven7.java.visitor.collection.VisitServices;
import com.heaven7.utils.Context;
import com.heaven7.ve.colorgap.ColorGapPostProcessor;
import com.heaven7.ve.colorgap.MediaPartItem;
import com.heaven7.ve.colorgap.ShotSortDelegate;

import java.util.List;

/**
 * @author heaven7
 */
public class ChapterColorGapPostProcessor implements ColorGapPostProcessor {

    private final ShotSortDelegate mSortDelegate =  new ShotSortDelegateImpl();
    private int sortRule;
    private int mLastSortRule = ShotSortDelegate.SHOT_SORT_RULE_UNKNOWN;

    public ChapterColorGapPostProcessor(int sortRule) {
        this.sortRule = sortRule;
    }

    public int getLastSortRule(){
        return mLastSortRule;
    }

    @Override
    public List<MediaPartItem> onPostProcess(Context context, List<MediaPartItem> items) {
        //List<Story> stories = mChapter.getAllStory();
        List<MediaPartItem> result = VisitServices.from(items).groupService(6).map(
                new ResultVisitor<List<MediaPartItem>, List<MediaPartItem>>() {
            @Override
            public List<MediaPartItem> visit(List<MediaPartItem> list, Object param) {
                int rule = sortRule;
                List<MediaPartItem> result = mSortDelegate.sortShots(context, rule, list);
                sortRule = ShotSortDelegate.getNextSortRule(rule);
                mLastSortRule = rule;
                return result;
            }
        }).pile(new PileVisitor<List<MediaPartItem>>() {
            @Override
            public List<MediaPartItem> visit(Object o, List<MediaPartItem> list1, List<MediaPartItem> list2) {
                list1.addAll(list2);
                return list1;
            }
        });
        //按照gap填充（镜头类型个数，3:2,1， shot-key）
        //排序。
        return result;
    }
}
