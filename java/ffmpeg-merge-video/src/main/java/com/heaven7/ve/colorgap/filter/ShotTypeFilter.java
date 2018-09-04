package com.heaven7.ve.colorgap.filter;

import com.heaven7.ve.colorgap.GapColorFilter;
import com.heaven7.ve.colorgap.MetaInfo;

import static com.heaven7.ve.colorgap.MetaInfo.*;

/**
 * Created by heaven7 on 2018/3/16 0016.
 */

public class ShotTypeFilter extends GapColorFilter {
    /**
     * create gap color filter, by condition and weight
     *
     * @param mCondition the condition
     */
    public ShotTypeFilter(ShotTypeCondition mCondition) {
        super(mCondition);
    }

    @Override
    public int getFlag() {
        return FLAG_SHOT_TYPE;
    }

    @Override
    protected float getPassScore() {
        return PASS_SCORE_SHOT_TYPE;
    }

    public static class ShotTypeCondition extends GapColorCondition{

        public ShotTypeCondition(String type) {
            super(MetaInfo.getShotTypeFrom(type));
        }
        public ShotTypeCondition(int shot_type) {
            super(shot_type);
        }
        @Override
        protected int getCategory(int type) {

            switch (type){
                case SHOT_TYPE_CLOSE_UP:
                case SHOT_TYPE_MEDIUM_CLOSE_UP:
                    return CATEGORY_CLOSE_UP;

                case SHOT_TYPE_MEDIUM_SHOT:
                case SHOT_TYPE_MEDIUM_LONG_SHOT:
                    return CATEGORY_MIDDLE_VIEW;

                case SHOT_TYPE_LONG_SHORT:
                case SHOT_TYPE_BIG_LONG_SHORT:
                    return CATEGORY_VISION;

                default:
                    throw new UnsupportedOperationException();
            }
        }
    }
}
