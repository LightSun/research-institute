package com.heaven7.ve.colorgap.filter;

import com.heaven7.ve.colorgap.GapColorFilter;

import static com.heaven7.ve.colorgap.MetaInfo.FLAG_SHOT_KEY;

/**
 * the shot key color filter.
 * Created by heaven7 on 2018/5/12 0012.
 */

public class ShotKeyFilter extends GapColorFilter {

    /**
     * create gap color filter, by condition
     *
     * @param mCondition the condition
     */
    public ShotKeyFilter(ShotKeyCondition mCondition) {
        super(mCondition);
    }
    @Override
    public int getFlag() {
        return FLAG_SHOT_KEY;
    }
    //only equals can pass.
    @Override
    protected float getPassScore() {
        return MIN_SCORE;
    }

    public static class ShotKeyCondition extends GapColorCondition{

        private String shotKey;

        public ShotKeyCondition(String shotKey) {
            super();
            this.shotKey = shotKey;
        }
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            if (!super.equals(o)) return false;

            ShotKeyCondition that = (ShotKeyCondition) o;

            return shotKey != null ? shotKey.equals(that.shotKey) : that.shotKey == null;
        }
    }
}
