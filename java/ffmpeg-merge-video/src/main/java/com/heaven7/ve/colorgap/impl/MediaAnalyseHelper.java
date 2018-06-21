package com.heaven7.ve.colorgap.impl;

import com.heaven7.core.util.Logger;
import com.heaven7.java.base.util.Predicates;
import com.heaven7.java.base.util.SparseArray;
import com.heaven7.java.visitor.PredicateVisitor;
import com.heaven7.java.visitor.collection.MapVisitService;
import com.heaven7.java.visitor.collection.VisitServices;
import com.heaven7.utils.ConcurrentUtils;
import com.heaven7.utils.FileUtils;
import com.heaven7.utils.LoadException;
import com.heaven7.ve.Context;
import com.heaven7.ve.MediaResourceItem;
import com.heaven7.ve.colorgap.*;

import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * this class can only used for video.
 * Created by heaven7 on 2018/4/16 0016.
 */

public class MediaAnalyseHelper {

    private static final String TAG = "MediaAnalyseHelper";

    private final MediaResourceScanner rectsScanner;
    private final MediaResourceScanner tagsScanner;
    private final MediaResourceLoader tagsLoader;
    private final MediaResourceLoader rectsLoader;

    private AtomicInteger mGroupCount;

    public MediaAnalyseHelper(MediaResourceScanner rectsScanner, MediaResourceScanner tagsScanner,
                              MediaResourceLoader rectsLoader, MediaResourceLoader tagsLoader) {
        this.rectsScanner = rectsScanner;
        this.tagsScanner = tagsScanner;
        this.tagsLoader = tagsLoader;
        this.rectsLoader = rectsLoader;
    }
    public void cancel() {
        ConcurrentUtils.shutDownNow();
    }
    public void scanAndLoad(Context context, List<MediaItem> items, final CyclicBarrier outBarrier) {
        ConcurrentUtils.submit(() -> scanAndLoad0(context, items, outBarrier));
    }

    /**
     * scan and load data for target items.
     * @param items the media items
     * @param barrier the barrier
     */
    private void scanAndLoad0(Context context, List<MediaItem> items, final CyclicBarrier barrier) {
        //filter video
        List<MediaItem> videoItems = VisitServices.from(items).visitForQueryList(new PredicateVisitor<MediaItem>() {
            @Override
            public Boolean visit(MediaItem mediaItem, Object param) {
                return !mediaItem.item.isImage();
            }
        }, null);
        //no tasks release directly.
        if(Predicates.isEmpty(videoItems)){
            ConcurrentUtils.awaitBarrier(barrier);
        }else {
            MapVisitService<String, List<MediaItem>> mapService = VisitServices.from(videoItems)
                    .groupService((item, param) -> {
                        //get the dir of this file
                       return FileUtils.getFileDir(item.item.getFilePath(), 2, true);
                    });
            mGroupCount = new AtomicInteger(mapService.size());
            mapService.fire((pair, param) -> {
                new BatchProcessor(context, barrier, pair.getKey(), pair.getValue()).process();
                return null;
            });
        }
    }

    private class BatchProcessor{
        private final CyclicBarrier outBarrier;
        final String dir;
        final List<MediaItem> sameDirItems;
        final Context context;

        final AtomicInteger mCount;

        /*public*/ BatchProcessor(Context context, CyclicBarrier barrier, String dir, List<MediaItem> sameDirItems) {
            this.context = context;
            this.outBarrier = barrier;
            this.dir = dir;
            this.sameDirItems = sameDirItems;
            //contains. rects and tags
            this.mCount = new AtomicInteger(sameDirItems.size() * 2);
        }

        public void process() {
            Logger.i(TAG, "process", "start batch process >>> dir = " + dir);
            for (MediaItem item : sameDirItems) {
                //Logger.d(TAG, "process", "start scan ---> item.path = " + item.item.getFilePath());
                ConcurrentUtils.submit(new ScanTask(context, item, dir, rectsScanner, rectsLoader, this, "rects"));
                ConcurrentUtils.submit(new ScanTask(context, item, dir, tagsScanner, tagsLoader, this,"tags"));
            }
        }

