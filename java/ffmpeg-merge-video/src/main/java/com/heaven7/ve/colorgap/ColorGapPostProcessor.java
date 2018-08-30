package com.heaven7.ve.colorgap;

import com.heaven7.utils.Context;
import com.heaven7.ve.colorgap.impl.ShotSortDelegateImpl;

import java.util.List;

/**
 * the post processor of color gap
 * @author heaven7
 */
public interface ColorGapPostProcessor {

    ColorGapPostProcessor DEFAULT = new ColorGapPostProcessor() {
        @Override
        public  List<MediaPartItem> onPostProcess(Context context,  List<MediaPartItem> items) {
            return items;
        }
    };

    /**
     * on post process the color gap items.
     * @param context the gap context
     * @param items the video items after filled
     * @return the post process gap items.
     */
    List<MediaPartItem> onPostProcess(Context context, List<MediaPartItem> items);

    /**
     * the sort rule color gap post processor.
     */
    class SortRuleColorGapPostProcessor implements ColorGapPostProcessor{

        final ShotSortDelegate delegate = new ShotSortDelegateImpl();
        final int sortRule;

        public SortRuleColorGapPostProcessor(int sortRule) {
            this.sortRule = sortRule;
        }

        @Override
        public  List<MediaPartItem> onPostProcess(Context context,  List<MediaPartItem> items) {
            List<MediaPartItem> partItems = delegate.sortShots(context, sortRule, items);
            assert partItems.size() == items.size();
            return partItems;
        }
    }
}
