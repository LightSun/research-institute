package com.heaven7.audiomix.sample;

import android.annotation.TargetApi;
import android.content.Context;
import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.util.SparseArray;
import android.widget.Toast;

import com.heaven7.core.util.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by heaven7 on 2018/3/29 0029.
 */

public class AudioMixer {

    private static final String TAG = "AudioMixer";

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void muxing(Context context, String path1, String path2) {
        Log.d(TAG, "start mix ");
        String outputFile = "";
        try {

            File file = new File(Environment.getExternalStorageDirectory() + File.separator + "AudioMixer_sample.mp4");
            if(file.exists()){
                file.delete();
            }
            file.createNewFile();
            outputFile = file.getAbsolutePath();

            MediaExtractor videoExtractor1 = new MediaExtractor();
            videoExtractor1.setDataSource(path1);

            List<MediaFormat> mediaFormatList = new ArrayList<>();
            for(int i = 0, size = videoExtractor1.getTrackCount() ; i< size ; i ++){
                MediaFormat trackFormat = videoExtractor1.getTrackFormat(i);
                String mime = trackFormat.getString(MediaFormat.KEY_MIME);
                Logger.d(TAG, "muxing", "mime = " + mime);
               // mediaFormatList.add(videoExtractor1.getTrackFormat(0));
            }

            MediaExtractor videoExtractor2 = new MediaExtractor();
            videoExtractor2.setDataSource(path2);

            Log.d(TAG, "Video[ path1 ] Extractor Track Count " + videoExtractor1.getTrackCount());
            Log.d(TAG, "Video[ path2 ] Extractor Track Count " + videoExtractor2.getTrackCount());

            MediaMuxer muxer = new MediaMuxer(outputFile, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);

            videoExtractor1.selectTrack(1);
            MediaFormat videoFormat = videoExtractor1.getTrackFormat(1);
            int videoTrack = muxer.addTrack(videoFormat);

            videoExtractor2.selectTrack(1);
            MediaFormat audioFormat = videoExtractor2.getTrackFormat(1);
            int audioTrack = muxer.addTrack(audioFormat);

            Log.d(TAG, "Video[ path1 ] Format " + videoFormat.toString());
            Log.d(TAG, "Video[ path2 ] Format " + audioFormat.toString());
            Logger.d(TAG, "muxing", "videoTrack index = " + videoTrack);
            Logger.d(TAG, "muxing", "audioTrack index = " + audioTrack);

            boolean sawEOS = false;
            int frameCount = 0;
            int offset = 100;
            int sampleSize = 256 * 1024;
            ByteBuffer videoBuf = ByteBuffer.allocate(sampleSize);
            ByteBuffer videoBuf2 = ByteBuffer.allocate(sampleSize);
            MediaCodec.BufferInfo videoBufferInfo = new MediaCodec.BufferInfo();
            MediaCodec.BufferInfo videoBufferInfo2 = new MediaCodec.BufferInfo();

            videoExtractor1.seekTo(0, MediaExtractor.SEEK_TO_CLOSEST_SYNC);
            videoExtractor2.seekTo(0, MediaExtractor.SEEK_TO_CLOSEST_SYNC);

            muxer.start();

            while (!sawEOS) {
                videoBufferInfo.offset = offset;
                videoBufferInfo.size = videoExtractor1.readSampleData(videoBuf, offset);

                if (videoBufferInfo.size < 0 || videoBufferInfo2.size < 0) {
                    Log.d(TAG, "saw input EOS.");
                    sawEOS = true;
                    videoBufferInfo.size = 0;

                } else {
                    videoBufferInfo.presentationTimeUs = videoExtractor1.getSampleTime();
                    videoBufferInfo.flags = videoExtractor1.getSampleFlags();
                    muxer.writeSampleData(videoTrack, videoBuf, videoBufferInfo);
                    videoExtractor1.advance();

                    frameCount++;
                   /* Log.d(TAG, "Frame (" + frameCount + ") Video PresentationTimeUs:" + videoBufferInfo.presentationTimeUs
                            + " Flags:" + videoBufferInfo.flags + " Size(KB) " + videoBufferInfo.size / 1024);
                    Log.d(TAG, "Frame (" + frameCount + ") Audio PresentationTimeUs:" + videoBufferInfo2.presentationTimeUs
                            + " Flags:" + videoBufferInfo2.flags + " Size(KB) " + videoBufferInfo2.size / 1024);*/
                }
            }

            Toast.makeText(context, "Video[ path1 ] frame:" + frameCount, Toast.LENGTH_SHORT).show();


            boolean sawEOS2 = false;
            int frameCount2 = 0;
            while (!sawEOS2) {
                frameCount2++;

                videoBufferInfo2.offset = offset;
                videoBufferInfo2.size = videoExtractor2.readSampleData(videoBuf2, offset);

                if (videoBufferInfo.size < 0 || videoBufferInfo2.size < 0) {
                    Log.d(TAG, "saw input EOS.");
                    sawEOS2 = true;
                    videoBufferInfo2.size = 0;
                } else {
                    videoBufferInfo2.presentationTimeUs = videoExtractor2.getSampleTime();
                    videoBufferInfo2.flags = videoExtractor2.getSampleFlags();
                    int trackIndex = videoExtractor2.getSampleTrackIndex();
                    muxer.writeSampleData(audioTrack, videoBuf2, videoBufferInfo2);
                    videoExtractor2.advance();


                 /*   Log.d(TAG, "Frame (" + frameCount + ") Video PresentationTimeUs:" + videoBufferInfo.presentationTimeUs
                            + " Flags:" + videoBufferInfo.flags + " Size(KB) " + videoBufferInfo.size / 1024);
                    Log.d(TAG, "Frame (" + frameCount + ") Audio PresentationTimeUs:" + videoBufferInfo2.presentationTimeUs
                            + " Flags:" + videoBufferInfo2.flags + " Size(KB) " + videoBufferInfo2.size / 1024);*/

                }
            }

            Toast.makeText(context, "Video[ path2 ] frame:" + frameCount2, Toast.LENGTH_SHORT).show();

            muxer.stop();
            muxer.release();
            Log.d(TAG, "end mix ");
        }catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "Mixer Error" + e.getMessage());
        }
    }

    /**
     * 归一化混音
     * */
    public static byte[] normalizationMix(byte[][] allAudioBytes){
        if (allAudioBytes == null || allAudioBytes.length == 0)
            return null;

        byte[] realMixAudio = allAudioBytes[0];

        //如果只有一个音频的话，就返回这个音频数据
        if(allAudioBytes.length == 1)
            return realMixAudio;

        //row 有几个音频要混音
        int row = realMixAudio.length /2;
        //
        short[][] sourecs = new short[allAudioBytes.length][row];
        for (int r = 0; r < 2; ++r) {
            for (int c = 0; c < row; ++c) {
                sourecs[r][c] = (short) ((allAudioBytes[r][c * 2] & 0xff) | (allAudioBytes[r][c * 2 + 1] & 0xff) << 8);
            }
        }

        //coloum第一个音频长度 / 2
        short[] result = new short[row];
        //转成short再计算的原因是，提供精确度，高端的混音软件据说都是这样做的，可以测试一下不转short直接计算的混音结果
        for (int i = 0; i < row; i++) {
            int a = sourecs[0][i] ;
            int b = sourecs[1][i] ;
            if (a <0 && b<0){
                int i1 = a  + b  - a  * b / (-32768);
                if (i1 > 32767){
                    result[i] = 32767;
                }else if (i1 < - 32768){
                    result[i] = -32768;
                }else {
                    result[i] = (short) i1;
                }
            }else if (a > 0 && b> 0){
                int i1 = a + b - a  * b  / 32767;
                if (i1 > 32767){
                    result[i] = 32767;
                }else if (i1 < - 32768){
                    result[i] = -32768;
                }else {
                    result[i] = (short) i1;
                }
            }else {
                int i1 = a + b ;
                if (i1 > 32767){
                    result[i] = 32767;
                }else if (i1 < - 32768){
                    result[i] = -32768;
                }else {
                    result[i] = (short) i1;
                }
            }
        }
        return toByteArray(result);
    }
    public static byte[] toByteArray(short[] src) {
        int count = src.length;
        byte[] dest = new byte[count << 1];
        for (int i = 0; i < count; i++) {
            dest[i * 2 +1] = (byte) ((src[i] & 0xFF00) >> 8);
            dest[i * 2] = (byte) ((src[i] & 0x00FF));
        }
        return dest;
    }
}
