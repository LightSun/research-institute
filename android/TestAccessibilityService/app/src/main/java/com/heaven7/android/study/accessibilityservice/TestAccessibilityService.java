package com.heaven7.android.study.accessibilityservice;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.heaven7.core.util.Logger;

import java.util.List;

public class TestAccessibilityService extends AccessibilityService {

    private static final String TAG = "TestAccessService";
    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        final int eventType = event.getEventType();
        String eventText = null;
        switch (eventType) {
            case AccessibilityEvent.TYPE_VIEW_CLICKED:
                eventText = "Clicked: ";
                break;
            case AccessibilityEvent.TYPE_VIEW_FOCUSED:
                eventText = "Focused: ";
                break;
        }
        eventText = eventText + event.getContentDescription();
        CharSequence pkg = event.getPackageName();
        Logger.d(TAG, "onAccessibilityEvent", "pkg = " + pkg + " ," + eventText);

        AccessibilityNodeInfo source = event.getSource();
        if (source == null) {
            return;
        }

        // Grab the parent of the view that fired the event.
        AccessibilityNodeInfo rowNode = getListItemNodeInfo(source);
        if (rowNode == null) {
            return;
        }
    }

    private AccessibilityNodeInfo getListItemNodeInfo(AccessibilityNodeInfo source) {
        List<AccessibilityNodeInfo> infos = source.findAccessibilityNodeInfosByText("Hello World!");
        if(infos == null){
           return null;
        }
        return infos.get(0);
    }

    @Override
    public void onInterrupt() {

    }
}
