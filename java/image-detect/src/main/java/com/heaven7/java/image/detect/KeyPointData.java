package com.heaven7.java.image.detect;

/**
 * the key-point of person
 */
public interface KeyPointData extends IDataTransformer<KeyPointData>{

    /**
     * get the key point count
     * @return the key point count
     */
    int getKeyPointCount();

    /**
     * get the body location
     * @return the location
     */
    LocationF getLocation();
}
