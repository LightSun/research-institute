package com.heaven7.ve.colorgap.impl;

import com.heaven7.java.base.anno.Nullable;
import com.heaven7.java.image.detect.AbstractBatchImageManager;
import com.heaven7.java.visitor.*;
import com.heaven7.java.visitor.collection.KeyValuePair;
import com.heaven7.java.visitor.collection.VisitServices;
import com.heaven7.ve.colorgap.MediaPartItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author heaven7
 */
public abstract class BaseRecognizeHelper<T> implements AbstractBatchImageManager.Callback<T>{

    private final List<Pair> mPairs = new ArrayList<>();
    private final AbstractBatchImageManager<T> mBIM;

    public BaseRecognizeHelper(List<MediaPartItem> mItems) {
        List<String> images = VisitServices.from(mItems).map(new ResultVisitor<MediaPartItem, String>() {
            @Override
            public String visit(MediaPartItem item, Object param) {
                return item.getKeyFrameImagePath();
            }
        }).getAsList();
        //save as pair
        VisitServices.from(images).fireWithIndex(new FireIndexedVisitor<String>() {
            @Override
            public Void visit(Object param, String imgPath, int index, int size) {
                mPairs.add(new Pair(mItems.get(index), imgPath));
                return null;
            }
        });
        mBIM = onCreateBatchImageManager(images);
    }

    public void start(){
        mBIM.detect(this);
    }

    @Override
    public final void onCallback(Map<String, T> map) {
        VisitServices.from(mPairs).fire(new FireVisitor<Pair>() {
            @Override
            public Boolean visit(Pair pair, Object param) {
                T value = map.get(pair.imgPath);
                onProcess(pair.partItem, pair.imgPath, value);
                return null;
            }
        });
        onDone();
    }

    protected abstract AbstractBatchImageManager<T> onCreateBatchImageManager(List<String> images);

    /**
     * called when request done and we want to do with the part and value.
     * @param part the media part
     * @param imgPath the image path used to request
     * @param value the value, null means request success. but data is empty
     */
    protected abstract void onProcess(MediaPartItem part, String imgPath, @Nullable T value);

    /**
     * called on request and set property done done
     */
    protected abstract void onDone();

    private static class Pair{
        final MediaPartItem partItem;
        final String imgPath;

        public Pair(MediaPartItem partItem, String imgPath) {
            this.partItem = partItem;
            this.imgPath = imgPath;
        }
    }
}
