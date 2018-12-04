package com.heaven7.audiomix.sample;

import android.Manifest;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.heaven7.core.util.Logger;
import com.heaven7.core.util.PermissionHelper;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private final PermissionHelper mHelper = new PermissionHelper(this);
    private AudioMixer mixer = new AudioMixer();
    private AudioMixMp mixMp;
    private AudioMixMp2 mixMp2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.bt_mix)
    public void onClickMix(View v){
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

    private void startMix() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                String path1 = "/storage/emulated/0/ttpod/mv/阿桑 - 一直很安静 - 标清.mp4";
               // String path1 = "/storage/emulated/0/ttpod/mv/刘瑞琦 - 歌路 - 高清.mp4";
                String path2 = "/storage/emulated/0/DCIM/Camera/VID_20180217_153925.mp4";
              //  mixer.muxing(getApplicationContext(), path1, path2);
              /*  mixMp = new AudioMixMp(new AudioMixMp.MediaEntity[]{
                        new AudioMixMp.MediaEntity(path2, 1f),
                        new AudioMixMp.MediaEntity(path1, 0.5f)
                });
                mixMp.play();*/

                mixMp2 = new AudioMixMp2(path1, path2);
                mixMp2.start();
            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mixMp != null){
            mixMp.cancel();
        }
    }
}
