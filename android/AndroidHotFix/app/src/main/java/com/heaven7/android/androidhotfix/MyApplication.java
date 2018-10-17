package com.heaven7.android.androidhotfix;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

/**
 * Created by heaven7 on 2018/10/17 0017.
 */
public class MyApplication extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
        BugFixUtils.loadPatch(base);
    }
}
