package com.heaven7.test_okh;

import com.google.gson.Gson;
import com.heaven7.core.util.Logger;
import com.heaven7.ve.starter.JavaImageWriter;
import okhttp3.*;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static com.heaven7.test_okh.OkHttpHelper.getMimeType;

public class TestAiUploadFlow {

    private static final String TAG = "TestAiUploadFlow";

    public static void main(String[] args) {
        //  testUpload();
        //createProject(TestHelper.getIds());
        // createProject(null, 1L);
        createProject2();
        // makeWorks();
        // testGetImage();
    }

    public static void testGetImage() {
        String url = "http://www.xiaoxiekeji.cn:8004/media/resources/media_files%5Ce10adc3949ba59abbe56e057f20f883e%5C11%5Cresource%5Cmoved_imgs%5C1533730443374.jpg";
        OkHttpClient okHttpClient = new OkHttpClient();
        Call call = okHttpClient.newCall(new Request.Builder()
                .url(url)
                .addHeader("token", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxMjM0NTYiLCJpYXQiOjE1MzMxOTcxNzQsImV4cCI6OTIyMzM2NTkwNDA2NjA4MH0.JJaBasuqJc8_u8p7z3LfkbK7Ev5dUARDmupBqRtTZDo")
                .get()
                .build());
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) {
                System.out.println(response.code());
                response.close();
            }
        });
    }

    /**
     * //cmd /c start /wait /b ffmpeg -i D:\\Users\heyunpeng\AppData\Local\Temp\media_files\e10adc3949ba59abbe56e057f20f883e\1\resource\1533358646780.mp4 -r 1 -ss 00:00:00.00 -s 1920*1080 D:\\Users\heyunpeng\AppData\Local\Temp\media_files\e10adc3949ba59abbe56e057f20f883e\1\temp\1533358646780\img_%05d.jpg -y
     */
    private static void testUpload() {
        // String url = "http://www.xiaoxiekeji.cn:8004/media/upload";
        String url = "http://www.xiaoxiekeji.cn:8004/media/uploadSimpleFile";
        String[] medias = {
                "E:\\BaiduNetdiskDownload\\taobao_service\\照片\\女装\\浅蓝围巾领开衫\\1-10.jpg",
                // "E:\\BaiduNetdiskDownload\\taobao_service\\东森（服装）\\女装南泉外拍第二次视频2\\扎染褙子\\VID_20180507_141100.mp4",
                // "E:\\BaiduNetdiskDownload\\taobao_service\\东森（服装）\\女装南泉外拍第二次视频2\\扎染褙子\\VID_20180507_141029.mp4",
                // "E:\\BaiduNetdiskDownload\\taobao_service\\东森（服装）\\女装南泉外拍第二次视频2\\扎染褙子\\VID_20180507_140059.mp4",
                // "E:\\BaiduNetdiskDownload\\taobao_service\\东森（服装）\\女装南泉外拍第二次视频2\\扎染褙子\\VID_20180507_140749.mp4",
                //"E:\\BaiduNetdiskDownload\\taobao_service\\东森（服装）\\女装南泉外拍第二次视频2\\扎染褙子\\VID_20180507_140458.mp4",
        };
        Map<String, String> map = new HashMap<>();
        map.put("token", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxMjM0NTYiLCJpYXQiOjE1MzMxOTcxNzQsImV4cCI6OTIyMzM2NTkwNDA2NjA4MH0.JJaBasuqJc8_u8p7z3LfkbK7Ev5dUARDmupBqRtTZDo");

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

    public static RequestBody getRequestBody(List<String> fileNames) {
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
        //匿名上传 builder.addFormDataPart("project_id", "1");
        return builder.build();
    }

    private static void makeWorks() {
        String url = "http://www.xiaoxiekeji.cn:8004/media/makeWorks";
        Map<String, String> map = new HashMap<>();
        map.put("token", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxMjM0NTYiLCJpYXQiOjE1MzMxOTcxNzQsImV4cCI6OTIyMzM2NTkwNDA2NjA4MH0.JJaBasuqJc8_u8p7z3LfkbK7Ev5dUARDmupBqRtTZDo");

        FormBody body = new FormBody.Builder()
                .add("project_id", "1")
                .build();
        OkHttpHelper.post(url, map, body, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ResponseBody body = response.body();
                Logger.i(TAG, "makeWorks", body != null ? body.string() : "Null");
                response.close();
            }
        });
    }

    //media_file/e10adc3949ba59abbe56e057f20f883e/23/resource/1533955748046.mp4
    private static void createProject2() {
        String json = TestHelper.JSON_CREATE_PROJECT;
        String url = "http://www.xiaoxiekeji.cn:8002/project/createProject";
        Map<String, String> map = new HashMap<>();
        map.put("token", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxMjM0NTYiLCJpYXQiOjE1MzMxOTcxNzQsImV4cCI6OTIyMzM2NTkwNDA2NjA4MH0.JJaBasuqJc8_u8p7z3LfkbK7Ev5dUARDmupBqRtTZDo");
        FormBody body = new FormBody.Builder()
                .add("project", json)
                // .add("makeWorks", "true")
                .build();
        OkHttpHelper.post(url, map, body, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ResponseBody body = response.body();
                Logger.i(TAG, "createProject", body != null ? body.string() : "Null");
                response.close();
            }
        });
    }

    private static void createProject(List<Long> ids, Long logoId) {
        //ids: 4
        String url = "http://www.xiaoxiekeji.cn:8002/project/createProject";
        Map<String, String> map = new HashMap<>();
        map.put("token", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxMjM0NTYiLCJpYXQiOjE1MzMxOTcxNzQsImV4cCI6OTIyMzM2NTkwNDA2NjA4MH0.JJaBasuqJc8_u8p7z3LfkbK7Ev5dUARDmupBqRtTZDo");

        Project project = new Project();
        project.setName("vada_1");
        project.setLogoPath("www.google.com");
        project.setRate_type(2);
        project.setDuration(25000);
        List<Project.CommodityCategory> cates = new ArrayList<>();
        Project.CommodityCategory pcc1 = new Project.CommodityCategory();
        pcc1.setName("category_1");
        pcc1.setCid(System.currentTimeMillis());
        pcc1.setFid(1L);
        cates.add(pcc1);

        Project.CommodityCategory pcc12 = new Project.CommodityCategory();
        pcc12.setName("category_2");
        pcc12.setCid(System.currentTimeMillis());
        pcc12.setFid(1L);
        cates.add(pcc12);
        project.setCategories(cates);

        /*if(ids == null) {
            ids = new ArrayList<>();
            ids.add(57L);
            ids.add(58L);
        }*/
        project.setPublicMediaIds(ids);
        if (logoId != null) {
            project.setLogoId(logoId);
        }

        // System.out.println(new Gson().toJson(project));
        FormBody body = new FormBody.Builder()
                .add("project", new Gson().toJson(project))
                // .add("makeWorks", "true")
                .build();
        OkHttpHelper.post(url, map, body, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ResponseBody body = response.body();
                Logger.i(TAG, "createProject", body != null ? body.string() : "Null");
                response.close();
            }
        });
    }


}
