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
            mVideoHelper = createVideoAnalyseHelper(VEGapUtils.asColorGapContext(context).getTestType());
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

    private VideoAnalyseHelper createVideoAnalyseHelper(int testType) {
        switch (testType){
            case ColorGapContext.TEST_TYPE_LOCAL_SERVER:
            case ColorGapContext.TEST_TYPE_SERVER:
                return new VideoAnalyseHelper(new MediaFaceScanner(), new MediaTagScanner(), new MediaHighLightScanner(),
                        new MediaFaceLoader(), new MediaTagLoader(), new MediaHighLightLoader());

            case ColorGapContext.TEST_TYPE_LOCAL:
                return  new VideoAnalyseHelper(new LocalMediaFaceScanner(), new LocalTagScanner(), new LocalHighLightScanner(),
                        new MediaFaceLoader(), new MediaTagLoader(), new MediaHighLightLoader());

        }
        throw new UnsupportedOperationException("test type = " + ColorGapContext.getTestTypeString(testType));
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

    private String getFileName(BaseMediaResourceItem item) {
        String path = item.getFilePath();
        int index = path.indexOf("/");
        return path.substring(index + 1);
    }

}
