package com.heaven7.ve.colorgap.impl;


import com.heaven7.java.visitor.PredicateVisitor;
import com.heaven7.java.visitor.collection.VisitServices;
import com.heaven7.ve.Context;
import com.heaven7.ve.MediaResourceItem;
import com.heaven7.ve.PathTimeTraveller;
import com.heaven7.ve.colorgap.MediaAnalyser;
import com.heaven7.ve.colorgap.MediaItem;
import com.heaven7.ve.colorgap.MetaInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * 1, 读取视频帧的信息。--》 bitmap
 * 2, 把bitmap丢给ai分析出我们要的数据.csv文件(tag信息)。
 * 3, 切镜头 (切割视频那里处理)
 * Created by heaven7 on 2018/3/18.
 */

public class MediaAnalyserImpl implements MediaAnalyser {

    private final MediaAnalyseHelper mVideoHelper = new MediaAnalyseHelper(new MockRectsScanner(), new MockTagsScanner(),
            new RectsLoader(), new TagsLoader());
    private final ImageMediumFileHelper mImageHelper = new ImageMediumFileHelper(ImageMediumFileHelper.MODE_SINGLE_TAG
            | ImageMediumFileHelper.MODE_RECTS);

    @Override
    public List<MediaItem> analyse(Context context, List<MediaResourceItem> items, CyclicBarrier barrier) {
        List<MediaItem> outItems = new ArrayList<>();
        for(MediaResourceItem item : items){
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

    @Override
    public int getAsyncModuleCount() {
        return 2;
    }

    @Override
    public void cancel() {
        mVideoHelper.cancel();
        mImageHelper.cancel();
    }

    protected MetaInfo.ImageMeta analyseMeta(Context context, MediaResourceItem item) {
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

    private String getFileName(MediaResourceItem item) {
        String path = item.getFilePath();
        int index = path.indexOf("/");
        return path.substring(index + 1);
    }

}
