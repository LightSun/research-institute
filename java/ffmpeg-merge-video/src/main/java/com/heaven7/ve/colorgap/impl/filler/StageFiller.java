package com.heaven7.ve.colorgap.impl.filler;

import com.heaven7.java.visitor.PredicateVisitor;
import com.heaven7.java.visitor.ResultVisitor;
import com.heaven7.java.visitor.collection.VisitServices;
import com.heaven7.utils.Context;
import com.heaven7.ve.colorgap.CutInfo;
import com.heaven7.ve.colorgap.MediaPartItem;
import com.heaven7.ve.gap.GapManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * stage filler
 * @author heaven7
 */
public abstract class StageFiller {

    public static final Comparator<CutInfo.PlaidInfo> COM_PLAID = new Comparator<CutInfo.PlaidInfo>() {
        @Override
        public int compare(CutInfo.PlaidInfo o1, CutInfo.PlaidInfo o2) {
            float o2_weight = o2.getGapColorFilter() != null ? o2.getGapColorFilter().getWeight() : 0;
            float o1_weight = o1.getGapColorFilter() != null ? o1.getGapColorFilter().getWeight() : 0;
            return Float.compare(o2_weight, o1_weight);
        }
    };

    public final LeftFillInfo fill(Context context, List<CutInfo.PlaidInfo> plaids, List<MediaPartItem> items, GapManager.GapCallback callback){
        List<CutInfo.PlaidInfo> newPlaids = new ArrayList<>(plaids);
        Collections.sort(newPlaids, COM_PLAID);
        fillImpl(context, newPlaids, items, callback);
        return buildLeftFillInfo(newPlaids, items, callback);
    }

    /**
     * fill the plaid by target media part items which named 'Shot'.
     * @param context the context
     * @param newPlaids the new plaids which is sorted by weight
     * @param items the shot items
     * @param callback the gap callback
     */
    protected abstract void fillImpl(Context context, List<CutInfo.PlaidInfo> newPlaids, List<MediaPartItem> items, GapManager.GapCallback callback);

    /**
     * build left fill info
     * @param plaids the plaids to fill
     * @param items the selected items to fill
     * @param callback the gap callback
     * @return the left fill info.
     */
    protected LeftFillInfo buildLeftFillInfo(List<CutInfo.PlaidInfo> plaids,
                                             List<MediaPartItem> items, GapManager.GapCallback callback){
        //use filled items to filter used items.
        List<GapManager.GapItem> filledItems = callback.getFilledItems();
        List<MediaPartItem> holdItems = VisitServices.from(filledItems).map(
                new ResultVisitor<GapManager.GapItem, MediaPartItem>() {
                    @Override
                    public MediaPartItem visit(GapManager.GapItem gapItem, Object param) {
                        return (MediaPartItem) gapItem.item;
                    }
                }).getAsList();
        List<CutInfo.PlaidInfo> holdPlaids = VisitServices.from(filledItems).map(
                new ResultVisitor<GapManager.GapItem, CutInfo.PlaidInfo>() {
                    @Override
                    public CutInfo.PlaidInfo visit(GapManager.GapItem gapItem, Object param) {
                        return (CutInfo.PlaidInfo) gapItem.plaid;
                    }
                }).getAsList();
        List<CutInfo.PlaidInfo> unusedPlaids = VisitServices.from(plaids).filter(new PredicateVisitor<CutInfo.PlaidInfo>() {
            @Override
            public Boolean visit(CutInfo.PlaidInfo item, Object param) {
                return !holdPlaids.contains(item);
            }
        }).getAsList();
        List<MediaPartItem> unUsedItems = VisitServices.from(items).filter(new PredicateVisitor<MediaPartItem>() {
            @Override
            public Boolean visit(MediaPartItem item, Object param) {
                return !holdItems.contains(item);
            }
        }).getAsList();
        //build left fill info
        LeftFillInfo info = new LeftFillInfo();
        info.setPlaids(unusedPlaids);
        info.setItems(unUsedItems);
        return info;
    }
}
