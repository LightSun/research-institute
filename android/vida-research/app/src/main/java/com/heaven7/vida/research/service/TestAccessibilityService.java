package com.heaven7.vida.research.service;

import android.accessibilityservice.AccessibilityService;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

import com.heaven7.core.util.Logger;
import com.heaven7.vida.research.utils.AccessibilityOperator;

/**
 * Created by heaven7 on 2019/2/11.
 */
public class TestAccessibilityService extends AccessibilityService {

    private static final String TAG = "TestAccessibilityServic";

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if(event.getPackageName() == null){
            return;
        }
       /* int eventType = event.getEventType();
        switch (eventType) {
            case AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED:
                //TODO处理通知栏来的事件类型
                break;
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
            case AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED:
                //TODO窗口出现变化的时候处理
                break;
        }*/
        String pkgName = event.getPackageName().toString();
        int eventType = event.getEventType();
        AccessibilityOperator.getInstance().updateEvent(this, event);

        Logger.d(TAG, "onAccessibilityEvent", "eventType: " + eventType + " pkgName: " + pkgName);
    }

    @Override
    public void onInterrupt() {

    }

    private ActivityInfo tryGetActivity(ComponentName componentName) {
        try {
            return getPackageManager().getActivityInfo(componentName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
    }

}
