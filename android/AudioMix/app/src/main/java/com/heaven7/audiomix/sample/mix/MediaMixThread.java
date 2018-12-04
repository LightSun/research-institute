package com.heaven7.audiomix.sample.mix;

import android.annotation.TargetApi;
import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaMuxer;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created by heaven7 on 2018/12/4 0004.
 */
@TargetApi(18)
/*public*/ abstract class MediaMixThread extends Thread implements MediaMixContext{

    public static final int BUFFER_SIZE = 512 * 1024; //512 * 1024
    public static final int MAX_INPUT_SIZE = 10240;
    public static final int BIT_RATE = 65536; // 64 * 1024
    public static final int SAMPLE_RATE = 44100; // acc sample rate
    public static final Long TIMEOUT_US = 1000L;

    private final MediaExtractor mExtractor = new MediaExtractor();
    private final MediaInfo info = new MediaInfo();
    private final String path;
    private MediaMuxer muxer;
    private MediaMixManagerDelegate mMixManageDelegate;

    private int mediaType = UNSUPPORT;
    /** in us */
    private long startTime = -1;
    /** in us */
    private long endTime = - 1;

    public MediaMixThread(String path) {
        this.path = path;
    }
    public String getPath() {
        return path;
    }
    public MediaInfo getMediaInfo() {
        return info;
    }
    public MediaExtractor getMediaExtractor() {
        return mExtractor;
    }
    public MediaMuxer getMediaMuxer() {
        return muxer;
    }
    public MediaMixManagerDelegate getMediaMixManageDelegate() {
        return mMixManageDelegate;
    }

    public long getStartTime() {
        return startTime;
    }
    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }
    public long getEndTime() {
        return endTime;
    }
    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }
    public int getMediaType() {
        return mediaType;
    }
    protected MediaInfo getMediaInfo(MediaFormat format){
        MediaInfo info = getMediaInfo();
        info.duration = format.getLong(MediaFormat.KEY_DURATION);
        info.mime = format.getString(MediaFormat.KEY_MIME);
        return info;
    }

    public void init(MediaMuxer muxer, MediaMixManagerDelegate delegate) throws IOException{
        this.mMixManageDelegate = delegate;
        this.muxer = muxer;
        this.mediaType = getMediaType(getPath());
        initImpl(muxer);
    }

    public void release(){
        muxer = null;
        mMixManageDelegate = null;
    }

    protected abstract void initImpl(MediaMuxer muxer) throws IOException;

    /**
     * read from extractor and write to muxer
     * @param trackIndex the track index
     */
    protected void readAndWriteDirectly(int trackIndex) {
        //super.run();
        MediaMuxer muxer = getMediaMuxer();
        if(muxer == null || trackIndex < 0){
            return;
        }
        ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
        MediaCodec.BufferInfo info = new MediaCodec.BufferInfo();
        //seek to the start pos
        MediaExtractor extractor = getMediaExtractor();
        long startTime = getStartTime();
        extractor.seekTo(startTime >=0 ? startTime : 0, MediaExtractor.SEEK_TO_CLOSEST_SYNC);

        long endTime = getEndTime();
        //start read
        int sampleSize;
        while ((sampleSize = extractor.readSampleData(buffer, 0)) > 0){
            info.offset = 0;
            info.size = sampleSize;
            info.flags = MediaCodec.BUFFER_FLAG_SYNC_FRAME;
            info.presentationTimeUs = extractor.getSampleTime();

            //not exactly
            if(endTime > 0 && endTime < info.presentationTimeUs){
                break;
            }
            muxer.writeSampleData(trackIndex, buffer, info);
            extractor.advance();
        }
    }

    public static class MediaInfo{
        /** the duration of media . video or audio . in us */
        public long duration;
        public String mime;
    }

    public interface MediaMixManagerDelegate {
        default void markVideoStart(){}
        default void markVideoEnd(){}
        default void markAudioStart(){}
        default void markAudioEnd(){}
    }
}
