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
public class LocalAiApiTest {

    private static final String TAG = "LocalAiApiTest";
    public static final String DOMAIN = "127.0.0.1";

    public static void main(String[] args) {
        testUpload();
       /* String[] md5s = {
                "6f3714b84d7c0a329005d990df2fb8a2",
        };
         createProjectWithMd5s(md5s);*/
       // updateProject(12L, md5sToMediaInfo(md5s));
    }
    private static void createProjectWithMd5s(String[] md5s){
        List<Project.MediaInfo> list = VisitServices.from(Arrays.asList(md5s)).map(new ResultVisitor<String, Project.MediaInfo>() {
            @Override
            public Project.MediaInfo visit(String s, Object param) {
                Project.MediaInfo info = new Project.MediaInfo();
                info.setFile_md5(s);
                return info;
            }
        }).getAsList();
        createProject(list);
    }

    public static void testUpload() {
        String url = "http://" + DOMAIN + ":8004/media/upload";
        String[] medias = {
                "F:\\videos\\tmp_store\\WeChat_20180816180201.mp4",
                //"F:\\videos\\tmp_store\\WeChat_20180816182503.mp4",
                // "E:\\BaiduNetdiskDownload\\taobao_service\\照片\\女装\\黑色阔腿裤\\1-2.jpg",
                // "E:\\BaiduNetdiskDownload\\taobao_service\\照片\\女装\\黑色阔腿裤\\1-4.jpg",

               // "F:\\videos\\story1\\dinner\\dinner_C0101.mp4",
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

    private static void createProject(List<Project.MediaInfo> mediaInfos) {
        //ids: 4
        String url = "http://" + DOMAIN + ":8002/project/createProject";
        Map<String, String> map = new HashMap<>();
        map.put("token", TestHelper.TEST_TOKEN);

        Project project = new Project();
        project.setName("vada_1");
        project.setLogoPath("www.google.com");
        project.setRate_type(2);
        project.setDuration(25000);

        project.setCategoryId(1);
        project.setCategorySecId(2);

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
       /* final boolean state = TestHelper.addCategroy(DOMAIN,cates);
        Logger.d(TAG, "createProject", "add category " + (state ? "success" : "failed"));*/

        project.setCategories(cates);
        if (!Predicates.isEmpty(mediaInfos)) {
            project.setMedia_infos(mediaInfos);
        }

        // System.out.println(new Gson().toJson(project));
        FormBody body = new FormBody.Builder()
                .add("project", new Gson().toJson(project))
                // .add("justLocalTest", sJustLocalTest + "")
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

    private static void updateProject(Long pid,List<Project.MediaInfo> mediaInfos) {
        //ids: 4
        String url = "http://" + DOMAIN + ":8002/project/createProject";
        Map<String, String> map = new HashMap<>();
        map.put("token", TestHelper.TEST_TOKEN);

        Project project = new Project();
        project.setId(pid);
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
        final boolean state = TestHelper.addCategroy(DOMAIN,cates);
        Logger.d(TAG, "createProject", "add category " + (state ? "success" : "failed"));

        project.setCategories(cates);
        if (!Predicates.isEmpty(mediaInfos)) {
            project.setMedia_infos(mediaInfos);
        }

        // System.out.println(new Gson().toJson(project));
        FormBody body = new FormBody.Builder()
                .add("project", new Gson().toJson(project))
                // .add("justLocalTest", sJustLocalTest + "")
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

    private static List<Project.MediaInfo> md5sToMediaInfo(String[] md5s){
        return VisitServices.from(Arrays.asList(md5s)).map(new ResultVisitor<String, Project.MediaInfo>() {
            @Override
            public Project.MediaInfo visit(String s, Object param) {
                Project.MediaInfo info = new Project.MediaInfo();
                info.setFile_md5(s);
                return info;
            }
        }).getAsList();
    }
}
