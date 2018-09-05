package com.heaven7.test;

import com.heaven7.java.visitor.FireVisitor;
import com.heaven7.java.visitor.MapFireVisitor;
import com.heaven7.java.visitor.PredicateVisitor;
import com.heaven7.java.visitor.ResultVisitor;
import com.heaven7.java.visitor.collection.KeyValuePair;
import com.heaven7.java.visitor.collection.VisitServices;
import com.heaven7.util.CountUtils;
import com.heaven7.util.CsvDetail;
import com.heaven7.util.FrameTagInfo;
import com.heaven7.utils.FileUtils;
import com.heaven7.utils.TextUtils;
import com.heaven7.ve.colorgap.*;
import com.heaven7.ve.colorgap.impl.SimpleColorGapContext;
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
    public void getTimePointsByTag() {
        String dir = "E:\\BaiduNetdiskDownload\\taobao_service";
        String[] tags = {"Gown"};
        float minPoss = 0.5f;

        Callback2 callback = new Callback2(new SimpleColorGapContext());
        loadData(dir, callback);
        List<FrameTagInfo> infos = callback.getFrameTagInfos(tags, minPoss);
        StringBuilder sb = buildFrameTagInfos(infos, tags, minPoss);
        //write to file
        String str1 = VisitServices.from(Arrays.asList(tags)).asListService().joinToString("_");
        File dst = new File(dir, str1 + "_" + minPoss + ".txt");
        FileUtils.writeTo(dst, sb.toString());
    }

    @Test
    public void getTimePointsByTagAndMedia() {
        String dir = "E:\\BaiduNetdiskDownload\\taobao_service";
        String media = "E:\\BaiduNetdiskDownload\\taobao_service\\东森（服装）\\女装南泉外拍第二次视频1\\碎花短马甲\\VID_20180507_152438.mp4";
        String[] tags = {"Jeans"};
        float minPoss = 0.5f;

        SimpleColorGapContext context = new SimpleColorGapContext();
        Callback2 callback = new Callback2(context);
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
    public void testGetTags() {
        String dir = "E:\\BaiduNetdiskDownload\\taobao_service";
        String mediaPath = "E:\\BaiduNetdiskDownload\\taobao_service\\东森（服装）\\开衫\\VID_20180602_131112.MP4";
        int start = 8;
        int end = 11;
        float minPoss = 0.5f;
        SimpleColorGapContext context = new SimpleColorGapContext();

        Callback2 callback = new Callback2(context);
        loadData(dir, callback);
        CsvDetail csvDetail = callback.getCsvDetail(mediaPath);
        List<Tag> tags = getTags(context, csvDetail.getFrames(),
                start, end, minPoss);
        //build result
        StringBuilder sb = buildResultText(merge(tags));
        //write to file
        String fileName = FileUtils.getFileName(mediaPath);
        String outDir = FileUtils.getFileDir(mediaPath, 1, true) + File.separator + "count_info";
        File dst = new File(outDir, fileName + "__" + start + "_" + end + ".txt");
        FileUtils.writeTo(dst, sb.toString());

        System.out.println(sb.toString());
    }

    @Test //get tags from start to end of target media
    public void testGetTags2() {
        //存储到特定目录
        String saveDir = "E:\\BaiduNetdiskDownload\\taobao_service\\细节_count";
        String dir = "E:\\BaiduNetdiskDownload\\taobao_service";
        String mediaPath = "E:\\BaiduNetdiskDownload\\taobao_service\\东森（服装）\\女装南泉外拍第二次视频1\\碎花短马甲\\VID_20180507_153236.mp4";

        int start = 23;
        int end = 23;
        float minPoss = 0.5f;

        SimpleColorGapContext context = new SimpleColorGapContext();
        Callback2 callback = new Callback2(context);
        loadData(dir, callback);
        CsvDetail csvDetail = callback.getCsvDetail(mediaPath);
        List<Tag> tags = getTags(context, csvDetail.getFrames(),
                start, end, minPoss);
        //build result
        StringBuilder sb = buildResultText(merge(tags));
        //write to file
        String fileName = FileUtils.getFileName(mediaPath);
        File dst = new File(saveDir, fileName + "__" + start + "_" + end + ".txt");
        // FileUtils.writeTo(dst, sb.toString());

        System.out.println(sb.toString());
    }

    @Test
    public void testCountAll() {
        String dir = "E:\\BaiduNetdiskDownload\\taobao_service";
        //String dir =  "E:\\BaiduNetdiskDownload\\taobao_service\\照片";

        Callback0 callback = new Callback0(new SimpleColorGapContext());
        loadData(dir, callback);
        callback.count(dir);
    }

    @Test //将tag转化为string.
    public void generateStrFile() {
        String dir = "E:\\BaiduNetdiskDownload\\taobao_service";
        // String dir = "E:\\BaiduNetdiskDownload\\taobao_service\\东森（服装）\\女装南泉外拍第二次视频2\\扎染褙子";
        SimpleColorGapContext context = new SimpleColorGapContext();
        Callback2 callback = new Callback2(context);
        loadData(dir, callback);
        VisitServices.from(callback.getCsvDetails()).fire(new FireVisitor<CsvDetail>() {
            @Override
            public Boolean visit(CsvDetail detail, Object param) {
                String filePath = detail.getFilePath();
                String dir = FileUtils.getFileDir(filePath, 1, true);
                String fileName = FileUtils.getFileName(filePath);
                File dst = new File(dir + File.separator + "strs" + File.separator + fileName + "_str.txt");

                String content = CountUtils.getContentFromFrames(context, detail.getFrames(), 0.5f);
                FileUtils.writeTo(dst, content);
                return null;
            }
        });
    }
    @Test //将tag转化为string. 将多个图片的tag文件(同目录) 写到一个文件里。
    public void generateStrFileForImage() {
        SimpleColorGapContext context = new SimpleColorGapContext();
        final String dir = "E:\\BaiduNetdiskDownload\\taobao_service";
        final String outDir = "E:\\BaiduNetdiskDownload\\taobao_service\\test";
        final int depth = 3;
        //String dir = "E:\\BaiduNetdiskDownload\\taobao_service\\照片\\男装\\翻领撞色套头衫";
        Callback2 callback = new Callback2(context);
        loadData(dir, callback);
        VisitServices.from(callback.getImagesCsvDetails())
                .groupService(new ResultVisitor<CsvDetail, String>() { //dir
                    @Override
                    public String visit(CsvDetail detail, Object param) {
                        return FileUtils.getFileDir(detail.getFilePath(), 1, true);
                    }
                }).fire(new MapFireVisitor<String, List<CsvDetail>>() {
            @Override
            public Boolean visit(KeyValuePair<String, List<CsvDetail>> pair, Object param) {
                String dir;
                if(TextUtils.isEmpty(outDir)){
                    dir = pair.getKey();
                }else{
                    String filePath = pair.getValue().get(0).getFilePath();
                    dir = outDir;
                    int dep = depth;
                    while (dep > 0){
                        dir += File.separator + FileUtils.getFileDir(filePath, dep, false);
                        dep --;
                    }
                }
                File dst = new File(dir + File.separator + "all_strs.txt");
                String content = CountUtils.getContentFromDetails(context, pair.getValue(), 0.5f);
                FileUtils.writeTo(dst, content);
                return null;
            }
        });

    }

    //for xxx_predication.csv is in 'tags'
    private static class Callback2 extends BaseContextOwner implements VideoDataLoadUtils.LoadCallback {

        final List<CsvDetail> datas = new ArrayList<>();

        public Callback2(ColorGapContext mContext) {
            super(mContext);
        }

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

        public List<CsvDetail> getCsvDetails() {
            return datas;
        }
        public List<CsvDetail> getImagesCsvDetails() {
            return VisitServices.from(datas).filter(null, new PredicateVisitor<CsvDetail>() {
                @Override
                public Boolean visit(CsvDetail detail, Object param) {
                    return !detail.getMediaSrc().isVideo();
                }
            }, null).getAsList();
        }

        public CsvDetail getCsvDetail(String mediaPath) {
            return VisitServices.from(datas).query(new PredicateVisitor<CsvDetail>() {
                @Override
                public Boolean visit(CsvDetail csvDetail, Object param) {
                    return csvDetail.getMediaSrc().getFilePath().equalsIgnoreCase(mediaPath);
                }
            });
        }

        public List<FrameTagInfo> getFrameTagInfos(String[] tags, float minPoss) {
            return VisitServices.from(datas).map(new ResultVisitor<CsvDetail, FrameTagInfo>() {
                @Override
                public FrameTagInfo visit(CsvDetail detail, Object param) {
                    return convertFrameTagInfo(getContext(), detail, tags, minPoss);
                }
            }).getAsList();
        }

        public List<FrameTagInfo> getFrameTagInfos(String mediaPath, String[] tags, float minPoss) {
            return VisitServices.from(datas).map(new ResultVisitor<CsvDetail, FrameTagInfo>() {
                @Override
                public FrameTagInfo visit(CsvDetail detail, Object param) {
                    if (!TextUtils.isEmpty(mediaPath) && !detail.getMediaSrc().getFilePath().equalsIgnoreCase(mediaPath)) {
                        return null;
                    }
                    return convertFrameTagInfo(getContext(), detail, tags, minPoss);
                }
            }).getAsList();
        }
    }


    private static class Callback0 extends BaseContextOwner implements VideoDataLoadUtils.LoadCallback {

        final List<FrameTags> list = new ArrayList<>();

        public Callback0(ColorGapContext mContext) {
            super(mContext);
        }

        @Override
        public void onFrameTagsLoaded(String simpleFileName, String fullPath, List<FrameTags> tags) {
            list.addAll(tags);
        }

        @Override
        public void onFaceRectsLoaded(String simpleFileName, String fullPath, List<FrameFaceRects> tags) {

        }

        //所有tag的信息
        public void count(String dir) {
            List<Tag> tags = getTags(getContext(), list, 0, Integer.MAX_VALUE, 0.5f);
            StringBuilder sb = buildResultText(merge(tags));
            //write to file
            File dst = new File(dir, "count_info.txt");
            FileUtils.writeTo(dst, sb.toString());
        }
    }
}
