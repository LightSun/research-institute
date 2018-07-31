package com.heaven7.java.image.detect;

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
     * @param count     the image count
     * @param imageData the image data
     * @param callback  the callback
     */
    void detectHighLightBatch(int count, byte[] imageData, OnDetectCallback<List<IHighLightData>> callback);

    /**
     * detect key-points in batch
     *
     * @param count     the image count
     * @param imageData the image data
     * @param callback  the callback
     */
    void detectKeyPointsBatch(int count, byte[] imageData, OnDetectCallback<List<KeyPointData>> callback);

    /**
     * detect subject identification in batch
     *
     * @param count     the image count
     * @param imageData the image data
     * @param callback  the callback
     */
    void detectSubjectIdentificationBatch(int count, byte[] imageData, OnDetectCallback<Location> callback);

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
         * @param batchList the batch list.
         */
        void onBatchSuccess(List<T> batchList);
    }

}
