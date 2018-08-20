package com.heaven7.test_okh;

import com.google.gson.Gson;
import com.heaven7.core.util.Logger;
import com.heaven7.java.base.util.Predicates;
import com.heaven7.java.visitor.ResultVisitor;
import com.heaven7.java.visitor.collection.VisitServices;
import okhttp3.*;

import java.io.IOException;
import java.util.*;

import static com.heaven7.test_okh.TestAiUploadFlow.getRequestBody;

/**
 * @author heaven7
 */
public class TestProject1 {

    private static final String TAG = "TestProject1";
    private static final boolean sJustLocalTest = false;

    //uploaded res: 1534768008279.mp4, 1534768008248.mp4
    public static void main(String[] args) {
        //testUpload();
       /* String[] medias = {
                "media_files\\e10adc3949ba59abbe56e057f20f883e\\unknown\\resource\\1534768008279.mp4",
        };
        testProjectByMediaPaths(medias, true);*/

        final List<Project.MediaInfo> mediaInfos = TestHelper.getMediaInfos();
        if(Predicates.isEmpty(mediaInfos)){
            Logger.w(TAG, "main", "no mediaInfos");
            return;
        }
        createProject(mediaInfos);
      //  updateProject(2L, mediaInfos);
    }

    private static void testProjectByMediaPaths(String[] mediaPaths, boolean update) {
        List<Project.MediaInfo> mediaInfos = VisitServices.from(Arrays.asList(mediaPaths))
                .map(new ResultVisitor<String, Project.MediaInfo>() {
                    @Override
                    public Project.MediaInfo visit(String s, Object param) {
                        Project.MediaInfo info = new Project.MediaInfo();
                        info.setSavePath(s);
                        return info;
                    }
                }).getAsList();
        if (update) {
            updateProject(7L, mediaInfos);
        } else {
            createProject(mediaInfos);
        }
    }

    private static void testUpload() {
        String url = "http://www.xiaoxiekeji.cn:8004/media/upload";
        String[] medias = {
                "F:\\videos\\tmp_store\\WeChat_20180816180201.mp4",
                "F:\\videos\\tmp_store\\WeChat_20180816182503.mp4",
        };
        Map<String, String> map = new HashMap<>();
        map.put("token", TestHelper.TEST_TOKEN);

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
    }

    private static void updateProject(Long id, List<Project.MediaInfo> mediaInfos) {
        //ids: 4
        String url = "http://www.xiaoxiekeji.cn:8002/project/updateProject";
        // String url = "http://127.0.0.1:8002/project/updateProject";
        Map<String, String> map = new HashMap<>();
        map.put("token", TestHelper.TEST_TOKEN);

        Project project = new Project();
        project.setId(id);
        project.setName("vada_1");
        project.setLogoPath("www.google.com");
        project.setRate_type(2);
        project.setDuration(25000);

        List<Project.CommodityCategory> cates = new ArrayList<>();
        Project.CommodityCategory pcc1 = new Project.CommodityCategory();
        pcc1.setName("category_3");
        pcc1.setCid(123456L);
        pcc1.setFid(1L);
        cates.add(pcc1);
        final boolean state = TestHelper.addCategroy(cates);
        Logger.d(TAG, "createProject", "add category " + (state ? "success" : "failed"));

        project.setCategories(cates);
        if (!Predicates.isEmpty(mediaInfos)) {
            project.setMedia_infos(mediaInfos);
        }
        // System.out.println(new Gson().toJson(project));
        FormBody body = new FormBody.Builder()
                .add("project", new Gson().toJson(project))
                .add("justLocalTest", sJustLocalTest + "")
                .build();
        OkHttpHelper.put(url, map, body, new Callback() {
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

    private static void createProject(List<Project.MediaInfo> mediaInfos) {
        //ids: 4
        String url = "http://www.xiaoxiekeji.cn:8002/project/createProject";
        // String url = "http://127.0.0.1:8002/project/createProject";
        Map<String, String> map = new HashMap<>();
        map.put("token", TestHelper.TEST_TOKEN);

        Project project = new Project();
        project.setName("vada_1");
        project.setLogoPath("www.google.com");
        project.setRate_type(2);
        project.setDuration(25000);
        List<Project.CommodityCategory> cates = new ArrayList<>();
        Project.CommodityCategory pcc1 = new Project.CommodityCategory();
        pcc1.setName("category_1");
        pcc1.setCid(123L);
        pcc1.setFid(1L);
        cates.add(pcc1);

        Project.CommodityCategory pcc12 = new Project.CommodityCategory();
        pcc12.setName("category_2");
        pcc12.setCid(456L);
        pcc12.setFid(1L);
        cates.add(pcc12);
        final boolean state = TestHelper.addCategroy(cates);
        Logger.d(TAG, "createProject", "add category " + (state ? "success" : "failed"));

        project.setCategories(cates);
        if (!Predicates.isEmpty(mediaInfos)) {
            project.setMedia_infos(mediaInfos);
        }

        // System.out.println(new Gson().toJson(project));
        FormBody body = new FormBody.Builder()
                .add("project", new Gson().toJson(project))
                .add("justLocalTest", sJustLocalTest + "")
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
