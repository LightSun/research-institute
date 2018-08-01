package com.heaven7.ve.colorgap;


import com.heaven7.core.util.Logger;
import com.heaven7.java.visitor.PredicateVisitor;
import com.heaven7.java.visitor.collection.VisitServices;
import com.heaven7.utils.LoadException;
import com.heaven7.utils.TextReadHelper;
import com.heaven7.utils.TextUtils;
import com.heaven7.ve.VEContext;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * the video data(video tag info ) from video . help load it.
 * Created by heaven7 on 2018/4/11 0011.
 */

public class VideoDataLoadUtils {

    private static final String TAG = "VideoDataLoadUtils";
    /** load data from asset or not */
    private static final boolean sUse_assets = false;

    /**
     * the load callback
     * @author heaven7
     */
    public interface LoadCallback{
        /**
         * called on frate tags loaded
         * @param simpleFileName the simple file name
         * @param fullPath the full path of file
         * @param tags the frame tags
         */
        void onFrameTagsLoaded(String simpleFileName, String fullPath, List<FrameTags> tags);

        /**
         * called on load face rects done
         * @param simpleFileName the simple file name
         * @param fullPath the full path of file
         * @param tags the face rects
         */
        void onFaceRectsLoaded(String simpleFileName, String fullPath, List<FrameFaceRects> tags);
    }

    public static void loadTagData(VEContext context, String filePath, LoadCallback callback)throws LoadException {
        TextReadHelper<FrameTags> reader = new TextReadHelper<FrameTags>(new CsvVideoTagCallback());
        callback.onFrameTagsLoaded(filePath, filePath, reader.read(context, filePath));
    }

    public static void loadRectData(VEContext context, String filePath, LoadCallback callback) throws LoadException{
        TextReadHelper<FrameFaceRects> reader = new TextReadHelper<FrameFaceRects>(new CsvFaceRectCallback());
        callback.onFaceRectsLoaded(filePath, filePath, reader.read(context, filePath));
    }
    /**
     * load the rect files from target dir
     * @param context the context
     * @param dir the dir
     * @param callback the load callback
     */
    public static void loadTagsData(VEContext context, String dir, LoadCallback callback){

        //dir 有多个视频源
        File file = new File(dir);
        if(!file.exists() || !file.isDirectory()){
            throw new IllegalArgumentException();
        }
        String tagPath = dir + "/tags";
        if(!sUse_assets){
            file = new File(tagPath);
        }else{
            file = new File("file:///android_asset/" + tagPath);
        }
        boolean loadTags = file.exists() && file.isDirectory();
        if(loadTags){
            TextReadHelper<FrameTags> reader = new TextReadHelper<FrameTags>(new CsvVideoTagCallback());
            List<String> urls = travelFiles(context, tagPath);
            for(String url : urls){
                if(filter(url)){
                    continue;
                }
                final String simpleFilename = url;
                //concat path. 一个url一个视频源
                url = tagPath + File.separator + url;
                List<FrameTags> tags = reader.read(context, url);
                callback.onFrameTagsLoaded(simpleFilename, url, tags);
            }
        }else{
            Logger.w(TAG, "loadTagsData", "load tags failed for dir = " + dir);
        }
    }
    /**
     * load the rect files from target dir
     * @param context the context
     * @param dir the dir
     * @param callback the load callback
     */
    public static void loadRectsData(VEContext context, String dir, LoadCallback callback){
        //dir 有多个视频源
        File file = new File(dir);
        if(!file.exists() || !file.isDirectory()){
            throw new IllegalArgumentException();
        }
        //key is per-second(start from 0) of video, value is data.
        //SparseArray<FrameData> csvDatas = new SparseArray<>();

        //face rects
        String rectsPath = dir + "/rects";
        if(!sUse_assets){
            file = new File(rectsPath);
        }else{
            file = new File("file:///android_asset/" + rectsPath);
        }
        final boolean loadRects = file.exists() && file.isDirectory();
        if(loadRects){
            TextReadHelper<FrameFaceRects> reader = new TextReadHelper<FrameFaceRects>(new CsvFaceRectCallback());
            List<String> urls = travelFiles(context, rectsPath);
            for(String url : urls){
                if(filter(url)){
                    continue;
                }
                final String simpleFilename = url;
                //concat path
                url = rectsPath + File.separator + url;
                List<FrameFaceRects> result = reader.read(context, url);
                callback.onFaceRectsLoaded(simpleFilename, url, result);
            }
        }else{
            Logger.w(TAG, "loadRectsData", "load rects failed for dir = " + dir);
        }
    }

