package com.heaven7.ve.colorgap.impl;

import com.heaven7.java.base.anno.Nullable;
import com.heaven7.java.base.util.Predicates;
import com.heaven7.java.image.detect.AbstractBatchImageManager;
import com.heaven7.java.image.detect.BatchImageKeypointManager;
import com.heaven7.java.image.detect.KeyPointData;
import com.heaven7.ve.colorgap.FrameDataDelegate;
import com.heaven7.ve.colorgap.MediaPartItem;
import com.heaven7.ve.colorgap.MediaResourceConfiguration;
import com.heaven7.ve.colorgap.helper.FrameDataUtils;

import java.util.List;

/**
 * @author heaven7
 */
public abstract class KeyPointRecognizeHelper extends BaseRecognizeHelper<List<KeyPointData>> {

    public KeyPointRecognizeHelper(List<MediaPartItem> mItems) {
        super(mItems);
    }

    @Override
    protected AbstractBatchImageManager<List<KeyPointData>> onCreateBatchImageManager(List<String> images) {
        return new BatchImageKeypointManager(images);
    }
    @Override
    protected void onProcess(MediaPartItem part, String imgPath, @Nullable List<KeyPointData> value) {
        if(!Predicates.isEmpty(value)) {
            FrameDataUtils.applyKeyPointData(part, value);
        }
        MediaResourceConfiguration config = part.getContext().getMediaResourceConfiguration();
        if(config != null){
            FrameDataDelegate<List<KeyPointData>> delegate = config.getKeyPointDelegate();
            if(delegate != null){
                delegate.saveFrameData(part, imgPath, value);
            }
        }
    }

}
