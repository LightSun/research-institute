package com.heaven7.ve.colorgap.filter;

import com.heaven7.ve.colorgap.GapColorFilter;
import com.heaven7.ve.colorgap.MetaInfo;

/**
 * @author heaven7
 */
public class ShotCategoryFilter extends GapColorFilter {

    /**
     * create gap color filter, by condition
     *
     * @param mCondition the condition
     */
    public ShotCategoryFilter(GapColorCondition mCondition) {
        super(mCondition);
    }

    @Override
    public int getFlag() {
        return MetaInfo.FLAG_SHOT_CATEGORY;
    }

    public static class ShotCategoryCondition extends GapColorFilter.GapColorCondition{

        public ShotCategoryCondition(int type) {
            super(type);
        }
        @Override
        protected int getCategory(int type) {
            return type;
        }
    }
}
