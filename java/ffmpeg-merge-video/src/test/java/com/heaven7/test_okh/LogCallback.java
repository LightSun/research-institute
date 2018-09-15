package com.heaven7.test_okh;

import com.heaven7.java.base.util.Logger;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.IOException;

public class LogCallback implements Callback {

    private static final String TAG = "LogCallback";
    private final String focusTag;

    public LogCallback(String focusTag) {
        this.focusTag = focusTag;
    }
    @Override
    public void onFailure(Call call, IOException e) {
        Logger.w(TAG, focusTag + "_onFailure", Logger.toString(e));
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        ResponseBody body = response.body();
        if (body == null) {
            response.close();
            return;
        }
        String json = body.string();
        Logger.i(TAG, focusTag + "_onResponse", json != null ? json : "Null");
        response.close();
    }
}
