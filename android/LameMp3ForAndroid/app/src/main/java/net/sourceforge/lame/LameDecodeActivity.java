package net.sourceforge.lame;

import android.Manifest;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.clam314.lame.R;
import com.heaven7.core.util.Logger;
import com.heaven7.core.util.PermissionHelper;
import com.heaven7.java.base.util.IOUtils;
import com.heaven7.java.pc.schedulers.Schedulers;

import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;

/**
 * Created by heaven7 on 2019/7/4.
 */
//todo see "https://github.com/sduslum/Mp3Decoder/blob/master/src/com/aispeech/MainActivity.java"
public class LameDecodeActivity extends AppCompatActivity {

    private static final String TAG = "LameDecodeActivity";
    private static final String FILE = Environment.getExternalStorageDirectory() + "/vida/resource/musics/103_60.mp3";
    private final PermissionHelper mHelper = new PermissionHelper(this);

    static {
        System.loadLibrary("mediaformat");
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_lame_decode);

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

    public void onClickLameDecode(View view){
        decode1();
    }

    private void decode1() {
        Schedulers.io().newWorker().schedule(new Runnable() {
            @Override
            public void run() {
                if(Lame.initializeDecoder() == 0) {
                    Logger.d(TAG, "onClickLameDecode", "initializeDecoder ok.");
                }
              //  executeDecode();
                executeDecode2();
            }
        });
    }

    private void executeDecode(){
        File file = new File(FILE);
        FileInputStream in = null;
        try{
            in = new FileInputStream(file);
            int frames;
            if(Lame.configureDecoder(in) == 0){
                frames = Lame.getDecoderFrameSize();
                Logger.d(TAG, "executeDecode", "configureDecoder ok. frame size = " + frames);
            }else {
                return;
            }
            short[] left = new short[frames];
            short[] right = new short[frames];
            int result;
            int total = 0;
            while ( (result = Lame.decodeFrame(in, left, right)) != -1){
                total += result;
                Logger.d(TAG, "executeDecode", "decodeFrame ok. result = " + result);
                Logger.d(TAG, "executeDecode", "decodeFrame ok. left = " + Arrays.toString(left));
            }
            //3709440
            Logger.w(TAG, "executeDecode", "decode done. total = " + total);
        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            IOUtils.closeQuietly(in);
            Lame.closeDecoder();
        }
    }
    private void executeDecode2(){
        File file = new File(FILE);
        FileInputStream in = null;
        try{
            in = new FileInputStream(file);
            int frames;
            if(Lame.configureDecoder(in) == 0){
                frames = Lame.getDecoderFrameSize();
                Logger.d(TAG, "executeDecode", "configureDecoder ok. frame size = " + frames);
            }else {
                Logger.w(TAG, "executeDecode2", "config decoder error.");
                return;
            }
            float[] left = new float[frames / 4];
            float[] right = new float[frames / 4];
            int result;
            int total = 0;
            while ( (result = Lame.decodeFrame2(in, left, right)) != -1){
                total += result;
                Logger.d(TAG, "executeDecode", "decodeFrame ok. result = " + result);
                Logger.d(TAG, "executeDecode", "decodeFrame ok. left = " + Arrays.toString(left));
            }
            //3709440
            Logger.w(TAG, "executeDecode", "decode done. total = " + total);
        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            IOUtils.closeQuietly(in);
            Lame.closeDecoder();
        }
    }
}
