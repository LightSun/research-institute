package com.heaven7.ve.colorgap;

import com.heaven7.utils.Context;
import com.heaven7.ve.kingdom.Kingdom;

/**
 * @author heaven7
 */
public class BaseContextOwner {

    private final ColorGapContext mContext;

    public BaseContextOwner(ColorGapContext mContext) {
        this.mContext = mContext;
    }
    public BaseContextOwner(Context mContext) {
        this.mContext = (ColorGapContext) mContext;
    }
    public ColorGapContext getContext(){
        return mContext;
    }
    public Kingdom getKingdom(){
        return mContext.getKingdom();
    }
}
