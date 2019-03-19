package android.meta.cts;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaCodec;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.os.Build;
import android.os.Handler;
import android.util.Log;

import com.heaven7.android.utils.MediaUtils;
import com.heaven7.core.util.Logger;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by heaven7 on 2019/3/19.
 */
public class AudioTrackRender implements AudioRender {

    private static final String TAG = "AudioTrackRender";
    public static final int STATE_NONE      = 0;
    public static final int STATE_PREPAREED = 1;
    public static final int STATE_STARTED   = 2;
    public static final int STATE_PAUSED    = 3;
    public static final int STATE_CANCELLED = 4;
    public static final int STATE_FINISHED  = 5;

    private static final int TIMEOUT_US = 500 * 1000; //0.5s
    private final MediaExtractor mExtractor = new MediaExtractor();
    private final AtomicInteger mState = new AtomicInteger(STATE_NONE);
    private final ExecutorService mService;
    private final Provider mProvider;
    private final Handler mEventHandler;

    private MediaCodec mDecoder;
    private AudioTrack mAudioTrack;
    private AudioListener mAudioListener;

    public AudioTrackRender(ExecutorService mService, Provider mProvider, Handler handler) {
        this.mService = mService;
        this.mProvider = mProvider;
        this.mEventHandler = handler;
    }

    @Override
    public void setAudioListener(AudioListener l) {
        this.mAudioListener = l;
    }

