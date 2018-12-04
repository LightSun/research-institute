package com.heaven7.audiomix.sample;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaMuxer;

import com.heaven7.core.util.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created by heaven7 on 2018/11/30 0030.
 */
public class VideoAudioMixer {

    private static final String TAG = "VideoAudioMixer";

    private final Context mContext;

    public VideoAudioMixer(Context context) {
        this.mContext = context instanceof Application ? context : context.getApplicationContext();
    }

    @TargetApi(18)
    public void mix(String videoPath, String audioPath, String outPath) throws IOException {
         //1, extract video and audio format.
         // read and write sample

        //1, prepare path
        File outFile = new File(outPath);
        if(outFile.exists()){
            outFile.delete();
        }else{
            outFile.getParentFile().mkdirs();
        }

        //extract video
        MediaExtractor videoExtractor = new MediaExtractor();
        videoExtractor.setDataSource(videoPath);
        int videoTrackIndex = - 1;
        for(int i = 0, size = videoExtractor.getTrackCount() ; i< size ; i ++){
            MediaFormat trackFormat = videoExtractor.getTrackFormat(i);
            String mime = trackFormat.getString(MediaFormat.KEY_MIME);
            if(mime.startsWith("video")){
                videoTrackIndex = i;
                break;
            }
            // mediaFormatList.add(videoExtractor1.getTrackFormat(0));
        }
        if(videoTrackIndex < 0){
            throw new IllegalStateException();
        }
        videoExtractor.selectTrack(videoTrackIndex);
        videoExtractor.seekTo(0, MediaExtractor.SEEK_TO_CLOSEST_SYNC);

        //extract audio
        MediaExtractor audioExtractor = new MediaExtractor();
        audioExtractor.setDataSource(audioPath);
        int audioTrackIndex = -1;
        for(int i = 0, size = audioExtractor.getTrackCount() ; i< size ; i ++){
            MediaFormat trackFormat = audioExtractor.getTrackFormat(i);
            String mime = trackFormat.getString(MediaFormat.KEY_MIME);
            Logger.d(TAG, "mix", "audio: mime = " + mime);
            if(mime.startsWith("audio")){
                audioTrackIndex = i;
                break;
            }
            // mediaFormatList.add(videoExtractor1.getTrackFormat(0));
        }
        if(audioTrackIndex < 0){
            throw new IllegalStateException();
        }
        audioExtractor.selectTrack(audioTrackIndex);
        audioExtractor.seekTo(0, MediaExtractor.SEEK_TO_CLOSEST_SYNC);
        //prepare param
        MediaCodec.BufferInfo videoBufferInfo = new MediaCodec.BufferInfo();
        MediaCodec.BufferInfo audioBufferInfo = new MediaCodec.BufferInfo();
        int frameCount = 0;
        int offset = 100;
        int sampleSize = 256 * 1024;
        ByteBuffer videoBuf = ByteBuffer.allocate(sampleSize);
        ByteBuffer audioBuf = ByteBuffer.allocate(sampleSize);
        //
       // MediaCodec codec = MediaCodec.createEncoderByType()

        //muxer
        MediaMuxer muxer = new MediaMuxer(outPath, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
        int audioTrack = muxer.addTrack(audioExtractor.getTrackFormat(audioTrackIndex));
        int videoTrack = muxer.addTrack(videoExtractor.getTrackFormat(videoTrackIndex));
        muxer.start();
        //video
        do{
            videoBufferInfo.offset = offset;
            videoBufferInfo.size = videoExtractor.readSampleData(videoBuf, offset);
            if(videoBufferInfo.size < 0){
                break;
            }
            videoBufferInfo.presentationTimeUs = videoExtractor.getSampleTime();
            videoBufferInfo.flags = videoExtractor.getSampleFlags();
            muxer.writeSampleData(videoTrack, videoBuf, videoBufferInfo);
            //next sample
            videoExtractor.advance();
            frameCount++;
        }while (true);
        //
        Logger.d(TAG, "mix", "video: frameCount = " + frameCount);
        frameCount = 0;
        //audio
        do{
            audioBufferInfo.offset = offset;
            audioBufferInfo.size = audioExtractor.readSampleData(audioBuf, offset);
            if(audioBufferInfo.size < 0){
                break;
            }
            audioBufferInfo.presentationTimeUs = audioExtractor.getSampleTime();
            audioBufferInfo.flags = audioExtractor.getSampleFlags();
            muxer.writeSampleData(audioTrack, audioBuf, audioBufferInfo);
            //next sample
            audioExtractor.advance();
            frameCount++;
        }while (true);
        Logger.d(TAG, "mix", "audio: frameCount = " + frameCount);

        //release
        muxer.stop();
        muxer.release();
    }

}
