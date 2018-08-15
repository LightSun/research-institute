package com.heaven7.ve.colorgap;

import com.heaven7.java.base.anno.Nullable;
import com.heaven7.java.base.util.Predicates;
import com.heaven7.java.base.util.Throwables;
import com.heaven7.java.visitor.StartEndVisitor;
import com.heaven7.java.visitor.collection.VisitServices;
import com.heaven7.utils.*;
import com.heaven7.ve.MediaResourceItem;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * the resource initializer of cut music / cut video .
 * Created by heaven7 on 2018/4/17 0017.
 */

public class ResourceInitializer {

    public static final String VOCABULARY_FILE_NAME = "vocabulary.csv";

    /**
     * init resource
     */
    public static void init(Context context) {
       // MockHelper.init(context);
        //Vocabulary.loadVocabulary(context, "table/" + VOCABULARY_FILE_NAME);
       //Kingdom.loadVocabulary(context, "table/" + VOCABULARY_FILE_NAME);
        throw new UnsupportedOperationException();
    }

    public static String getFilePathOfRects(MediaResourceItem item, String srcDir) {
        return MockHelper.getFilePathOfRects(item, srcDir);
    }

    public static String getFilePathOfTags(MediaResourceItem item, String srcDir) {
        return MockHelper.getFilePathOfTags(item, srcDir);
    }

    /**
     * get image file name prefix
     */
    public static <T extends CutItemDelegate> String getImagesFileNamePrefix(List<T> items) {
        StringBuilder sb = new StringBuilder();
        VisitServices.from(items).fireWithStartEnd(new StartEndVisitor<T>() {
            @Override
            public boolean visit(Object param, T delegate, boolean start, boolean end) {
                sb.append(delegate.getItem().getFilePath().hashCode());
                if (!end) {
                    sb.append("_");
                }
                return false;
            }
        });
        return sb.toString().hashCode() + "";
    }

    public static String getFilePathOTagsForImageItem(MediaResourceItem item, String srcDir, @Nullable String prefix) {
        return MockHelper.getFilePathOTagsForImageItem(item, srcDir, prefix);
    }

    public static String getFilePathOfRectsForImageItem(MediaResourceItem item, String srcDir, @Nullable String prefix) {
        return MockHelper.getFilePathOfRectsForImageItem(item, srcDir, prefix);
    }

    private static class MockHelper {

        private static final String IMAGE_MARK = "#@!___image___!@#";
        private static Map<String, String> sMap = new HashMap<>();
        private static Map<String, String> sTagDirMap = new HashMap<>();
        private static Map<String, String> sImageTagDirMap = new HashMap<>();
        private static boolean inited;

