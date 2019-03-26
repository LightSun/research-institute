package com.heaven7.vida.research.sample;

import android.Manifest;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.heaven7.core.util.Logger;
import com.heaven7.core.util.PermissionHelper;
import com.heaven7.vida.research.R;

/**
 * Created by heaven7 on 2018/2/7 0007.
 */

public class TestRoundViewActivity extends AppCompatActivity {

    private static final String TAG = "TestRoundViewActivity";
    private final PermissionHelper mHelper = new PermissionHelper(this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_test_round_view);
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
                            String[] files = new String[]{"/storage/emulated/0/ttpod/mv/阿桑 - 一直很安静 - 标清.mp4",
                                    "/storage/emulated/0/ttpod/mv/刘瑞琦 - 歌路 - 高清.mp4"
                            };
                            AudioMix mix = new AudioMix(files);
                            mix.play();
                        }
                    }
                });

    }
}
