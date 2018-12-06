package com.heaven7.android.sample;

import android.Manifest;
import android.meta.cts.CameraToMpegTest;
import android.view.View;

/**
 * Created by heaven7 on 2018/12/6 0006.
 */
public class TestCameraToMpegActivity extends TestMediaCodecAudioTrackActivity {

    private CameraToMpegTest mTest;

    @Override
    public void onClickStart(View view) {
        if(mTest == null){
            mTest = new CameraToMpegTest(this);
        }
        try {
            mTest.testEncodeCameraToMp4();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    @Override
    protected PermissionProvider getPermissionProvider() {
        return new PermissionProvider() {
            @Override
            public String[] getRequestPermissions() {
                return new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
            }
            @Override
            public int[] getRequestPermissionCodes() {
                return new int[]{1,2};
            }
        };
    }
}
