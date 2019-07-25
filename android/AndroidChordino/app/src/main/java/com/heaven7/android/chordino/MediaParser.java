package com.heaven7.android.chordino;

import android.support.annotation.Keep;

import java.io.File;
import java.io.IOException;

/**
 * parse mp3. this called from native.
 * Created by heaven7 on 2019/7/18.
 */
@Keep
public final class MediaParser {

    private final CommonSoundFile mSoundFile = new CommonSoundFile();
    private float[] mSampleDatas;
    private int offset = 0;
    private float[] mTempArr;

    public MediaParser() {
    }

    @Keep
    public void openMedia(String file){
        try {
            mSoundFile.ReadFile(new File(file));
            mSampleDatas = mSoundFile.getFrameGains();
            System.out.println("sample size = " + mSampleDatas.length);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Keep
    public float[] readMedia(int blockSize, int[] sampleCount){
        if(mTempArr == null){
            mTempArr = new float[blockSize * mSoundFile.getChannels()];
        }
        int expectSize = blockSize * mSoundFile.getChannels();
        int remaining = mSampleDatas.length - offset;
        if(remaining >= expectSize){
            System.arraycopy(mSampleDatas, offset, mTempArr, 0, expectSize);
            offset += expectSize;
            sampleCount[0] = blockSize;
        }else {
            System.arraycopy(mSampleDatas, offset, mTempArr, 0, remaining);
            offset += remaining;
            sampleCount[0] = remaining / mSoundFile.getChannels();
        }
        return mTempArr;
    }
}
