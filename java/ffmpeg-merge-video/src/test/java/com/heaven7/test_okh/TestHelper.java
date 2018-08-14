package com.heaven7.test_okh;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.heaven7.java.visitor.ResultVisitor;
import com.heaven7.java.visitor.collection.VisitServices;

import java.util.ArrayList;
import java.util.List;

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
}
