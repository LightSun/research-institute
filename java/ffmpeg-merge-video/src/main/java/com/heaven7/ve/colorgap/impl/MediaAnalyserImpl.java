package com.heaven7.ve.colorgap.impl;


import com.heaven7.java.visitor.PredicateVisitor;
import com.heaven7.java.visitor.collection.VisitServices;
import com.heaven7.utils.Context;
import com.heaven7.ve.BaseMediaResourceItem;
import com.heaven7.ve.PathTimeTraveller;
import com.heaven7.ve.colorgap.*;
import com.heaven7.ve.colorgap.impl.montage.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CyclicBarrier;

/**
 * 1, 读取视频帧的信息。--》 bitmap
 * 2, 把bitmap丢给ai分析出我们要的数据.csv文件(tag信息)。
 * 3, 切镜头 (切割视频那里处理)
 * Created by heaven7 on 2018/3/18.
 */

public class MediaAnalyserImpl implements MediaAnalyser {

    private VideoAnalyseHelper mVideoHelper;
    private final ImageMediumFileHelper mImageHelper = new ImageMediumFileHelper();

    @Override
    public List<MediaItem> analyse(Context context, List<BaseMediaResourceItem> items, CyclicBarrier barrier) {
        if(mVideoHelper == null) {
            mVideoHelper = createVideoAnalyseHelper(VEGapUtils.asColorGapContext(context));
        }

        List<MediaItem> outItems = new ArrayList<>();
        for(BaseMediaResourceItem item : items){
            final MediaItem mediaItem = new MediaItem();
            mediaItem.item = item;
            mediaItem.imageMeta = analyseMeta(context, item);
            outItems.add(mediaItem);
        }
        List<MediaItem> images = new ArrayList<>();
        List<MediaItem> videoItems = VisitServices.from(outItems).filter(null, new PredicateVisitor<MediaItem>() {
            @Override
            public Boolean visit(MediaItem mediaItem, Object param) {
                return mediaItem.item.isVideo();
            }
        }, images).getAsList();
        //auto handle empty videos
        mVideoHelper.scanAndLoad(context, videoItems, barrier);
        mImageHelper.scanAndLoad(context, images, barrier);

        return outItems;
    }

    private VideoAnalyseHelper createVideoAnalyseHelper(ColorGapContext context) {
        DebugParam param = context.getDebugParam();
        MediaResourceScanner faceScanner = null;
        MediaResourceScanner tagScanner = null;
        MediaResourceScanner highLightScanner = null;
        //from configuration
        MediaResourceConfiguration configuration = context.getMediaResourceConfiguration();
        if(configuration != null) {
             faceScanner = configuration.getFaceScanner();
             tagScanner = configuration.getTagScanner();
             highLightScanner = configuration.getHighLightScanner();
        }

        int testType = context.getTestType();
        switch (testType){
            case ColorGapContext.TEST_TYPE_LOCAL_SERVER:
            case ColorGapContext.TEST_TYPE_SERVER: { //server config
                 if(faceScanner == null) {
                     faceScanner = param.hasFlags(DebugParam.FLAG_ASSIGN_FACE_SCANNER) ?
                            param.getFaceScanner() : new MediaFaceScanner();
                 }
                 if(tagScanner == null) {
                     tagScanner = param.hasFlags(DebugParam.FLAG_ASSIGN_TAG_SCANNER) ?
                             param.getTagScanner() : new MediaTagScanner();
                 }
                 if(highLightScanner == null) {
                     highLightScanner = param.hasFlags(DebugParam.FLAG_ASSIGN_HIGH_LIGHT_SCANNER) ?
                             param.getHighLightScanner() : new MediaHighLightScanner();
                 }
            }break;

            case ColorGapContext.TEST_TYPE_LOCAL: {
                 if(faceScanner == null) {
                     faceScanner = param.hasFlags(DebugParam.FLAG_ASSIGN_FACE_SCANNER) ?
                            param.getFaceScanner() : new LocalMediaFaceScanner();
                 }
                 if(tagScanner == null) {
                     tagScanner = param.hasFlags(DebugParam.FLAG_ASSIGN_TAG_SCANNER) ?
                             param.getTagScanner() : new LocalTagScanner();
                 }
                 if(highLightScanner == null) {
                     highLightScanner = param.hasFlags(DebugParam.FLAG_ASSIGN_HIGH_LIGHT_SCANNER) ?
                             param.getHighLightScanner() : new LocalHighLightScanner();
                 }
            }break;

            default:
                throw new UnsupportedOperationException("test type = " + ColorGapContext.getTestTypeString(testType));
        }
        return new VideoAnalyseHelper(faceScanner, tagScanner, highLightScanner,
                new MediaFaceLoader(), new MediaTagLoader(), new MediaHighLightLoader());
    }

    @Override
    public int getAsyncModuleCount() {
        return 2;
    }

    @Override
    public void cancel() {
        mVideoHelper.cancel();
        mImageHelper.cancel();
    }

    @Override
    public void preLoadData(ColorGapContext context, ColorGapParam param) {
        mImageHelper.loadResource(context, param);
    }

    protected MetaInfo.ImageMeta analyseMeta(Context context, BaseMediaResourceItem item) {
        MetaInfo.ImageMeta meta = new MetaInfo.ImageMeta();
        meta.setPath(item.getFilePath());
        meta.setDate(item.getTime());
        meta.setDuration(item.getDuration());
        //meta.setFps();
        meta.setHeight(item.getHeight());
        meta.setWidth(item.getWidth());
        meta.setMediaType(item.isImage() ? PathTimeTraveller.TYPE_IMAGE : PathTimeTraveller.TYPE_VIDEO);
        //meta.setShotType();
        //meta.setTags();
        //meta.setCameraMotion();
        //meta.setLocation();
        //VideoDataLoadUtils.load(context, "", )
        return meta;
    }

}
