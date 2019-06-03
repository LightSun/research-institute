package com.heaven7.android.bpmdetect;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.heaven7.android.bpmdetect.soundfile.SoundFile;
import com.heaven7.android.util2.FilePathCompat;
import com.heaven7.core.util.Logger;
import com.heaven7.core.util.PermissionHelper;
import com.heaven7.java.pc.schedulers.Schedulers;


import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";
    private final PermissionHelper mHelper = new PermissionHelper(this);
    private final BpmDetector mDetector = new BpmDetector();
    private SoundFile mSoundFile;
    private String mAudioFile;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onInitialize(Context context, Bundle savedInstanceState) {
        mHelper.startRequestPermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO}, new int[]{1, 2}, new PermissionHelper.ICallback() {
            @Override
            public void onRequestPermissionResult(String s, int code, boolean success) {
                Logger.d(TAG, "onRequestPermissionResult", "success = " + success);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            String filePath = FilePathCompat.getFilePath(this, data.getData());
            System.out.println(filePath);
            Logger.d(TAG, "onActivityResult", "filePath = " + filePath);
            Toast.makeText(this, filePath, Toast.LENGTH_LONG).show();
            mAudioFile = filePath;
            setSoundFile();
        }
    }

    @OnClick(R.id.bt_start_detect)
    public void onClickStart(View view){
        if(mAudioFile == null){
            Logger.w(TAG, "onClickStart", "please select an audio file.");
        }else {
            setSoundFile();
        }
    }

    private void setSoundFile() {
        Schedulers.io().newWorker().schedule(new Runnable() {
            @Override
            public void run() {
                try {
                    mSoundFile = SoundFile.create(mAudioFile);
                    setAudioData();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @OnClick(R.id.bt_select_audio)
    public void onClickSelectAudio(View view){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("audio/*"); //mime. multi "audio/*;video/*"
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent,1);
    }

    private void setAudioData(){
        int[] gains = mSoundFile.getFrameGains();
        final short[] data = new short[gains.length];
        for (int i = 0, size = gains.length ; i < size ; i ++){
            data[i] = (short) gains[i];
        }
        Logger.d(TAG, "setAudioData", "getFrameGains len = " + gains.length);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mDetector.attach(mSoundFile.getChannels(), mSoundFile.getSampleRate());
                mDetector.setSampleData(data, mSoundFile.getNumSamples());
                Logger.d(TAG, "setAudioData", " bpm = " + mDetector.getBmp() + " ,file = " + mAudioFile);
            }
        });
    }
}
