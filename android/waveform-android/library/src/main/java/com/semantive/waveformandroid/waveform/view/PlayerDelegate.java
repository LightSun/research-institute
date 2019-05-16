package com.semantive.waveformandroid.waveform.view;

/**
 * Created by heaven7 on 2019/5/13.
 */
public interface PlayerDelegate {

    boolean isPlaying();

    void pause();

    int getCurrentPosition();

    /** in mill seconds */
    int getPlayStartOffset();

    /** in mill seconds */
    int getEndPosition();
}
