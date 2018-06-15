package com.heaven7.test;

import com.heaven7.java.visitor.PredicateVisitor;
import com.heaven7.java.visitor.ResultVisitor;
import com.heaven7.java.visitor.collection.VisitServices;
import com.heaven7.util.CsvDetail;
import com.heaven7.util.FrameTagInfo;
import com.heaven7.utils.FileUtils;
import com.heaven7.utils.TextUtils;
import com.heaven7.ve.colorgap.*;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.heaven7.util.CountUtils.*;

/**
 * 统计视频和图像的所有tag.
 * 1， 根据标签找出对应的的源视频/图片，如果是视频则找出符合特征的 时间点。
 * 2,  按照指定视频，和时间-找出所有>0.5f 的tag
 */
public class CountTagTest {

    static {
        ResourceInitializer.init(null);
    }

    @Test
    public void getTimePointsByTag(){
        String dir = "E:\\BaiduNetdiskDownload\\taobao_service";
        String[] tags = {"Gown"};
        float minPoss = 0.5f;

        Callback2 callback = new Callback2();
        loadData(dir, callback);
        List<FrameTagInfo> infos = callback.getFrameTagInfos(tags, minPoss);
        StringBuilder sb = buildFrameTagInfos(infos, tags, minPoss);
        //write to file
        String str1 = VisitServices.from(Arrays.asList(tags)).asListService().joinToString("_");
        File dst = new File(dir, str1 + "_"+ minPoss + ".txt");
        FileUtils.writeTo(dst, sb.toString());
    }

    @Test
    public void getTimePointsByTagAndMedia(){
        String dir = "E:\\BaiduNetdiskDownload\\taobao_service";
        String media = "E:\\BaiduNetdiskDownload\\taobao_service\\东森（服装）\\女装南泉外拍第二次视频1\\碎花短马甲\\VID_20180507_152438.mp4";
        String[] tags = {"Jeans"};
        float minPoss = 0.5f;

        Callback2 callback = new Callback2();
        loadData(dir, callback);
        List<FrameTagInfo> infos = callback.getFrameTagInfos(media, tags, minPoss);
        StringBuilder sb = buildFrameTagInfos(infos, tags, minPoss);
        //write to file
      /*  String str1 = VisitServices.from(Arrays.asList(tags)).asListService().joinToString("_");
        File dst = new File(dir, str1 + "_"+ minPoss + ".txt");
        FileUtils.writeTo(dst, sb.toString());*/
        System.out.println(sb.toString());
    }

    @Test //get tags from start to end of target media
    public void testGetTags(){
        String dir = "E:\\BaiduNetdiskDownload\\taobao_service";
        String mediaPath = "E:\\BaiduNetdiskDownload\\taobao_service\\东森（服装）\\开衫\\VID_20180602_131112.MP4";
        int start = 8;
        int end = 11;
        float minPoss = 0.5f;

        Callback2 callback = new Callback2();
        loadData(dir, callback);
        CsvDetail csvDetail = callback.getCsvDetail(mediaPath);
        List<Tag> tags = getTags(csvDetail.getFrames(),
                start, end, minPoss);
        //build result
        StringBuilder sb = buildResultText(merge(tags));
        //write to file
        String fileName = FileUtils.getFileName(mediaPath);
        String outDir = FileUtils.getFileDir(mediaPath, 1, true)+ File.separator + "count_info";
        File dst = new File(outDir, fileName + "__"+ start + "_"+ end + ".txt");
        FileUtils.writeTo(dst, sb.toString());

        System.out.println(sb.toString());
    }

