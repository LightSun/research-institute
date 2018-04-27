package com.heaven7.vida.research.retrofit;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * param interceptor
 * @author heaven7
 */
public class ParamsInterceptor implements Interceptor {

    //static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = addDefaultParams(chain.request());
        return chain.proceed(request);
    }

    private Request addDefaultParams(Request request) {
        Request.Builder rBuilder = request.newBuilder();
        HttpUrl.Builder urlBuilder = request.url().newBuilder();
       // request.headers().toMultimap().put("")
        //CommonDataHelper.addCommonData(rBuilder, urlBuilder);
        rBuilder.addHeader("Content-Type", "application/x-www-form-urlencoded");
        return rBuilder.url(urlBuilder.build()).build();
    }

}