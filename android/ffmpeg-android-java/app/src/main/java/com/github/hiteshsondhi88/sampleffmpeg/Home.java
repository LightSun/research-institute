package com.github.hiteshsondhi88.sampleffmpeg;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import dagger.ObjectGraph;

import com.github.hiteshsondhi88.libffmpeg.ExecuteBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.FFmpeg;
import com.github.hiteshsondhi88.libffmpeg.LoadBinaryResponseHandler;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegCommandAlreadyRunningException;
import com.github.hiteshsondhi88.libffmpeg.exceptions.FFmpegNotSupportedException;
import com.heaven7.core.util.Logger;
import com.heaven7.core.util.PermissionHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * ffmpeg -f concat -safe 0 -i 'vidlist.txt' -i 'music.m4a' -c copy -movflags faststart -shortest -y 'test.mp4'
 */
public class Home extends Activity implements View.OnClickListener {

    private static final String TAG = Home.class.getSimpleName();
    private final PermissionHelper mHelper = new PermissionHelper(this);

    @Inject
    FFmpeg ffmpeg;

    @InjectView(R.id.command)
    EditText commandEditText;

    @InjectView(R.id.command_output)
    LinearLayout outputLayout;

    @InjectView(R.id.run_command)
    Button runButton;

    private ProgressDialog progressDialog;
    private boolean permissionOk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.inject(this);
        ObjectGraph.create(new DaggerDependencyModule(this)).inject(this);

