package com.heaven7.java.image.detect;

import com.heaven7.java.base.util.SparseArray;
import com.heaven7.java.base.util.Throwables;
import com.heaven7.java.image.ImageFactory;
import com.heaven7.java.image.ImageInitializer;
import com.heaven7.java.image.Matrix2;
import com.heaven7.java.visitor.FireIndexedVisitor;
import com.heaven7.java.visitor.PileVisitor;
import com.heaven7.java.visitor.collection.VisitServices;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static com.heaven7.java.image.Matrix2Utils.mergeByColumn;
import static com.heaven7.java.image.Matrix2Utils.mergeByRow;

/**
 * the abstract video manager for image detect.
 *
 * @param <T> the core data type
 */
public abstract class AbstractVideoManager<T> {

    public static final int DEFAULT_BATCH_SIZE = 4;

    private final VideoFrameDelegate vfd;
    private final String videoSrc;
    private final int frameGap;
    private final ImageDetector detector;

    private final SparseArray<T> dataMap = new SparseArray<>();

    private final AtomicBoolean markDone = new AtomicBoolean(false);
    /** the request count of network */
    private AtomicInteger count;

    private Callback<T> mCallback;

    private int batchSize = DEFAULT_BATCH_SIZE;

    public AbstractVideoManager(VideoFrameDelegate vfd, String videoSrc) {
        this(vfd, videoSrc, 1, ImageFactory.getImageInitializer().getImageDetector());
    }

    public AbstractVideoManager(
            VideoFrameDelegate vfd, String videoSrc, int gap, ImageDetector detector) {
        this.detector = detector;
        this.videoSrc = videoSrc;
        this.vfd = vfd;
        this.frameGap = gap;
    }

