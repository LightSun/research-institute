package com.heaven7.ve.colorgap.impl.filler;

import com.heaven7.java.visitor.PredicateVisitor;
import com.heaven7.java.visitor.ResultVisitor;
import com.heaven7.java.visitor.collection.VisitServices;
import com.heaven7.utils.Context;
import com.heaven7.ve.colorgap.MediaPartItem;
import com.heaven7.ve.cross_os.IPlaidInfo;
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

    public static final Comparator<IPlaidInfo> COM_PLAID = new Comparator<IPlaidInfo>() {
        @Override
        public int compare(IPlaidInfo o1, IPlaidInfo o2) {
            float o2_weight = o2.getColorFilter() != null ? o2.getColorFilter().getWeight() : 0;
            float o1_weight = o1.getColorFilter() != null ? o1.getColorFilter().getWeight() : 0;
            return Float.compare(o2_weight, o1_weight);
        }
    };

    public final LeftFillInfo fill(Context context, List<IPlaidInfo> plaids, List<MediaPartItem> items, GapManager.GapCallback callback){
        List<IPlaidInfo> newPlaids = new ArrayList<>(plaids);
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
    protected abstract void fillImpl(Context context, List<IPlaidInfo> newPlaids, List<MediaPartItem> items, GapManager.GapCallback callback);

    /**
     * build left fill info
     * @param plaids the plaids to fill
     * @param items the selected items to fill
     * @param callback the gap callback
     * @return the left fill info.
     */
    protected LeftFillInfo buildLeftFillInfo(List<IPlaidInfo> plaids,
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
        List<IPlaidInfo> holdPlaids = VisitServices.from(filledItems).map(
                new ResultVisitor<GapManager.GapItem, IPlaidInfo>() {
                    @Override
                    public IPlaidInfo visit(GapManager.GapItem gapItem, Object param) {
                        return (IPlaidInfo) gapItem.plaid;
                    }
                }).getAsList();
        List<IPlaidInfo> unusedPlaids = VisitServices.from(plaids).filter(new PredicateVisitor<IPlaidInfo>() {
            @Override
            public Boolean visit(IPlaidInfo item, Object param) {
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
