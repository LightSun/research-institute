package com.heaven7.ve.strategy;


import com.heaven7.ve.colorgap.ColorGapContext;
import com.heaven7.ve.cross_os.IMediaResourceItem;

import java.util.List;

/**
 * @author heaven7
 */
public interface ResourceStrategy {

    int STRATEGY_VIDEO        = 1;
    int STRATEGY_IMAGE        = 2;
    int STRATEGY_SINGLE_SHOT  = 3;


    int getResourceStrategy(ColorGapContext context, List<IMediaResourceItem> list);

}
