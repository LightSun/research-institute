package com.heaven7.audiomix.sample;

import android.Manifest;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.heaven7.audiomix.sample.mix.MediaMixHelper;
import com.heaven7.core.util.Logger;
import com.heaven7.core.util.PermissionHelper;

import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by heaven7 on 2018/12/3 0003.
 */
public class TestVideoAudioMixActivity extends AppCompatActivity {

    private static final String TAG = "TestVideoAudioMix";
    private final PermissionHelper mHelper = new PermissionHelper(this);
   // private Compounder mMixer;
    private MediaMixHelper mMixer ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.bt_mix)
    public void onClickMix(View v) {
        requestPermissions();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void requestPermissions() {
        mHelper.startRequestPermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, new int[]{1},
                (requestPermission, requestCode, success) -> {
                    Logger.w(TAG, "onRequestPermissionResult",
                            "success = " + success + " ,permission = " + requestPermission);
                    if (success) {
                        startMix();
                    }
                });
    }

    //13 - 4s
    //60 - 15s
    private void startMix() {
        String video = Environment.getExternalStorageDirectory() + "/vida/test_videos/video.mp4";
        String audio = Environment.getExternalStorageDirectory() + "/vida/test_videos/music.mp3";
        String out = Environment.getExternalStorageDirectory() + "/vida/test_videos/merge_mix3.mp4";
       /* if (mMixer == null) {
            mMixer = Compounder.createCompounder(video, audio, out);
        }
        long start = System.currentTimeMillis();
        mMixer.start();
        Logger.d(TAG, "startMix", "video-audio mix done... time = " + (System.currentTimeMillis() - start));*/

       if(mMixer == null){
           mMixer = new MediaMixHelper();
       }
       mMixer.start(video, audio, out);
    }
}
