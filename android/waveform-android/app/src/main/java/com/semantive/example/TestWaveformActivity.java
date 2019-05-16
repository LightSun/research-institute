package com.semantive.example;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;

import com.heaven7.core.util.Logger;
import com.heaven7.core.util.MainWorker;
import com.heaven7.core.util.PermissionHelper;
import com.heaven7.java.pc.schedulers.Schedulers;
import com.semantive.waveformandroid.waveform.soundfile.CheapSoundFile;
import com.semantive.waveformandroid.waveform.view.MusicAnnotatorView;

import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by heaven7 on 2019/5/13.
 */
public class TestWaveformActivity extends BaseActivity {

    @BindView(R.id.waveform)
    MusicAnnotatorView waveformView2;

    private static final String TAG = "TestWaveformActivity";
    static final String FILE = Environment.getExternalStorageDirectory() + "/vida/resource/musics/M7.mp3";
    private final PermissionHelper mHelper = new PermissionHelper(this);

    @Override
    protected int getLayoutId() {
        return R.layout.ac_test_waveform;
    }

    @Override
    protected void onInitialize(Context context, Bundle savedInstanceState) {
        mHelper.startRequestPermission(new String[]{Manifest.permission.RECORD_AUDIO,
                Manifest.permission.WRITE_EXTERNAL_STORAGE}, new int[]{1, 2}, new PermissionHelper.ICallback() {
            @Override
            public void onRequestPermissionResult(String requestPermission, int requestCode, boolean success) {
                Logger.d("RingdroidSelectActivity", "onRequestPermissionResult", "success = " + success);
                if(success){
                    Schedulers.io().newWorker().schedule(() -> loadFile());
                }
            }
        });
    }

    private void loadFile() {
        try {
            waveformView2.setSoundFile(CheapSoundFile.create(FILE, new CheapSoundFile.ProgressListener() {
                @Override
                public boolean reportProgress(double fractionComplete) {
                   // Logger.d(TAG, "reportProgress", "process = " + fractionComplete);
                    return true;
                }
            }));
            int start = waveformView2.millisecsToPixels(10000);
            int end = waveformView2.millisecsToPixels(30000);
            waveformView2.setParameters(start, end, start);
            waveformView2.postInvalidate();
           /* MainWorker.postDelay(1000, new Runnable() {
                @Override
                public void run() {
                    waveformView2.addAnnotatorWidthAnim(15000);
                }
            });*/
            MainWorker.postDelay(1000, new Runnable() {
                @Override
                public void run() {
                    waveformView2.seekToCenter(0);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @OnClick(R.id.bt_set_start)
    public void onClickSetStart(){

    }
    @OnClick(R.id.bt_set_end)
    public void onClickSetEnd(){

    }
    @OnClick(R.id.bt_set_start)
    public void onClickSetOffset(){

    }
}
