package com.heaven7.ve.gap;

import com.heaven7.ve.colorgap.GapColorFilter;

/**
 * the video part item delegate
 * Created by heaven7 on 2018/3/17 0017.
 */

public interface ItemDelegate {

    String getVideoPath();

    /** if the plaid is hold by item. true if is hold. */
    boolean isHold();

    void setHold(boolean hold);

    /**
     * get the color condition of frame data which is from video
     * @return the gap color condition
     */
    GapColorFilter.GapColorCondition getColorCondition();

    /**
     * get the domain tag score
     * @return the domain tag score
     */
    float getTotalScore();

    /**
     * copy item
     * @return the new item of copied.
     */
    ItemDelegate copy();

    boolean isVideo();

    /**
     * get max duration of video
     */
    long getMaxDuration();
}

