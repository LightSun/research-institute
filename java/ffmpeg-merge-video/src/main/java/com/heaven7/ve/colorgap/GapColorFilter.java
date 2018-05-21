package com.heaven7.ve.colorgap;

import static com.heaven7.ve.colorgap.MetaInfo.computeWeight;

/**
 * Created by heaven7 on 2018/3/15 0015.
 */

public abstract class GapColorFilter {

    public static final float MIN_SCORE       = 0;
    public static final float MAX_SCORE       = 1f;
    public static final float PASS_SCORE      = .6f;
    public static final float GOOD_SCORE      = .8f;

    public static final float PASS_SCORE_SHOT_TYPE      = .7f;

    protected final GapColorCondition mCondition;
    /** the self weight */
    private final float weight;
    /**
     * create gap color filter, by condition
     * @param mCondition the condition
     */
    public GapColorFilter(GapColorCondition mCondition) {
        this.mCondition = mCondition;
        this.weight = computeWeight(getFlag());
    }

    /**
     * get the weight of the filter. if it is group will return the sum of the weight.
     * @return the weight,
     */
    public float getWeight() {
        return weight;
    }
    public boolean shouldPass(GapColorCondition condition){
         return computeScore(condition) >= PASS_SCORE;
    }
    /**
     * compute the max score.
     * @param condition the gap color condition
     * @return the score.
     */
    public float computeScore(GapColorCondition condition){
        //all the same
        if(mCondition.equals(condition)){
            return MAX_SCORE;
        }
        if(!mCondition.isCoreType() || !mCondition.isSampleTypeCondition(condition, false)){
            return MIN_SCORE;
        }
        //same core type , and sample type condition
        return mCondition.isSameCategory(condition) ? getPassScore() : MIN_SCORE;
    }

    /**
     * compute the score with weight.
     * @param condition the gap condition
     * @return the score
     */
    public final float computeScoreWithWeight(GapColorCondition condition){
        return weight * computeScore(condition);
    }

    /**
     * get the pass score. when not equals. but something like.
     * @return the score of passed.
     */
    protected float getPassScore() {
        return PASS_SCORE;
    }

    /**
     * get the filter flag
     * @return the filter flag
     */
    public abstract int getFlag();


    /**
     * the gap color condition
     * @author heaven7
     */
    public static abstract class GapColorCondition{

        protected final int type;
        protected final int category;

        public GapColorCondition(int type) {
            this.type = type;
            category = getCategory(type);
        }
        public GapColorCondition(){
            this.type = this.category = 0;
        }

        protected int getCategory(int type){
            return 0;
        }

        /** indicate the type is the core type of the same category. default return true . that not care about it.  */
        public boolean isCoreType(){
            return true;
        }

        @SuppressWarnings("unchecked")
        public <T extends GapColorCondition> T as(){
            return (T) this;
        }

        /** is the sample type condition or not. */
        public boolean isSampleTypeCondition(GapColorCondition gcc , boolean doubleSide){
            Class<? extends GapColorCondition> clazz = this.getClass();
            //the target condition must be the child type(can be the same type) of current
            boolean sameType = clazz.isAssignableFrom(gcc.getClass());
            return doubleSide ? (sameType || gcc.getClass().isAssignableFrom(clazz)) : sameType;
        }
        /** indicate is the sample category or not */
        public final boolean isSameCategory(GapColorCondition gcc){
            return category == gcc.category;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            GapColorCondition that = (GapColorCondition) o;

            if (type != that.type) return false;
            return category == that.category;
        }
    }
}
