package com.heaven7.ve.colorgap.impl.filler;

import com.heaven7.core.util.Logger;
import com.heaven7.java.visitor.FireIndexedVisitor;
import com.heaven7.java.visitor.ResultVisitor;
import com.heaven7.java.visitor.collection.VisitServices;
import com.heaven7.utils.Context;
import com.heaven7.ve.colorgap.*;
import com.heaven7.ve.gap.GapManager;
import com.heaven7.ve.gap.ItemDelegate;
import com.heaven7.ve.gap.PlaidDelegate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class BasePlaidFiller implements PlaidFiller {

    private static final String TAG = "BasePlaidFiller";
    private final StageFiller stageFiller;
    private final boolean shouldAdjustTime;

    public BasePlaidFiller(StageFiller filler, boolean shouldAdjustTime) {
        this.stageFiller = filler;
        this.shouldAdjustTime = shouldAdjustTime;
    }
    public BasePlaidFiller() {
        this(new NormalStageFiller(), true);
    }

    @Override
    public List<GapManager.GapItem> fillPlaids(Context mContext, List<CutInfo.PlaidInfo> infoes, List<MediaPartItem> parts, ColorGapPostProcessor processor) {
        final boolean notEnough = parts.size() < infoes.size();
        if(notEnough){
            Logger.d(TAG, "fill", "shot is not enough --- expect plaid.count = "
                    + infoes.size() + " but is " + parts.size());
        }
        /*
         * 1，过滤出符合条件的 media part item
         * 2, gap
         */
        GapCallbackImpl gapCallback = new GapCallbackImpl(mContext, infoes, parts);
        stageFiller.fill(mContext, infoes, parts, gapCallback);

        if(gapCallback.filledItems.size() != infoes.size()){
            throw new IllegalStateException("fill failed.");
        }
        //sort as the order of plaids
        gapCallback.sortAsPlaids();

        //post processor
        if(processor != null){
            List<MediaPartItem> list = VisitServices.from(gapCallback.filledItems).map(
                    new ResultVisitor<GapManager.GapItem, MediaPartItem>() {
                        @Override
                        public MediaPartItem visit(GapManager.GapItem gapItem, Object param) {
                            return (MediaPartItem) gapItem.item;
                        }
                    }).getAsList();
            List<MediaPartItem> resultList = processor.onPostProcess(mContext, list);
            gapCallback.changeGapItem(resultList);
        }

        //adjust times by plaids
        if(shouldAdjustTime) {
            gapCallback.adjustTimes();
        }
        return gapCallback.filledItems;
    }

    public static class GapCallbackImpl extends BaseContextOwner implements GapManager.GapCallback{

        final List<GapManager.GapItem> filledItems = new ArrayList<>();
        final List<CutInfo.PlaidInfo> infoes;
        final List<MediaPartItem> parts;

        public GapCallbackImpl(Context mContext, List<CutInfo.PlaidInfo> infoes, List<MediaPartItem> parts) {
            super(mContext);
            this.infoes = infoes;
            this.parts = parts;
        }
        /** adjust the video start and end times. after call this the video parts' times may be overlapped. */
        public void adjustTimes() {
            VEGapUtils.adjustTime(getContext(), filledItems);
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

        /** change the item to target item */
        public void changeGapItem(List<MediaPartItem> resultList) {
            VisitServices.from(filledItems).fireWithIndex(new FireIndexedVisitor<GapManager.GapItem>() {
                @Override
                public Void visit(Object param, GapManager.GapItem gapItem, int index, int size) {
                    gapItem.item = resultList.get(index);
                    return null;
                }
            });
        }
    }
}
