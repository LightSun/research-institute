package com.heaven7.android;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.heaven7.core.util.Logger;
import com.heaven7.core.util.PermissionHelper;

public abstract class BasePermissionActivity extends BaseActivity {

    private final PermissionHelper mHelper = new PermissionHelper(this);

    @Override
    protected void onInitialize(Context context, Bundle savedInstanceState) {
        requestPermissions(getPermissionProvider());
    }

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

    protected PermissionProvider getPermissionProvider(){
        return new SdcardPermissionProvider();
    }


    public interface PermissionProvider{
        String[] getRequestPermissions();
        int[] getRequestPermissionCodes();
    }

    public static final class SdcardPermissionProvider implements PermissionProvider{

        @Override
        public String[] getRequestPermissions() {
            return new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        }
        @Override
        public int[] getRequestPermissionCodes() {
            return new int[]{1};
        }
    }
}