    @Override
    public void prepare() {
        try {
            mState.set(STATE_NONE);
            String path = mProvider.getFilePath();
            MediaFormat format = MediaUtils.getMediaFormat(mExtractor, MediaUtils.TYPE_AUDIO, path);
            if (format == null) {
                Log.e(TAG, "format is null .path = " + path);
                return;
            }

            String mime = format.getString(MediaFormat.KEY_MIME);
            //Log.d(TAG, "format : " + format);
            int mSampleRate = format.getInteger(MediaFormat.KEY_SAMPLE_RATE);
            // int channel = format.getInteger(MediaFormat.KEY_CHANNEL_COUNT);
            //long duration = format.getLong(MediaFormat.KEY_DURATION);
            //Log.d(TAG, "length:" + duration / 1000000);

            mDecoder = MediaCodec.createDecoderByType(mime);
            mDecoder.configure(format, null, null, 0);

            int buffsize = AudioTrack.getMinBufferSize(mSampleRate, AudioFormat.CHANNEL_OUT_STEREO, AudioFormat.ENCODING_PCM_16BIT);
            // 创建AudioTrack对象
            mAudioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, mSampleRate,
                    AudioFormat.CHANNEL_OUT_STEREO,
                    AudioFormat.ENCODING_PCM_16BIT,
                    buffsize,
                    AudioTrack.MODE_STREAM);
            mState.compareAndSet(STATE_NONE, STATE_PREPAREED);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public void start() {
        if(mState.compareAndSet(STATE_PREPAREED, STATE_STARTED)){
            mService.submit(this::startImpl);
        }else {
            if(mState.get() == STATE_CANCELLED){
                release0();
            }
        }
    }

    private void startImpl() {
        mDecoder.start();
        mAudioTrack.play();
        //seek by start time
        long startTime = mProvider.getStartTime();
        if(startTime > 0){
            mExtractor.seekTo(startTime, MediaExtractor.SEEK_TO_CLOSEST_SYNC);
        }
        ByteBuffer[] inputBuffers = mDecoder.getInputBuffers();
        ByteBuffer[] outputBuffers = mDecoder.getOutputBuffers();

        MediaCodec.BufferInfo info = new MediaCodec.BufferInfo();
        long endTime = mProvider.getEndTime();
        while (mState.get() == STATE_STARTED){
            //in
            int inIndex = mDecoder.dequeueInputBuffer(TIMEOUT_US);
            if (inIndex >= 0) {
                ByteBuffer buffer = inputBuffers[inIndex];
                int sampleSize = mExtractor.readSampleData(buffer, 0);
                if (sampleSize < 0 || (endTime > 0  && mExtractor.getSampleTime() > endTime)) {
                    Log.d(TAG, "InputBuffer BUFFER_FLAG_END_OF_STREAM. endTime(in ms) = " + endTime / 1000);
                    mDecoder.queueInputBuffer(inIndex, 0, 0, 0, MediaCodec.BUFFER_FLAG_END_OF_STREAM);
                }else {
                   // Logger.d(TAG, "startImpl", "sample time(in ms) = " + mExtractor.getSampleTime() / 1000);
                    mDecoder.queueInputBuffer(inIndex, 0, sampleSize, mExtractor.getSampleTime(), 0);
                    mExtractor.advance();
                }
                 //out
                int outIndex = mDecoder.dequeueOutputBuffer(info, TIMEOUT_US);
                switch (outIndex) {
                    case MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED:
                        Log.d(TAG, "INFO_OUTPUT_BUFFERS_CHANGED");
                        outputBuffers = mDecoder.getOutputBuffers();
                        break;

                    case MediaCodec.INFO_OUTPUT_FORMAT_CHANGED:
                        MediaFormat format = mDecoder.getOutputFormat();
                        Log.d(TAG, "New format " + format);
                        mAudioTrack.setPlaybackRate(format.getInteger(MediaFormat.KEY_SAMPLE_RATE));
                        break;

                    case MediaCodec.INFO_TRY_AGAIN_LATER:
                        Log.w(TAG, "dequeueOutputBuffer timed out!");
                        break;

                    default:
                        ByteBuffer outBuffer = outputBuffers[outIndex];
                        //Log.v(TAG, "outBuffer: " + outBuffer);

                        final byte[] chunk = new byte[info.size];
                        // Read the buffer all at once
                        outBuffer.get(chunk);
                        outBuffer.clear();
                        mAudioTrack.write(chunk, info.offset, info.offset + info.size);
                        mDecoder.releaseOutputBuffer(outIndex, false);
                        break;
                }
                //check end
                if ((info.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) {
                    Log.d(TAG, "OutputBuffer BUFFER_FLAG_END_OF_STREAM");
                    mState.compareAndSet(STATE_STARTED, STATE_FINISHED);
                    break;
                }
            }
        }
        //check state
        switch (mState.get()){
            case STATE_PAUSED:
                Logger.d(TAG, "startImpl", "STATE_PAUSED");
                break;
            case STATE_CANCELLED:
                Logger.d(TAG, "startImpl", "STATE_CANCELLED");
                break;

            case STATE_FINISHED:
                Logger.d(TAG, "startImpl", "STATE_FINISHED");
                release0();
                mEventHandler.post(() -> {
                    if(mAudioListener != null){
                        mAudioListener.onPlayEnd(AudioTrackRender.this, 0);
                    }
                });
                break;

            default:
                Logger.d(TAG, "startImpl", "wrong state = " + mState.get());
        }
    }

    @Override
    public void pause() {
        mState.set(STATE_PAUSED);
    }

    @Override
    public void cancel() {
        mState.set(STATE_CANCELLED);
        release0();
    }

    private void release0() {
        mExtractor.release();
        if(mDecoder != null){
            mDecoder.stop();
            mDecoder.release();
            mDecoder = null;
        }
        if(mAudioTrack != null ){
            mAudioTrack.stop();
            mAudioTrack.release();
            mAudioTrack = null;
        }
    }

    @Override
    public void setVolume(float volume) {
        if(mAudioTrack != null){
            if(Build.VERSION.SDK_INT >= 21){
                mAudioTrack.setVolume(volume);
            }else {
                mAudioTrack.setStereoVolume(volume, volume);
            }
        }
    }

    @Override
    public boolean isPlaying() {
        return mState.get() == STATE_STARTED;
    }

    @Override
    public boolean isPlayDone() {
        return mState.get() == STATE_FINISHED;
    }

    public interface Provider{
        /** in us */
        long getStartTime();
        long getEndTime();
        String getFilePath();
    }
}
