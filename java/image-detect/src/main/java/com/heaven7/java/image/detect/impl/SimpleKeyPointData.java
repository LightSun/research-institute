package com.heaven7.java.image.detect.impl;

import com.heaven7.java.image.detect.KeyPointData;
import com.heaven7.java.image.detect.LocationF;

/**
 * @author heaven7
 */
public class SimpleKeyPointData implements KeyPointData {

    private int keyPointCount;
    private LocationF location;

    public void setKeyPointCount(int keyPointCount) {
        this.keyPointCount = keyPointCount;
    }
    public void setLocation(LocationF location) {
        this.location = location;
    }

    @Override
    public int getKeyPointCount() {
        return keyPointCount;
    }

    @Override
    public LocationF getLocation() {
        return location;
    }
}
