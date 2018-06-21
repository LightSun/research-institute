package com.heaven7.ve.colorgap.impl;

import com.heaven7.core.util.Logger;
import com.heaven7.java.visitor.*;
import com.heaven7.java.visitor.collection.KeyValuePair;
import com.heaven7.java.visitor.collection.ListVisitService;
import com.heaven7.java.visitor.collection.MapVisitService;
import com.heaven7.java.visitor.collection.VisitServices;
import com.heaven7.utils.ConcurrentUtils;
import com.heaven7.utils.FileUtils;
import com.heaven7.utils.TextUtils;
import com.heaven7.ve.Context;
import com.heaven7.ve.colorgap.*;

import java.util.ArrayList;
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
