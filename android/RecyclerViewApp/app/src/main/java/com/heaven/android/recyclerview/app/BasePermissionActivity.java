package com.heaven.android.recyclerview.app;

import android.Manifest;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.heaven7.core.util.Logger;
import com.heaven7.core.util.PermissionHelper;

/**
 * Created by heaven7 on 2018/10/20 0020.
 */
public abstract class BasePermissionActivity extends AppCompatActivity {

    private final PermissionHelper mHelper = new PermissionHelper(this);

    @Override
    public final void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * start request permissions
     * @param provider the permission provider
     */
    public final void requestPermissions(PermissionProvider provider) {
        final String tag = getClass().getSimpleName();
        mHelper.startRequestPermission(provider.getRequestPermissions(), provider.getRequestPermissionCodes(),
                (requestPermission, requestCode, success) -> {
                    Logger.w(tag, "onRequestPermissionResult",
                            "success = " + success + " ,permission = " + requestPermission);
                    onRequestPermissionEnd(requestPermission, requestCode, success);
                });
    }

    /**
     * called on request permission end
     * @param permission the permission
     * @param requestCode the request code
     * @param success true if request permission success
     */
    protected abstract void onRequestPermissionEnd(String permission, int requestCode, boolean success);


    public interface PermissionProvider{
        String[] getRequestPermissions();
        int[] getRequestPermissionCodes();
    }

    public static final PermissionProvider PP_SD = new PermissionProvider() {
        @Override
        public String[] getRequestPermissions() {
            return new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        }

        @Override
        public int[] getRequestPermissionCodes() {
            return new int[]{1};
        }
    };
}
