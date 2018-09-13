package com.heaven7.test_okh;

import com.google.gson.Gson;
import com.heaven7.java.visitor.ResultVisitor;
import com.heaven7.java.visitor.collection.VisitServices;
import okhttp3.FormBody;

import java.util.*;

/**
 * @author heaven7
 */
public class LocalApiTest2 {

    private static final String TAG = "LocalApiTest2";
    public static final String DOMAIN = "127.0.0.1";

    public static void main(String[] args) {
        // addMediaInfo2Db();
        testMakeWorks();
    }

    public static void testMakeWorks(){
       // String md5sss = VisitServices.from(Arrays.asList(md5s)).joinToString(",");
        String md5sss = "f66bdb29a3ca58f6464c009468143854,289eeef1069a5221764c59adac4b9c93," +
                "d2f1414918e86b325e3d4a8c1fdd4323,a4539fda91ada6c7775234987d53eb93," +
                "4a58657c0dd5150e8369528b29685c84,f59d59623c1d825dfa7fa6d188a05204," +
                "4a768d24ab1285be606d8b4221bf7825,dd3e2965dd11094cd07784b8e65fa967," +
                "7850ac3fa18c8a9fba582d034f4a21f6,22052e35afe0c473b398dad92e4ddaf3," +
                "471d429dd2d526ca754f4300d3dcb3fb,62de35d56ed8a2f5c3115c145e3a754a," +
                "e61cf3c12cce2967adfd3aeae08cdc67,2875b6543524e7c8aa2a70052392f3ea," +
                "1143a502ab4599f8d5a52dfedda2f422,8a6408ee04d827fd201be016ec83baf3," +
                "1edca1955c7224b25ea46f1af6c27b4,a9c576701f269e39dc7ec1331bfc15e7," +
                "c77ef2beca21a7a8125fd877dbe3995d,b1d0aa8ee56801da9389972106230a64," +
                "93c8e3871aac0d78cc6b1378982b7bfd,d1f7a3c4570325726a99820b3dfd75dd," +
                "f3dc9a0a3b37eeb09b5613016b18894c,dce77a3251b8963e36f4f6ec120bb0b," +
                "4fbe909dc767b6dc9849ac44e779f13d,27920aa8459be55d564ecd2729cddd12,151df75c537c28cccfec7de077771c72";
        List<String> list = Arrays.asList(md5sss.split(","));
        List<Project.MediaInfo> mediaInfos = VisitServices.from(list).map(new ResultVisitor<String, Project.MediaInfo>() {
            @Override
            public Project.MediaInfo visit(String s, Object param) {
                Project.MediaInfo info = new Project.MediaInfo();
                info.setFile_md5(s);
                return info;
            }
        }).getAsList();

        Project project = new Project();
        project.setId(1L);
        project.setName("vada_1");
        project.setLogoPath("www.google.com");
        project.setRate_type(2);
        project.setDuration(25000);

        Project.CommodityCategory pcc1 = new Project.CommodityCategory();
        pcc1.setName("category_1");
        pcc1.setCid(123L);
        pcc1.setFid(1L);
        Project.CommodityCategory pcc12 = new Project.CommodityCategory();
        pcc12.setName("category_2");
        pcc12.setCid(456L);
        pcc12.setFid(1L);
        project.setCategory(pcc1);
        project.setCategorySec(pcc12);
        project.setMedia_infos(mediaInfos);

        //build param
        String url = "http://" + DOMAIN + ":8004/media/makeWorks";
        Map<String, String> map = new HashMap<>();
        map.put("token", TestHelper.TEST_TOKEN);

        FormBody body = new FormBody.Builder()
                .add("project", new Gson().toJson(project))
                .build();
        OkHttpHelper.post(url, map, body, new LogCallback("testMakeWorks"));
    }

    public static void addMediaInfo2Db(){
        String url = "http://" + DOMAIN + ":8004/media/test/addMediaInfos";
        Map<String, String> map = new HashMap<>();
        map.put("token", TestHelper.TEST_TOKEN);

        FormBody body = new FormBody.Builder()
                .add("media_dir", "D:\\Users\\Administrator\\AppData\\Local\\Temp\\media_files\\resource")
                .build();
        OkHttpHelper.post(url, map, body, new LogCallback("testMakeWorks"));
    }
}
