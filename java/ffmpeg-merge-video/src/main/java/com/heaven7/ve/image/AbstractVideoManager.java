package com.heaven7.ve.image;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * the abstract video manager for image detect.
 *
 * @param <C> the callback type
 */
public abstract class AbstractVideoManager<C> {

    private final VideoFrameDelegate vfd;
    private final String videoSrc;
    private final int frameGap;

    private AtomicBoolean markDone = new AtomicBoolean(false);
    private AtomicInteger count;
    private C mCallback;

    public AbstractVideoManager(VideoFrameDelegate vfd, String videoSrc) {
        this(vfd, videoSrc, 1);
    }

    public AbstractVideoManager(VideoFrameDelegate vfd, String videoSrc, int gap) {
        this.videoSrc = videoSrc;
        this.vfd = vfd;
        this.frameGap = gap;
    }

    public void detect(C callback) {
        if (mCallback != null) {
            throw new IllegalStateException();
        }
        this.mCallback = callback;
        final int duration = vfd.getDuration(videoSrc);
        onPreDetect(callback, duration);

        this.count = new AtomicInteger(duration + 1);
        int time = 0;
        while (time <= duration) {
            byte[] data = vfd.getFrame(videoSrc, time);
            onDetect(callback, time, data);
            time += frameGap;
        }
        //mark done
        markDone.compareAndSet(false, true);
        checkDone();
    }

    private void checkDone() {
        if (markDone.get() && count.get() == 0) {
            onDetectDone(mCallback, videoSrc);
        }
    }

    /**
     * called when one time of image is detect done.
     *
     * @param time the time in seconds.
     */
    protected void publishDetectDone(int time) {
        count.decrementAndGet();
        checkDone();
    }

    /**
     * called on pre detect
     *
     * @param callback the callback
     * @param duration the duration of video
     */
    protected void onPreDetect(C callback, int duration) {

    }

    protected void onDetect(C callback, int time, byte[] data) {

    }

    protected void onDetectDone(C mCallback, String videoSrc) {

    }

    public interface VideoFrameDelegate {
        /**
         * get the frame from video file.
         *
         * @param videoFile     the video file
         * @param timeInSeconds the time to get the frame . in seconds
         * @return the frame data.
         */
        byte[] getFrame(String videoFile, long timeInSeconds);

        /**
         * get the video duration. in seconds
         */
        int getDuration(String videoFile);
    }
}
