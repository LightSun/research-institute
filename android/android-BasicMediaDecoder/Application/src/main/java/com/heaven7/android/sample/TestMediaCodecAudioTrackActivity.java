package com.heaven7.android.sample;

import android.content.Context;
import android.meta.cts.AudioDecoderThread;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;

import com.example.android.basicmediadecoder.R;
import com.heaven7.android.BasePermissionActivity;

import java.io.IOException;

import butterknife.OnClick;

/**
 * Created by heaven7 on 2018/12/5 0005.
 */
public class TestMediaCodecAudioTrackActivity extends BasePermissionActivity {

    private AudioDecoderThread mAudioThread;
    private AudioDecoderThread mAudioThread2;

    @Override
    protected int getLayoutId() {
        return R.layout.ac_codec_audio_track;
    }

    @Override
    protected void onInitialize(Context context, Bundle savedInstanceState) {
        mAudioThread = new AudioDecoderThread();
        mAudioThread2 = new AudioDecoderThread();
        super.onInitialize(context, savedInstanceState);
    }

    @Override
    protected void onRequestPermissionEnd(String permission, int requestCode, boolean success) {

    }

    @OnClick(R.id.bt_start)
    public void onClickStart(View view){
        String video = Environment.getExternalStorageDirectory() + "/vida/test_videos/v1.mp4";
        String audio = Environment.getExternalStorageDirectory() + "/vida/test_videos/music.mp3";
        try {
            mAudioThread.startPlay(audio);
            mAudioThread2.startPlay(video);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        mAudioThread.stop();
        mAudioThread2.stop();
        super.onDestroy();
    }
}
