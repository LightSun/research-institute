package com.heaven7.ve.image;

/**
 * the key-point of person
 */
public interface KeyPointData {

    /**
     * indicate has target count of keypoints or not
     * @param count the count
     * @return true if the count of key-point has reached.
     */
    boolean hasKeyPoints(int count);
}
