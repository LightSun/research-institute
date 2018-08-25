package com.heaven7.test_okh;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.heaven7.java.visitor.ResultVisitor;
import com.heaven7.java.visitor.collection.VisitServices;
import okhttp3.*;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.heaven7.test_okh.OkHttpHelper.getMimeType;

/**
 * @author heaven7
 */
public class TestHelper {

    static final String JSON =
            "[" +
            "{\"id\":135,\"savePath\":\"media_files\\\\e10adc3949ba59abbe56e057f20f883e\\\\unknown\\\\resource\\\\1533955748046.mp4\",\"filename\":\"C0013.MP4\"}\n" +
            ",{\"id\":137,\"savePath\":\"media_files\\\\e10adc3949ba59abbe56e057f20f883e\\\\unknown\\\\resource\\\\1533955831402.mp4\",\"filename\":\"C0013.MP4\"}\n" +
            ",{\"id\":140,\"savePath\":\"media_files\\\\e10adc3949ba59abbe56e057f20f883e\\\\unknown\\\\resource\\\\1533955894965.mp4\",\"filename\":\"C0013.MP4\"}\n" +
            ",{\"id\":142,\"savePath\":\"media_files\\\\e10adc3949ba59abbe56e057f20f883e\\\\unknown\\\\resource\\\\1533956004815.mp4\",\"filename\":\"C0013.MP4\"}\n" +
            ",{\"id\":145,\"savePath\":\"media_files\\\\e10adc3949ba59abbe56e057f20f883e\\\\unknown\\\\resource\\\\1533956103979.mp4\",\"filename\":\"C0013.MP4\"}\n" +
            ",{\"id\":147,\"savePath\":\"media_files\\\\e10adc3949ba59abbe56e057f20f883e\\\\unknown\\\\resource\\\\1533956158232.mp4\",\"filename\":\"C0013.MP4\"}\n" +
            ",{\"id\":150,\"savePath\":\"media_files\\\\e10adc3949ba59abbe56e057f20f883e\\\\unknown\\\\resource\\\\1533956315008.mp4\",\"filename\":\"C0013.MP4\"}\n" +
            ",{\"id\":154,\"savePath\":\"media_files\\\\e10adc3949ba59abbe56e057f20f883e\\\\unknown\\\\resource\\\\1533956346907.mp4\",\"filename\":\"C0013.MP4\"}\n" +
            ",{\"id\":156,\"savePath\":\"media_files\\\\e10adc3949ba59abbe56e057f20f883e\\\\unknown\\\\resource\\\\1533956376072.mp4\",\"filename\":\"C0013.MP4\"}\n" +
            ",{\"id\":164,\"savePath\":\"media_files\\\\e10adc3949ba59abbe56e057f20f883e\\\\unknown\\\\resource\\\\1533956509556.mp4\",\"filename\":\"C0013.MP4\"}\n" +
            ",{\"id\":168,\"savePath\":\"media_files\\\\e10adc3949ba59abbe56e057f20f883e\\\\unknown\\\\resource\\\\1533956594271.mp4\",\"filename\":\"C0013.MP4\"}\n" +
            ",{\"id\":176,\"savePath\":\"media_files\\\\e10adc3949ba59abbe56e057f20f883e\\\\unknown\\\\resource\\\\1533956712118.mp4\",\"filename\":\"C0013.MP4\"}\n" +
            ",{\"id\":178,\"savePath\":\"media_files\\\\e10adc3949ba59abbe56e057f20f883e\\\\unknown\\\\resource\\\\1533956754593.mp4\",\"filename\":\"C0013.MP4\"}\n" +
            ",{\"id\":181,\"savePath\":\"media_files\\\\e10adc3949ba59abbe56e057f20f883e\\\\unknown\\\\resource\\\\1533956778072.mp4\",\"filename\":\"C0013.MP4\"}\n" +
            ",{\"id\":184,\"savePath\":\"media_files\\\\e10adc3949ba59abbe56e057f20f883e\\\\unknown\\\\resource\\\\1533956819297.mp4\",\"filename\":\"C0013.MP4\"}\n" +
            ",{\"id\":189,\"savePath\":\"media_files\\\\e10adc3949ba59abbe56e057f20f883e\\\\unknown\\\\resource\\\\1533956865114.mp4\",\"filename\":\"C0013.MP4\"}\n" +
            ",{\"id\":190,\"savePath\":\"media_files\\\\e10adc3949ba59abbe56e057f20f883e\\\\unknown\\\\resource\\\\1533956894154.mp4\",\"filename\":\"C0013.MP4\"}\n" +
            ",{\"id\":193,\"savePath\":\"media_files\\\\e10adc3949ba59abbe56e057f20f883e\\\\unknown\\\\resource\\\\1533956939269.mp4\",\"filename\":\"C0013.MP4\"}\n" +
            ",{\"id\":199,\"savePath\":\"media_files\\\\e10adc3949ba59abbe56e057f20f883e\\\\unknown\\\\resource\\\\1533956980478.mp4\",\"filename\":\"C0013.MP4\"}\n" +
            ",{\"id\":200,\"savePath\":\"media_files\\\\e10adc3949ba59abbe56e057f20f883e\\\\unknown\\\\resource\\\\1533957093920.mp4\",\"filename\":\"C0013.MP4\"}\n" +
            ",{\"id\":201,\"savePath\":\"media_files\\\\e10adc3949ba59abbe56e057f20f883e\\\\unknown\\\\resource\\\\1533957313697.mp4\",\"filename\":\"C0013.MP4\"}\n" +
            ",{\"id\":202,\"savePath\":\"media_files\\\\e10adc3949ba59abbe56e057f20f883e\\\\unknown\\\\resource\\\\1533957462553.mp4\",\"filename\":\"C0013.MP4\"}\n" +
            ",{\"id\":203,\"savePath\":\"media_files\\\\e10adc3949ba59abbe56e057f20f883e\\\\unknown\\\\resource\\\\1533957572965.mp4\",\"filename\":\"C0013.MP4\"}\n" +
            ",{\"id\":204,\"savePath\":\"media_files\\\\e10adc3949ba59abbe56e057f20f883e\\\\unknown\\\\resource\\\\1533957610675.mp4\",\"filename\":\"C0013.MP4\"}\n" +
            ",{\"id\":205,\"savePath\":\"media_files\\\\e10adc3949ba59abbe56e057f20f883e\\\\unknown\\\\resource\\\\1533957687125.mp4\",\"filename\":\"C0013.MP4\"}\n" +
            ",{\"id\":206,\"savePath\":\"media_files\\\\e10adc3949ba59abbe56e057f20f883e\\\\unknown\\\\resource\\\\1533958063007.mp4\",\"filename\":\"C0013.MP4\"}\n" +
            ",{\"id\":207,\"savePath\":\"media_files\\\\e10adc3949ba59abbe56e057f20f883e\\\\unknown\\\\resource\\\\1533958114541.mp4\",\"filename\":\"C0013.MP4\"}\n" +
            ",{\"id\":208,\"savePath\":\"media_files\\\\e10adc3949ba59abbe56e057f20f883e\\\\unknown\\\\resource\\\\1533958554470.mp4\",\"filename\":\"C0013.MP4\"}\n" +
            ",{\"id\":209,\"savePath\":\"media_files\\\\e10adc3949ba59abbe56e057f20f883e\\\\unknown\\\\resource\\\\1533958623641.mp4\",\"filename\":\"C0013.MP4\"}\n" +
            ",{\"id\":210,\"savePath\":\"media_files\\\\e10adc3949ba59abbe56e057f20f883e\\\\unknown\\\\resource\\\\1533958685245.mp4\",\"filename\":\"C0013.MP4\"}\n" +
            ",{\"id\":211,\"savePath\":\"media_files\\\\e10adc3949ba59abbe56e057f20f883e\\\\unknown\\\\resource\\\\1533958713347.mp4\",\"filename\":\"C0013.MP4\"}\n" +
            ",{\"id\":212,\"savePath\":\"media_files\\\\e10adc3949ba59abbe56e057f20f883e\\\\unknown\\\\resource\\\\1533958864353.mp4\",\"filename\":\"C0013.MP4\"}\n" +
            ",{\"id\":213,\"savePath\":\"media_files\\\\e10adc3949ba59abbe56e057f20f883e\\\\unknown\\\\resource\\\\1533959399714.mp4\",\"filename\":\"C0013.MP4\"}\n" +
            ",{\"id\":218,\"savePath\":\"media_files\\\\e10adc3949ba59abbe56e057f20f883e\\\\unknown\\\\resource\\\\1533959560864.mp4\",\"filename\":\"C0013.MP4\"}\n" +
            ",{\"id\":220,\"savePath\":\"media_files\\\\e10adc3949ba59abbe56e057f20f883e\\\\unknown\\\\resource\\\\1533959620085.mp4\",\"filename\":\"C0013.MP4\"}\n" +
            ",{\"id\":221,\"savePath\":\"media_files\\\\e10adc3949ba59abbe56e057f20f883e\\\\unknown\\\\resource\\\\1533959656826.mp4\",\"filename\":\"C0013.MP4\"}\n" +
            ",{\"id\":224,\"savePath\":\"media_files\\\\e10adc3949ba59abbe56e057f20f883e\\\\unknown\\\\resource\\\\1533959751898.mp4\",\"filename\":\"C0013.MP4\"}\n" +
            ",{\"id\":226,\"savePath\":\"media_files\\\\e10adc3949ba59abbe56e057f20f883e\\\\unknown\\\\resource\\\\1533959848360.mp4\",\"filename\":\"C0013.MP4\"}\n" +
            ",{\"id\":232,\"savePath\":\"media_files\\\\e10adc3949ba59abbe56e057f20f883e\\\\unknown\\\\resource\\\\1533960130294.mp4\",\"filename\":\"C0013.MP4\"}\n" +
            ",{\"id\":233,\"savePath\":\"media_files\\\\e10adc3949ba59abbe56e057f20f883e\\\\unknown\\\\resource\\\\1533960187091.mp4\",\"filename\":\"C0013.MP4\"}\n" +
            ",{\"id\":246,\"savePath\":\"media_files\\\\e10adc3949ba59abbe56e057f20f883e\\\\unknown\\\\resource\\\\1533960489838.mp4\",\"filename\":\"C0013.MP4\"}\n" +
            ",{\"id\":251,\"savePath\":\"media_files\\\\e10adc3949ba59abbe56e057f20f883e\\\\unknown\\\\resource\\\\1533960726018.mp4\",\"filename\":\"C0013.MP4\"}\n" +
            ",{\"id\":252,\"savePath\":\"media_files\\\\e10adc3949ba59abbe56e057f20f883e\\\\unknown\\\\resource\\\\1533960757698.mp4\",\"filename\":\"C0013.MP4\"}\n" +
            ",{\"id\":253,\"savePath\":\"media_files\\\\e10adc3949ba59abbe56e057f20f883e\\\\unknown\\\\resource\\\\1533960808045.mp4\",\"filename\":\"C0013.MP4\"}\n" +
            ",{\"id\":262,\"savePath\":\"media_files\\\\e10adc3949ba59abbe56e057f20f883e\\\\unknown\\\\resource\\\\1533960863720.mp4\",\"filename\":\"C0013.MP4\"}\n" +
            ",{\"id\":265,\"savePath\":\"media_files\\\\e10adc3949ba59abbe56e057f20f883e\\\\unknown\\\\resource\\\\1533960923261.mp4\",\"filename\":\"C0013.MP4\"}\n" +
            ",{\"id\":266,\"savePath\":\"media_files\\\\e10adc3949ba59abbe56e057f20f883e\\\\unknown\\\\resource\\\\1533961043421.mp4\",\"filename\":\"C0013.MP4\"}\n" +
            ",{\"id\":267,\"savePath\":\"media_files\\\\e10adc3949ba59abbe56e057f20f883e\\\\unknown\\\\resource\\\\1533961090691.mp4\",\"filename\":\"C0013.MP4\"}\n" +
            ",{\"id\":268,\"savePath\":\"media_files\\\\e10adc3949ba59abbe56e057f20f883e\\\\unknown\\\\resource\\\\1533961312811.mp4\",\"filename\":\"C0013.MP4\"}\n" +
            ",{\"id\":269,\"savePath\":\"media_files\\\\e10adc3949ba59abbe56e057f20f883e\\\\unknown\\\\resource\\\\1533961380217.mp4\",\"filename\":\"C0013.MP4\"}\n" +
            ",{\"id\":270,\"savePath\":\"media_files\\\\e10adc3949ba59abbe56e057f20f883e\\\\unknown\\\\resource\\\\1533961457496.mp4\",\"filename\":\"C0013.MP4\"}\n" +
            ",{\"id\":271,\"savePath\":\"media_files\\\\e10adc3949ba59abbe56e057f20f883e\\\\unknown\\\\resource\\\\1533961585201.mp4\",\"filename\":\"C0013.MP4\"}\n" +
            ",{\"id\":272,\"savePath\":\"media_files\\\\e10adc3949ba59abbe56e057f20f883e\\\\unknown\\\\resource\\\\1533961615037.mp4\",\"filename\":\"C0013.MP4\"}\n" +
            ",{\"id\":273,\"savePath\":\"media_files\\\\e10adc3949ba59abbe56e057f20f883e\\\\unknown\\\\resource\\\\1533961686193.mp4\",\"filename\":\"C0013.MP4\"}\n" +
            ",{\"id\":274,\"savePath\":\"media_files\\\\e10adc3949ba59abbe56e057f20f883e\\\\unknown\\\\resource\\\\1533961934291.mp4\",\"filename\":\"C0013.MP4\"}\n" +
            ",{\"id\":275,\"savePath\":\"media_files\\\\e10adc3949ba59abbe56e057f20f883e\\\\unknown\\\\resource\\\\1533961961285.mp4\",\"filename\":\"C0013.MP4\"}\n" +
            ",{\"id\":276,\"savePath\":\"media_files\\\\e10adc3949ba59abbe56e057f20f883e\\\\unknown\\\\resource\\\\1533962071244.mp4\",\"filename\":\"C0013.MP4\"}\n" +
            ",{\"id\":277,\"savePath\":\"media_files\\\\e10adc3949ba59abbe56e057f20f883e\\\\unknown\\\\resource\\\\1533962103486.mp4\",\"filename\":\"C0013.MP4\"}\n" +
            ",{\"id\":278,\"savePath\":\"media_files\\\\e10adc3949ba59abbe56e057f20f883e\\\\unknown\\\\resource\\\\1533962145445.mp4\",\"filename\":\"C0013.MP4\"}\n" +
            ",{\"id\":279,\"savePath\":\"media_files\\\\e10adc3949ba59abbe56e057f20f883e\\\\unknown\\\\resource\\\\1533962191840.mp4\",\"filename\":\"C0013.MP4\"}\n" +
            ",{\"id\":280,\"savePath\":\"media_files\\\\e10adc3949ba59abbe56e057f20f883e\\\\unknown\\\\resource\\\\1533962232581.mp4\",\"filename\":\"C0013.MP4\"}\n" +
            ",{\"id\":357,\"savePath\":\"media_files\\\\e10adc3949ba59abbe56e057f20f883e\\\\unknown\\\\resource\\\\1534139989794.mp4\",\"filename\":\"C0013.MP4\"}\n" +
            "]" ;

