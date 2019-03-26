package com.heaven7.vida.research.sample;

import android.Manifest;
import android.content.res.AssetManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.heaven7.core.util.Logger;
import com.heaven7.core.util.PermissionHelper;
import com.heaven7.vida.research.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

/**
 * Created by heaven7 on 2018/10/27 0027.
 */
public class TestReadAssetsActivity extends AppCompatActivity {

    private static final String TAG = "TestReadAssetsActivity";
    private final PermissionHelper mHelper = new PermissionHelper(this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_test_switch_image);

        requestPermissions();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    private void requestPermissions() {
        mHelper.startRequestPermission(new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE }, new int[]{1},
                new PermissionHelper.ICallback() {
                    @Override
                    public void onRequestPermissionResult(String requestPermission, int requestCode, boolean success) {
                        Logger.w(TAG, "onRequestPermissionResult",
                                "success = " + success + " ,permission = " + requestPermission);
                        if (success) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    testReadAssets();
                                }
                            }).start();
                        }
                    }
                });

    }
    private void testReadAssets() {
        try{
            AssetManager assets = getAssets();
            String[] alls = assets.list("all");
            // called [ testReadAssets() ]: list all is >>> [1.txt, 2.txt, sub]
            Logger.d(TAG, "testReadAssets", "list all is >>> " + Arrays.toString(alls));
            //read dir cause java.io.FileNotFoundException: data/sub
            InputStream in = assets.open("data/sub");
            Logger.d(TAG, "testReadAssets", "read dir >>> success");
            in.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
