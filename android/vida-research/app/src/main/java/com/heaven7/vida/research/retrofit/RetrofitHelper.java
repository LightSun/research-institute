package com.heaven7.vida.research.retrofit;

import android.content.Context;
import android.text.TextUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author heaven7
 */

public final class RetrofitHelper {

    private static final long CONNECT_TIMEOUT = 30L;
    private static final long READ_TIMEOUT = 30L;
    private static final long WRITE_TIMEOUT = 30L;
    private static final long MAX_CACHE_SIZE = 100 * 1024 * 1024L;
    private static final String CACHE_NAME = "cache_network";
    private static final String PACKAGE_NAME = "com.classroom100.classroom";
    private static final String DEFAULT_CACHE_DIR = Utils.getRootDirectory() + "/Android/data/" +
            PACKAGE_NAME + File.separator + CACHE_NAME;

    private static final boolean DEBUG = true;

    private static Context sContext;
    private static String sBaseUrl;
    private static Retrofit sRetrofitInstance;

    public static void init(Context context, String baseUrl) {
        if (context == null || TextUtils.isEmpty(baseUrl)) {
            throw new IllegalArgumentException();
        }
        sRetrofitInstance = null;
        sContext = context.getApplicationContext();
        sBaseUrl = baseUrl;
        CommonDataHelper.init(context);
    }

    //lazy load
    public static synchronized  <T> T build(Class<T> cls) {
        if(sRetrofitInstance == null){
            checkInit();
            if(sRetrofitInstance == null) {
                sRetrofitInstance = buildRetrofit(sBaseUrl, getInterceptors());
            }
        }
        return sRetrofitInstance.create(cls);
    }

    public static <T> T build(String baseUrl, Class<T> cls) {
        checkInit();
        return buildRetrofit(baseUrl, getInterceptors()).create(cls);
    }

    private static List<Interceptor> getInterceptors() {
        List<Interceptor> list = new ArrayList<>();
        list.add(new ParamsInterceptor());
        list.add(new CacheInterceptor(sContext));
        if (DEBUG) {
            list.add(new OkHttpLogInterceptor());
        }
        return list;
    }

    private static OkHttpClient buildDefaultClient(List<Interceptor> interceptors) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS);
        builder.readTimeout(READ_TIMEOUT, TimeUnit.SECONDS);
        builder.writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS);
        builder.cache(new Cache(new File(DEFAULT_CACHE_DIR), MAX_CACHE_SIZE));
        builder.retryOnConnectionFailure(true);
        for (Interceptor interceptor : interceptors) {
            builder.addInterceptor(interceptor);
        }

       /* SSLSocketFactory sslFactory = HTTPSTrustManager.getSSLSocketFactory(sContext);
        if(sslFactory != null) {
            builder.sslSocketFactory(sslFactory);
        }*/

        return builder.build();
    }

    private static Retrofit buildRetrofit(String baseUrl, List<Interceptor> interceptors) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(buildDefaultClient(interceptors))
                .addConverterFactory(GsonConverterFactory.create())
               // .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    private static void checkInit(){
        if (sContext == null){
            throw new RuntimeException("not initialize before use !");
        }
    }

}
