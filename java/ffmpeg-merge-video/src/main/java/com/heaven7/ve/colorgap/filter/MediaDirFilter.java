package com.heaven7.ve.colorgap.filter;

import com.heaven7.java.visitor.collection.VisitServices;
import com.heaven7.ve.colorgap.GapColorFilter;
import com.heaven7.ve.colorgap.MetaInfo;

import java.util.ArrayList;
import java.util.List;

import static com.heaven7.ve.colorgap.MetaInfo.FLAG_MEDIA_DIR;

/**
 * Created by heaven7 on 2018/3/16 0016.
 */
public class MediaDirFilter extends GapColorFilter {

    public static final MediaDirFilter AIR_SHOT;

    static {
        MediaDirCondition empty = new MediaDirCondition();
        empty.addTag(MetaInfo.DIR_EMPTY);
        AIR_SHOT = new MediaDirFilter(empty);
    }

    /**
     * create gap color filter by condition
     *
     * @param mCondition the condition
     */
    public MediaDirFilter(MediaDirCondition mCondition) {
        super(mCondition);
    }
    @Override
    public float computeScore(GapColorCondition condition) {
        if (!mCondition.isSampleTypeCondition(condition, true)) {
            return MIN_SCORE;
        }
        MediaDirCondition mCondition = this.mCondition.as();
        return mCondition.contains(condition.as()) ? MAX_SCORE : MIN_SCORE;
    }

    @Override
    public int getFlag() {
        return FLAG_MEDIA_DIR;
    }

    public static class MediaDirCondition extends GapColorCondition {

        /*pro*/ final List<String> tags = new ArrayList<>();

        public MediaDirCondition() {
            super();
        }
        public void clear() {
            tags.clear();
        }

        public void addTag(String tag) {
            VisitServices.from(tags).addIfNotExist(tag);
        }
        public void addAllTags(List<String> tags){
            this.tags.addAll(tags);
        }
        public boolean contains(MediaDirCondition mdc) {
            return tags.containsAll(mdc.tags);
        }

        public float intersectPercent(MediaDirCondition mdc) {
            int intersectCount = 0;
            for (String tag : tags) {
                if (mdc.tags.contains(tag)) {
                    intersectCount++;
                }
            }
            return intersectCount * 1f / tags.size();
        }
        @Override
        public boolean equals(Object o) {
            if (o == null || !(o instanceof MediaDirCondition)) {
                return false;
            }
            return tags.equals(((MediaDirCondition) o).tags);
        }
    }
}
