package com.heaven7.vida.research.sample;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.heaven7.core.util.Logger;
import com.heaven7.core.util.PermissionHelper;
import com.heaven7.vida.research.utils.LocationUtils;

/**
 * 测试定位获取城市. android 标准api需要google服务支持
 * Created by heaven7 on 2018/11/7 0007.
 */
public class TestGetCityActivity extends AppCompatActivity {

    private static final String TAG = "TestGetCityActivity";
    private PermissionHelper mHelper = new PermissionHelper(this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestPermissions();
    }

    public void requestPermissions(){
        String[] pers = {
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
               // Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS
        };
        int[] codes = {1,2,
                //3
        };
        mHelper.startRequestPermission(pers, codes, new PermissionHelper.ICallback() {
            @Override
            public void onRequestPermissionResult(String requestPermission, int requestCode, boolean success) {
                Logger.d(TAG, "onRequestPermissionResult", "success = " + success
                        + " ,requestPermission = " + requestPermission);
                if(success){
                    LocationUtils.getCNBylocation(getApplicationContext());
                    Logger.d(TAG, "onRequestPermissionResult", "city name = " + LocationUtils.cityName);
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
