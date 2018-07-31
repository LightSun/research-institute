package com.heaven7.java.image.detect;

import java.util.List;

/**
 * the image detector
 * @author heaven7
 */
public interface ImageDetector {

    /**
     * detect high light
     * @param imageData the image data
     * @param callback the callback
     */
    void detectHighLight(byte[] imageData, OnDetectCallback callback);

    /**
     * detect key-points
     * @param imageData the image data
     * @param callback the callback
     */
    void detectKeyPoints(byte[] imageData, OnDetectCallback callback);
    /**
     * detect subject identification
     * @param imageData the image data
     * @param callback the callback
     */
    void detectSubjectIdentification(byte[] imageData, OnDetectCallback callback);
    /**
     * detect high light in batch
     * @param imageData the image data
     * @param callback the callback
     */
    void detectHighLightBatch(byte[] imageData, OnDetectCallback callback);
    /**
     * detect key-points in batch
     * @param imageData the image data
     * @param callback the callback
     */
    void detectKeyPointsBatch(byte[] imageData, OnDetectCallback callback);
    /**
     * detect subject identification in batch
     * @param imageData the image data
     * @param callback the callback
     */
    void detectSubjectIdentificationBatch(byte[] imageData, OnDetectCallback callback);

    /**
     * the detect callback
     * @param <T> the core type of result
     */
    interface OnDetectCallback<T>{
        /**
         * called when failed
         * @param code the code
         * @param msg the msg.
         */
        void onFailed(int code, String msg);

        /**
         * called on detect success.
         * @param data the data
         */
        void onSuccess(T data);
        /**
         * called on batch success. note you should keep the result order of request frames.
         * @param batchList the batch list.
         */
        void onBatchSuccess(List<T> batchList);
    }

}
