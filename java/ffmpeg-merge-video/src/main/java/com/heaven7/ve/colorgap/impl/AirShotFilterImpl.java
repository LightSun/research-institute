package com.heaven7.ve.colorgap.impl;

import com.heaven7.java.base.util.Predicates;
import com.heaven7.ve.colorgap.*;

import java.util.List;

/**
 * Created by heaven7 on 2018/5/15 0015.
 */
public class AirShotFilterImpl implements AirShotFilter {

    @Override
    public MediaPartItem filter(CutInfo.PlaidInfo plaid, Chapter left, Chapter right, List<MediaPartItem> airShots) {
        if(Predicates.isEmpty(airShots)){
            return null;
        }
        //1, 先考虑时间 插入
        long pre_end = left.getFilledEndTime();
        long next_start = right.getFilledStartTime();
        for(MediaPartItem item : airShots){
            if(item.isHold()){
                continue;
            }
            long date = item.imageMeta.getDate();
            if(date >= pre_end && date <= next_start){
                return item;
            }
        }
        //2, 如果按照时间插入不了，再计算得分
        return VEGapUtils.filterByScore(plaid, airShots);
    }

    @Override
    public MediaPartItem filter(CutInfo.PlaidInfo plaid, MediaPartItem biasShot, List<MediaPartItem> airShots) {
        if(Predicates.isEmpty(airShots)){
            return null;
        }
        return VEGapUtils.filterByScore(plaid, airShots);
    }

}
