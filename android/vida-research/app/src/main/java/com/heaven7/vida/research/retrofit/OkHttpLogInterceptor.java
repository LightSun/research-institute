package com.heaven7.vida.research.retrofit;

import android.text.TextUtils;

import com.heaven7.core.util.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.Okio;

/**
 * An {@link Interceptor} implementation which logs all info
 * about outgoing request and related response including complete header set,
 * request and response bodies and network time
 *
 * @author Emmar Kardeslik
 */
public class OkHttpLogInterceptor implements Interceptor {
    private static final double MILLI_AS_NANO = 1e6d;
    boolean isShowFullLog;
    private static final String TAG ="RetrofitNetwork";

    public OkHttpLogInterceptor() {
        this(true);
    }

    public OkHttpLogInterceptor(boolean isShowFullLog) {
        this.isShowFullLog = isShowFullLog;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (isShowFullLog) {
            Logger.i(TAG,"---> REQUEST " + request.method() + " " + request.url());
            logHeaders(request.headers(), "request");
            // copy original request for logging request body
            Request copy = request.newBuilder().build();
            RequestBody requestBody = copy.body();
            if (requestBody == null) {
                Logger.i(TAG,"Body - no body");
            } else {
                Buffer buffer = new Buffer();
                requestBody.writeTo(buffer);
                Charset charset = requestBody.contentType().charset();
                if(charset == null){
                    charset = Charset.forName("utf-8");
                }
                Logger.i(TAG,"Body - " + buffer.readString(charset));
            }
            Logger.i(TAG,"---> END");
        }
        long t1 = System.nanoTime();
        Response response = chain.proceed(request);
        long t2 = System.nanoTime();

        if (isShowFullLog) {
            Logger.i(TAG,"<--- RESPONSE " + response.code() + " " + response.request().url());
            logHeaders(response.headers(), "response");
        }
        ResponseBody body = response.body();
        InputStream responseStream = null;
        if (body != null) {
            responseStream = body.byteStream();
        }
        byte[] b = input2byte(responseStream);
        String responseBody = new String(b);
        try {
            JSONObject json = new JSONObject(responseBody);
            if (TextUtils.isEmpty(responseBody)) {
                Logger.i(TAG,request.method() + " " + request.url() + "Body - no body");
            } else {
                Logger.i(TAG,request.method() + " " + request.url() + "Body - " + " \n" + json.toString(2));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (body != null) {
            Logger.i(TAG,"<--- END " + "(Size: " + body.contentLength() + " bytes - " + "Network time: " + (t2 - t1)
                    / MILLI_AS_NANO + " ms)");
        }
        if (responseStream != null) {
            response = response.newBuilder()
                    .body(new ForwardingResponseBody(body, new ByteArrayInputStream(b)))
                    .build();
        }
        return response;
    }

    private static class ForwardingResponseBody extends ResponseBody {
        private final ResponseBody mBody;
        private final BufferedSource mInterceptedSource;

        public ForwardingResponseBody(ResponseBody body, InputStream interceptedStream) {
            mBody = body;
            mInterceptedSource = Okio.buffer(Okio.source(interceptedStream));
        }

        @Override
        public MediaType contentType() {
            return mBody.contentType();
        }

        @Override
        public long contentLength() {
            return mBody.contentLength();
        }

        @Override
        public BufferedSource source() {
            return mInterceptedSource;
        }
    }

    private static void logHeaders(Headers headers, String tag) {
        for (String headerName : headers.names()) {
            for (String headerValue : headers.values(headerName)) {
                Logger.i(TAG,tag + " >>> Header - [" + headerName + ": " + headerValue + "]");
            }
        }
    }

    private byte[] input2byte(InputStream inStream)
            throws IOException {
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        byte[] buff = new byte[1024];
        int rc = 0;
        while ((rc = inStream.read(buff, 0, 1024)) != -1) {
            swapStream.write(buff, 0, rc);
        }
        return swapStream.toByteArray();
    }
}