    public int getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }

    /**
     * detect the all frames of video
     *
     * @param callback the callback
     */
    public final void detect(Callback<T> callback) {
        if (mCallback != null) {
            throw new IllegalStateException();
        }
        this.mCallback = callback;
        final int duration = vfd.getDuration(videoSrc);
        onPreDetect(callback, duration);

        this.count = new AtomicInteger();
        int time = 0;
        while (time <= duration) {
            byte[] data = vfd.getFrame(videoSrc, time);
            count.incrementAndGet();
            onDetect(detector, callback, time, data);
            time += frameGap;
        }
        // mark done
        markDone.compareAndSet(false, true);
        checkDone();
    }

    /**
     * detect the all frames of video
     *
     * @param callback the callback
     */
    public final void detectBatch(Callback<T> callback) {
        if (mCallback != null) {
            throw new IllegalStateException();
        }
        this.mCallback = callback;
        final int duration = vfd.getDuration(videoSrc);
        onPreDetect(callback, duration);

        this.count = new AtomicInteger();
        int time = 0;
        int pileSize = 0;
        //the times list
        List<Integer> times = newList(1);
        //every two mat is a line
        List<Matrix2<Integer>> matLines = newList(2);
        Matrix2<Integer> single = null;
        while (time <= duration) {
            times.add(time);
            Matrix2<Integer> mat = vfd.getFrameMatrix(videoSrc, time);
            if (single == null) {
                single = mat;
            } else {
                // merge every two . as a line
                mergeByRow(single, mat);
                matLines.add(single);
                single = null;
            }
            if (pileSize == batchSize) {
                doDetectBatch(callback, times, matLines);
                // reset
                times = newList(1);
                matLines = newList(2);
            }
            time += frameGap;
        }
        // check left times
        if (!times.isEmpty()) {
            if (single != null) {
                if (matLines.isEmpty()) {
                    // only one
                    count.incrementAndGet();
                    onDetectBatch(detector, callback, times, transformMat(single));
                } else {
                    // just copy a new data. and merge as a line
                    Matrix2<Integer> mat2 = single.copy();
                    mergeByRow(single, mat2);
                    matLines.add(single);
                    // do detect batch
                    doDetectBatch(callback, times, matLines);
                }
            } else {
                if (!matLines.isEmpty()) {
                    doDetectBatch(callback, times, matLines);
                }
            }
        }

        // mark done
        markDone.compareAndSet(false, true);
        checkDone();
    }

    private void doDetectBatch(
            Callback<T> callback, List<Integer> times, List<Matrix2<Integer>> matLines) {
        count.incrementAndGet();
        Matrix2<Integer> mergedMat =
                VisitServices.from(matLines)
                        .pile(
                                new PileVisitor<Matrix2<Integer>>() {
                                    @Override
                                    public Matrix2<Integer> visit(
                                            Object o,
                                            Matrix2<Integer> mat1,
                                            Matrix2<Integer> mat2) {
                                        mergeByColumn(mat1, mat2);
                                        return mat1;
                                    }
                                });
        onDetectBatch(detector, callback, times, transformMat(mergedMat));
    }

    private <E> ArrayList<E> newList(int divide) {
        // divide = 2 means half capacity
        return new ArrayList<>(batchSize * 4 / 3 / divide + 1);
    }

    private void checkDone() {
        if (markDone.get() && count.get() == 0) {
            onDetectDone(mCallback, videoSrc);
            mCallback = null;
        }
    }

    /**
     * transform matrix to byte array. default image type is 1. format is 'jpg'.
     * @param mat the matrix
     * @return the byte array
     */
    protected byte[] transformMat(Matrix2<Integer> mat){
        ImageInitializer initer = ImageFactory.getImageInitializer();
        Throwables.checkNull(initer);
        //default BufferImage.TYPE_RGB = 1;
        return initer.getMatrix2Transformer().transform(mat, 1, "jpg");
    }

    /**
     * called when one time of image is detect done.
     *
     * @param time the times list, every element is time in seconds.
     */
    protected void publishDetectDone(List<Integer> time) {
        count.decrementAndGet();
        checkDone();
    }

    /**
     * called on pre detect
     *
     * @param callback the callback
     * @param duration the duration of video
     */
    protected void onPreDetect(Callback<T> callback, int duration) {}

    /**
     * called on all frames of video detect done
     *
     * @param mCallback the callback
     * @param videoSrc the video file. absolute file name
     */
    protected void onDetectDone(Callback<T> mCallback, String videoSrc) {
        mCallback.onCallback(videoSrc, dataMap);
    }

    /**
     * called on detect image which is from frame
     *
     * @param detector the image detector
     * @param callback the callback
     * @param time the time of this frame in seconds
     * @param data the image data.
     */
    protected abstract void onDetect(
            ImageDetector detector, Callback<T> callback, int time, byte[] data);

    /**
     * called on detect image which is from frame
     *
     * @param detector the image detector
     * @param callback the callback
     * @param times the times indicate the frames
     * @param batchData the batch image data.
     */
    protected abstract void onDetectBatch(
            ImageDetector detector,
            Callback<T> callback,
            List<Integer> times,
            byte[] batchData);

    public interface VideoFrameDelegate {
        /**
         * get the frame from video file.
         *
         * @param videoFile the video file
         * @param timeInSeconds the time to get the frame . in seconds
         * @return the frame data.
         */
        byte[] getFrame(String videoFile, long timeInSeconds);

        /**
         * get the frame as int array. the stride is width
         *
         * @param videoFile the video file
         * @param timeInSeconds the time in seconds
         * @return the int array.
         */
        Matrix2<Integer> getFrameMatrix(String videoFile, long timeInSeconds);

        /** get the video duration. in seconds */
        int getDuration(String videoFile);
    }

    public interface Callback<T> {
        /**
         * callback on all frames data done for video
         *
         * @param videoSrc the video file
         * @param dataMap key is frame time is seconds, value is core data
         */
        void onCallback(String videoSrc, SparseArray<T> dataMap);
    }

    protected class InternalCallback implements ImageDetector.OnDetectCallback<T> {
        public final List<Integer> times;

        public InternalCallback(int time) {
            this.times = Arrays.asList(time);
        }

        public InternalCallback(List<Integer> times) {
            Throwables.checkEmpty(times);
            this.times = times;
        }

        public void markEnd() {
            publishDetectDone(times);
        }

        @Override
        public void onFailed(int code, String msg) {
            markEnd();
        }

        @Override
        public void onSuccess(T data) {
            dataMap.put(times.get(0), data);
            markEnd();
        }

        @Override
        public void onBatchSuccess(List<T> batchList) {
            VisitServices.from(times)
                    .fireWithIndex(
                            new FireIndexedVisitor<Integer>() {
                                @Override
                                public Void visit(Object param, Integer time, int index, int size) {
                                    T t = batchList.get(index);
                                    if (t != null) {
                                        dataMap.put(time, t);
                                    }
                                    return null;
                                }
                            });
            markEnd();
        }
    }
}
