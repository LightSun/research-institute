package com.heaven7.ve.gap;

import com.heaven7.core.util.Logger;
import com.heaven7.java.base.util.Throwables;
import com.heaven7.ve.collect.ColorGapPerformanceCollector;

import java.util.List;

/**
 * Created by heaven7 on 2018/3/17 0017.
 */
//TODO 视频太短小于音乐格子？
public class GapManager {

    private static final String TAG = "GapManager";

    /** the distance of different video src */
    private static final int DISTANCE = 10;

    private final List<ItemDelegate> items;
    private final List<PlaidDelegate> plaids;

    @SuppressWarnings("unchecked")
    public GapManager(List<? extends PlaidDelegate> plaids, List<? extends ItemDelegate> items) {
        this.items = (List<ItemDelegate>) items;
        this.plaids = (List<PlaidDelegate>) plaids;
    }

    /**
     * fill the plaids.
     * @param callback the callback
     * @param reuseItem true to reuse item.(may be reuse multi times)
     */
    public void fill(GapCallback callback, boolean reuseItem, boolean ignoreFillFailed){
        final String method = "fill";
        List<GapItem> filledItems = callback.getFilledItems();
        Throwables.checkNull(filledItems);
        for(PlaidDelegate plaid : plaids){
            //ignore is populated
            if(plaid.isHold()){
                continue;
            }

            //plaid.computeScore();
            //no items. select a best match to the first item
            if(filledItems.size() == 0){
                GapItem gapItem = new GapItem(plaid, callback.bestMatch(plaid, items));
                filledItems.add(gapItem);
                gapItem.markHold();

                if(callback.isDebug()) {
                    callback.getPerformanceCollector().addMessage(ColorGapPerformanceCollector.MODULE_GAP_CALLBACK,
                            TAG, method, "filled item by first gap. " + gapItem.item.toString());
                }
            }else{
                float maxValue = -1f;
                ItemDelegate maxValueItem = null;
                for(ItemDelegate item : items) {
                    //if item not enough, item will use repeat.
                    if(!reuseItem && item.isHold()){
                        continue;
                    }
                    float filterScore = plaid.computeScore(item.getColorCondition());
                    float tagScore = item.getTotalScore();
                   // float matrixScore = computeValueMatrix(plaid, item, callback);

                    float tmpValue = filterScore + tagScore;

                    if(tmpValue > maxValue){
                        maxValue = tmpValue;
                        maxValueItem = item;
                    }
                }
                //no item match. or item.duration is incorrect
                boolean failed = maxValueItem == null || (maxValueItem.isVideo() && maxValueItem.getMaxDuration() < plaid.getDuration());
                if(failed){
                    if (ignoreFillFailed){
                        continue;
                    }else{
                        if(reuseItem){
                            throw new IllegalStateException("can't find the max value item for plaid.");
                        }else {
                            Logger.w("GapManager", "fill", "can't find the max value item for plaid.");
                            break;
                        }
                    }
                }
                //for item .if it is used before. we copy a new item
                GapItem gapItem = new GapItem(plaid, !maxValueItem.isHold() ? maxValueItem : maxValueItem.copy());
                filledItems.add(gapItem);
                gapItem.markHold();

                if(callback.isDebug()) {
                    callback.getPerformanceCollector().addMessage(ColorGapPerformanceCollector.MODULE_GAP_CALLBACK,
                            TAG, method, "filled item by max score. max score = " + maxValue
                                    + ",\n" + gapItem.item.toString());
                }
            }
        }
    }

    private static float computeValueMatrix(PlaidDelegate plaid, ItemDelegate item, GapCallback callback) {
        int baseValue = 10;
        List<GapItem> filledItems = callback.getFilledItems();
        for(GapItem gapItem : filledItems){
            int alreadyIndex = callback.indexOfPlaid(gapItem.plaid);
            int targetIndex = callback.indexOfPlaid(plaid);
            boolean fromSameVideo = gapItem.item.getVideoPath().equals(item.getVideoPath());
            int item_space = callback.indexOfItem(item) - callback.indexOfItem(gapItem.item);
            //0.5 is Attenuation coefficient
            baseValue += Math.pow(0.5, targetIndex - alreadyIndex) * (fromSameVideo ? item_space : DISTANCE);
        }
        return baseValue;
    }

    public static class GapItem{
        public final PlaidDelegate plaid;
        public ItemDelegate item;

        public GapItem(PlaidDelegate plaid, ItemDelegate item) {
            this.plaid = plaid;
            this.item = item;
        }
        public void markHold(){
            plaid.setHold(true);
            item.setHold(true);
        }

        /** set new item delegate. this often used to replace 'bias-shot to air-shot' */
        public ItemDelegate setItemDelegate(ItemDelegate item) {
            if(this.item == item){
                return null;
            }
            ItemDelegate old = this.item;
            this.item.setHold(false);
            this.item = item;
            item.setHold(true);
            return old;
        }
    }

    public interface GapCallback{

        int indexOfPlaid(PlaidDelegate plaid);

        int indexOfItem(ItemDelegate item);
        /** get the filled items */
       List<GapItem> getFilledItems();

       ItemDelegate bestMatch(PlaidDelegate plaid, List<ItemDelegate> items);

       ColorGapPerformanceCollector getPerformanceCollector();

       boolean isDebug();
    }
}