        loadFFMpegBinary();
        initUI();
        requestPermissions();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    private void requestPermissions() {
        mHelper.startRequestPermission(new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                }, new int[]{1},
                new PermissionHelper.ICallback() {
                    @Override
                    public void onRequestPermissionResult(String requestPermission, int requestCode, boolean success) {
                        Logger.w(TAG, "onRequestPermissionResult",
                                "success = " + success + " ,permission = " + requestPermission);
                        permissionOk = success;
                    }
                });
    }

    private void initUI() {
        runButton.setOnClickListener(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle(null);
    }

    private void loadFFMpegBinary() {
        try {
            ffmpeg.loadBinary(new LoadBinaryResponseHandler() {
                @Override
                public void onFailure() {
                    showUnsupportedExceptionDialog();
                }
            });
        } catch (FFmpegNotSupportedException e) {
            showUnsupportedExceptionDialog();
        }
    }
    private static String strArr2String(String[] command){
        StringBuilder sb = new StringBuilder();
        for (String cmd : command){
            sb.append(cmd).append(" ");
        }
        return sb.toString();
    }

    private void execFFmpegBinary(final String[] command, final Runnable success) {
        try {
            ffmpeg.execute(command, new ExecuteBinaryResponseHandler() {
                long startTime;
                @Override
                public void onFailure(String s) {
                    addTextViewToLayout("FAILED with output : "+s);
                    Logger.d(TAG, "onFailed", strArr2String(command));
                }

                @Override
                public void onSuccess(String s) {
                    Logger.d(TAG, "onSuccess", strArr2String(command));
                    addTextViewToLayout("SUCCESS with output : "+ s);
                    if(success != null){
                        success.run();
                    }
                }

                @Override
                public void onProgress(String s) {
                    Log.d(TAG, "Started command : ffmpeg "+command);
                    addTextViewToLayout("progress : "+s);
                    progressDialog.setMessage("Processing\n"+s);
                }

                @Override
                public void onStart() {
                    startTime = System.currentTimeMillis();
                    outputLayout.removeAllViews();

                    Log.d(TAG, "Started command : ffmpeg " + command);
                    progressDialog.setMessage("Processing...");
                    progressDialog.show();
                }

                @Override
                public void onFinish() {
                    Log.d(TAG, "Finished command : ffmpeg "+ command);
                    Log.d(TAG, "Finished command : cost time "+ (System.currentTimeMillis() - startTime));
                    progressDialog.dismiss();
                }
            });
        } catch (FFmpegCommandAlreadyRunningException e) {
            // do nothing for now
        }
    }

    private void addTextViewToLayout(String text) {
        TextView textView = new TextView(Home.this);
        textView.setText(text);
        outputLayout.addView(textView);
    }

    private void showUnsupportedExceptionDialog() {
        new AlertDialog.Builder(Home.this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(getString(R.string.device_not_supported))
                .setMessage(getString(R.string.device_not_supported_message))
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Home.this.finish();
                    }
                })
                .create()
                .show();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.run_command:
                if(!permissionOk){
                    Toast.makeText(Home.this, "please wait permission....", Toast.LENGTH_LONG).show();
                    return;
                }
                String cmd = commandEditText.getText().toString();
                if(TextUtils.isEmpty(cmd)){
                    Toast.makeText(Home.this, getString(R.string.empty_command_toast), Toast.LENGTH_LONG).show();
                    return;
                }
                switch (cmd){
                    case "Test":
                        execFFmpegBinary(new String[]{"-version"}, null);
                        break;
                    case "#heaven7#":
                        runCmd1(cmd.substring(9));
                        break;

                    case "mix":
                       // executeMix();
                        //extractAudioFromVideo();
                        mixImpl();
                        break;

                    default:
                        if(cmd.startsWith("#heaven7#")){
                            runCmd1(cmd);
                        }else {
                            String[] command = cmd.split(" ");
                            if (command.length != 0) {
                                execFFmpegBinary(command, null);
                            }
                        }
                        break;
                }
        }
    }

    private void runCmd1(String cmd) {
        String[] cmds;
        if(TextUtils.isEmpty(cmd)){
           // String video = Environment.getExternalStorageDirectory() + "/vida/test_videos/v1__2.mp4";
            String video = Environment.getExternalStorageDirectory() + "/vida/test_videos/video_goog.mp4";
            String concatPath = Environment.getExternalStorageDirectory() + "/vida/test_videos/concat.txt";
            //Logger.d("Home", "onClick", "concat file: " + new File(concatPath).exists());
            String musicPath = Environment.getExternalStorageDirectory() +"/vida/test_videos/music.mp3";
            String out = Environment.getExternalStorageDirectory() +"/vida/test_videos/merged.mp4";
            cmds = TestUtils.buildMergeVideosCmd4(video, musicPath, out);
        }else {
            String[] strs = cmd.split(" ");
            cmds = TestUtils.buildMergeVideosCmd(strs[0], strs[1], strs[2]);
        }
        execFFmpegBinary(cmds, null);
    }
    public void executeMix(){
        //1.mp3  video.mp4
        //extract audio from audio file
        String[] cmds = TestUtils.extractAudio(AUDIOPATH, 0.3f, false);
        execFFmpegBinary(cmds, new Runnable() {
            @Override
            public void run() {
                extractAudioFromVideo();
            }
        }) ;
    }
    private void mixImpl(){
        List<MixParam> params = new ArrayList<>();
        params.add(new MixParam(TestUtils.getExtractAudioFileName(AUDIOPATH, 0.3f),
                0.0f, 5.0f));
        params.add(new MixParam(TestUtils.getExtractAudioFileName(VIDEOPATH, 0.7f),
                10.0f, 15.0f));
        String[] cmds = TestUtils.mix(params, OUT);
        execFFmpegBinary(cmds, null);
    }

    private void extractAudioFromVideo() {
        String[] cmds = TestUtils.extractAudio(VIDEOPATH, 0.7f, true);
        execFFmpegBinary(cmds, new Runnable() {
            @Override
            public void run() {
                //mixImpl();
            }
        });
    }

    String AUDIOPATH =  Environment.getExternalStorageDirectory() + "/vida/test_videos/1.mp3";
    String VIDEOPATH =  Environment.getExternalStorageDirectory() + "/vida/test_videos/video.mp4";
    String OUT = Environment.getExternalStorageDirectory() + "/vida/test_videos/mix_result.mp3";
}
