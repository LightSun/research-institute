package com.semantive.example;

import android.Manifest;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.heaven7.core.util.Logger;
import com.heaven7.core.util.PermissionHelper;
import com.semantive.waveformandroid.waveform.Segment;
import com.semantive.waveformandroid.waveform.WaveformFragment;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final PermissionHelper mHelper = new PermissionHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mHelper.startRequestPermission(new String[]{Manifest.permission.RECORD_AUDIO,
                Manifest.permission.WRITE_EXTERNAL_STORAGE}, new int[]{1, 2}, new PermissionHelper.ICallback() {
            @Override
            public void onRequestPermissionResult(String requestPermission, int requestCode, boolean success) {
                Logger.d("RingdroidSelectActivity", "onRequestPermissionResult", "success = " + success);
                if(success){
                    getSupportFragmentManager().beginTransaction()
                            .add(R.id.container, new CustomWaveformFragment())
                            .commit();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public static class CustomWaveformFragment extends WaveformFragment {

        /**
         * Provide path to your audio file.
         */
        @Override
        protected String getFileName() {
           // return Environment.getExternalStorageDirectory() + "/vida/resource/musics/M7.mp3";
            return Environment.getExternalStorageDirectory() + "/KuwoMusic/music/领悟-梁静茹-5017877.aac";
        }

        /**
         * Optional - provide list of segments (start and stop values in seconds) and their corresponding colors
         */
        @Override
        protected List<Segment> getSegments() {
            return Arrays.asList(
                    new Segment(55.2, 55.8, Color.rgb(238, 23, 104)),
                    new Segment(56.2, 56.6, Color.rgb(238, 23, 104)),
                    new Segment(58.4, 59.9, Color.rgb(184, 92, 184)));
        }
    }
}
