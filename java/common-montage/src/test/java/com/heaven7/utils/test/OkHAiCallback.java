package com.heaven7.utils.test;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.heaven7.core.util.Logger;
import com.heaven7.java.visitor.MapFireVisitor;
import com.heaven7.java.visitor.collection.KeyValuePair;
import com.heaven7.java.visitor.collection.VisitServices;
import com.vida.common.IOUtils;
import com.vida.common.ai.LocalAiGeneratorDelegate;
import okhttp3.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author heaven7
 */
public class OkHAiCallback implements LocalAiGeneratorDelegate.Callback {

    private static final String TAG = "OkHAiCallback";

    @Override
    public void tfrecordToTag(String tfrecordPath, String targetTagPath) {
        if(!new File(tfrecordPath).exists()){
            Logger.w(TAG, "tfrecordToTag", "failed for no tfrecord file. expect path = " + tfrecordPath);
            return;
        }
        String url = "http://" + "www.xiaoxiekeji.cn" + ":8004/media/test/tfrecordToTag";
        Map<String, String> map = new HashMap<>();
        map.put("token", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxMjM0NTYiLCJpYXQiOjE1MzMxOTcxNzQsImV4cCI6OTIyMzM2NTkwNDA2NjA4MH0.JJaBasuqJc8_u8p7z3LfkbK7Ev5dUARDmupBqRtTZDo");

        //String tfrecordFile = "D:\\Users\\Administrator\\AppData\\Local\\Temp\\media_files\\test\\1535163447364_output.tfrecord";
       // String outFile = "D:\\Users\\Administrator\\AppData\\Local\\Temp\\media_files\\test\\1535163447364_predictions.csv";
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(10, TimeUnit.MINUTES)
                .writeTimeout(10, TimeUnit.MINUTES)
                .connectTimeout(10, TimeUnit.MINUTES)
                .build();
        //get response
        ApiProtocol<String> ap = postSync(client, url, map, getRequestBody(Arrays.asList(tfrecordPath)));
        if(ap == null){
            Logger.d(TAG, "tfrecordToTag", "failed");
            return;
        }
        if(ap.getCode() != 0 || ap.getData() == null){
            Logger.d(TAG, "tfrecordToTag", ap.getMsg());
            return;
        }
        byte[] datas = ap.getData().getBytes(StandardCharsets.UTF_8);
        OutputStream fos = null;
        try {
            fos = new BufferedOutputStream(new FileOutputStream(targetTagPath));
            IOUtils.copyLarge(new ByteArrayInputStream(datas), fos);
            fos.flush();
            Logger.d(TAG, "tfrecordToTag", "write tag file success. targetTagPath = " + targetTagPath);
        }catch (IOException e){
            Logger.d(TAG, "tfrecordToTag", "write tag file failed.  " + Logger.toString(e));
        }finally {
            IOUtils.closeQuietly(fos);
        }
    }
    public static ApiProtocol<String> postSync(OkHttpClient client, String url, Map<String, String> headers, RequestBody body){
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
        Response response = null;
        try {
            response = call.execute();
            ResponseBody body2 = response.body();
            if(body2 == null){
                System.err.println("body is null");
                return null;
            }
            String json = body2.string();
            ApiProtocol<String> ap =  new Gson().fromJson(json, new TypeToken<ApiProtocol<String>>(){}.getType());
            return ap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }finally {
            IOUtils.closeQuietly(response);
        }
    }
    private static RequestBody getRequestBody(List<String> fileNames) {
        MultipartBody.Builder builder = new MultipartBody.Builder();
        for (int i = 0; i < fileNames.size(); i++) {
            File file = new File(fileNames.get(i));
            //mime
            builder.addFormDataPart(
                    "file",
                    file.getName(),
                    RequestBody.create(null, file)
            );
        }
        return builder.build();
    }
}
