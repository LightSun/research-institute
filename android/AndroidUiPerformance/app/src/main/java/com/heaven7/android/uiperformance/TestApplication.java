package com.heaven7.android.uiperformance;

import android.app.Application;

/**
 * Created by heaven7 on 2019/6/19.
 */
public final class TestApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        BlockDetectByChoreographer.start();
    }
}
