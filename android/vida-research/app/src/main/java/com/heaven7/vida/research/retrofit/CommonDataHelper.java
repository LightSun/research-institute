package com.heaven7.vida.research.retrofit;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import com.google.gson.annotations.SerializedName;
import com.heaven7.android.util2.NetHelper;
import com.heaven7.vida.research.utils.CommonUtils;

import java.util.Locale;
import java.util.Map;

import okhttp3.HttpUrl;
import okhttp3.Request;

/**
 * http/https公共参数
 * Created by heaven7 on 2017/3/21 0021.
 */

public final class CommonDataHelper {

    /** a flag of use common data*/
    private static volatile boolean sUsing = false;
    private static final CommonRequestData sData = new CommonRequestData();
    private static final NetHelper sNetReceiver = new NetHelper();

    private static final NetHelper.OnNetStateChangedListener NET_LISTENER = new
            NetHelper.OnNetStateChangedListener() {
                @Override
                public void onNetStateChanged(Context context, byte netState) {
                    sData.net = NetHelper.getNetStateString(netState);
                }
    };

    /*public*/ static void init(Context context) {

        DisplayMetrics dm = CommonUtils.getDisplayMetrics(context);
       // sData.appVersion = String.valueOf(SystemInfo.appVersionCode);
        sData.androidId = CommonUtils.getAndroidId(context);
        sData.resolution = dm.widthPixels + "*" + dm.heightPixels;

        sNetReceiver.addOnNetStateChangedListener(NET_LISTENER);
        sNetReceiver.register(context);
    }

    public static void setToken(String uid , String token){
        sData.uid = uid;
        //sData.uid = "1";
        sData.token = token;
    }
    public static void setToken(String token){
        sData.token = token;
    }

    public static void setImei(String imei){
        sData.imei = imei;
    }

    public static void destroy(Context context){
        sNetReceiver.clearListeners();
        sNetReceiver.unregister(context);
    }

    public static void addCommonData(Request.Builder rb, HttpUrl.Builder builder) {
        final CommonRequestData data = getRightCommonData();
        sUsing = true;
        data.timeStamp = System.currentTimeMillis() + "";
        rb.addHeader("x-access-token", "bearer " + sData.token);
        for (Map.Entry<String, String> en : data.toMap().entrySet()) {
            builder.addQueryParameter(en.getKey(), en.getValue());
        }
        sUsing = false;
    }

    private static CommonRequestData getRightCommonData() {
        if (sUsing) {
            return new CommonRequestData(sData);
        }
        return sData;
    }

    public static String getUid(){
        return sData.getUid();
    }

    private static class CommonRequestData implements CommonUtils.IRawData {
        @SerializedName("_appver")
        private String appVersion;

        @SerializedName("_locale")
        private final String local = Locale.getDefault().toString();

        @SerializedName("_andarch")
        private final String cpu = !TextUtils.isEmpty(Build.CPU_ABI) ? Build.CPU_ABI : Build.CPU_ABI2;

        @SerializedName("_andver")
        private final String sysVersion = Build.VERSION.SDK_INT + "";

        @SerializedName("_andid")
        private String androidId;

        @SerializedName("_devtype")
        private final String type = "1";

        @SerializedName("_iosver")
        private final String iosVersion = "";

        @SerializedName("_ts")
        private String timeStamp;

        @SerializedName("_model")
        private final String model = Build.MODEL;

        @SerializedName("_net")
        private String net;

        @SerializedName("_res")
        private String resolution;

       // @SerializedName("_token") //in header now
        private String token = "d55lJUKHgWHpUw9BuBOsSgaEW8u9Gnb1S"; //test token

        @SerializedName("_imei")
        private String imei;

        @SerializedName("_idfa")
        private final String idfa = "";  //ios

        @SerializedName("_uid")
        private String uid = "";

        private CommonRequestData() {
        }

        public CommonRequestData(CommonRequestData data) {
            this.appVersion = data.appVersion;
            this.androidId = data.androidId;
            this.timeStamp = data.timeStamp;
            this.net = data.net;
            this.resolution = data.resolution;
            this.token = data.token;
            this.imei = data.imei;
            this.uid = data.uid;
        }

        public Map<String, String> toMap() {
            return CommonUtils.toMap(this);
        }

        public String getTimeStamp() {
            return timeStamp;
        }

        public void setTimeStamp(String timeStamp) {
            this.timeStamp = timeStamp;
        }

        public String getAppVersion() {
            return appVersion;
        }

        public void setAppVersion(String appVersion) {
            this.appVersion = appVersion;
        }

        public String getLocal() {
            return local;
        }

        public String getCpu() {
            return cpu;
        }

        public String getSysVersion() {
            return sysVersion;
        }

        public String getAndroidId() {
            return androidId;
        }

        public void setAndroidId(String androidId) {
            this.androidId = androidId;
        }

        public String getType() {
            return type;
        }

        public String getIosVersion() {
            return iosVersion;
        }

        public String getModel() {
            return model;
        }

        public String getNet() {
            return net;
        }

        public void setNet(String net) {
            this.net = net;
        }

        public String getResolution() {
            return resolution;
        }

        public void setResolution(String resolution) {
            this.resolution = resolution;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getImei() {
            return imei;
        }

        public void setImei(String imei) {
            this.imei = imei;
        }

        public String getIdfa() {
            return idfa;
        }

        public String getUid() {
            return uid;
        }
        public void setUid(String uid) {
            this.uid = uid;
        }
    }


    /**
     _appver: app版本号(2017032090)
     _locale: 语言(zh_CN)
     _andarch: device_arch（?）
     _andver:  Android系统版本号(22)
     _andid: Android ID号
     _devtype: 设备类型(1: 安卓手机, 2: iPhone)
     _iosver: IOS系统版本
     _ts: 时间戳(1468903601)
     _model: 手机型号(Mi - 4c)
     _net: 网络类型(0: 'unknown', 1: 'wifi', 2: '2G', 3: '3G', 4: '4G')
     _res: 屏幕分辨率(800x600)
     _token: 请求用户token
     _imei:  imei序列号
     _idfa: IOS设备标识
     _uid: 用户ID号
     _ref: Referer，主要统计访问来源（？）
     */

}
