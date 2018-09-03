package com.heaven7.ve.colorgap.filter;

import com.heaven7.ve.colorgap.GapColorFilter;
import com.heaven7.ve.utils.Flags;

/**
 * @author heaven7
 */
public abstract class FlagsColorFilter extends GapColorFilter {

    /**
     * create gap color filter, by condition
     *
     * @param mCondition the condition
     */
    public FlagsColorFilter(GapColorCondition mCondition) {
        super(mCondition);
    }

    @Override
    public final float computeScore(GapColorCondition condition) {
        if(mCondition.equals(condition)){
            return MAX_SCORE;
        }
        if(mCondition.getClass() != condition.getClass()){
            return MIN_SCORE;
        }
        boolean match = Flags.hasFlags(mCondition.getType(), condition.getType());
        return match ? MAX_SCORE : MIN_SCORE;
    }


    public static class FlagsColorCondition extends GapColorCondition{

        public FlagsColorCondition(int type) {
            super(type);
        }
        @Override
        protected final int getCategory(int type) {
            return type;
        }
    }
}
