package com.heaven7.ve.colorgap.impl;

import com.heaven7.core.util.Logger;
import com.heaven7.java.visitor.ResultVisitor;
import com.heaven7.java.visitor.collection.KeyValuePair;
import com.heaven7.java.visitor.collection.VisitServices;
import com.heaven7.utils.Context;
import com.heaven7.ve.colorgap.MediaPartItem;
import com.heaven7.ve.colorgap.MetaInfo;
import com.heaven7.ve.colorgap.ShotSortDelegate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author heaven7
 */
public class ShotSortDelegateImpl implements ShotSortDelegate {

    private static final String TAG = "ShotSortDelegateImpl";

    @Override
    public List<MediaPartItem> sortShots(Context context, int sortRule, List<MediaPartItem> items) {
        /*List<KeyValuePair<Integer, List<MediaPartItem>>> pairs = VisitServices.from(items).groupService(
                new ResultVisitor<MediaPartItem, Integer>() {
            @Override
            public Integer visit(MediaPartItem item, Object param) {
                return item.getChapterIndex();
            }
        }).mapPair().sortService(new Comparator<KeyValuePair<Integer, List<MediaPartItem>>>() {
            @Override
            public int compare(KeyValuePair<Integer, List<MediaPartItem>> o1, KeyValuePair<Integer, List<MediaPartItem>> o2) {
                return Integer.compare(o1.getKey(), o2.getKey());
            }
        }).getAsList();
        //理论上说，排序镜头是为章节服务的. 所以只能有1个章节
        if(pairs.size() > 1){
            Logger.w(TAG, "sortShots", "sort Shot can't used for multi chapters.");
            return items;
        }*/
        List<MediaPartItem> newItem = new ArrayList<>(items);
        switch (sortRule){
            case SHOT_SORT_RULE_AESC://near -> far
                Collections.sort(newItem, new Comparator<MediaPartItem>() {
                    @Override
                    public int compare(MediaPartItem o1, MediaPartItem o2) {
                        //近景 是特写。值最大。所以降序
                        int o2_shortType = MetaInfo.getShotTypeFrom(o2.getImageMeta().getShotType());
                        int o1_shortType = MetaInfo.getShotTypeFrom(o1.getImageMeta().getShotType());
                        return Integer.compare(o2_shortType, o1_shortType);
                    }
                });
                break;

            case SHOT_SORT_RULE_DESC:
                Collections.sort(newItem, new Comparator<MediaPartItem>() {
                    @Override
                    public int compare(MediaPartItem o1, MediaPartItem o2) {
                        //近景 是特写。值最大。所以降序
                        int o2_shortType = MetaInfo.getShotTypeFrom(o2.getImageMeta().getShotType());
                        int o1_shortType = MetaInfo.getShotTypeFrom(o1.getImageMeta().getShotType());
                        return Integer.compare(o1_shortType, o2_shortType);
                    }
                });
                break;

            default:
                throw new UnsupportedOperationException("wrong sort rule. " + ShotSortDelegate.getRuleString(sortRule));
        }
        return newItem;
    }

}
