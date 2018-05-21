package com.heaven7.ve.colorgap.filter;

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
        return mCondition.equals(condition.as()) ?  MAX_SCORE : MIN_SCORE;
    }

    public static class VideoTagCondition extends GapColorCondition{
        //[[or],[or]] 为了满足二维内部是or的关系，一维之间是and的关系
        private final List<List<Integer>> tags;

        public VideoTagCondition(List<List<Integer>> tags) {
            super();
            this.tags = tags;
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
