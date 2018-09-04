package com.heaven7.ve.colorgap.impl;

import com.heaven7.java.image.ImageFactory;
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
                if(item.getItem().isVideo()){
                    return ImageFactory.getImageInitializer().getVideoFrameDelegate().getFrameImagePath(
                            item.item.getFilePath(), item.getKeyFrameTime());
                }
                return item.getItem().getFilePath();
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
        VisitServices.from(map).map2MapValue(new MapResultVisitor<String, T, MediaPartItem>() {
            @Override
            public MediaPartItem visit(KeyValuePair<String, T> t, Object param) {
                return VisitServices.from(mPairs).query(new PredicateVisitor<Pair>() {
                    @Override
                    public Boolean visit(Pair pair, Object param) {
                        return pair.imgPath.equals(t.getKey());
                    }
                }).partItem;
            }
        }).fire(new MapFireVisitor<MediaPartItem, T>() {
            @Override
            public Boolean visit(KeyValuePair<MediaPartItem, T> pair, Object param) {
                onProcess(pair.getKey(), pair.getValue());
                return null;
            }
        });
        onDone();
    }

    protected abstract AbstractBatchImageManager<T> onCreateBatchImageManager(List<String> images);

    /**
     * called when request done and we want to do with the part and value.
     * @param part the media part
     * @param value the value
     */
    protected abstract void onProcess(MediaPartItem part, T value);

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
