package com.heaven7.ve.colorgap;

/**
 * the frame data delegate
 * @param <T> the frame data.
 * @author heaven7
 */
public interface FrameDataDelegate<T> {

    /**
     * get frame data for target image path.
     * @param srcItem the src media part item
     * @param imgPath the image path .used
     * @return the frame data
     */
    T getFrameData(MediaPartItem srcItem, String imgPath);

    /**
     * call this to save key point data.
     * @param srcItem the src media part item
     * @param imgPath the image path .used
     * @param data the data to save
     */
    void saveFrameData(MediaPartItem srcItem, String imgPath, T data);

}
