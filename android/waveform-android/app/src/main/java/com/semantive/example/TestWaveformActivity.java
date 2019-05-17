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
import com.semantive.waveformandroid.waveform.view.EditorWaveformView;
import com.semantive.waveformandroid.waveform.view.MusicAnnotatorView;
import com.semantive.waveformandroid.waveform.view.MusicStartEndView;
import com.semantive.waveformandroid.waveform.view.WaveformView;

import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by heaven7 on 2019/5/13.
 */
public class TestWaveformActivity extends BaseActivity {

    @BindView(R.id.waveform)
    MusicAnnotatorView mAnnotatorView;
    @BindView(R.id.waveform2)
    MusicStartEndView mStartEndWaveform;
    @BindView(R.id.waveform3)
    EditorWaveformView mEditWaveForm;

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
                    Schedulers.io().newWorker().schedule(new Runnable() {
                        @Override
                        public void run() {
                            loadFile(mAnnotatorView, new Task(mAnnotatorView));
                            loadFile(mStartEndWaveform, new Task(mStartEndWaveform));
                            loadFile(mEditWaveForm, new Runnable() {
                                @Override
                                public void run() {
                                    mEditWaveForm.addAnnotatorWidthAnim(15000);
                                    mEditWaveForm.setMinOffsetX(-mEditWaveForm.secondsToPixels(20));
                                }
                            });
                        }
                    });
                }
            }
        });
    }

    private void loadFile(final WaveformView wv, Runnable task) {
        try {
            wv.setSoundFile(CheapSoundFile.create(FILE, new CheapSoundFile.ProgressListener() {
                @Override
                public boolean reportProgress(double fractionComplete) {
                   // Logger.d(TAG, "reportProgress", "process = " + fractionComplete);
                    return true;
                }
            }));
            int start = wv.millisecsToPixels(3000);
            int end = wv.millisecsToPixels(30000);
            wv.setSelectRange(start, end);
            //test a delay task
            MainWorker.postDelay(1000, task);
            //
            if(wv == mAnnotatorView){
                MainWorker.postDelay(1000, new Runnable() {
                    @Override
                    public void run() {
                        mAnnotatorView.addAnnotatorWidthAnim(5000);
                        mAnnotatorView.addAnnotatorWidthAnim(8000);
                    }
                });
            }
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
        mAnnotatorView.finishAdjustMode();
    }
    @OnClick(R.id.bt_set_end)
    public void onClickSetEnd(){

    }
    @OnClick(R.id.bt_set_start)
    public void onClickSetOffset(){

    }

    private static class Task implements Runnable{
        final WaveformView wv;

        public Task(WaveformView wv) {
            this.wv = wv;
        }
        @Override
        public void run() {
            wv.seekToCenter(3000);
        }
    }
}
