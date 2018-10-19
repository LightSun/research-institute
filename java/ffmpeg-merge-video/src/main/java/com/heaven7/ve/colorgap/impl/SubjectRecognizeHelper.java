package com.heaven7.ve.colorgap.impl;

import com.heaven7.java.image.detect.AbstractBatchImageManager;
import com.heaven7.java.image.detect.BatchImageSubjectIdentifyManager;
import com.heaven7.java.image.detect.KeyPointData;
import com.heaven7.java.image.detect.Location;
import com.heaven7.ve.colorgap.*;
import com.heaven7.ve.colorgap.helper.FrameDataUtils;

import java.util.List;

import static com.heaven7.ve.colorgap.MetaInfoUtils.getShotTypeString;

/**
 * the subject recognize/identify helper
 * @author heaven7
 */
public abstract class SubjectRecognizeHelper extends BaseRecognizeHelper<Location> {


    public SubjectRecognizeHelper(List<MediaPartItem> mItems) {
       super(mItems);
    }

    @Override
    protected AbstractBatchImageManager<Location> onCreateBatchImageManager(List<String> images) {
        return new BatchImageSubjectIdentifyManager(images);
    }

    @Override
    protected void onProcess(MediaPartItem part, String imagePath, Location value) {
        FrameDataUtils.applySubject(part, value);
        MediaResourceConfiguration config = part.getContext().getMediaResourceConfiguration();
        if(config != null){
            FrameDataDelegate<Location> delegate = config.getSubjectDelegate();
            if(delegate != null){
                delegate.saveFrameData(part,imagePath , value);
            }
        }
    }

}
