package com.heaven7.ve.test;

import com.heaven7.utils.CommonUtils;

import java.util.concurrent.TimeUnit;

/*public*/ class CutInterval {

    private final int startTime; //in seconds
    private final int endTime;

    public CutInterval(int startTime, int endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public long getStartTimeInFrames() {
        return CommonUtils.timeToFrame(startTime, TimeUnit.SECONDS);
    }

    public long getEndTimeInFrames() {
        return CommonUtils.timeToFrame(endTime, TimeUnit.SECONDS);
    }

}