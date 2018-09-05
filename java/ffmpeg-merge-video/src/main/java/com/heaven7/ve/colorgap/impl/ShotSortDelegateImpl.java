package com.heaven7.ve.colorgap.impl;

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

  //  private static final String TAG = "ShotSortDelegateImpl";

    @Override
    public List<MediaPartItem> sortShots(Context context, int sortRule, List<MediaPartItem> items) {
        List<MediaPartItem> newItem = new ArrayList<>(items);
        switch (sortRule){
            case SHOT_SORT_RULE_AESC://near -> far
                Collections.sort(newItem, new Comparator<MediaPartItem>() {
                    @Override
                    public int compare(MediaPartItem o1, MediaPartItem o2) {
                        //close-up is min , so aesc
                        int o2_shortType = MetaInfo.getShotTypeFrom(o2.getImageMeta().getShotType());
                        int o1_shortType = MetaInfo.getShotTypeFrom(o1.getImageMeta().getShotType());
                        return Integer.compare(o1_shortType, o2_shortType);
                    }
                });
                break;

            case SHOT_SORT_RULE_DESC:
                Collections.sort(newItem, new Comparator<MediaPartItem>() {
                    @Override
                    public int compare(MediaPartItem o1, MediaPartItem o2) {
                        //close-up is min , so desc
                        int o2_shortType = MetaInfo.getShotTypeFrom(o2.getImageMeta().getShotType());
                        int o1_shortType = MetaInfo.getShotTypeFrom(o1.getImageMeta().getShotType());
                        return Integer.compare(o2_shortType, o1_shortType);
                    }
                });
                break;

            default:
                throw new UnsupportedOperationException("wrong sort rule. " + ShotSortDelegate.getRuleString(sortRule));
        }
        return newItem;
    }

}
