package com.heaven7.android.chordino;

import android.Manifest;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.heaven7.core.util.Logger;
import com.heaven7.core.util.PermissionHelper;
import com.heaven7.java.pc.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private final PermissionHelper mHelper = new PermissionHelper(this);

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("sndfile");
        System.loadLibrary("chordino_depend");
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Example of a call to a native method
        TextView tv = (TextView) findViewById(R.id.sample_text);
        tv.setText(stringFromJNI());

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

    public void startChord(View view){
        Schedulers.io().newWorker().schedule(new Runnable() {
            @Override
            public void run() {
                String sn = "extract_chord";
                String file = Environment.getExternalStorageDirectory() + "/vida/test_videos/Home_v3.wav";
                Logger.d(TAG, "run", "file = " + file);
                extractChords(sn, file);
                Logger.w(TAG, "run", "extractChords done !!!");
            }
        });
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public native String stringFromJNI();

    public native void extractChords(String simpleName, String fileName);

}
