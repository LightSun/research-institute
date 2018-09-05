package com.heaven7.ve.colorgap.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.heaven7.core.util.Logger;
import com.heaven7.java.base.util.Predicates;
import com.heaven7.java.visitor.FireVisitor;
import com.heaven7.java.visitor.MapFireVisitor;
import com.heaven7.java.visitor.NormalizeVisitor;
import com.heaven7.java.visitor.ResultVisitor;
import com.heaven7.java.visitor.collection.KeyValuePair;
import com.heaven7.java.visitor.collection.MapVisitService;
import com.heaven7.java.visitor.collection.VisitServices;
import com.heaven7.utils.*;
import com.heaven7.ve.Constants;
import com.heaven7.ve.collect.ColorGapPerformanceCollector;
import com.heaven7.ve.colorgap.*;
import com.vida.common.IOUtils;
import com.vida.common.entity.MediaData;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;

import static com.heaven7.ve.collect.ColorGapPerformanceCollector.MODULE_ANALYSE_MEDIA;

/**
 * the image analyse helper help we analyse image rects and tags.
 *
 * @author heaven7
 */
/*public*/ class ImageAnalyseHelper {

    private static final String TAG = "ImageAnalyseHelper";
    private final Gson mGson = new GsonBuilder().create();

    private final ImageResource mImageRes = new ImageResource();

    public void scanAndLoad(Context context, List<MediaItem> imageItems, final CyclicBarrier barrier) {
        ConcurrentManager.getDefault().submit(() -> scanAndLoad0(context, imageItems, barrier));
    }

    private void scanAndLoad0(Context context, List<MediaItem> imageItems, CyclicBarrier barrier) {
        if (imageItems.isEmpty()) {
            ConcurrentUtils.awaitBarrier(barrier);
        } else {
            //load image
            new BatchScanner(context, imageItems, barrier).startScanByDir();
        }
    }

    /**
     * load all image resource for batch images.such as rects_path, tag_path.
     * note this must called in sub thread.
     *  @param context the color gap context
     * @param param the resource data dir of batch images.
     */
    public void loadImageResource(ColorGapContext context, ColorGapParam param) {
        String batchImageDataDir = param.getBatchImageDataDir();
        Logger.d(TAG, "loadImageResource", " dataDir = " + batchImageDataDir);
        ImageResource imageRes = this.mImageRes;
        imageRes.setBatchDataDir(batchImageDataDir);
        imageRes.setResourceDataDir(param.getResourceDataDir());

        File face_config = new File(batchImageDataDir, "face_config.txt");
        File tfs_config = new File(batchImageDataDir, "tfs_config.txt");
        //csv, imapath imgpath ...
        final TextReadHelper<ConfigLine> reader = new TextReadHelper<>(new ConfigLineCallback());
        if (face_config.exists()) {
            List<ConfigLine> faceLines = reader.read(null, face_config.getAbsolutePath());
            if (!Predicates.isEmpty(faceLines)) {
                String rectsPath = faceLines.get(0).getCsvRectsPath();
                imageRes.setRectsPath(rectsPath);
                Logger.d(TAG, "loadImageResource", "set Rects path done >>> rectsPath = " + rectsPath);
            }
        }
        //tfs_config
        if (tfs_config.exists()) {
            List<ConfigLine> faceLines = reader.read(null, tfs_config.getAbsolutePath());
            if (!Predicates.isEmpty(faceLines)) {
                String tagPath = faceLines.get(0).getCsvTagPath();
                imageRes.setTagPath(tagPath);
                Logger.d(TAG, "loadImageResource", "set Tag path done >>> tagPath = " + tagPath);
            }
        }
        //load high light data
    }


    private static class ImageResource {
        String resourceDataDir;
        String batchDataDir;
        String rectsPath;
        String tagPath;

        public String getBatchDataDir() {
            return batchDataDir;
        }
        public void setBatchDataDir(String batchDataDir) {
            this.batchDataDir = batchDataDir;
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

        public void setResourceDataDir(String resourceDataDir) {
            this.resourceDataDir = resourceDataDir;
        }
        public String getResourceDataDir() {
            return resourceDataDir;
        }
    }

    /**
     * the line of config file
     */
    private static class ConfigLine {
        String dataPath;// may be rects or tfrecord path
        List<String> imagePaths;

        public ConfigLine(String line) {
            String[] strs = line.split(",");
            if (!Predicates.isEmpty(strs)) {
                this.dataPath = strs[0];
                if (strs.length >= 2) {
                    imagePaths = Arrays.asList(strs[1].split(" "));
                }
            }
        }

        public String getCsvRectsPath() {
            return dataPath;
        }

        public String getCsvTagPath() {
            if (TextUtils.isEmpty(dataPath)) {
                return null;
            }
            String dir = FileUtils.getFileDir(dataPath, 1, true);
            String fileName = FileUtils.getFileName(dataPath);
            final String baseFileName;
            if (fileName.endsWith("_outputs")) {
                baseFileName = fileName.substring(0, fileName.lastIndexOf("outputs"));
            } else if (fileName.endsWith("_output")) {
                baseFileName = fileName.substring(0, fileName.lastIndexOf("output"));
            } else {
                throw new IllegalStateException("wrong file. please check your py script of gen.");
            }
            return dir + File.separator + baseFileName + "predictions.csv";
        }
    }

    private static class ImageResBatchLine {
        String imageResourceDir;
        int imageCount;

        public ImageResBatchLine(String line) {
            String[] strs = line.split(",");
            if (!Predicates.isEmpty(strs)) {
                this.imageResourceDir = strs[0];
                if (strs.length >= 2) {
                    imageCount = Integer.valueOf(strs[1]);
                }
            }
        }
    }

    private static class ConfigLineCallback extends TextReadHelper.BaseAssetsCallback<ConfigLine> {
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

        BatchScanner(Context context, List<MediaItem> mediaItems, CyclicBarrier barrier) {
            this.mContext = context;
            this.mMediaItems = mediaItems;
            this.mBarrier = barrier;
        }

        public ColorGapPerformanceCollector getCollector(){
            ColorGapContext cgc = (ColorGapContext) mContext;
            return cgc.getColorGapPerformanceCollector();
        }

        /**
         * 1, 生成时： 分批，生成tag和face.
         * 2. 加载时, 预先加载 tfs_config.txt and face_config.txt, 读取对应的文件路径
         */
        public void startScanByDir() {
            Logger.d(TAG, "startScanByDir");
            Group group = new Group(mMediaItems);
            ImageResource imageRes = ImageAnalyseHelper.this.mImageRes;
            ConcurrentManager.getDefault().submit(() -> doWithBacthRects(group, imageRes));
            ConcurrentManager.getDefault().submit(() -> doWithBacthTags(group, imageRes));
            ConcurrentManager.getDefault().submit(() -> doWithHighLights(group, imageRes));
        }

        private void doWithBacthRects(Group group, ImageResource imageRes) {
            if (TextUtils.isEmpty(imageRes.rectsPath)) {
                Logger.w(TAG, "doWithBacthRects", "no rect file for face data. data dir = " + mImageRes.getBatchDataDir());
                group.markDownRects();
                return;
            }
            group.rects = ImageDataLoader.loadRects(mContext, imageRes.rectsPath, null);
            group.markDownRects();
            getCollector().addMessage(MODULE_ANALYSE_MEDIA, TAG, "doWithBacthRects", "load batch rects done");
            doIfAllDone(group);
        }

        private void doWithBacthTags(Group group, ImageResource imageRes) {
            if (TextUtils.isEmpty(imageRes.tagPath)) {
                Logger.w(TAG, "doWithBacthTags", "no rect file for tag data. data dir = " + mImageRes.getBatchDataDir());
                group.markDownTags();
                return;
            }
            group.tags = ImageDataLoader.loadTags(mContext, imageRes.tagPath, null);
            getCollector().addMessage(MODULE_ANALYSE_MEDIA, TAG, "doWithBacthTags", "load batch tags done");
            group.markDownTags();
            doIfAllDone(group);
        }

        private void doWithHighLights(Group group, ImageResource imageRes) {
            VisitServices.from(group.items).fire(new FireVisitor<MediaItem>() {
                @Override
                public Boolean visit(MediaItem item, Object param) {
                    final String fileName = FileUtils.getFileName(item.item.getFilePath());
                    //...data/highlight/filename.ihighlight.
                    final String highLightFile = imageRes.getResourceDataDir() + File.separator
                            + Constants.DIR_HIGH_LIGHT + File.separator + fileName
                            + "." + Constants.EXTENSION_IMAGE_HIGH_LIGHT;
                    File file = new File(highLightFile);
                    if (!file.exists()) {
                        Logger.w(TAG, "doWithHighLights", "can't find high light file. " + highLightFile);
                        return true;
                    }
                    FileReader reader = null;
                    try {
                        String json = IOUtils.readString(reader = new FileReader(file));
                        MediaData mediaData = mGson.fromJson(json, MediaData.class);
                        item.imageMeta.setMediaData(mediaData);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        IOUtils.closeQuietly(reader);
                    }
                    return null;
                }
            });
            group.markDownHighLight();
            getCollector().addMessage(MODULE_ANALYSE_MEDIA, TAG, "doWithHighLights", "load image high-light done");
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
                ConcurrentUtils.awaitBarrier(mBarrier);
            }
        }
    }

    private static class Group {

        final AtomicInteger mRectsCount;
        final AtomicInteger mTagsCount;
        final AtomicInteger mHighLightCount;

        final List<MediaItem> items;
        List<ImageDataLoader.ImageFaceRects> rects = new ArrayList<>();
        List<ImageDataLoader.ImageTags> tags = new ArrayList<>();

        String filenamePrefix;

        Group(List<MediaItem> items) {
            this.items = items;
            this.mRectsCount = new AtomicInteger(items.size());
            this.mTagsCount = new AtomicInteger(items.size());
            this.mHighLightCount = new AtomicInteger(items.size());
            filenamePrefix = ResourceInitializer.getImagesFileNamePrefix(items);
        }

        void reduceTags(int delta) {
            if (delta == 0) {
                return;
            }
            int val;
            do {
                val = mTagsCount.get();
            } while (!mTagsCount.compareAndSet(val, val - delta));
        }

        void markDownHighLight() {
            int val;
            do {
                val = mHighLightCount.get();
            } while (!mHighLightCount.compareAndSet(val, 0));
        }

        void markDownTags() {
            int val;
            do {
                val = mTagsCount.get();
            } while (!mTagsCount.compareAndSet(val, 0));
        }

        void reduceRects(int delta) {
            if (delta == 0) {
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
                    if (rects != null) {
                        data.frameData.setFaceRects(rects.getRects());
                        data.frameData.setFaceRectPath(rects.getRectsPath());
                    }
                    if (tags != null) {
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
            return mTagsCount.get() == 0 && mRectsCount.get() == 0
                    && mHighLightCount.get() == 0;
        }
    }

    private static class ImageData {
        MediaItem item;
        final VideoDataLoadUtils.FrameData frameData = new VideoDataLoadUtils.FrameData();
    }
}
