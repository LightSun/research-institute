package com.heaven7.audiomix.sample;

import android.annotation.TargetApi;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.util.Log;

import com.heaven7.audiomix.sample.utils.MediaUtils;
import com.heaven7.core.util.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * 音视频合成器 test ok. 60s -> 15s
 */
@TargetApi(18)
public class Compounder {
    private static final String TAG = "Compounder";

    private static int MP3_TYPE = 0;
    private static int AAC_TYPE = 1;
    private static int M4A_TYPE = 2;
    private static int NOT_DC_EC_TYPE  = 3;   //不用重新编解码
    private static int NOT_SUPPORT_TYPE =  -1;//不支持类型


    private int MAX_INPUT_SIZE = 10240;
    private int VIDEO_READ_SAMPLE_SIZE = 524288;//512 * 1024
    private int BIT_RATE = 65536; // 64 * 1024
    private int SAMPLE_RATE = 44100;// acc sample rate
    private Long TIMEOUT_US = 1000L;

    private String mAudioPath;
    private String mVideoPath;
    private String mDstFilePath;

    private MediaExtractor mVideoExtractor = new MediaExtractor();
    private MediaExtractor mAudioExtractor = new MediaExtractor();
    private MediaMuxer mMediaMuxer = null;
    private int mAudioTrackIndex = -1;
    private int mVideoTrackIndex = -1;
    private int mAudioDEType = -1;
    private long mMaxTimeStamp = 0;

    private MediaFormat mVideoFormat;
    private MediaFormat mAudioFormat;
    private MediaCodec mDecoder;
    private MediaCodec mEncoder;

    private ByteBuffer[] mDecodeInputBuffer;
    private ByteBuffer[] mDecodeOutputBuffer;
    private ByteBuffer[] mEncodeInputBuffer;
    private ByteBuffer[] mEncodeOutputBuffer;

    /**
     *
     * @param videoPath 源视频路径
     * @param audioPath 音频路径
     * @param audioDEType 音频类型
     * @param dstPath 目标文件路径
     */
    private Compounder(String videoPath, String audioPath, int audioDEType, String dstPath){
        mVideoPath = videoPath;
        mAudioPath = audioPath;
        mAudioDEType = audioDEType;
        mDstFilePath = dstPath;
    }

    /**
     *
     * @param videoPath 源视频路径
     * @param audioPath 音频路径
     * @param dstPath 目标文件路径
     * @return null || compounder
     */
    public static Compounder createCompounder(String videoPath, String audioPath, String dstPath){
        if(checkVideo(videoPath)){
            int audioEDType = checkAudio(audioPath);
            if(audioEDType != NOT_SUPPORT_TYPE)
                return new Compounder(videoPath, audioPath, audioEDType, dstPath);
        }
        return null;
    }

    /**
     * 检查音频是否合法
     */
    private static int checkAudio(String audioPath) {
        if(audioPath != null){
            File file = new File(audioPath);
            if(file.exists() && file.isFile()){
                if(audioPath.endsWith(".mp3")){
                    return MP3_TYPE;
                }
                if(audioPath.endsWith(".aac")){
                    return AAC_TYPE;
                }
                if(audioPath.endsWith(".m4a")){
                    return M4A_TYPE;
                }
            }
        }
        return -1;
    }

    /**
     * 检查视频是否符合要求
     */
    private static boolean checkVideo(String videoPath) {
        if(videoPath != null && videoPath.endsWith(".mp4")){
            File file = new File(videoPath);
            if(file.exists() && file.isFile()){
                return true;
            }
        }
        return false;
    }

    /**
     * 开始合成
     */
    public void start(){
        if(mAudioDEType == MP3_TYPE) {
            initDecodeAudio();
            initEncodeAudio();
            if (initMuxer()) {
                if (handleTrack(mMediaMuxer, mVideoTrackIndex, mVideoExtractor)) {
                    decodeInputBuffer();//解码--->编码--->合成输出文件
                }
            }
        }else if (mAudioDEType == NOT_DC_EC_TYPE){//直接合成文件
            if(initMuxer()){
                handleTrack(mMediaMuxer, mVideoTrackIndex, mVideoExtractor);
                handleTrack(mMediaMuxer, mAudioTrackIndex, mAudioExtractor);
            }
        }
        release();
        Log.e(TAG, "finished~~~~~~~~~~~~~~~~~~~~");
    }

