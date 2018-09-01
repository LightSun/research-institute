package com.heaven7.ve.colorgap.impl;

import com.heaven7.ve.colorgap.IShotRecognizer;
import com.heaven7.ve.colorgap.MediaPartItem;
import com.heaven7.ve.colorgap.MetaInfo;
import com.heaven7.ve.colorgap.ShotRecognition;

import java.util.List;

/**
 * @author heaven7
 */
public class SimpleShotRecognizer implements IShotRecognizer {

    @Override
    public void requestKeyPoint(List<MediaPartItem> parts, Callback callback) {
        new KeyPointRecognizeHelper(parts){
            @Override
            protected void onDone() {
                callback.onRecognizeDone(parts);
            }
        }.start();
    }

    @Override
    public void requestSubject(List<MediaPartItem> parts, Callback callback) {
        new SubjectRecognizeHelper(parts){
            @Override
            protected void onDone() {
                //process story to color filter(gen shot key, filter)
                callback.onRecognizeDone(parts);
            }
        }.start();
    }

    @Override
    public int recognizeShotCategory(MediaPartItem item) {
        return ShotRecognition.recognizeShotCategory(item);
    }

    @Override
    public int getShotType(MediaPartItem item) {
        int shotType = ShotRecognition.getShotType(item);
        if(shotType != MetaInfo.SHOT_TYPE_NONE){
            return shotType;
        }
        item.addDetail("default shot-type mediumShot");
        return MetaInfo.SHOT_TYPE_MEDIUM_SHOT;
    }
}
