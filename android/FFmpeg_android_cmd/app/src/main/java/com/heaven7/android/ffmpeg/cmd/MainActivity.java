package com.heaven7.android.ffmpeg.cmd;

import android.Manifest;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.heaven7.core.util.Logger;
import com.heaven7.core.util.PermissionHelper;
import com.heaven7.java.visitor.collection.VisitServices;

import java.util.ArrayList;
import java.util.List;


//compile ffmpeg see : https://blog.csdn.net/byhook/article/details/83582640
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private PermissionHelper mHelper = new PermissionHelper(this);


    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("ffmpeg");
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Example of a call to a native method
       /* TextView tv = (TextView) findViewById(R.id.sample_text);
        tv.setText(stringFromJNI());*/

        mHelper.startRequestPermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                new int[]{1}, new PermissionHelper.ICallback() {
                    @Override
                    public void onRequestPermissionResult(String requestPermission, int requestCode, boolean success) {
                        Logger.d(TAG, "onRequestPermissionResult", "success = " + success);
                    }
                }) ;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    //public native String stringFromJNI();


    public native int executeCmd(int len, String[] cmds);


    public void onClickStart(View view) {
        // String cmd = "ffmpeg -version";

        String video = Environment.getExternalStorageDirectory() + "/vida/test_videos/test1.mp4";
        String concatPath = Environment.getExternalStorageDirectory() + "/vida/test_videos/concat.txt";
        //Logger.d("Home", "onClick", "concat file: " + new File(concatPath).exists());

        String musicPath = Environment.getExternalStorageDirectory() +"/vida/test_videos/music.mp3";
        String out = Environment.getExternalStorageDirectory() +"/vida/test_videos/merged.mp4";

        String[] cmds = FFmpegTestUtils.buildMergeVideosCmd4(video, musicPath, out);
        String cmd = VisitServices.from(cmds).joinToString(" ");

        long start = System.currentTimeMillis();
        boolean success = executeCmd(cmds.length, cmds) == 0;
        Logger.d(TAG, "executeCmd",   "cost time = " + (System.currentTimeMillis() - start));
      //  boolean success = FFmpegBridge.jxFFmpegCMDRun(cmd) == 0;
        Toast.makeText(this, "FFmpegCMDRun: success = " + success, Toast.LENGTH_LONG).show();
        System.out.println("FFmpegCMDRun: success = " + success);
        System.out.println("cmd:  " + cmd);
    }
    public void onClickMix(View view){
        //1.mp3  video.mp4
        String audioPath =  Environment.getExternalStorageDirectory() + "/vida/test_videos/1.mp3";
        String videoPath =  Environment.getExternalStorageDirectory() + "/vida/test_videos/video.mp4";
        String out = Environment.getExternalStorageDirectory() + "/vida/test_videos/mix_result.mp3";

        //extract audio from audio file
        String[] cmds = FFmpegTestUtils.extractAudio(audioPath, 0.3f, false);
        long start = System.currentTimeMillis();
        boolean success = executeCmd(cmds.length, cmds) == 0;
        Logger.d(TAG, "extract-audio(volume)",   "success = " + success + " ,cost time = "
                + (System.currentTimeMillis() - start) + "");
        if(!success){
            return;
        }
        //extract audio from video
        cmds = FFmpegTestUtils.extractAudio(videoPath, 0.7f, true);
        start = System.currentTimeMillis();
        success = executeCmd(cmds.length, cmds) == 0;
        Logger.d(TAG, "extract-video(volume)",   "success = " + success + " ,cost time = "
                + (System.currentTimeMillis() - start) + "");
        if(!success){
            return;
        }
        //mix now
        List<MixParam> params = new ArrayList<>();
        params.add(new MixParam(FFmpegTestUtils.getExtractAudioFileName(audioPath, 0.3f),
                0.0f, 5.0f));
        params.add(new MixParam(FFmpegTestUtils.getExtractAudioFileName(videoPath, 0.7f),
                10.0f, 15.0f));
        cmds = FFmpegTestUtils.mix(params, out);
        start = System.currentTimeMillis();
        success = executeCmd(cmds.length, cmds) == 0;
        Logger.d(TAG, "mix-audios",   "success = " + success + " ,cost time = "
                + (System.currentTimeMillis() - start) + "");
    }
}
