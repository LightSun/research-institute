package com.heaven7.ve;

/**
 * @author heaven7
 */
public class NativeMedia implements INativeObject, ApiNativeMedia {

    private Callback mCallback;
    private long mPtr;

    public NativeMedia() {
        this.mPtr = nCreate();
    }

    public native void addVideoPart(String videoFile, float fileStart, int beginFrame, int countFrames, int beforeIndex);

    public native void addMusicTrack(String musicPath, float insertTime, float startTime, float endTime, int audioType);

    public native void setWriteOutPath(String path);

    public native void startGenMcVideo();

    public native void pauseGenMcVideo();

    public native void resumeGenMcVideo();

    public native void stopGenMcVideo();

    @Override
    public Callback getCallback() {
        return mCallback;
    }
    @Override
    public void setCallback(Callback mCallback) {
        this.mCallback = mCallback;
    }
    @Override
    public long getNativePointer() {
        return mPtr;
    }
    @Override
    public void destroyNative() {
        if (mPtr != 0) {
            nRelease(mPtr);
            mPtr = 0;
        }
    }
    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        destroyNative();
    }

    private native long nCreate();

    private native void nRelease(long mPtr);

}