    @Test //get tags from start to end of target media
    public void testGetTags2(){
        //存储到特定目录
        String saveDir = "E:\\BaiduNetdiskDownload\\taobao_service\\细节_count";
        String dir = "E:\\BaiduNetdiskDownload\\taobao_service";
        String mediaPath = "E:\\BaiduNetdiskDownload\\taobao_service\\东森（服装）\\女装南泉外拍第二次视频1\\碎花短马甲\\VID_20180507_153236.mp4";

        int start = 23;
        int end = 23;
        float minPoss = 0.5f;

        Callback2 callback = new Callback2();
        loadData(dir, callback);
        CsvDetail csvDetail = callback.getCsvDetail(mediaPath);
        List<Tag> tags = getTags(csvDetail.getFrames(),
                start, end, minPoss);
        //build result
        StringBuilder sb = buildResultText(merge(tags));
        //write to file
        String fileName = FileUtils.getFileName(mediaPath);
        File dst = new File(saveDir, fileName + "__"+ start + "_"+ end + ".txt");
       // FileUtils.writeTo(dst, sb.toString());

        System.out.println(sb.toString());
    }

    @Test
    public void testCountAll(){
        String dir =  "E:\\BaiduNetdiskDownload\\taobao_service";
        //String dir =  "E:\\BaiduNetdiskDownload\\taobao_service\\照片";

        Callback0 callback = new Callback0();
        loadData(dir, callback);
        callback.count(dir);
    }

    //for xxx_predication.csv is in 'tags'
    private static class Callback2 implements VideoDataLoadUtils.LoadCallback{

        final List<CsvDetail> datas = new ArrayList<>();
        @Override
        public void onFrameTagsLoaded(String simpleFileName, String fullPath, List<FrameTags> tags) {
            //find correct video/image
            CsvDetail detail = new CsvDetail();
            detail.setMediaSourceFromTagPath(fullPath);
            detail.setFrames(tags);
            datas.add(detail);
        }
        @Override
        public void onFaceRectsLoaded(String simpleFileName, String fullPath, List<FrameFaceRects> tags) {

        }

        public CsvDetail getCsvDetail(String mediaPath){
            return VisitServices.from(datas).query(new PredicateVisitor<CsvDetail>() {
                @Override
                public Boolean visit(CsvDetail csvDetail, Object param) {
                    return csvDetail.getMediaSrc().getFilePath().equalsIgnoreCase(mediaPath);
                }
            });
        }
        public List<FrameTagInfo> getFrameTagInfos(String[] tags, float minPoss){
             return VisitServices.from(datas).map(new ResultVisitor<CsvDetail, FrameTagInfo>() {
                 @Override
                 public FrameTagInfo visit(CsvDetail detail, Object param) {
                     return convertFrameTagInfo(detail, tags, minPoss);
                 }
             }).getAsList();
        }
        public List<FrameTagInfo> getFrameTagInfos(String mediaPath, String[] tags, float minPoss){
            return VisitServices.from(datas).map(new ResultVisitor<CsvDetail, FrameTagInfo>() {
                @Override
                public FrameTagInfo visit(CsvDetail detail, Object param) {
                    if(!TextUtils.isEmpty(mediaPath) && !detail.getMediaSrc().getFilePath().equalsIgnoreCase(mediaPath)){
                        return null;
                    }
                    return convertFrameTagInfo(detail, tags, minPoss);
                }
            }).getAsList();
        }
    }


    private static class Callback0 implements VideoDataLoadUtils.LoadCallback{

        final List<FrameTags> list = new ArrayList<>();

        @Override
        public void onFrameTagsLoaded(String simpleFileName, String fullPath, List<FrameTags> tags) {
            list.addAll(tags);
        }
        @Override
        public void onFaceRectsLoaded(String simpleFileName, String fullPath, List<FrameFaceRects> tags) {

        }
        //所有tag的信息
        public void count(String dir){
            List<Tag> tags = getTags(list, 0, Integer.MAX_VALUE, 0.5f);
            StringBuilder sb = buildResultText(merge(tags));
            //write to file
            File dst = new File(dir, "count_info.txt");
            FileUtils.writeTo(dst, sb.toString());
        }
    }
}
