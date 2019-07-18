package com.heaven7.android.chordino;

import java.io.File;
import java.io.IOException;

/**
 * Created by heaven7 on 2019/6/5.
 */
public interface ISoundFile {

    interface ProgressListener {
        /**
         * Will be called by the CheapSoundFile subclass periodically
         * with values between 0.0 and 1.0.  Return true to continue
         * loading the file, and false to cancel.
         */
         boolean reportProgress(double fractionComplete);
    }

    void setProgressListener(ProgressListener listener);

    void ReadFile(File f) throws IOException;

    //采样数/每帧的采样数
    int getNumFrames();

    float[] getFrameGains();

    int getSampleRate();

    int getChannels();
}