    //here only load csv,
    private static boolean filter(String url) {
        //may be other file format
        return !url.endsWith(".csv");
    }

    private static int getFrameIdxFromVideoId(String videoId){
        // /Users/jiechen/Developer/Python/tensorflow/feature_extractor/tmp/0.mp4 or 0
        String[] strs = videoId.split("/");
        String str = strs[strs.length - 1 ]; //0.mp4
        if(str.contains(".")){
            return Integer.parseInt(str.split("\\.")[0]);
        }
        return Integer.parseInt(str);
    }

    private static List<String> travelFiles(VEContext context, String dir) {
        final String[] list;
        if(sUse_assets) {
            //not contain all files. only have direct file
            return Arrays.asList(list);
        }else{
            list = new File(dir).list();
            return VisitServices.from(new ArrayList<>(Arrays.asList(list))).visitForQueryList(
                    new PredicateVisitor<String>() {
                        @Override
                        public Boolean visit(String s, Object param) {
                            File file = new File(dir + File.separator + s);
                            return file.isFile();
                        }
                    }, null);
        }
    }

    /** the frame data of video
     * @author heaven7 */
    public static class FrameData{
         private String tagPath;
         private String faceRectPath;
         private FrameTags tag;
         private FrameFaceRects faceRects;

        public String getTagPath() {
            return tagPath;
        }
        public void setTagPath(String tagPath) {
            this.tagPath = tagPath;
        }

        public String getFaceRectPath() {
            return faceRectPath;
        }
        public void setFaceRectPath(String faceRectPath) {
            this.faceRectPath = faceRectPath;
        }

        public FrameTags getTag() {
            return tag;
        }
        public void setTag(FrameTags tag) {
            this.tag = tag;
        }

        public FrameFaceRects getFaceRects() {
            return faceRects;
        }
        public void setFaceRects(FrameFaceRects faceRects) {
            this.faceRects = faceRects;
        }
    }

    private static class CsvVideoTagCallback extends TextReadHelper.BaseAssetsCallback<FrameTags>{
        @Override
        public FrameTags parse(String line) {
            if(TextUtils.isEmpty(line)){
                return null;
            }
            String[] strs = line.split(",");
            final int frameIdx;
            try {//guard  index
                frameIdx = getFrameIdxFromVideoId(strs[0]);
            }catch (NumberFormatException e){
                return null;
            }
            FrameTags ft = new FrameTags();
            ft.setFrameIdx(frameIdx);

            try {
                // 69 1.000000 67 1.000000 520 0.996915 377 0.976848 220 0.967641 1 0.528127 645 0.400145
                String[] strs2 = strs[1].split(" ");
                for (int i = 0, size = strs2.length; i < size; i += 2) {
                    Tag tag = new Tag(Integer.parseInt(strs2[i]), Float.parseFloat(strs2[i + 1]));
                    if (tag.getPossibility() > -1f) {
                        ft.addTag(tag);
                    }
                }
            }catch (ArrayIndexOutOfBoundsException e){
                //ignore
            }
            return ft;
        }
    }

    private static class CsvFaceRectCallback extends TextReadHelper.BaseAssetsCallback<FrameFaceRects>{
        @Override
        public FrameFaceRects parse(String line) {
            if(TextUtils.isEmpty(line)){
                return null;
            }
            String[] strs = line.split(",");
            final int frameIdx;
            try {//guard  index
                frameIdx = Integer.parseInt(strs[0]);
            }catch (NumberFormatException e){
                return null;
            }
            FrameFaceRects rects = new FrameFaceRects();
            rects.setFrameIdx(frameIdx);
            try {
                for(int k = 1, size = strs.length ;  k < size ; k ++) {
                    String[] floats = strs[k].split(" ");
                    int len = floats.length;
                    for (int i = 0; i < len; i += 4) {
                        // "0.146632 0.054032 0.096487 0.171532"
                        FaceRect rect = new FaceRect();
                        rect.setX(Float.parseFloat(floats[i]));
                        rect.setY(Float.parseFloat(floats[i + 1]));
                        rect.setWidth(Float.parseFloat(floats[i + 2]));
                        rect.setHeight(Float.parseFloat(floats[i + 3]));
                        rects.addFaceRect(rect);
                    }
                }
            }catch (RuntimeException e){
                return null;
            }
            return rects;
        }
    }

}
