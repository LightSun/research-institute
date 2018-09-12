package com.heaven7.ve.colorgap.helper;


import com.heaven7.ve.colorgap.MetaInfo;
import com.heaven7.ve.colorgap.MetaInfoUtils;
import com.heaven7.ve.utils.MapRecognizer;

import java.util.Map;

/**
 * @author heaven7
 */
public class ShotTypeRecognizer extends MapRecognizer<Float> {

    public ShotTypeRecognizer(Map<String, Float> mMap) {
        super(mMap);
    }

    public float getScore(int shotType){
        if(shotType == MetaInfo.SHOT_TYPE_NONE){
            return 0f;
        }
        return getValue(MetaInfoUtils.getShotTypeString(shotType));
    }
}
