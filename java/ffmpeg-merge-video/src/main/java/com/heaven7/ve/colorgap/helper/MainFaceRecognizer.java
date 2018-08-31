package com.heaven7.ve.colorgap.helper;

import com.heaven7.ve.utils.MapRecognizer;

import java.util.Map;

/**
 * @author heaven7
 */
public class MainFaceRecognizer extends MapRecognizer<Float> {

    public MainFaceRecognizer(Map<String, Float> mMap) {
        super(mMap);
    }

    public float getScore(int mainFaceCount){
        return getValue(mainFaceCount + "");
    }

}
