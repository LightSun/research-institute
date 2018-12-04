package com.heaven7.audiomix.sample.mix;

import android.annotation.TargetApi;
import android.media.MediaMuxer;

import com.heaven7.core.util.Logger;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by heaven7 on 2018/12/4 0004.
 */
@TargetApi(18) //TODO have bugs
public class MediaMixHelper implements MediaMixThread.MediaMixManagerDelegate {

    private static final String TAG = "MediaMixHelper";

    private final AtomicInteger mFinishRef = new AtomicInteger();
    private MediaMuxer mMediaMuxer;
    private VideoMixThread mVideoThread;
    private AudioMixThread mAudioThread;
    private long mStartTime;

    public void start(String videoPath, String audioPath, String destFile){
        if(mMediaMuxer != null){
            throw new IllegalStateException("can't start mix twice.");
        }
        mFinishRef.set(2);
        try {
            mMediaMuxer = new MediaMuxer(destFile, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
            mVideoThread = new VideoMixThread(videoPath);
            mAudioThread = new AudioMixThread(audioPath);

            mVideoThread.init(mMediaMuxer, this);
            mAudioThread.init(mMediaMuxer, this);
            mMediaMuxer.start();

            mStartTime = System.currentTimeMillis();

            mVideoThread.start();
            mAudioThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void markAudioEnd() {
        Logger.d(TAG, "markAudioEnd", "");
        checkDone();
    }

    @Override
    public void markVideoEnd() {
        Logger.d(TAG, "markVideoEnd", "");
        checkDone();
    }

    private void checkDone(){
        if(mFinishRef.decrementAndGet() == 0){
            Logger.d(TAG, "checkDone", "mix done . cost time " + (System.currentTimeMillis() - mStartTime));
            //TODO all done. should callback
            cancel();//TODO have bugss
        }
    }

    public synchronized void cancel(){
         if(mVideoThread != null){
             mVideoThread.interrupt();
             mVideoThread.release();
             mVideoThread = null;
         }
         if(mAudioThread != null){
             mAudioThread.interrupt();
             mAudioThread.release();
             mAudioThread = null;
         }
        if(mMediaMuxer != null){
            mMediaMuxer.stop();
            mMediaMuxer.release();
            mMediaMuxer = null;
        }
    }

    public interface Callback{
        void onMixDone(String videoPath, String audioPath, String destFile);
    }
}
