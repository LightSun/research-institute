package com.heaven7.ve.colorgap.helper;

import com.heaven7.ve.utils.MapRecognizer;

import java.util.Map;

/**
 * @author heaven7
 */
public class PersonCountRecognizer extends MapRecognizer<Float> {

    public PersonCountRecognizer(Map<String, Float> mMap) {
        super(mMap);
    }

    public float getScore(int personCount){
        return getValue(personCount + "");
    }

}
