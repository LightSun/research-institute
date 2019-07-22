package com.heaven.android.recyclerview.app;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.heaven7.core.util.Logger;
import com.heaven7.core.util.PermissionHelper;

import java.util.List;

/**
 * Created by heaven7 on 2017/12/22.
 */
public class TestMainActivity extends AbsMainActivity {

    private final PermissionHelper mHelper = new PermissionHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestPermissions(BasePermissionActivity.PP_SD);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,@NonNull String[] permissions,@NonNull int[] grantResults) {
        mHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * start request permissions
     * @param provider the permission provider
     */
    public final void requestPermissions(BasePermissionActivity.PermissionProvider provider) {
        final String tag = getClass().getSimpleName();
        mHelper.startRequestPermission(provider.getRequestPermissions(), provider.getRequestPermissionCodes(),
                (requestPermission, requestCode, success) -> {
                    Logger.w(tag, "onRequestPermissionResult",
                            "success = " + success + " ,permission = " + requestPermission);
                });
    }

    @Override
    protected void addDemos(List<ActivityInfo> list) {
        list.add(new ActivityInfo(MainActivity.class, "mine MainActivity"));
        list.add(new ActivityInfo(me.yokeyword.itemtouchhelperdemo.MainActivity.class, "other MainActivity"));
    }

}
