package com.heaven7.ve.colorgap.impl;

import com.heaven7.core.util.Logger;
import com.heaven7.java.base.util.Predicates;
import com.heaven7.java.base.util.Throwables;
import com.heaven7.java.visitor.*;
import com.heaven7.java.visitor.collection.KeyValuePair;
import com.heaven7.java.visitor.collection.ListVisitService;
import com.heaven7.java.visitor.collection.MapVisitService;
import com.heaven7.java.visitor.collection.VisitServices;
import com.heaven7.utils.*;
import com.heaven7.ve.Context;
import com.heaven7.ve.colorgap.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * the image analyse helper help we analyse image rects and tags.
 *
 * @author heaven7
 */
/*public*/ class ImageAnalyseHelper {

    private static final int DEFAULT_MEMBER_COUNT = 5;
    private static final String TAG = "ImageAnalyseHelper";
    private final ImageResourceScanner rectsScanner;
    private final ImageResourceScanner tagsScanner;

    private ImageDataDirMapper mDataDirMapper;//TODO data dir mapper.
    private List<ImageResource> mImageRess;

    public ImageAnalyseHelper(ImageResourceScanner rectsScanner, ImageResourceScanner tagsScanner) {
        this.rectsScanner = rectsScanner;
        this.tagsScanner = tagsScanner;
    }

    public void scanAndLoad(Context context, List<MediaItem> imageItems, boolean singleTag,
                            boolean singleRect, final CyclicBarrier barrier) {
        ConcurrentUtils.submit(() -> scanAndLoad0(context, imageItems, singleTag, singleRect, barrier));
    }

    private void scanAndLoad0(Context context, List<MediaItem> imageItems, boolean singleTag,
                              boolean singleRect, CyclicBarrier barrier) {
        if (imageItems.isEmpty() || (rectsScanner == null && tagsScanner == null)) {
            ConcurrentUtils.awaitBarrier(barrier);
        } else {
            //load image
            new BatchScanner(context, imageItems, barrier).startScan(singleRect, singleTag);
        }
    }

    /**
     * load all image resource for batch images.such as rects_path, tag_path.
     * note this must called in sub thread.
     * @param resourceDir the resource dir of save all medias.
     */
    public void loadImageResource(String resourceDir){
        File batchFileList = new File(resourceDir, "image_batch_list.txt");
        if(!batchFileList.exists()){
            return;
        }
        List<ImageResBatchLine> lines = new TextReadHelper<ImageResBatchLine>(
                new TextReadHelper.BaseAssetsCallback<ImageResBatchLine>() {
            @Override
            public ImageResBatchLine parse(String line) {
                return new ImageResBatchLine(line);
            }
        }).read(null, batchFileList.getAbsolutePath());
        final TextReadHelper<ConfigLine> reader = new TextReadHelper<>(new ConfigLineCallback());
        mImageRess = VisitServices.from(lines).map(new ResultVisitor<ImageResBatchLine, ImageResource>() {
            @Override
            public ImageResource visit(ImageResBatchLine line, Object param) {
                String dataDir = mDataDirMapper.map(line.imageResourceDir);
                ImageResource imageRes = new ImageResource();
                imageRes.setBatchDir(line.imageResourceDir);

                File face_config = new File(dataDir, "face_config.txt");
                File tfs_config = new File(dataDir, "tfs_config.txt");
                //csv, imapath imgpath ...
                if(face_config.exists()){
                    List<ConfigLine> faceLines = reader.read(null, face_config.getAbsolutePath());
                    if(!Predicates.isEmpty(faceLines)){
                        String rectsPath = faceLines.get(0).getCsvRectsPath();
                        imageRes.setRectsPath(rectsPath);
                    }
                }
                //tfs_config
                if(tfs_config.exists()){
                    List<ConfigLine> faceLines = reader.read(null, face_config.getAbsolutePath());
                    if(!Predicates.isEmpty(faceLines)){
                        String tagPath = faceLines.get(0).getCsvTagPath();
                        imageRes.setTagPath(tagPath);
                    }
                }
                return imageRes;
            }
        }).getAsList();
    }

    private static class ImageResource{
         String batchDir;
         String rectsPath;
         String tagPath;

        public String getBatchDir() {
            return batchDir;
        }
        public void setBatchDir(String batchDir) {
            this.batchDir = batchDir;
        }
        public String getRectsPath() {
            return rectsPath;
        }
        public void setRectsPath(String rectsPath) {
            this.rectsPath = rectsPath;
        }

        public String getTagPath() {
            return tagPath;
        }
        public void setTagPath(String tagPath) {
            this.tagPath = tagPath;
        }
    }

    /**
     * the image data dir mapper.
     */
    public interface ImageDataDirMapper{
        /**
         * map the source dir to data dir.
         * @param imageResDir the image resource dir
         * @return the image data dir
         */
        String map(String imageResDir);
    }

    /**
     * the line of config file
     */
    private static class ConfigLine{
        String dataPath;// may be rects or tfrecord path
        List<String> imagePaths;

        public ConfigLine(String line) {
            String[] strs = line.split(",");
            if(!Predicates.isEmpty(strs)){
                this.dataPath = strs[0];
                if(strs.length >= 2){
                    imagePaths = Arrays.asList(strs[1].split(" "));
                }
            }
        }

        public String getCsvRectsPath(){
            return dataPath;
        }
        public String getCsvTagPath(){
            if(TextUtils.isEmpty(dataPath)){
                return null;
            }
            String dir = FileUtils.getFileDir(dataPath, 1, true);
            String fileName = FileUtils.getFileName(dataPath);
            final String baseFileName;
            if(fileName.endsWith("_outputs")){
                baseFileName = fileName.substring(0, fileName.lastIndexOf("outputs"));
            }else if(fileName.endsWith("_output")){
                baseFileName = fileName.substring(0, fileName.lastIndexOf("output"));
            }else{
                throw new IllegalStateException("wrong file. please check your py script of gen.");
            }
            return dir + File.separator + baseFileName + "predictions.csv";
        }
    }

    private static class ImageResBatchLine{
        String imageResourceDir;
        int imageCount;

        public ImageResBatchLine(String line) {
            String[] strs = line.split(",");
            if(!Predicates.isEmpty(strs)){
                this.imageResourceDir = strs[0];
                if(strs.length >= 2){
                    imageCount = Integer.valueOf(strs[1]);
                }
            }
        }
    }
    private static class ConfigLineCallback extends TextReadHelper.BaseAssetsCallback<ConfigLine>{
        @Override
        public ConfigLine parse(String line) {
            return new ConfigLine(line);
        }
    }

    /*
     * 1, 加载所有图片相关的路径
     */

    private class BatchScanner {

        private final List<MediaItem> mMediaItems;
        private final CyclicBarrier mBarrier;
        private final Context mContext;
        private AtomicInteger mGroupCount;

        public BatchScanner(Context context, List<MediaItem> mediaItems, CyclicBarrier barrier) {
            this.mContext = context;
            this.mMediaItems = mediaItems;
            this.mBarrier = barrier;
        }

        public void startScan(boolean singleRect, boolean singleTag) {
            ListVisitService<List<MediaItem>> groupService = VisitServices.from(mMediaItems)
                    .groupService(DEFAULT_MEMBER_COUNT);
            mGroupCount = new AtomicInteger(groupService.size());

            groupService.map(new ResultVisitor<List<MediaItem>, Group>() {
                @Override
                public Group visit(List<MediaItem> mediaItems, Object param) {
                    return new Group(mediaItems);
                }
            }).fire(new FireVisitor<Group>() {
                @Override
                public Boolean visit(final Group group, Object param) {
                    ConcurrentUtils.submit(new Runnable() {
                        @Override
                        public void run() {
                            doWidthRects(group, singleRect);
                        }
                    });
                    ConcurrentUtils.submit(new Runnable() {
                        @Override
                        public void run() {
                            doWidthTags(group, singleTag);
                        }
                    });
                    return null;
                }
            });
        }

        /**
         * 1, 生成时： 分批，生成tag和face.
         * 2. 加载时, 预先加载 tfs_config.txt and face_config.txt, 读取对应的文件路径
         */
        public void startScanByDir() {
            VisitServices.from(mMediaItems).groupService(new ResultVisitor<MediaItem, String>() {
                @Override
                public String visit(MediaItem mediaItem, Object param) {
                    return FileUtils.getFileDir(mediaItem.item.getFilePath(), 1, true);
                }
            }).map(new MapResultVisitor<String, List<MediaItem>, Group>() {
                @Override
                public Group visit(KeyValuePair<String, List<MediaItem>> t, Object param) {
                    Group group = new Group(t.getValue());
                    group.dir = t.getKey();
                    return group;
                }
            }).fire(new FireVisitor<Group>() {
                @Override
                public Boolean visit(Group group, Object param) {
                    List<ImageResource> list = VisitServices.from(mImageRess).filter(new PredicateVisitor<ImageResource>() {
                        @Override
                        public Boolean visit(ImageResource imageResource, Object param) {
                            return imageResource.batchDir.equals(group.dir);
                        }
                    }).getAsList();
                    if(list.isEmpty()){
                        throw new IllegalStateException("you must call #loadImageResource");
                    }
                    ImageResource imageRes = list.get(0);
                    ConcurrentManager.getDefault().schedule(() -> doWithBacthRects(group, imageRes));
                    ConcurrentManager.getDefault().schedule(() -> doWithBacthTags(group, imageRes));
                    return null;
                }
            });
        }

        private void doWithBacthRects(Group group, ImageResource imageRes) {
            if(TextUtils.isEmpty(imageRes.rectsPath)){
                Logger.w(TAG, "doWithBacthRects", "no rect file for face data. group dir = " + group.dir);
                group.markDownRects();
                return;
            }
            group.rects = ImageDataLoader.loadRects(mContext, imageRes.rectsPath, null);
            group.markDownRects();
            doIfAllDone(group);
        }
        private void doWithBacthTags(Group group, ImageResource imageRes) {
            if(TextUtils.isEmpty(imageRes.tagPath)){
                Logger.w(TAG, "doWithBacthTags", "no tag file for tag data. group dir = " + group.dir);
                group.markDownTags();
                return;
            }
            group.tags = ImageDataLoader.loadTags(mContext, imageRes.tagPath, null);
            group.markDownTags();
            doIfAllDone(group);
        }

        private void doWidthRects(Group group, boolean singleRect) {
            if (rectsScanner != null) {
                //filename prefix
                final String prefix = singleRect ? null : group.filenamePrefix;

                List<KeyValuePair<MediaItem, String>> list = VisitServices.from(group.items)
                        .map2mapAsKey(new ResultVisitor<MediaItem, String>() {
                    @Override
                    public String visit(MediaItem item, Object param) {
                        String dir = FileUtils.getFileDir(item.item.getFilePath(), 2, true);
                        return rectsScanner.scan(mContext, item.item, dir, prefix);
                    }
                }).mapPair().getAsList();

                ListVisitService<KeyValuePair<MediaItem, String>> rectsService = VisitServices.from(list)
                        .filter(new PredicateVisitor<KeyValuePair<MediaItem, String>>() {
                            @Override
                            public Boolean visit(KeyValuePair<MediaItem, String> pair, Object param) {
                                if (pair.getValue() == null) {
                                    Logger.d(TAG, "doScan", "scan failed for image: "
                                            + pair.getKey().item.getFilePath());
                                }
                                return pair.getValue() != null;
                            }
                        }).asListService();
                //reduce the rects count
                group.reduceRects(list.size() - rectsService.size());

                if (singleRect) {
                    rectsService.fire(new FireVisitor<KeyValuePair<MediaItem, String>>() {
                        @Override
                        public Boolean visit(KeyValuePair<MediaItem, String> pair, Object param) {
                            group.addRects(ImageDataLoader.loadRects(mContext, pair.getValue(), null));
                            group.decrementRectCount();
                            return null;
                        }
                    });
                } else {
                    rectsService.fireBatch(new FireBatchVisitor<KeyValuePair<MediaItem, String>>() {
                        @Override
                        public Void visit(Collection<KeyValuePair<MediaItem, String>> coll, Object param) {
                            KeyValuePair<MediaItem, String> pair = coll.iterator().next();
                            group.rects = ImageDataLoader.loadRects(mContext, pair.getValue(), null);
                            return null;
                        }
                    });
                    group.reduceRects(rectsService.size());
                }
            } else {
                group.markDownRects();
            }
            doIfAllDone(group);
        }

        private void doWidthTags(Group group, boolean singleTag) {
            if (tagsScanner != null) {
                //filename prefix
                final String prefix = singleTag ? null : group.filenamePrefix;

                List<KeyValuePair<MediaItem, String>> list = VisitServices.from(group.items)
                        .map2mapAsKey(new ResultVisitor<MediaItem, String>() {
                    @Override
                    public String visit(MediaItem item, Object param) {
                        String dir = FileUtils.getFileDir(item.item.getFilePath(), 2, true);
                        return tagsScanner.scan(mContext, item.item, dir, prefix);
                    }
                }).mapPair().getAsList();

                ListVisitService<KeyValuePair<MediaItem, String>> service = VisitServices.from(list)
                        .filter(new PredicateVisitor<KeyValuePair<MediaItem, String>>() {
                            @Override
                            public Boolean visit(KeyValuePair<MediaItem, String> pair, Object param) {
                                if (pair.getValue() == null) {
                                    Logger.d(TAG, "doScan", "scan failed for image: " + pair.getKey().item.getFilePath());
                                }
                                return !TextUtils.isEmpty(pair.getValue());
                            }
                        }).asListService();
                //reduce the rects count
                group.reduceTags(list.size() - service.size());

                if (singleTag) {
                    service.fire(new FireVisitor<KeyValuePair<MediaItem, String>>() {
                        @Override
                        public Boolean visit(KeyValuePair<MediaItem, String> pair, Object param) {
                            group.addTags(ImageDataLoader.loadTags(mContext, pair.getValue(), null));
                            group.decrementTagCount();
                            return null;
                        }
                    });
                } else {
                    service.fireBatch(new FireBatchVisitor<KeyValuePair<MediaItem, String>>() {
                        @Override
                        public Void visit(Collection<KeyValuePair<MediaItem, String>> coll, Object param) {
                            KeyValuePair<MediaItem, String> pair = coll.iterator().next();
                            group.tags = ImageDataLoader.loadTags(mContext, pair.getValue(), null);
                            return null;
                        }
                    });
                    group.reduceTags(service.size());
                }
            } else {
                group.markDownTags();
            }
            doIfAllDone(group);
        }

        private void doIfAllDone(Group group) {
            if (group.isAllDone()) {
                group.normalize().fire(new MapFireVisitor<String, ImageData>() {
                    @Override
                    public Boolean visit(KeyValuePair<String, ImageData> pair, Object param) {
                        ImageData data = pair.getValue();
                        data.item.imageMeta.getFrameDataMap().put(0, data.frameData);
                        return null;
                    }
                });
                if (mGroupCount.decrementAndGet() == 0) {
                    //all done
                    ConcurrentUtils.awaitBarrier(mBarrier);
                }
            }
        }
    }

    private static class Group {

        final AtomicInteger mRectsCount;
        final AtomicInteger mTagsCount;

        final List<MediaItem> items;
        List<ImageDataLoader.ImageFaceRects> rects = new ArrayList<>();
        List<ImageDataLoader.ImageTags> tags = new ArrayList<>();
        /** the die of all media items */
        String dir;

        String filenamePrefix;

        Group(List<MediaItem> items) {
            this.items = items;
            this.mRectsCount = new AtomicInteger(items.size());
            this.mTagsCount = new AtomicInteger(items.size());
            filenamePrefix = ResourceInitializer.getImagesFileNamePrefix(items);
        }

        void reduceTags(int delta) {
            if(delta == 0){
                return;
            }
            int val;
            do {
                val = mTagsCount.get();
            } while (!mTagsCount.compareAndSet(val, val - delta));
        }

        void markDownTags() {
            int val;
            do {
                val = mTagsCount.get();
            } while (!mTagsCount.compareAndSet(val, 0));
        }

        void reduceRects(int delta) {
            if(delta == 0){
                return;
            }
            int val;
            do {
                val = mRectsCount.get();
            } while (!mRectsCount.compareAndSet(val, val - delta));
        }

        void markDownRects() {
            int val;
            do {
                val = mRectsCount.get();
            } while (!mRectsCount.compareAndSet(val, 0));
        }

        public void decrementTagCount() {
            mTagsCount.decrementAndGet();
        }

        public void decrementRectCount() {
            mRectsCount.decrementAndGet();
        }

        public MapVisitService<String, ImageData> normalize() {
            return VisitServices.from(items).normalize(null, rects, tags, new ResultVisitor<MediaItem, String>() {
                @Override
                public String visit(MediaItem mediaItem, Object param) {
                    return mediaItem.item.getFilePath();
                }
            }, new ResultVisitor<ImageDataLoader.ImageFaceRects, String>() {
                @Override
                public String visit(ImageDataLoader.ImageFaceRects rects, Object param) {
                    return rects.getSrcPath();
                }
            }, new ResultVisitor<ImageDataLoader.ImageTags, String>() {
                @Override
                public String visit(ImageDataLoader.ImageTags tags, Object param) {
                    return tags.getSrcPath();
                }
            }, new NormalizeVisitor<String, MediaItem, ImageDataLoader.ImageFaceRects, ImageDataLoader.ImageTags, ImageData>() {
                @Override
                public ImageData visit(String key, MediaItem item, ImageDataLoader.ImageFaceRects rects, ImageDataLoader.ImageTags tags, Object param) {
                    ImageData data = new ImageData();
                    data.item = item;
                    if(rects != null) {
                        data.frameData.setFaceRects(rects.getRects());
                        data.frameData.setFaceRectPath(rects.getRectsPath());
                    }
                    if(tags != null) {
                        data.frameData.setTag(tags.getTags());
                        data.frameData.setTagPath(tags.getTagPath());
                    }
                    return data;
                }
            });
        }

        public void addRects(List<ImageDataLoader.ImageFaceRects> rects) {
            if (this.rects == null) {
                this.rects = new ArrayList<>();
            }
            this.rects.addAll(rects);
        }

        public void addTags(List<ImageDataLoader.ImageTags> imageTags) {
            if (this.tags == null) {
                this.tags = new ArrayList<>();
            }
            this.tags.addAll(imageTags);
        }

        public boolean isAllDone() {
            return mTagsCount.get() == 0 && mRectsCount.get() == 0;
        }
    }

    private static class ImageData {
        MediaItem item;
        final VideoDataLoadUtils.FrameData frameData = new VideoDataLoadUtils.FrameData();
    }
}
