package com.heaven7.test_okh;

import com.heaven7.java.visitor.MapFireVisitor;
import com.heaven7.java.visitor.collection.KeyValuePair;
import com.heaven7.java.visitor.collection.VisitServices;
import okhttp3.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class OkHttpHelper {

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
    public static void post(OkHttpClient client, String url, Map<String, String> headers, RequestBody body, Callback callback){
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
        Call call = client.newCall(builder.build());
        call.enqueue(callback);
    }
    public static void put(String url, Map<String, String> headers, RequestBody body, Callback callback){
        Request.Builder builder = new Request.Builder().url(url)
                .put(body);
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

    /**
     * 通过上传的文件的完整路径生成RequestBody
     * @param fileNames 完整的文件路径
     */
    private static RequestBody getRequestBody(List<String> fileNames) {
        //创建MultipartBody.Builder，用于添加请求的数据
        MultipartBody.Builder builder = new MultipartBody.Builder();
        for (int i = 0; i < fileNames.size(); i++) { //对文件进行遍历
            File file = new File(fileNames.get(i)); //生成文件
            //根据文件的后缀名，获得文件类型
            String fileType = getMimeType(file.getName());
            builder.addFormDataPart( //给Builder添加上传的文件
                    "file",  //请求的名字
                    file.getName(), //文件的文字，服务器端用来解析的
                    RequestBody.create(MediaType.parse(fileType), file) //创建RequestBody，把上传的文件放入
            );
        }
        return builder.build(); //根据Builder创建请求
    }
    /**
     * 获得Request实例
     * @param fileNames 完整的文件路径
     */
    private static Request getRequest(String url, List<String> fileNames) {
        Request.Builder builder = new Request.Builder();
        builder.url(url)
                .post(getRequestBody(fileNames));
        return builder.build();
    }

    /**
     * 根据url，发送异步Post请求
     *
     * @param url       提交到服务器的地址
     * @param fileNames 完整的上传的文件的路径名
     * @param callback  OkHttp的回调接口
     */
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

    public static void main(String[] args) {

    }
}