        public void decreaseTask() {
            if(mCount.decrementAndGet() == 0){
                //batch done
                Logger.i(TAG, "decreaseTask", " >>> batch task(scan and load) done. dir = " + dir);
                if(mGroupCount.decrementAndGet() == 0){
                    //all done
                    Logger.i(TAG, "decreaseTask", " >>> all tasks(scan and load) done.");
                    try {
                        outBarrier.await();
                    } catch (InterruptedException e) {
                        //ignore e.printStackTrace();
                    } catch (BrokenBarrierException e) {
                        throw new RuntimeException(e);
                    }
                }else{
                    Logger.i(TAG, "decreaseTask", " >>> group count = " + mGroupCount.get());
                }
            }else{
                Logger.v(TAG, "decreaseTask", "remain batch.size = " + mCount.get() + " , dir = " + dir);
            }
        }
    }

    private class ScanTask implements Runnable {
        final Context context;
        final String tag;

        final MediaItem item;
        final String dir;
        final MediaResourceScanner scanner;
        final MediaResourceLoader loader;

        final BatchProcessor parent;

        public ScanTask(Context context, MediaItem item, String dir,
                        MediaResourceScanner scanner, MediaResourceLoader loader, BatchProcessor parent, String tag) {
            this.context = context;
            this.item = item;
            this.dir = dir;
            this.scanner = scanner;
            this.loader = loader;
            this.parent = parent;
            this.tag = tag;
        }

        @Override
        public void run() {
            Logger.d(TAG, "run", ">>> start scan "+ tag +": path = " + item.item.getFilePath());
            String path = scanner.scan(context,item.item, dir);
            if(path == null){
                Logger.w(TAG, "run", tag + " scan failed. dir = " + dir + ",item path = " + item.item.getFilePath());
                //may some one failed. we notify success. but no data.
                parent.decreaseTask();
            }else {
                Logger.d(TAG, "run", "<<< scan done "+ tag + ": path = " + item.item.getFilePath());
                ConcurrentUtils.submit(new LoadTask(context, item.item, path, item.imageMeta, loader, parent));
            }
        }
    }

    private static class LoadTask implements Runnable, VideoDataLoadUtils.LoadCallback {

        final Context context;
        final String path;
        final MediaResourceItem item;
        final MediaResourceLoader loader;

        final MetaInfo.ImageMeta out;
        final BatchProcessor parent;

        /*public*/ LoadTask(Context context, MediaResourceItem item, String path, MetaInfo.ImageMeta out,
                            MediaResourceLoader loader, BatchProcessor parent) {
            this.context = context;
            this.item = item;
            this.path = path;
            this.loader = loader;
            this.out = out;
            this.parent = parent;
            out.getFrameDataMap();//ensure non null
        }

        @Override
        public void run() {
            Logger.d(TAG, "run", "====== start load file path = " + path);
            try {
                loader.load(context, item, path, this);
                Logger.d(TAG, "run", "====== load task done. file path = " + path);
            }catch (LoadException e){
                Logger.w(TAG, "run", "load failed for path = " + path);
            }finally {
                parent.decreaseTask();
            }
        }

        private VideoDataLoadUtils.FrameData getFrameData(int frameIdx) {
            synchronized (out) {
                SparseArray<VideoDataLoadUtils.FrameData> frameDataMap = out.getFrameDataMap();
                VideoDataLoadUtils.FrameData frameData = frameDataMap.get(frameIdx);
                if (frameData == null) {
                    frameData = new VideoDataLoadUtils.FrameData();
                    frameDataMap.put(frameIdx, frameData);
                }
                return frameData;
            }
        }

        @Override
        public void onFrameTagsLoaded(String simpleFileName, String fullPath, List<FrameTags> tags) {
            for (FrameTags ft : tags) {
                VideoDataLoadUtils.FrameData frameData = getFrameData(ft.getFrameIdx());
                frameData.setTagPath(fullPath);
                frameData.setTag(ft);
            }
        }

        @Override
        public void onFaceRectsLoaded(String simpleFileName, String fullPath, List<FrameFaceRects> faceRects) {
            for (FrameFaceRects rects : faceRects) {
                VideoDataLoadUtils.FrameData frameData = getFrameData(rects.getFrameIdx());
                frameData.setFaceRectPath(fullPath);
                frameData.setFaceRects(rects);
            }
        }
    }

}
