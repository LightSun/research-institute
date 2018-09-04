package com.vida.ai.third.baidu;


import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.heaven7.core.util.Logger;
import com.heaven7.java.base.util.Throwables;
import com.vida.ai.third.baidu.entity.VThirdBaiduErrorResponse;
import com.vida.common.GsonUtils;
import okhttp3.*;

import java.io.IOException;

public abstract class VThirdBaiduCallback<T> implements Callback {

    /** empty body */
    public static final int FAILED_TYPE_EMPTY_BODY        = 1;
    /** error code of another server */
    public static final int FAILED_TYPE_ERROR_CODE        = 2;
    /** json syntax error */
    public static final int FAILED_TYPE_JSON_SYNTAX_ERROR = 3;
    /** net work error */
    public static final int FAILED_TYPE_NET_ERROR         = 4;

    private static final int THRESOLD_MAX_FAILED_COUNT = 3;

    protected final String TAG = getClass().getName();
    private int mFailedCount;

    private final RequestService mService;

    public VThirdBaiduCallback(RequestService mService) {
        Throwables.checkNull(mService);
        this.mService = mService;
    }

    public static String getFailedTypeString(int failedType){
        switch (failedType){
            case FAILED_TYPE_EMPTY_BODY:
                return "FAILED_TYPE_EMPTY_BODY";
            case FAILED_TYPE_ERROR_CODE:
                return "FAILED_TYPE_ERROR_CODE";
            case FAILED_TYPE_JSON_SYNTAX_ERROR:
                return "FAILED_TYPE_JSON_SYNTAX_ERROR";
            case FAILED_TYPE_NET_ERROR:
                return "FAILED_TYPE_NET_ERROR";
        }
        return null;
    }

    @Override
    public void onFailure(Call call, IOException e) {
        beforeResponse();
       // Logger.w(TAG, "onFailure", Logger.toString(e));
        onFailed(call, FAILED_TYPE_NET_ERROR, Logger.toString(e));
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        beforeResponse();
        ResponseBody body = response.body();
        if (body == null) {
            onFailed(call, FAILED_TYPE_EMPTY_BODY, "response body is null.");
            return;
        }
        String json = body.string();
        response.close();
        System.out.println(json);
        VThirdBaiduErrorResponse vThirdBaiduErrorResponse = new GsonBuilder().create().fromJson(json, VThirdBaiduErrorResponse.class);
        if (vThirdBaiduErrorResponse.getError_code() != null) {
            onFailed(call, FAILED_TYPE_ERROR_CODE, vThirdBaiduErrorResponse.getError_msg());
            return;
        }
        try {
            T obj = new GsonBuilder().create().fromJson(json, GsonUtils.getSuperclassTypeParameter(getClass()));
            onSuccess(call, obj);
        }catch (JsonSyntaxException e){
            //caused by baidu json not Inconsistent. like VBodyAnalysis.person_info. string or array
            onFailed(call, FAILED_TYPE_JSON_SYNTAX_ERROR, Logger.toString(e));
        }
    }

    protected void beforeResponse() {

    }

    protected void onFailed(Call call, int failedType, String msg) {
        switch (failedType){
            case FAILED_TYPE_ERROR_CODE:
            case FAILED_TYPE_NET_ERROR:
            case FAILED_TYPE_EMPTY_BODY:
                Logger.w(TAG, "onFailed", "url = " + call.request().url().toString()
                        + " ,failedType ="+ getFailedTypeString(failedType) + " ,msg = " + msg);
                mFailedCount ++;
                if(mFailedCount >= THRESOLD_MAX_FAILED_COUNT ){
                    onFailed(call, msg);
                }else {
                    mService.postRequest(call.request(), this);
                }
                break;

            case FAILED_TYPE_JSON_SYNTAX_ERROR:
                onFailed(call, msg);
                break;
        }
    }
    protected void onFailed(Call call, String msg) {

    }

    protected abstract void onSuccess(Call call, T obj);

    public interface RequestService{
        void postRequest(Request request, okhttp3.Callback callback);
    }
}