        public static synchronized void init(Context context) {
            if (!inited) {
                inited = true;
                Properties prop = ConfigUtil.loadResources("table/wedding_map.properties");
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
                        //Logger.d("MockHelper", "init", "key = " + key + " ,value = " + str);
                    }
                }
                //load tag dir prop
                prop = ConfigUtil.loadResources("table/tag_map.properties");
                for (Map.Entry<Object, Object> en : prop.entrySet()) {
                    sTagDirMap.put(en.getKey().toString(), en.getValue().toString());
                }
                //load image tag dir prop
                prop = ConfigUtil.loadResources("table/image_tag_map.properties");
                for (Map.Entry<Object, Object> en : prop.entrySet()) {
                    sImageTagDirMap.put(CommonUtils.urlDecode(en.getKey().toString()), en.getValue().toString());
                }
            }
        }
        public static String getFilePathOTagsForImageInBatch(MediaResourceItem item, String srcDir) {

            final String filePath = item.getFilePath();
            String preDir1 = FileUtils.getFileDir(filePath, 1, false);
            String preDir2 = FileUtils.getFileDir(filePath, 2, false);

            // String key = FileUtils.encodeChinesePath(srcDir);
            String tagDir = sImageTagDirMap.get(srcDir);
            Throwables.checkNull(tagDir);
            //.../datas/tags/$IMAGE_MARK/..[ raw dirs]../xxx_predictions.csv
            //.../datas/tags/$IMAGE_MARK/..[ raw dirs]../tfs_config.txt
            String tfs_config = tagDir + File.separator + "tags" + File.separator + IMAGE_MARK
                    + File.separator + preDir2 + File.separator + preDir1 + File.separator
                    + "tfs_config.txt";

            return null;
        }

        public static String getFilePathOTagsForImageItem(MediaResourceItem item, String srcDir, @Nullable String prefix) {

            final String filePath = item.getFilePath();
            String preDir1 = FileUtils.getFileDir(filePath, 1, false);
            String preDir2 = FileUtils.getFileDir(filePath, 2, false);

            // String key = FileUtils.encodeChinesePath(srcDir);
            String tagDir = sImageTagDirMap.get(srcDir);
            Throwables.checkNull(tagDir);
            //.../datas/rects/IMAGE_MARK/
            String targetDir;
            if (TextUtils.isEmpty(prefix)) {
                //.../datas/tags/$IMAGE_MARK/..[ raw dirs]../$filename_predictions.csv
                targetDir = tagDir + File.separator + "tags" + File.separator + IMAGE_MARK
                        + File.separator + preDir2 + File.separator + preDir1 + File.separator
                        + FileUtils.getFileName(filePath) + "_predictions.csv";
            } else {
                //.../datas/tags/$IMAGE_MARK/$prefix_rects.csv
                targetDir = tagDir + File.separator + "tags" + File.separator + IMAGE_MARK
                        + File.separator + prefix + "_predictions.csv";
            }
            return new File(targetDir).exists() ? targetDir : null;
        }

        public static String getFilePathOfRectsForImageItem(MediaResourceItem item, String srcDir, @Nullable String prefix) {
            final String filePath = item.getFilePath();
            String preDir1 = FileUtils.getFileDir(filePath, 1, false);
            String preDir2 = FileUtils.getFileDir(filePath, 2, false);

            //String key = FileUtils.encodeChinesePath(srcDir);
            String tagDir = sImageTagDirMap.get(srcDir);
            Throwables.checkNull(tagDir);
            //.../datas/rects/IMAGE_MARK/
            String targetDir;
            System.out.println("srcDir = " + srcDir + " ,prefix = " + prefix);
            if (TextUtils.isEmpty(prefix)) {
                //.../datas/rects/$IMAGE_MARK/..[ raw dirs]../$filename_rects.csv
                targetDir = tagDir + File.separator + "rects" + File.separator + IMAGE_MARK
                        + File.separator + preDir2 + File.separator + preDir1 + File.separator
                        + FileUtils.getFileName(filePath) + "_rects.csv";
            } else {
                //.../datas/rects/$IMAGE_MARK/$prefix_rects.csv
                targetDir = tagDir + File.separator + "rects" + File.separator + IMAGE_MARK
                        + File.separator + prefix + "_rects.csv";
            }
            return new File(targetDir).exists() ? targetDir : null;
        }

        //srcDir 视频文件目录。 不包含'rects'
        public static String getFilePathOfRects(MediaResourceItem item, String srcDir) {
            String filePath = item.getFilePath();
            int index = filePath.lastIndexOf("/");
            if (index == -1) {
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
            if (index == -1) {
                index = postPath.indexOf("\\");
            }
            return tagDir + "/" + postPath.substring(0, index) + "/rects/" + realName + "_rects.csv";
        }

        // get file path of video item
        public static String getFilePathOfTags(MediaResourceItem item, String srcDir) {
            String filePath = item.getFilePath();
            int index = filePath.lastIndexOf("/");
            if (index == -1) {
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
            if (index == -1) {
                index = postPath.indexOf("\\");
            }

            return tagDir + "/" + postPath.substring(0, index) + "/tags/" + realName + "_predictions.csv";
        }
    }

    public static abstract class BatchLoader{

        public void load(String dir){
            String dataDir = getDataDir(dir);
            File tfs_config = new File(dataDir, "tfs_config.txt");
            TextReadHelper<TfsLine> reader = new TextReadHelper<>(new TextReadHelper.Callback<TfsLine>() {
                @Override
                public BufferedReader open(Context context, String url) throws IOException {
                    return new BufferedReader(new FileReader(url));
                }
                @Override
                public TfsLine parse(String line) {
                    String[] strs = line.split(",");
                    if(strs.length == 1){
                        return null;
                    }
                    String[] files = strs[1].split(" ");
                    if(Predicates.isEmpty(files)){
                        return null;
                    }
                    return new TfsLine(strs[0], Arrays.asList(files));
                }
            });
            List<TfsLine> tfsLines = reader.read(null, tfs_config.getAbsolutePath());
        }
        protected abstract String getDataDir(String mediaDir);
    }

    public static class TfsLine{
        final String tagFilename;
        /** relative files of tf record */
        final List<String> files;

        public TfsLine(String tfrecordName, List<String> files) {
            this.tagFilename = convert(tfrecordName);
            this.files = files;
        }
        private String convert(String tfrecordName){
            String fileName = FileUtils.getFileName(tfrecordName);
            if(fileName.endsWith("_output") || fileName.endsWith("_outputs")){
                String prefix = fileName.substring(0, fileName.lastIndexOf("_"));
                return FileUtils.getFileDir(tfrecordName, 1, true)
                        + File.separator + prefix + "_predictions.csv";
            }
            throw new IllegalStateException("file format is incorrect, check the generator of tfrecord.");
        }
    }
}
