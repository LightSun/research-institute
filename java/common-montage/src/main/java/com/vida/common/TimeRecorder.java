package com.vida.common;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeRecorder {

    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private String mName;
    private long mStartTime;
    private long mEndTime;

    public TimeRecorder(String mName) {
        this.mName = mName;
    }
    public TimeRecorder() {
        this("");
    }

    public void begin() {
        mStartTime = System.currentTimeMillis();
    }

    public void end() {
        mEndTime = System.currentTimeMillis();
    }

    public void setName(String mName) {
        this.mName = mName;
    }
    public void reset() {
        mStartTime = 0;
        mEndTime = 0;
    }

    public long endAndGetCost() {
        end();
        return cost();
    }

    public long cost() {
        if (mStartTime == 0 || mEndTime == 0) {
            throw new IllegalStateException("you must call begin() and end().");
        }
        return mEndTime - mStartTime;
    }
    @Override
    public String toString(){
        return toString(mName);
    }
    public String toString(String name){
        return name + " >>> startTime = " + SDF.format(new Date(mStartTime))
                + " ,endTime = " + SDF.format(new Date(mEndTime)) + " ,costTime = "
                + (mEndTime - mStartTime) + "ms";
    }
}
