package com.heaven7.ve.colorgap.filter;

import com.heaven7.ve.colorgap.MetaInfo;

/**
 * @author heaven7
 */
public class ShotCategoryFilter extends FlagsColorFilter {

    /**
     * create gap color filter, by condition
     *
     * @param mCondition the condition
     */
    public ShotCategoryFilter(ShotCategoryCondition mCondition) {
        super(mCondition);
    }

    @Override
    public int getFlag() {
        return MetaInfo.FLAG_SHOT_CATEGORY;
    }
    public static class ShotCategoryCondition extends FlagsColorCondition{
        public ShotCategoryCondition(int type) {
            super(type);
        }
    }
}
