package com.heaven7.android.sample;

import android.content.Context;
import android.meta.cts.AudioDecoderThread;
import android.meta.cts.AudioTrackRender;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;

import com.example.android.basicmediadecoder.R;
import com.heaven7.android.BasePermissionActivity;
import com.heaven7.java.base.util.threadpool.Executors2;

import java.io.IOException;
import java.util.concurrent.ExecutorService;

import butterknife.OnClick;

/**
 * Created by heaven7 on 2018/12/5 0005.
 */
public class TestMediaCodecAudioTrackActivity2 extends BasePermissionActivity {

    private ExecutorService mService = Executors2.newFixedThreadPool(2);
    private AudioTrackRender mRender;
    private String video = Environment.getExternalStorageDirectory() + "/vida/test_videos/v1.mp4";
    private String audio = Environment.getExternalStorageDirectory() + "/vida/test_videos/music.mp3";

    @Override
    protected int getLayoutId() {
        return R.layout.ac_codec_audio_track;
    }

    @Override
    protected void onInitialize(Context context, Bundle savedInstanceState) {
        mRender = new AudioTrackRender(mService, new AudioTrackRender.Provider() {
            @Override
            public long getStartTime() {
                return 2 * 1000000;
            }
            @Override
            public long getEndTime() {
                return 10 * 1000000;
            }
            @Override
            public String getFilePath() {
                return video;
            }
        }, new Handler());
        super.onInitialize(context, savedInstanceState);
        mRender.prepare();
    }

    @Override
    protected void onRequestPermissionEnd(String permission, int requestCode, boolean success) {

    }

    @OnClick(R.id.bt_start)
    public void onClickStart(View view){
        mRender.start();
    }

    @Override
    protected void onDestroy() {
        if(mRender != null){
            mRender.cancel();
        }
        if(mService != null){
            mService.shutdownNow();
        }
        super.onDestroy();
    }
}
