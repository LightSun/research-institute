package com.heaven7.android.bpmdetect;

/**
 * Created by heaven7 on 2019/6/1.
 */
public final class BpmDetector {

    private long ptr;

    static {
        System.loadLibrary("audio-detect");
    }

    public boolean isAttached(){
        return ptr != 0;
    }

    /**
     * attach bpm detector
     * @param numChannels the channels count
     * @param sampleRate the sample rate
     */
    public void attach(int numChannels, int sampleRate){
        if(isAttached()){
            throw new IllegalStateException("you should call detach first.");
        }
        ptr = _onNativeInit(numChannels, sampleRate);
    }

    /**
     * detach the bpm detector
     */
    public void detach(){
        if(ptr != 0){
            _onNativeRelease(ptr);
            ptr = 0;
        }
    }

    /**
     * set sample datas
     * @param data the sample data
     * @param numSamples the number of samples.
     */
    public void putSampleData(short[] data, int numSamples){
        checkAttached();
        _setSampleData(ptr, data, numSamples);
    }

    public float getBmp(){
        checkAttached();
        return _getBmp(ptr);
    }

    private void checkAttached() {
        if(ptr == 0){
            throw new IllegalStateException("you should call attach first.");
        }
    }

    @Override
    protected void finalize() throws Throwable {
        detach();
        super.finalize();
    }

    private native void _onNativeRelease(long ptr);

    private native void _setSampleData(long ptr, short[] data, int numSamples);

    private native long _onNativeInit(int numChannels, int sampleRate);

    private native float _getBmp(long ptr);
}
