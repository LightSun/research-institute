package com.heaven7.ve.colorgap.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.heaven7.core.util.Logger;
import com.heaven7.java.base.util.Predicates;
import com.heaven7.java.visitor.*;
import com.heaven7.java.visitor.collection.KeyValuePair;
import com.heaven7.java.visitor.collection.ListVisitService;
import com.heaven7.java.visitor.collection.MapVisitService;
import com.heaven7.java.visitor.collection.VisitServices;
import com.heaven7.utils.*;
import com.heaven7.ve.Constants;
import com.heaven7.ve.colorgap.*;
import com.heaven7.ve.colorgap.impl.montage.ImageDataDirMapperImpl;
import com.vida.common.IOUtils;
import com.vida.common.entity.MediaData;

import java.io.File;
import java.io.FileReader;
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

    private static final String TAG = "ImageAnalyseHelper";
    private final Gson mGson = new GsonBuilder().create();

    private ImageDataDirMapper mDataDirMapper = new ImageDataDirMapperImpl();
    private List<ImageResource> mImageRess;


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
     *
     * @param context
     * @param resourceDir the resource dir of save all medias.
     */
    public void loadImageResource(ColorGapContext context, String resourceDir) {
        File batchFileList = new File(resourceDir, "image_batch_list.txt");
        if (!batchFileList.exists()) {
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
        //load face and tag data
        mImageRess = VisitServices.from(lines).map(new ResultVisitor<ImageResBatchLine, ImageResource>() {
            @Override
            public ImageResource visit(ImageResBatchLine line, Object param) {
                String dataDir = mDataDirMapper.mapDataDir(line.imageResourceDir);
                Logger.d(TAG, "loadImageResource", "res dir = " + line.imageResourceDir + " ,dataDir = " + dataDir);
                ImageResource imageRes = new ImageResource();
                imageRes.setBatchDir(line.imageResourceDir);

                File face_config = new File(dataDir, "face_config.txt");
                File tfs_config = new File(dataDir, "tfs_config.txt");
                //csv, imapath imgpath ...
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
                return imageRes;
            }
        }).getAsList();
        //load high light data

    }

    private static class ImageResource {
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
        private AtomicInteger mGroupCount;

        BatchScanner(Context context, List<MediaItem> mediaItems, CyclicBarrier barrier) {
            this.mContext = context;
            this.mMediaItems = mediaItems;
            this.mBarrier = barrier;
        }

        /**
         * 1, 生成时： 分批，生成tag和face.
         * 2. 加载时, 预先加载 tfs_config.txt and face_config.txt, 读取对应的文件路径
         */
        public void startScanByDir() {
            Logger.d(TAG, "startScanByDir");
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
            }).save(new SaveVisitor<Group>() {
                @Override
                public void visit(Collection<Group> collection) {
                    mGroupCount = new AtomicInteger(collection.size());
                }
            })
                    .fire(new FireVisitor<Group>() {
                        @Override
                        public Boolean visit(Group group, Object param) {
                            List<ImageResource> list = VisitServices.from(mImageRess).filter(new PredicateVisitor<ImageResource>() {
                                @Override
                                public Boolean visit(ImageResource imageResource, Object param) {
                                    return imageResource.batchDir.equals(group.dir);
                                }
                            }).getAsList();
                            if (list.isEmpty()) {
                                throw new IllegalStateException("you must call #loadImageResource");
                            }
                            ImageResource imageRes = list.get(0);
                            ConcurrentManager.getDefault().submit(() -> doWithBacthRects(group, imageRes));
                            ConcurrentManager.getDefault().submit(() -> doWithBacthTags(group, imageRes));
                            ConcurrentManager.getDefault().submit(() -> doWithHighLights(group, imageRes));
                            return null;
                        }
                    });
        }

        private void doWithBacthRects(Group group, ImageResource imageRes) {
            if (TextUtils.isEmpty(imageRes.rectsPath)) {
                Logger.w(TAG, "doWithBacthRects", "no rect file for face data. group dir = " + group.dir);
                group.markDownRects();
                return;
            }
            group.rects = ImageDataLoader.loadRects(mContext, imageRes.rectsPath, null);
            group.markDownRects();
            doIfAllDone(group);
        }

        private void doWithBacthTags(Group group, ImageResource imageRes) {
            if (TextUtils.isEmpty(imageRes.tagPath)) {
                Logger.w(TAG, "doWithBacthTags", "no tag file for tag data. group dir = " + group.dir);
                group.markDownTags();
                return;
            }
            group.tags = ImageDataLoader.loadTags(mContext, imageRes.tagPath, null);
            group.markDownTags();
            doIfAllDone(group);
        }

        private void doWithHighLights(Group group, ImageResource imageRes) {
            VisitServices.from(group.items).fire(new FireVisitor<MediaItem>() {
                @Override
                public Boolean visit(MediaItem item, Object param) {
                    String fileName = FileUtils.getFileName(item.item.getFilePath());
                    String fullDir = FileUtils.getFileDir(item.item.getFilePath(), 1, true);
                    String dir = FileUtils.getFileDir(item.item.getFilePath(), 1, false);
                    String dataDir = mDataDirMapper.mapDataDir(fullDir);
                    //highlight file. .../data/highlight/dir/xxx.ihighlight
                    String hlFilename = dataDir + File.separator + Constants.DIR_HIGH_LIGHT
                            + File.separator + dir + File.separator
                            + fileName + "." + Constants.EXTENSION_IMAGE_HIGH_LIGHT;
                    File file = new File(hlFilename);
                    if (!file.exists()) {
                        Logger.w(TAG, "doWithHighLights", "can't find high light file. " + hlFilename);
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
        final AtomicInteger mHighLightCount;

        final List<MediaItem> items;
        List<ImageDataLoader.ImageFaceRects> rects = new ArrayList<>();
        List<ImageDataLoader.ImageTags> tags = new ArrayList<>();
        /**
         * the die of all media items
         */
        String dir;

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
