package com.heaven7.test_okh;

import com.heaven7.core.util.Logger;
import okhttp3.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.heaven7.test_okh.OkHttpHelper.getMimeType;

public class TestAiUploadFlow {

    private static final String TAG = "TestAiUploadFlow";

    public static void main(String[] args) {
        testUpload();
    }

    private static void testUpload() {
        String url = "http://192.168.3.142:12563/media/upload";
        String[] medias = {
               // "E:\\BaiduNetdiskDownload\\taobao_service\\照片\\女装\\浅蓝围巾领开衫\\1-10.jpg",
                "E:\\BaiduNetdiskDownload\\taobao_service\\东森（服装）\\女装南泉外拍第二次视频2\\扎染褙子\\VID_20180507_141100.mp4",
                "E:\\BaiduNetdiskDownload\\taobao_service\\东森（服装）\\女装南泉外拍第二次视频2\\扎染褙子\\VID_20180507_141029.mp4",
                "E:\\BaiduNetdiskDownload\\taobao_service\\东森（服装）\\女装南泉外拍第二次视频2\\扎染褙子\\VID_20180507_140824.mp4",
                "E:\\BaiduNetdiskDownload\\taobao_service\\东森（服装）\\女装南泉外拍第二次视频2\\扎染褙子\\VID_20180507_140749.mp4",
                //"E:\\BaiduNetdiskDownload\\taobao_service\\东森（服装）\\女装南泉外拍第二次视频2\\扎染褙子\\VID_20180507_140458.mp4",
        };
        OkHttpHelper.post(url, getRequestBody(Arrays.asList(medias)), new Callback() {
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
        builder.addFormDataPart("project_id", "pro_123456");
        builder.addFormDataPart("token", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJoZWF2ZW43IiwiaWF0IjoxNTMyMDAzNjg5LCJzdWIiOiI0ZTk3NDY4MTY3ZGI2ZWRiMTIzMjBjYmU3ZjY1NzYzNyIsImV4cCI6MTUzMjYwODQ4OX0.jKDA4vWucIru5pGNsvwh-rTR6cds-rgvYg2bJjqL9GQ");
        return builder.build();
    }

}
