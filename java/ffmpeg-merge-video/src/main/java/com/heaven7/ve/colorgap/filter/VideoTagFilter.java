package com.heaven7.ve.colorgap.filter;

import com.heaven7.java.base.util.Throwables;
import com.heaven7.ve.colorgap.GapColorFilter;

import java.util.List;

import static com.heaven7.ve.colorgap.MetaInfo.FLAG_VIDEO_TAG;

/**
 * Created by heaven7 on 2018/3/16 0016.
 */

public class VideoTagFilter extends GapColorFilter {
    /**
     * create gap color filter, by condition and weight
     *
     * @param mCondition the condition
     */
    public VideoTagFilter(VideoTagCondition mCondition) {
        super(mCondition);
    }

    @Override
    public int getFlag() {
        return FLAG_VIDEO_TAG;
    }

    @Override
    public float computeScore(GapColorCondition condition) {
        if (!mCondition.isSampleTypeCondition(condition, true)) {
            return MIN_SCORE;
        }
        VideoTagCondition mCondition = this.mCondition.as();
        VideoTagCondition inputCondition = condition.as();
        if(mCondition.tags.size() != inputCondition.tags.size()){
            return MIN_SCORE;
        }
        int size = mCondition.tags.size();
        int macthSize = 0;
        for(int i = 0 ; i < size ; i ++){
            List<Integer> list = inputCondition.tags.get(i);
            //input param match the case .
            for(int tag: list){
                if(mCondition.containsTag(i, tag)){
                    macthSize ++;
                    break;
                }
            }
        }
        return macthSize * 1f / size * MAX_SCORE;
    }

    public static class VideoTagCondition extends GapColorCondition{
        //[[or],[or]] 每个一维数组元素内部是 or.  之间是and关系
        private final List<List<Integer>> tags;

        public VideoTagCondition(List<List<Integer>> tags) {
            super();
            Throwables.checkNull(tags);
            this.tags = tags;
        }
        public boolean containsTag(int index, int tag){
            return tags.get(index).contains(tag);
        }
        @Override
        public boolean equals(Object o) {
            if (o == null || !(o instanceof VideoTagCondition)) {
                return false;
            }
            return tags.equals(((VideoTagCondition) o).tags);
        }
    }

}
