package com.heaven7.java.image.detect;

import com.heaven7.java.image.ImageDetectFactory;
import com.heaven7.java.image.ImageReader;
import com.heaven7.java.visitor.FireVisitor;
import com.heaven7.java.visitor.collection.VisitServices;
import com.heaven7.java.visitor.util.SparseArray;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * the batch image manager
 *
 * @author heaven7
 */
public abstract class AbstractBatchImageManager<T> {

    private final List<String> mImages;
    private final ImageDetector mImageDetector;
    private final ImageReader mImageReader = ImageDetectFactory.getImageInitializer().getImageReader();
    private final AtomicInteger mCount = new AtomicInteger();
    private final AtomicBoolean mMarkDone = new AtomicBoolean();
    private Callback<T> mCallback;

    private Map<String, T> mDataMap;

    public AbstractBatchImageManager(List<String> mImages, ImageDetector detector) {
        this.mImages = mImages;
        this.mImageDetector = detector;
    }

    public void detect(Callback<T> callback) {
        if (mCallback != null) {
            throw new IllegalStateException();
        }
        this.mCallback = callback;
        this.mDataMap = new HashMap<>();
        VisitServices.from(mImages).fire(new FireVisitor<String>() {
            @Override
            public Boolean visit(String file, Object param) {
                byte[] data = readBytes(file);
                mCount.incrementAndGet();
                onDetect(mImageDetector, mCallback, file, data);
                return null;
            }
        });
        mMarkDone.compareAndSet(false, true);
        checkDone();
    }

    private void checkDone() {
        if (mMarkDone.get() && mCount.get() == 0) {
            onDetectDone();
        }
    }

    private void onDetectDone() {
        mCallback.onCallback(mDataMap);
        mMarkDone.compareAndSet(true, false);
        mCount.set(0);
    }

    protected void publishDetectDone() {
        mCount.decrementAndGet();
        checkDone();
    }

    protected byte[] readBytes(String imgFile) {
        return mImageReader.readBytes(imgFile, "jpg");
    }

    /**
     * called on detect image which is from frame
     *
     * @param detector the image detector
     * @param callback the callback
     * @param imgFile  the image file
     * @param data     the image data.
     */
    protected abstract void onDetect(
            ImageDetector detector, Callback<T> callback, String imgFile, byte[] data);

    protected class InternalCallback implements ImageDetector.OnDetectCallback<T> {

        final String imageFile;

        public InternalCallback(String imageFile) {
            this.imageFile = imageFile;
        }

        @Override
        public void onFailed(int code, String msg) {
            publishDetectDone();
        }
        @Override
        public void onSuccess(T data) {
            mDataMap.put(imageFile, data);
            publishDetectDone();
        }
        @Override
        public void onBatchSuccess(SparseArray<T> batchList) {
            throw new UnsupportedOperationException();
        }
    }


    public interface Callback<T> {
        void onCallback(Map<String, T> dataMap);
    }
}
