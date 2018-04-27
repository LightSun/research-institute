package com.heaven7.vida.research;

import android.app.Application;

import com.heaven7.vida.research.retrofit.RetrofitHelper;

/**
 * Created by heaven7 on 2018/4/27 0027.
 */

public class VidaTestApplication extends Application {

    public static final String URL_FACE  = "https://aip.baidubce.com/";

    @Override
    public void onCreate() {
        super.onCreate();
        RetrofitHelper.init(this, URL_FACE);
    }
}
