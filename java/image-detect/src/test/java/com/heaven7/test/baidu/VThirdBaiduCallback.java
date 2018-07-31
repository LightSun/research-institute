package com.heaven7.test.baidu;


import com.google.gson.GsonBuilder;
import com.heaven7.core.util.Logger;
import com.heaven7.test.baidu.entity.VThirdBaiduErrorResponse;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.IOException;

public abstract class VThirdBaiduCallback<T> implements Callback {

    protected final String TAG = getClass().getName();

    @Override
    public void onFailure(Call call, IOException e) {
        beforeResponse();
        Logger.w(TAG, "onFailure", Logger.toString(e));
        onNetworkError(call);
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        beforeResponse();
        ResponseBody body = response.body();
        if (body == null) {
            onFailed(call, "response body is null.");
            return;
        }
        String json = body.string();
        response.close();
       // System.out.println(json);
        VThirdBaiduErrorResponse vThirdBaiduErrorResponse = new GsonBuilder().create().fromJson(json, VThirdBaiduErrorResponse.class);
        if (vThirdBaiduErrorResponse.getError_code() != null) {
            onFailed(call, vThirdBaiduErrorResponse.getError_msg());
        } else {
            T obj = new GsonBuilder()
                            .create()
                            .fromJson(json, GsonUtils.getSuperclassTypeParameter(getClass()));
            onSuccess(call, obj);
        }
    }

    protected void beforeResponse() {

    }

    protected void onNetworkError(Call call) {

    }

    protected void onFailed(Call call,  String msg){
        Logger.w(TAG , "onFailed", "url = " + call.request().url().toString()
                + " ,msg = " + msg);
    }

    protected abstract void onSuccess(Call call, T obj);
}
