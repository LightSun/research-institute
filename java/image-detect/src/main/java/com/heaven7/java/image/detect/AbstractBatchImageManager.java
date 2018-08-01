package com.heaven7.java.image.detect;

import com.heaven7.java.image.ImageFactory;
import com.heaven7.java.image.ImageReader;
import com.heaven7.java.image.utils.BatchProcessor;
import com.heaven7.java.visitor.FireVisitor;
import com.heaven7.java.visitor.collection.VisitServices;
import com.heaven7.java.visitor.util.SparseArray;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * the batch image manager
 *
 * @author heaven7
 */
public abstract class AbstractBatchImageManager<T> extends BatchProcessor{

    private final List<String> mImages;
    private final ImageDetector mImageDetector;
    private final ImageReader mImageReader = ImageFactory.getImageInitializer().getImageReader();
    private Callback<T> mCallback;

    private Map<String, T> mDataMap;

    public AbstractBatchImageManager(List<String> mImages, ImageDetector detector) {
        this.mImages = mImages;
        this.mImageDetector = detector;
    }

    public void detect(Callback<T> callback) {
        markStart();
        this.mCallback = callback;
        this.mDataMap = new HashMap<>();
        VisitServices.from(mImages).fire(new FireVisitor<String>() {
            @Override
            public Boolean visit(String file, Object param) {
                byte[] data = readBytes(file).getData();
                addCount(1);
                onDetect(mImageDetector, mCallback, file, data);
                return null;
            }
        });
        markEnd();
    }

    @Override
    protected void reset() {
        super.reset();
        mCallback = null;
    }

    @Override
    protected void onDone() {
        mCallback.onCallback(mDataMap);
    }

    protected ImageReader.ImageInfo readBytes(String imgFile) {
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
            onTasksEnd(1);
        }
        @Override
        public void onSuccess(T data) {
            mDataMap.put(imageFile, data);
            onTasksEnd(1);
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
