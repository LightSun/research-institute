package com.heaven7.ve.colorgap;

import com.heaven7.java.base.util.Throwables;
import com.heaven7.utils.ConfigUtil;
import com.heaven7.ve.Context;
import com.heaven7.ve.MediaResourceItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Properties;

import static com.heaven7.ve.colorgap.Vocabulary.VOCABULARY_FILE_NAME;

/**
 * the resource initializer of cut music / cut video .
 * Created by heaven7 on 2018/4/17 0017.
 */

public class ResourceInitializer {

    /** init resource */
    public static void init(Context context){
        MockVideoHelper.init(context);
        Vocabulary.loadVocabulary(context, "table/" + VOCABULARY_FILE_NAME);
    }

    public static String getFilePathOfRects(MediaResourceItem item, String srcDir){
        return MockVideoHelper.getFilePathOfRects(item, srcDir);
    }
    public static String getFilePathOfTags(MediaResourceItem item, String srcDir) {
        return MockVideoHelper.getFilePathOfTags(item, srcDir);
    }

    //todo just for test
    private static class MockVideoHelper {

        private static Map<String, String> sMap = new HashMap<>();
        private static Map<String, String> sTagDirMap = new HashMap<>();
        private static boolean inited;

        public static synchronized void init(Context context) {
            if (!inited) {
                inited = true;
                Properties prop = ConfigUtil.loadResources( "table/wedding_map.properties");
                for (Map.Entry<Object, Object> en : prop.entrySet()) {
                    //String key = en.getKey().toString();
                    String val = en.getValue().toString();
                    String[] fileName = val.split(",");
                    List<String> names = new ArrayList<>(Arrays.asList(fileName));

                    ListIterator<String> it = names.listIterator();
                    String fn;
                    for (; it.hasNext(); ) {
                        String str = fn = it.next();
                        if (str.contains(" ")) {
                            str = str.replace(" ", "_");
                        }
                        if (str.contains("(") && str.contains(")")) {
                            str = str.replace("(", "");
                            str = str.replace(")", "");
                        }
                        sMap.put(fn, str);
                        //Logger.d("MockVideoHelper", "init", "key = " + key + " ,value = " + str);
                    }
                }

                prop = ConfigUtil.loadResources("table/tag_map.properties");
                for (Map.Entry<Object, Object> en : prop.entrySet()) {
                    sTagDirMap.put(en.getKey().toString(), en.getValue().toString());
                }
            }
        }

        //srcDir 视频文件目录。 不包含'rects'
        public static String getFilePathOfRects(MediaResourceItem item, String srcDir) {
            String filePath = item.getFilePath();
            int index = filePath.lastIndexOf("/");
            if(index == -1){
                index = filePath.lastIndexOf("\\");
            }
            String name = filePath.substring(index + 1, filePath.lastIndexOf("."));
            String realName = sMap.get(name);
            Throwables.checkNull(realName);
            //get tag dir
            String tagDir = sTagDirMap.get(srcDir);
            Throwables.checkNull(tagDir);

            //F:\videos\wedding - > F:\\test\\data
            index = filePath.indexOf(srcDir);
            String postPath = filePath.substring(index + srcDir.length() + 1);
            index = postPath.indexOf("/");
            if(index == -1){
                index = postPath.indexOf("\\");
            }
            return tagDir + "/" + postPath.substring(0, index) + "/rects/" + realName + "_rects.csv";
        }

        public static String getFilePathOfTags(MediaResourceItem item, String srcDir) {
            String filePath = item.getFilePath();
            int index = filePath.lastIndexOf("/");
            if(index == -1){
                index = filePath.lastIndexOf("\\");
            }
            String name = filePath.substring(index + 1, filePath.lastIndexOf("."));
            String realName = sMap.get(name);
            Throwables.checkNull(realName);

            //get tag dir
            String tagDir = sTagDirMap.get(srcDir);
            Throwables.checkNull(tagDir);

            //F:\videos\wedding - > F:\\test\\data
            index = filePath.indexOf(srcDir);
            String postPath = filePath.substring(index + srcDir.length() + 1);
            index = postPath.indexOf("/");
            if(index == -1){
                index = postPath.indexOf("\\");
            }

            return tagDir + "/" + postPath.substring(0, index) + "/tags/" + realName + "_predictions.csv";
        }

    }
}
