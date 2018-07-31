package com.heaven7.java.image.detect;

import com.heaven7.java.visitor.util.SparseArray;

import java.util.List;

/**
 * the image detector
 *
 * @author heaven7
 */
public interface ImageDetector {

    /**
     * detect high light
     *
     * @param imageData the image data
     * @param callback  the callback
     */
    void detectHighLight(byte[] imageData, OnDetectCallback<List<IHighLightData>> callback);//one to many

    /**
     * detect key-points
     *
     * @param imageData the image data
     * @param callback  the callback
     */
    void detectKeyPoints(byte[] imageData, OnDetectCallback<List<KeyPointData>> callback); //one to many

    /**
     * detect subject identification
     *
     * @param imageData the image data
     * @param callback  the callback
     */
    void detectSubjectIdentification(byte[] imageData, OnDetectCallback<Location> callback);

    /**
     * detect high light in batch
     *
     * @param info     the batch info
     * @param imageData the image data
     * @param callback  the callback
     */
    void detectHighLightBatch(BatchInfo info, byte[] imageData, OnDetectCallback<List<IHighLightData>> callback);

    /**
     * detect key-points in batch
     *
     * @param info     the batch info
     * @param imageData the image data
     * @param callback  the callback
     */
    void detectKeyPointsBatch(BatchInfo info, byte[] imageData, OnDetectCallback<List<KeyPointData>> callback);

    /**
     * detect subject identification in batch
     *
     * @param info     the batch info
     * @param imageData the image data
     * @param callback  the callback
     */
    void detectSubjectIdentificationBatch(BatchInfo info, byte[] imageData, OnDetectCallback<Location> callback);

    /**
     * the detect callback
     *
     * @param <T> the core type of result
     */
    interface OnDetectCallback<T> {
        /**
         * called when failed
         *
         * @param code the code
         * @param msg  the msg.
         */
        void onFailed(int code, String msg);

        /**
         * called on detect success.
         *
         * @param data the data
         */
        void onSuccess(T data);

        /**
         * called on batch success. note you should keep the result order of request frames.
         *
         * @param batchData the batch data. key is the order of image. start from 1.
         */
        void onBatchSuccess(SparseArray<T> batchData);
    }

}