    /**
     * 初始化混合器
     */
    private boolean initMuxer() {
        Logger.d(TAG, "initMuxer", "");
        try {
            if((mVideoFormat = MediaUtils.getMediaFormat(mVideoExtractor, MediaUtils.TYPE_VIDEO, mVideoPath)) != null) {
                mMediaMuxer = new MediaMuxer(mDstFilePath, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
                mVideoTrackIndex = mMediaMuxer.addTrack(mVideoFormat);//设置视频格式
                Log.e(TAG, " video long is : " +  mVideoFormat.getLong(MediaFormat.KEY_DURATION));
                mMaxTimeStamp = mVideoFormat.getLong(MediaFormat.KEY_DURATION);
                if(mAudioDEType == NOT_DC_EC_TYPE){
                    MediaFormat audioFormat = MediaUtils.getMediaFormat(mAudioExtractor, MediaUtils.TYPE_AUDIO, mAudioPath);
                    if(audioFormat != null) {
                        mAudioTrackIndex = mMediaMuxer.addTrack(audioFormat);
                    }else{
                        return false;
                    }
                    mMediaMuxer.start();
                }
                else if(mAudioDEType == MP3_TYPE) {
                    if(mDecoder == null || mEncoder == null){
                        return false;
                    }
                    mAudioTrackIndex = mMediaMuxer.addTrack(mEncoder.getOutputFormat());//设置目标的音频格式
                    mMediaMuxer.start();
                }
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
            mMediaMuxer = null;
        }
        return false;
    }


    /**
     * 获取输出的解码后的buffer
     */
    private void decodeOutputBuffer() {
        MediaCodec.BufferInfo info = new MediaCodec.BufferInfo();
        int outputIndex = mDecoder.dequeueOutputBuffer(info, -1);
        if (outputIndex >= 0) {
            byte[] chunk = new byte[info.size];
            mDecodeOutputBuffer[outputIndex].position(info.offset);
            mDecodeOutputBuffer[outputIndex].limit(info.offset + info.size);
            mDecodeOutputBuffer[outputIndex].get(chunk);
            mDecodeOutputBuffer[outputIndex].clear();
            mDecoder.releaseOutputBuffer(outputIndex, false);
            if (chunk.length > 0) {
                encodData(chunk, info.presentationTimeUs);
            }
        }
    }

    /**
     * 编码PCM数据
     * @param input pcm数据
     * @param presentationTimeUs 时间戳
     */
    private synchronized void encodData(byte[] input, long presentationTimeUs) {
        int inputBufferIndex = mEncoder.dequeueInputBuffer(-1);
        if (inputBufferIndex >= 0) {
            ByteBuffer inputBuffer = mEncodeInputBuffer[inputBufferIndex];
            inputBuffer.clear();
            inputBuffer.put(input);
            mEncoder.queueInputBuffer(inputBufferIndex, 0, input.length, presentationTimeUs, 0);
        }

        MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
        int outputBufferIndex = mEncoder.dequeueOutputBuffer(bufferInfo, TIMEOUT_US);
       /* if(mAudioTrackIndex < 0 && outputBufferIndex == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED){
            mAudioTrackIndex = mMediaMuxer.addTrack(mEncoder.getOutputFormat());
            mMediaMuxer.start();
            outputBufferIndex = mEncoder.dequeueOutputBuffer(bufferInfo, TIMEOUT_US);
        }*/
        while (outputBufferIndex >= 0) {
            int outBitsSize = bufferInfo.size;
            ByteBuffer outputBuffer = mEncodeOutputBuffer[outputBufferIndex];
            outputBuffer.position(bufferInfo.offset);
            outputBuffer.limit(bufferInfo.offset + outBitsSize);
            outputBuffer.position(bufferInfo.offset);
            mMediaMuxer.writeSampleData(mAudioTrackIndex, outputBuffer, bufferInfo);
            mEncoder.releaseOutputBuffer(outputBufferIndex, false);
            outputBufferIndex = mEncoder.dequeueOutputBuffer(bufferInfo, 0);
        }
    }

    /**
     * 输入要解码的buffer
     * @return 输入是否成功
     */
    private void decodeInputBuffer() {
        Logger.d(TAG, "decodeInputBuffer", "start >>> ");
        while (true) {
            int inputBufIndex = mDecoder.dequeueInputBuffer(TIMEOUT_US);
            if (inputBufIndex >= 0) {
                mDecodeInputBuffer[inputBufIndex].clear();
                long presentationTimeUs = mAudioExtractor.getSampleTime();
                int sampleSize = mAudioExtractor.readSampleData(mDecodeInputBuffer[inputBufIndex], 0);
                if (sampleSize > 0) {
                    if (presentationTimeUs > mMaxTimeStamp) {
                        return;//超过最大时间戳的音频不处理
                    }
                    mDecoder.queueInputBuffer(inputBufIndex, 0, sampleSize, presentationTimeUs, 0);
                    mAudioExtractor.advance();
                    decodeOutputBuffer();
                }
            }else{
                break;
            }
        }
        Logger.d(TAG, "decodeInputBuffer", "end <<< ");
    }

    /**
     * 初始化编码器
     * @return 初始化是否成功
     */
    private boolean initEncodeAudio() {
        Logger.d(TAG, "initEncodeAudio", "");
        try {
            //设置目标音频格式
            mEncoder = MediaCodec.createByCodecName("OMX.google.aac.encoder");
            MediaFormat mediaFormat = MediaFormat.createAudioFormat("audio/mp4a-latm", SAMPLE_RATE, 2);
            mediaFormat.setInteger(MediaFormat.KEY_AAC_PROFILE, MediaCodecInfo.CodecProfileLevel.AACObjectLC);
            mediaFormat.setInteger(MediaFormat.KEY_BIT_RATE, BIT_RATE);
            mediaFormat.setInteger(MediaFormat.KEY_MAX_INPUT_SIZE, MAX_INPUT_SIZE);
            mEncoder.configure(mediaFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
            mEncoder.start();
            mEncodeInputBuffer = mEncoder.getInputBuffers();
            mEncodeOutputBuffer = mEncoder.getOutputBuffers();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "initEncodeAudio---" + e.getMessage());
        }
        return false;
    }

    /**
     * 初始化音频解码器
     */
    private boolean initDecodeAudio() {
        Logger.d(TAG, "initDecodeAudio", "");
        try {
            mAudioFormat = MediaUtils.getMediaFormat(mAudioExtractor, MediaUtils.TYPE_AUDIO, mAudioPath);
            String mime = mAudioFormat.getString(MediaFormat.KEY_MIME);
            Log.e(TAG, "设置decoder的音频格式是：" + mime);
            mDecoder = MediaCodec.createDecoderByType(mime);
            mDecoder.configure(mAudioFormat, null, null, 0);
            mDecoder.start();

            mDecodeInputBuffer = mDecoder.getInputBuffers();
            mDecodeOutputBuffer = mDecoder.getOutputBuffers();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "initDecodeAudio---" + e.getMessage());
        }
        return false;
    }

    /**
     * 写入提取的数据到目标文件
     */
    private boolean handleTrack(MediaMuxer mediaMuxer, int trackIndex, MediaExtractor extractor) {
        Logger.d(TAG, "handleTrack", "start >>>");
        if(mediaMuxer == null || trackIndex < 0 || extractor == null){
            return false;
        }
        ByteBuffer inputBuffer = ByteBuffer.allocate(VIDEO_READ_SAMPLE_SIZE);
        MediaCodec.BufferInfo info = new MediaCodec.BufferInfo();
        int sampleSize;
        while ((sampleSize = extractor.readSampleData(inputBuffer, 0)) > 0){
            info.offset = 0;
            info.size = sampleSize;
            info.flags = MediaCodec.BUFFER_FLAG_SYNC_FRAME;
            info.presentationTimeUs = extractor.getSampleTime();
            Logger.d(TAG, "handleTrack", "getSampleTime = " + extractor.getSampleTime());
            if(mMaxTimeStamp < info.presentationTimeUs){
                break;
            }
            extractor.advance();
            mediaMuxer.writeSampleData(trackIndex, inputBuffer, info);
        }
        Logger.d(TAG, "handleTrack", "end <<< ");
        return true;
    }

    /**
     * 释放资源
     */
    private void release() {
        try {
            if(mEncoder != null) {
                mEncoder.stop();
                mEncoder.release();
            }
            if(mDecoder != null) {
                mDecoder.stop();
                mDecoder.release();
            }
            if(mMediaMuxer != null) {
                mMediaMuxer.stop();
                mMediaMuxer.release();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}