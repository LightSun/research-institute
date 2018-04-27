package com.heaven7.vida.research.retrofit;

import android.content.Context;

import com.heaven7.android.util2.NetworkCompatUtil;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 网络缓存拦截器 配置Cache-Control同时配置
 * {@link #HEADER_USER_CACHE_TYPE}，若只配置Cache-Control则默认使用 {@link #TYPE_NETWORK_WITH_CACHE}
 */
public class CacheInterceptor implements Interceptor {

    private static final String HEADER_CACHE_CONTROL = "Cache-Control";
    private static final String HEADER_PRAGMA = "Pragma";
    private static final String CACHE_CONTROL_ONLY_CACHE = "public, only-if-cached, max-age=2419200";
    private static final String CACHE_CONTROL_NO_CACHE = "public, max-age=1";
    private static final String HEADER_USER_CACHE_TYPE = "User-Cache-Type";
    // 断网情况下，加载缓存，联网情况下，优先加载缓存，默认情况
    public static final String TYPE_NETWORK_WITH_CACHE = "network_with_cache";
    // 断网情况下，加载缓存，联网情况下，只加载网络
    public static final String TYPE_NETWORK_NO_CACHE = "network_no_cache";

    private final Context mContext;

    public CacheInterceptor(Context context) {
        this.mContext = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        String cacheControl = request.cacheControl().toString();
       /* if (TextUtils.isEmpty(cacheControl)) {
            return chain.proceed(request);
        }*/
        String cacheType = request.header(HEADER_USER_CACHE_TYPE);
        final boolean haveNetwork = NetworkCompatUtil.hasConnectedNetwork(mContext);
        if (!haveNetwork) {
            request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build();
        }
        Response originalResponse = chain.proceed(request);
        if (!haveNetwork) {
            cacheControl = CACHE_CONTROL_ONLY_CACHE;
        } else if (TYPE_NETWORK_NO_CACHE.equals(cacheType)) {
            cacheControl = CACHE_CONTROL_NO_CACHE;
        }
        return originalResponse.newBuilder()
                .header(HEADER_CACHE_CONTROL, cacheControl)
                .removeHeader(HEADER_PRAGMA)
                .build();
    }
}
