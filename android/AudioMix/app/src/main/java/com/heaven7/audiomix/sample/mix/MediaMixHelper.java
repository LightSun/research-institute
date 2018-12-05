package com.heaven7.audiomix.sample.mix;

import android.annotation.TargetApi;
import android.media.MediaCodec;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.support.annotation.NonNull;

import com.heaven7.core.util.Logger;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by heaven7 on 2018/12/4 0004.
 */
@TargetApi(18) //TODO have bugs
public class MediaMixHelper implements MediaMixThread.MediaMixManagerDelegate {

    private static final String TAG = "MediaMixHelper";

    private final AtomicInteger mFinishRef = new AtomicInteger();
    private final AtomicBoolean mStarted = new AtomicBoolean();
    private MediaMuxer mMediaMuxer;
    private VideoMixThread mVideoThread;
    private AudioMixThread mAudioThread;
    private long mStartTime;

    public void start(String videoPath, String audioPath, String destFile){
        if(mMediaMuxer != null){
            throw new IllegalStateException("can't start mix twice.");
        }
        mFinishRef.set(2);
        mStarted.set(false);
        try {
            mMediaMuxer = new MediaMuxer(destFile, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
            mVideoThread = new VideoMixThread(videoPath);
            mAudioThread = new AudioMixThread(audioPath);

            mVideoThread.init(mMediaMuxer, this);
            mAudioThread.init(mMediaMuxer, this);

            mStartTime = System.currentTimeMillis();

            markMuxerStart();
            //start audio wait audio format added.
            mAudioThread.start();
            mVideoThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isStarted() {
        return mStarted.get();
    }

    @Override
    public void markMuxerStart() {
        if(mStarted.compareAndSet(false , true)){
            Logger.d(TAG, "markMuxerStart", "");
            mMediaMuxer.start();
            //start video thread now.
            //mVideoThread.start();
        }
    }

    @Override
    public void writeSampleData(int trackIndex, @NonNull ByteBuffer byteBuf, @NonNull MediaCodec.BufferInfo bufferInfo) {
        synchronized (this){
            mMediaMuxer.writeSampleData(trackIndex, byteBuf, bufferInfo);
        }
    }

    @Override
    public int addTrack(MediaFormat format) {
        return mMediaMuxer.addTrack(format);
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
            release();//TODO have bugss
        }
    }

    private void release() {
        if(mVideoThread != null){
            mVideoThread.release();
            mVideoThread = null;
        }
        if(mAudioThread != null){
            mAudioThread.release();
            mAudioThread = null;
        }
        if(mMediaMuxer != null){
            mMediaMuxer.stop();
            mMediaMuxer.release();
            mMediaMuxer = null;
        }
        mStarted.compareAndSet(true, false);
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
        mStarted.compareAndSet(true, false);
    }

    public interface Callback{
        void onMixDone(String videoPath, String audioPath, String destFile);
    }
}
