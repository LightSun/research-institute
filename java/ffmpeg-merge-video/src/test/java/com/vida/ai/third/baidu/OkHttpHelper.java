package com.vida.ai.third.baidu;

import com.google.gson.GsonBuilder;
import com.heaven7.java.base.anno.Nullable;
import com.heaven7.java.visitor.MapFireVisitor;
import com.heaven7.java.visitor.collection.KeyValuePair;
import com.heaven7.java.visitor.collection.VisitServices;
import com.vida.common.IOUtils;
import okhttp3.*;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class OkHttpHelper {
    public static <T> T postSyncAndGetData(String url, @Nullable Map<String, String> headers, RequestBody body, Type type) {
        String json = postSyncAndGetData(url, headers, body);
        if (json == null) {
            return null;
        }
        return new GsonBuilder().create().fromJson(json, type);
    }

    public static String postSyncAndGetData(String url, @Nullable Map<String, String> headers, RequestBody body) {
        Response res = null;
        try {
            res = postSync(url, headers, body);
            ResponseBody resBody = res.body();
            if (resBody == null) {
                return null;
            }
            return resBody.string();
        } catch (IOException e) {
            return null;
        } finally {
            if (res != null) {
                res.close();
            }
        }
    }

    public static void post(String url, RequestBody body, Callback callback) {
        post(url, Collections.emptyMap(), body, callback);
    }

    public static void post(String url, Map<String, String> headers, RequestBody body, Callback callback){
        Request.Builder builder = new Request.Builder().url(url)
                .post(body);
        if(!headers.isEmpty()) {
            VisitServices.from(headers).fire(new MapFireVisitor<String, String>() {
                @Override
                public Boolean visit(KeyValuePair<String, String> pair, Object param) {
                    builder.addHeader(pair.getKey(), pair.getValue());
                    return null;
                }
            });
        }
        OkHttpClient okHttpClient = new OkHttpClient();
        Call call = okHttpClient.newCall(builder.build());
        call.enqueue(callback);
    }
    public static Response postSync(String url, RequestBody body) throws IOException {
        return postSync(url, Collections.emptyMap(), body);
    }
    public static Response postSync(String url, Map<String, String> headers, RequestBody body) throws IOException {
        Request.Builder builder = new Request.Builder().url(url)
                .post(body);
        if(!headers.isEmpty()) {
            VisitServices.from(headers).fire(new MapFireVisitor<String, String>() {
                @Override
                public Boolean visit(KeyValuePair<String, String> pair, Object param) {
                    builder.addHeader(pair.getKey(), pair.getValue());
                    return null;
                }
            });
        }
        return new OkHttpClient().newCall(builder.build()).execute();
    }

    private static RequestBody getRequestBody(List<String> fileNames) {
        MultipartBody.Builder builder = new MultipartBody.Builder();
        for (int i = 0; i < fileNames.size(); i++) {
            File file = new File(fileNames.get(i));
            String fileType = getMimeType(file.getName());
            builder.addFormDataPart(
                    "file",
                    file.getName(),
                    RequestBody.create(MediaType.parse(fileType), file)
            );
        }
        return builder.build();
    }
    private static Request getRequest(String url, List<String> fileNames) {
        Request.Builder builder = new Request.Builder();
        builder.url(url)
                .post(getRequestBody(fileNames));
        return builder.build();
    }
    public static byte[] getData(String url) {
        Request.Builder builder = new Request.Builder();
        Request request = builder.url(url).get().build();
        Response response = null;
        try {
            response = new OkHttpClient().newCall(request).execute();
            ResponseBody body = response.body();
            if(body == null){
                return null;
            }
            return body.bytes();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            IOUtils.closeQuietly(response);
        }
        return null;
    }

    public static void upLoadFile(String url, List<String> fileNames, Callback callback) {
        OkHttpClient okHttpClient = new OkHttpClient();
        Call call = okHttpClient.newCall(getRequest(url, fileNames));
        call.enqueue(callback);
    }

    public static String getMimeType(String link){
        Path path = Paths.get(link);
        String contentType = null;
        try {
            contentType = Files.probeContentType(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return contentType;
    }
}
