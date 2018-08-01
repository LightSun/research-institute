package com.heaven7.ve.colorgap.impl;

import com.heaven7.ve.colorgap.IShotRecognizer;
import com.heaven7.ve.colorgap.MediaPartItem;
import com.heaven7.ve.colorgap.ShotRecognition;

/**
 * @author heaven7
 */
public class SimpleShotRecognizer implements IShotRecognizer {

    @Override
    public int recognizeShotCategory(MediaPartItem item) {
        return ShotRecognition.recognizeShotCategory(item);
    }

    @Override
    public int getShotType(MediaPartItem item) {
        return ShotRecognition.getShotType(item);
    }
}