    static final String JSON_CREATE_PROJECT = "{\n" +
            "  \"publicMediaIds\": [\n" +
            "    382,\n" +
            "    383,\n" +
            "    384,\n" +
            "    385\n" +
            "  ],\n" +
            "  \"categories\": [\n" +
            "    {\n" +
            "      \"cid\": 1,\n" +
            "      \"name\": \"女装\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"cid\": 101,\n" +
            "      \"name\": \"甜美\"\n" +
            "    }\n" +
            "  ],\n" +
            "  \"commodity\": {\n" +
            "    \"series_type\": 0,\n" +
            "    \"type\": 1\n" +
            "  },\n" +
            "  \"name\": \"新创建测试1\",\n" +
            "  \"logoPath\":\"http://www.xiaoxiekeji.cn:8004/media/resourcesmedia_files\\\\c9f0f895fb98ab9159f51fd0297e236d\\\\simple_files\\\\1534320395795.jpg\",\n" +
            "  \"rate_type\": 2,\n" +
            "  \"duration\": 30000,\n" +
            "  \"logoId\": 8\n" +
            "}";

    public static final String TEST_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiIxMjM0NTYiLCJpYXQiOjE1MzMxOTcxNzQsImV4cCI6OTIyMzM2NTkwNDA2NjA4MH0.JJaBasuqJc8_u8p7z3LfkbK7Ev5dUARDmupBqRtTZDo";

