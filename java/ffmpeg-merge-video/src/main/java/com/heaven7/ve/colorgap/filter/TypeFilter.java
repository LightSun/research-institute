package com.heaven7.ve.colorgap.filter;

import com.heaven7.ve.colorgap.GapColorFilter;

/**
 * Created by heaven7 on 2018/3/16 0016.
 */

/*public*/ abstract class TypeFilter extends GapColorFilter {

    protected final int type;

    /**
     * create gap color filter, by condition
     *
     * @param mCondition the condition
     * @param type     the filter type
     */
    public TypeFilter(GapColorCondition mCondition, int type) {
        super(mCondition);
        this.type = type;
    }
}
