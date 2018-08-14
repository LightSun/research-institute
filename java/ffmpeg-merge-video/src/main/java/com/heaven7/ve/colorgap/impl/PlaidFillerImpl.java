package com.heaven7.ve.colorgap.impl;


import com.heaven7.core.util.Logger;
import com.heaven7.java.base.util.Predicates;
import com.heaven7.utils.CommonUtils;
import com.heaven7.ve.TimeTraveller;
import com.heaven7.ve.colorgap.*;
import com.heaven7.ve.gap.GapManager;
import com.heaven7.ve.gap.ItemDelegate;
import com.heaven7.ve.gap.PlaidDelegate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * the filler which use gap with 'compute-score' to fill.
 * Created by heaven7 on 2018/3/17 0017.
 */

public class PlaidFillerImpl implements PlaidFiller {

    @Override
    public List<GapManager.GapItem> fillPlaids(List<CutInfo.PlaidInfo> infoes, List<MediaPartItem> parts) {
        final boolean notEnough = parts.size() < infoes.size();
        if(notEnough){
            Logger.d("PlaidFillerImpl", "fill", "shot is not enough --- expect plaid.count = "
                    + infoes.size() + " but is " + parts.size());
        }
        /*
         * 1， 匹配出音乐格子对应的 video parts. 然后gap
         */
        List<CutInfo.PlaidInfo> sortedPlaids = new ArrayList<>(infoes);
        //sort by weight. desc
        Collections.sort(sortedPlaids, new Comparator<CutInfo.PlaidInfo>() {
            @Override
            public int compare(CutInfo.PlaidInfo o1, CutInfo.PlaidInfo o2) {
                float o2_weight = o2.getGapColorFilter() != null ? o2.getGapColorFilter().getWeight() : 0;
                float o1_weight = o1.getGapColorFilter() != null ? o1.getGapColorFilter().getWeight() : 0;
                return Float.compare(o2_weight, o1_weight);
            }
        });
        /*
         * 1，过滤出符合条件的 media part item
         * 2, gap
         */
        GapCallbackImpl gapCallback = new GapCallbackImpl(infoes, parts);
        //没有匹配条件的填充物
        List<CutInfo.PlaidInfo> notPopulatePlaids = new ArrayList<>();
        for(int i = 0 , size = sortedPlaids.size() ; i < size ; i++){
            CutInfo.PlaidInfo info = sortedPlaids.get(i);
            List<MediaPartItem> result = filter(info, parts);
            if(Predicates.isEmpty(result)){
                notPopulatePlaids.add(info);
            }else{
                new GapManager(Arrays.asList(info), result).fill(gapCallback, false);
            }
        }
        if(notPopulatePlaids.size() > 0){
            //base gap
            new GapManager(notPopulatePlaids, parts).fill(gapCallback, false);
        }
        //check if not fill done. (that often means shot is not enough)
        if(gapCallback.filledItems.size() < infoes.size() ){
            new GapManager(notPopulatePlaids, parts).fill(gapCallback, true);
        }
        if(gapCallback.filledItems.size() != infoes.size()){
            throw new IllegalStateException("fill failed.");
        }
        //sort as the order of plaids
        gapCallback.sortAsPlaids();
        //adjust times
        gapCallback.adjustTimes();
        return gapCallback.filledItems;
    }

    private static List<MediaPartItem> filter(CutInfo.PlaidInfo info, List<MediaPartItem> parts) {
        GapColorFilter filter = info.getGapColorFilter();
        if(filter == null){
            return Collections.emptyList();
        }
        List<MediaPartItem> result = new ArrayList<>();
        for(MediaPartItem item : parts){
            //not hold and should pass
            if(item.isHold()){
                continue;
            }
            if(filter.shouldPass(item.getColorCondition())){
                result.add(item);
            }
        }
        return result;
    }

    private static class GapCallbackImpl implements GapManager.GapCallback{

        final List<GapManager.GapItem> filledItems = new ArrayList<>();
        final List<CutInfo.PlaidInfo> infoes;
        final List<MediaPartItem> parts;

        public GapCallbackImpl(List<CutInfo.PlaidInfo> infoes, List<MediaPartItem> parts) {
            this.infoes = infoes;
            this.parts = parts;
        }
        /** adjust the video start and end times. after call this the video parts' times may be overlapped. */
        public void adjustTimes() {
            for(GapManager.GapItem gapItem : filledItems) {
                CutInfo.PlaidInfo plaid = (CutInfo.PlaidInfo) gapItem.plaid;
                MediaPartItem mpi = (MediaPartItem) gapItem.item;
                //for image. set time directly
                if(mpi.item.isImage()){
                    mpi.videoPart.setStartTime(0);
                    mpi.videoPart.setEndTime(plaid.getDuration());
                    continue;
                }
                TimeTraveller videoPart = mpi.videoPart;
                if(videoPart.getMaxDuration() < plaid.getDuration()){
                    throw new IllegalStateException("caused by video max duration < part music duration.");
                }
                //if duration is the same . no need adjust time.
                if(videoPart.getDuration() != plaid.getDuration()){
                    if(videoPart.getMaxDuration() < plaid.getDuration()){
                        throw new IllegalStateException("video relative music part is too short.");
                    }
                    //videoPart.adjustTimeAsCenter(plaid.getDuration());
                    //the key frame time often is the high-light time
                    int keyFrameTime = mpi.getKeyFrameTime();
                    videoPart.adjustTime(CommonUtils.timeToFrame(keyFrameTime, TimeUnit.SECONDS),
                            plaid.getDuration());
                }
            }
        }
        /** sort the filled video items as the music plaids' order. */
        public void sortAsPlaids(){
            Collections.sort(filledItems, new Comparator<GapManager.GapItem>() {
                @Override
                public int compare(GapManager.GapItem o1, GapManager.GapItem o2) {
                    int o1_index = infoes.indexOf(o1.plaid);
                    int o2_index = infoes.indexOf(o2.plaid);
                    return Integer.compare(o1_index, o2_index);
                }
            });
        }

        @Override
        public int indexOfPlaid(PlaidDelegate plaid) {
            return infoes.indexOf(plaid);
        }
        @Override
        public int indexOfItem(ItemDelegate item) {
            return parts.indexOf(item);
        }
        @Override
        public List<GapManager.GapItem> getFilledItems() {
            return filledItems;
        }
        @Override
        public ItemDelegate bestMatch(PlaidDelegate plaid, List<ItemDelegate> items) {
            GapColorFilter filter = ((CutInfo.PlaidInfo) plaid).getGapColorFilter();
            if(filter == null){
                //if filter is null. return the first item directly
                List<ItemDelegate> tmp_items = new ArrayList<>(items);
                Collections.sort(tmp_items, (o1, o2) -> {
                    int o1_index = parts.indexOf(o1);
                    int o2_index = parts.indexOf(o2);
                    return Integer.compare(o1_index, o2_index);
                });
                return tmp_items.get(0);
            }

            //compute max score
            ItemDelegate bestItem = VEGapUtils.filterByScore(plaid, items);
            if(bestItem == null){
                throw new IllegalStateException("can't find best match");
            }
            return bestItem;
        }
    }
}
