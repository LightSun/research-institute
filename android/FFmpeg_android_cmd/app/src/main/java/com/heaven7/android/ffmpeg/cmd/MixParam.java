package com.heaven7.android.ffmpeg.cmd;

/**
 * Created by heaven7 on 2019/9/3.
 */
public final class MixParam {

    public String filepath;
    public float startTime; //in seconds
    public float endTime;

    public MixParam(String filepath, float startTime, float endTime) {
        this.filepath = filepath;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
