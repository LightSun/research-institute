package com.heaven7.android.bpmdetect.soundfile;

/**
 * Created by heaven7 on 2019/6/14.
 */
public interface IAudioOutputDelegate {

    void out(short[] data, int numberSamples);

    void onDetectInit(int channels, int sampleRate);
}
