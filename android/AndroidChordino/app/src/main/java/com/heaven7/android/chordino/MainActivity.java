package com.heaven7.android.chordino;

import android.Manifest;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.gson.Gson;
import com.heaven7.core.util.Logger;
import com.heaven7.core.util.PermissionHelper;
import com.heaven7.core.util.Toaster;
import com.heaven7.java.base.util.FileUtils;
import com.heaven7.java.pc.schedulers.Schedulers;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private final PermissionHelper mHelper = new PermissionHelper(this);

    static final String[] FILES = {
            "/WindCloud/14_60.wav",
            "/WindCloud/1_60.wav",
            "/WindCloud/103_60.wav",
            "/WindCloud/17_60.wav",
            "/WindCloud/21_60.wav",
    };
    int index = -1;

    static final String[] FILES_mp3 = {
            "/vida/test_musics/167_full_bach-jesu-joy-of-man-s-desiring-string-quartet_0237.mp3",
            "/vida/resource/musics/14_60.mp3",
            "/vida/resource/musics/17_60.mp3",
            "/vida/resource/musics/1_60.mp3",
            "/vida/resource/musics/21_60.mp3",
            "/vida/resource/musics/35_60.mp3",
    };
    int index_mp3 = -1;

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("mediaformat");
        System.loadLibrary("sndfile");
        System.loadLibrary("lame");
        System.loadLibrary("chordino_depend");
        System.loadLibrary("cut_gen");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mHelper.startRequestPermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, new int[]{1},
                new PermissionHelper.ICallback() {
            @Override
            public void onRequestPermissionResult(String s, int i, boolean b) {
                Logger.d(TAG, "onRequestPermissionResult", "success = " + b);
            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    public void startNextChord(View view){
        index ++;
        if(index >= FILES.length){
            Toaster.show(this, "all files done.");
            return;
        }
        startChordWav(view);
    }
    public void startNextMp3(View view){
        index_mp3 ++;
        if(index_mp3 >= FILES_mp3.length){
            Toaster.show(this, "all files done.");
            return;
        }
        startChordmp3(view);
    }
    public void onClickGenerateCut(View view){
        Schedulers.io().newWorker().schedule(new Runnable() {
            @Override
            public void run() {
                String file = Environment.getExternalStorageDirectory() + FILES_mp3[0];
                Logger.d(TAG, "onClickGenerateCut", "file = " + file);
                int[] ints = Chordino.generateCuts(file);
                Logger.d(TAG, "onClickGenerateCut", Arrays.toString(ints));
            }
        });
    }
    public void onClickGenAllCuts(View view){
        Schedulers.io().newWorker().schedule(new Runnable() {
            @Override
            public void run() {
                //TODO
                String inDir = "";
                String outDir = "";
                List<String> files = FileUtils.getFiles(new File(inDir), "mp3");
                for (String file : files){
                    String name = FileUtils.getFileName(file);
                    int[] ints = Chordino.generateCuts(file);
                    FileUtils.writeTo(new File(outDir, name +".cuts"), new Gson().toJson(ints));
                    Logger.d(TAG, "onClickGenerateCut", Arrays.toString(ints));
                }
            }
        });
    }
    public void onClickTestSplit(View view){
       // int[] ints = Chordino.testSplit(0, 11000);
        int[] ints = Chordino.testSplit(0, 10000);
        Logger.d(TAG, "onClickTestSplit", Arrays.toString(ints));
    }
    public void onClickTestMerge(View view){
        int[] ints = {
                57130, 58197, 59690, 63530, 63570
        };
       /* int[] ints = {
             0, 100, 400, 800, 900, 1000, 1100, 1400, 1500
        };*/
       /*
         *  0, 400, 800, 1000, 1400
         *  0, 400, 800, 1400
         */
        int[] result = Chordino.testMerge(ints);
        Logger.d(TAG, "onClickTestMerge", Arrays.toString(result));
    }

    //===========================================
    public void startChordmp3(View view){
        Schedulers.io().newWorker().schedule(new Runnable() {
            @Override
            public void run() {
                String sn = "startChordmp3";
                String file = Environment.getExternalStorageDirectory() + FILES_mp3[index_mp3];
                Logger.d(TAG, "run", "file = " + file);
                extractChords(sn, file);
                Logger.w(TAG, "run", "extractChords done !!!");
            }
        });
    }
    public void startChordWav(View view){
        Schedulers.io().newWorker().schedule(new Runnable() {
            @Override
            public void run() {
                String sn = "startChordWav";
                String file = Environment.getExternalStorageDirectory() + FILES[index];
                Logger.d(TAG, "run", "file = " + file);
                extractChords(sn, file);
                Logger.w(TAG, "run", "extractChords done !!!");
            }
        });
    }

    public native void extractChords(String simpleName, String fileName);

}
