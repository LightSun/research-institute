package com.heaven7.test_okh;

import com.google.gson.Gson;
import com.heaven7.core.util.Logger;
import com.heaven7.java.visitor.ResultVisitor;
import com.heaven7.java.visitor.collection.VisitServices;
import com.heaven7.utils.FileUtils;
import okhttp3.*;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 测试ai-server: 制作等接口
 * @author heaven7
 */
public class TestUploadStoreLine {

    private static final String TAG = "TestUploadStoreLine";

    //ffmpeg -i "F:\videos\story4\storyTest\C0218.MP4" -r 30 -b:a 32k "F:\videos\story4\storyTest\C0218_new.MP4"
    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("jvm shut-down.....");
            }
        }));
        // testUpload2();
        //  requestGenShotInfo();
        //  requestMovePublicRes();
        requestMakeWorks();
    }

    public static void requestMakeWorks() {
        requestTest0("makeWorks", "requestMakeWorks");
    }

    public static void requestMovePublicRes() {
        requestTest0("movePublicMediaRes", "requestMovePublicRes");
    }

    public static void requestGenShotInfo() {
        requestTest0("genShotInfo", "requestGenShotInfo");
    }

    public static void requestGenAiInfo() {
        requestTest0("genAiInfos", "requestGenAiInfo");
    }

    private static void requestTest0(String path, String mTag) {
        final String url = "http://www.xiaoxiekeji.cn:8004/media/test/" + path;
        Map<String, String> map = new HashMap<>();
        map.put("token", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxMjM0NTYiLCJpYXQiOjE1MzMxOTcxNzQsImV4cCI6OTIyMzM2NTkwNDA2NjA4MH0.JJaBasuqJc8_u8p7z3LfkbK7Ev5dUARDmupBqRtTZDo");
        OkHttpHelper.post(url, map, new FormBody.Builder()
                        .add("pub_media_infos", TestHelper.JSON)
                        .add("project_id", "23")
                        .build(),
                new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Logger.w(TAG, mTag, Logger.toString(e));
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        ResponseBody body = response.body();
                        if (body == null) {
                            response.close();
                            return;
                        }
                        String json = body.string();
                        Logger.i(TAG, mTag, json != null ? json : "Null");
                        response.close();
                    }
                });
    }

    private static void testUpload() {
        final String dir = "F:\\videos\\story4\\storyTest";
        List<String> files = FileUtils.getFiles(new File(dir), "mp4");
        new UploadHelper(files).run();
    }

    private static void testUpload2() {
        String[] files = {
                "F:\\videos\\story4\\storyTest\\C0218_new.MP4"
        };
        new UploadHelper(Arrays.asList(files)).run();
    }

    private static class UploadHelper implements Runnable {
        private final List<String> videos;
        private final AtomicInteger mIndex;
        private final List<PublicMediaInfo> infos = new ArrayList<>();

        public UploadHelper(List<String> videos) {
            this.videos = videos;
            this.mIndex = new AtomicInteger(0);
        }

        @Override
        public void run() {
            int index = mIndex.getAndIncrement();
            System.out.println("start index = " + index);
            if (index >= videos.size()) {
                String ids = VisitServices.from(infos).map(new ResultVisitor<PublicMediaInfo, Long>() {
                    @Override
                    public Long visit(PublicMediaInfo publicMediaInfo, Object param) {
                        return publicMediaInfo.getId();
                    }
                }).asListService().joinToString(",");
                FileUtils.writeTo("F:\\videos\\story4\\storyTest\\public_infos.txt", new Gson().toJson(infos));
                FileUtils.writeTo("F:\\videos\\story4\\storyTest\\public_info_ids.txt", ids);
            } else {
                start(videos.get(index));
            }
        }

        void start(String video) {
            System.out.println(System.currentTimeMillis() + " ,video = " + video);
            final String url = "http://www.xiaoxiekeji.cn:8004/media/upload";
            Map<String, String> map = new HashMap<>();
            map.put("token", "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxMjM0NTYiLCJpYXQiOjE1MzMxOTcxNzQsImV4cCI6OTIyMzM2NTkwNDA2NjA4MH0.JJaBasuqJc8_u8p7z3LfkbK7Ev5dUARDmupBqRtTZDo");
            OkHttpHelper.post(url, map, TestAiUploadFlow.getRequestBody(Arrays.asList(video)),
                    new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Logger.w(TAG, "testUpload", Logger.toString(e));
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            ResponseBody body = response.body();
                            if (body == null) {
                                System.err.println("wrong body for request . video = " + video);
                                response.close();
                                return;
                            }
                            String json = body.string();
                            Logger.i(TAG, "testUpload", json != null ? json : "Null");
                            response.close();
                            PublicMediaResponse res = toPublicMediaResponse(json);
                            if (res.getCode() != 0) {
                                System.err.println("upload failed for video (" + video + "). code = " + res.getCode() + " ,msg = " + res.getMessage());
                                run();
                            } else {
                                infos.addAll(res.getData().getPublicMediaInfos());
                                run();
                            }
                        }
                    });
        }
    }

    private static PublicMediaResponse toPublicMediaResponse(String json) {
        return new Gson().fromJson(json, PublicMediaResponse.class);
    }


}
