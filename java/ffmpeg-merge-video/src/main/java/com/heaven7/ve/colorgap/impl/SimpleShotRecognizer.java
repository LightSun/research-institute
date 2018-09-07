package com.heaven7.ve.colorgap.impl;

import com.heaven7.core.util.Logger;
import com.heaven7.ve.colorgap.*;

import java.util.List;

/**
 * @author heaven7
 */
public class SimpleShotRecognizer implements IShotRecognizer {

    private static final String TAG = "SimpleShotRecognizer";

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
       // Logger.d(TAG, "requestSubject", "size = " + parts.size());
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
        if(item.getContext().getInitializeParam().hasFlag(ColorGapContext.FLAG_ASSIGN_SHOT_TYPE)){
            return MetaInfo.getShotTypeFrom(item.imageMeta.getShotType());
        }
        return ShotRecognition.getShotType(item);
    }
}