    public static List<Long> getIds(){
        ArrayList<PublicMediaInfo> list = new Gson().fromJson(JSON, new TypeToken<ArrayList<PublicMediaInfo>>() {
        }.getType());
        return VisitServices.from(list).map(new ResultVisitor<PublicMediaInfo, Long>() {
            @Override
            public Long visit(PublicMediaInfo publicMediaInfo, Object param) {
                return publicMediaInfo.getId();
            }
        }).getAsList();
    }

    public static List<Project.MediaInfo> getMediaInfos(){
        String url = "http://www.xiaoxiekeji.cn:8004/media/test/getPublicMediaInfos";
        Map<String, String> map = new HashMap<>();
        map.put("token", TEST_TOKEN);
        try {
            final Response res = OkHttpHelper.postSync(url, map, new FormBody.Builder()
                    .add("count", "2")
                    .build());
            final ResponseBody body = res.body();
            if(body != null){
                final String json = body.string();
                res.close();
                final Type type = new TypeToken<ApiProtocol<ArrayList<Project.MediaInfo>>>() {
                }.getType();
                ApiProtocol<ArrayList<Project.MediaInfo>> ap =  new Gson().fromJson(json, type);
                return ap.getData();
            }else {
                res.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean addCategroy(List<Project.CommodityCategory> cates){
        String url = "http://www.xiaoxiekeji.cn:8002/test/project/addCategory";
        Map<String, String> map = new HashMap<>();
        map.put("token", TEST_TOKEN);
        try {
            final Response res = OkHttpHelper.postSync(url, map, new FormBody.Builder()
                    .add("categories", new Gson().toJson(cates))
                    .build());
            res.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean addCategroy(String domain, List<Project.CommodityCategory> cates){
        String url = "http://" + domain + ":8002/test/project/addCategory";
        Map<String, String> map = new HashMap<>();
        map.put("token", TEST_TOKEN);
        try {
            final Response res = OkHttpHelper.postSync(url, map, new FormBody.Builder()
                    .add("categories", new Gson().toJson(cates))
                    .build());
            res.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
