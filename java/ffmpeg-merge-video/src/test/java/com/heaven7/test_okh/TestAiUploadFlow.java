package com.heaven7.test_okh;

import com.heaven7.core.util.Logger;
import okhttp3.*;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static com.heaven7.test_okh.OkHttpHelper.getMimeType;

public class TestAiUploadFlow {

    private static final String TAG = "TestAiUploadFlow";

    public static void main(String[] args) {
        testUpload();
    }

    private static void testUpload() {
        String url = "http://192.168.3.142:12563/media/upload";
        String[] medias = {
                "E:\\BaiduNetdiskDownload\\taobao_service\\照片\\女装\\浅蓝围巾领开衫\\1-10.jpg",
               // "E:\\BaiduNetdiskDownload\\taobao_service\\东森（服装）\\女装南泉外拍第二次视频2\\扎染褙子\\VID_20180507_141100.mp4",
                //"E:\\BaiduNetdiskDownload\\taobao_service\\东森（服装）\\女装南泉外拍第二次视频2\\扎染褙子\\VID_20180507_141029.mp4",
                //"E:\\BaiduNetdiskDownload\\taobao_service\\东森（服装）\\女装南泉外拍第二次视频2\\扎染褙子\\VID_20180507_140824.mp4",
                //"E:\\BaiduNetdiskDownload\\taobao_service\\东森（服装）\\女装南泉外拍第二次视频2\\扎染褙子\\VID_20180507_140749.mp4",
                //"E:\\BaiduNetdiskDownload\\taobao_service\\东森（服装）\\女装南泉外拍第二次视频2\\扎染褙子\\VID_20180507_140458.mp4",
        };
        Map<String,String> map = new HashMap<>();
        map.put("token", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxMjM0NTYiLCJpYXQiOjE1MzI1ODg5NTYsImV4cCI6MTUzMzE5Mzc1Nn0.0qhooMVp4yDmQ92TAPXbuRO1J6ZBjLUJgqERuADaSVw");

        OkHttpHelper.post(url, map, getRequestBody(Arrays.asList(medias)), new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Logger.w(TAG, "testUpload", Logger.toString(e));
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ResponseBody body = response.body();
                Logger.i(TAG, "testUpload", body != null ? body.string() : "Null");
                response.close();
            }
        });
        /**
         https://oauth.jd.com/oauth/authorize?response_type=code&client_id=446F683F1B27A0468779367E33D7CF43&redirect_uri=http://192.168.3.142&state=1
         */
    }
    private static RequestBody getRequestBody(List<String> fileNames) {
        MultipartBody.Builder builder = new MultipartBody.Builder();
        for (int i = 0; i < fileNames.size(); i++) {
            File file = new File(fileNames.get(i));
            //mime
            String fileType = getMimeType(file.getName());
            builder.addFormDataPart(
                    "file",
                    file.getName(),
                    RequestBody.create(MediaType.parse(fileType), file)
            );
        }
        builder.addFormDataPart("project_id", "1");
        return builder.build();
    }

}
