package com.heaven7.ve.colorgap.filter;


import com.heaven7.java.base.util.SparseArray;
import com.heaven7.java.visitor.collection.VisitServices;
import com.heaven7.ve.colorgap.GapColorFilter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.heaven7.ve.colorgap.MetaInfo.*;

/**
 * Created by heaven7 on 2018/3/16 0016.
 */

public class GroupFilter extends GapColorFilter {

    public static final int MODE_NORMAL = 1;
    public static final int MODE_STRICT = 2;

    private int mode = MODE_NORMAL;
    private final List<GapColorFilter> mFilters;

    /**
     * create gap color filter, by condition and weight
     *
     * @param filters the color filters
     */
    public GroupFilter( List<GapColorFilter> filters) {
        super(null);
        this.mFilters = filters;
    }
    public GroupFilter() {
       this(new ArrayList<>());
    }

    public int getMode() {
        return mode;
    }
    public void setMode(int mode) {
        this.mode = mode;
    }

    public void removeGapColorFilter(GapColorFilter filter) {
        VisitServices.from(mFilters).removeIfExist(filter);
    }

    public boolean hasGapColorFilter(GapColorFilter filter){
        if(filter instanceof GroupFilter){
            throw new IllegalArgumentException("can't support group filter");
        }
        for(GapColorFilter gcf : mFilters){
            if(gcf == filter){
                return true;
            }
        }
        return false;
    }

    /** add the color filter .if exist same flag, overlap */
    public void addGapColorFilter(GapColorFilter filter) {
        if(filter instanceof GroupFilter){
            throw new IllegalArgumentException();
        }
        for(Iterator<GapColorFilter> it = mFilters.iterator(); it.hasNext(); ){
            if(it.next().getFlag() == filter.getFlag()){
                //overlap
                it.remove();
            }
        }
        VisitServices.from(mFilters).addIfNotExist(filter);
    }

    public int getTotalFlags(){
        int flag = 0;
        for(GapColorFilter filter : mFilters){
            flag |= filter.getFlag();
        }
        return flag;
    }

    @Override
    public boolean shouldPass(GapColorCondition condition) {
        if(mode == MODE_NORMAL) {
            return computeScore(condition) / mFilters.size() >= PASS_SCORE;
        }else{
            //mode_strict
            if( !(condition instanceof GroupCondition)){
                return false;
            }
            final GroupCondition gc =  condition.as();
            for(GapColorFilter filter : mFilters){
                GapColorCondition gcc = gc.getCondition(filter.getFlag());
                if(gcc != null && !filter.shouldPass(gcc)){
                    return false;
                }
            }
            return true;
        }
    }

    @Override
    public float computeScore(GapColorCondition condition) {
        if( !(condition instanceof GroupCondition)){
            return MIN_SCORE;
        }
        final GroupCondition gc =  condition.as();
        float score = MIN_SCORE;
        for(GapColorFilter filter : mFilters){
            GapColorCondition gcc = gc.getCondition(filter.getFlag());
            if(gcc != null) {
                score += filter.computeScore(gcc);
            }
        }
        return score;
    }
    @Override
    public int getFlag() {
        return 0;
    }

    @Override
    public float getWeight() {
        //for group filter weight is the sum
        float weight = 0;
        for(GapColorFilter filter : mFilters){
            weight += filter.getWeight();
        }
        return weight;
    }

    public static class GroupCondition extends GapColorCondition {

        private final SparseArray<GapColorCondition> mConditions;

        public GroupCondition() {
            super();
            this.mConditions = new SparseArray<>();
        }
        public void addGapColorCondition(GapColorCondition condition){
            final int key;
            if(condition instanceof TimeFilter.TimeCondition){
                key = FLAG_TIME;
            }else if(condition instanceof LocationFilter.LocationCondition){
                key = FLAG_LOCATION;
            }else if(condition instanceof CameraMotionFilter.CameraMotionCondition){
                key = FLAG_CAMERA_MOTION;
            }else if( condition instanceof VideoTagFilter.VideoTagCondition){
                key = FLAG_VIDEO_TAG;
            } else if( condition instanceof MediaDirFilter.MediaDirCondition)
                key = FLAG_MEDIA_DIR;
            else if(condition instanceof MediaTypeFilter.MediaTypeCondition){
                key = FLAG_MEDIA_TYPE;
            }else if(condition instanceof ShotTypeFilter.ShotTypeCondition){
                key = FLAG_SHOT_TYPE;
            }else if(condition instanceof ShotKeyFilter.ShotKeyCondition){
                key = FLAG_SHOT_KEY;
            }else if(condition instanceof ShotCategoryFilter.ShotCategoryCondition){
                key = FLAG_SHOT_CATEGORY;
            }
            else{
                throw new IllegalArgumentException("unsupport condition = " + condition.getClass().getName());
            }
            mConditions.put(key, condition);
        }
        public GapColorCondition getCondition(int flag) {
            return mConditions.get(flag);
        }

    }
}
