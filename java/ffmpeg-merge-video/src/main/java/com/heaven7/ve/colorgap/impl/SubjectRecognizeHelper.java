package com.heaven7.ve.colorgap.impl;

import com.heaven7.java.image.detect.AbstractBatchImageManager;
import com.heaven7.java.image.detect.BatchImageSubjectIdentifyManager;
import com.heaven7.java.image.detect.Location;
import com.heaven7.ve.colorgap.MediaPartItem;
import com.heaven7.ve.colorgap.MetaInfo;
import com.heaven7.ve.colorgap.VEGapUtils;

import java.util.List;

import static com.heaven7.ve.colorgap.MetaInfo.getShotTypeString;

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
    protected void onProcess(MediaPartItem part, Location value) {
        int area = value.getArea();
        part.imageMeta.setSubjectLocation(value);
        MetaInfo.ImageMeta imageMeta = part.imageMeta;
        if(imageMeta.getWidth() == 0 || imageMeta.getHeight() == 0){
            throw new IllegalStateException("width and height can't be 0. path = " + part.getItem().getFilePath());
        }
        float val = area * 1f / (imageMeta.getWidth() * imageMeta.getHeight());
        int shotType = VEGapUtils.getShotTypeBySubjectRate(val);
        imageMeta.setShotType(getShotTypeString(shotType));
        part.addDetail("SubjectRecognize(value = "+ val +")");
    }

}
