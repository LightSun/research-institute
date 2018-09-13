package com.heaven7.ve.colorgap.impl;

import com.heaven7.java.image.detect.AbstractBatchImageManager;
import com.heaven7.java.image.detect.BatchImageSubjectIdentifyManager;
import com.heaven7.java.image.detect.Location;
import com.heaven7.ve.colorgap.MediaPartItem;
import com.heaven7.ve.colorgap.MetaInfo;
import com.heaven7.ve.colorgap.VEGapUtils;

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
    protected void onProcess(MediaPartItem part, Location value) {
        int area = value.getArea();
        part.imageMeta.setSubjectLocation(value);
        MetaInfo.ImageMeta imageMeta = part.imageMeta;
        if(imageMeta.getWidth() == 0 || imageMeta.getHeight() == 0){
            throw new IllegalStateException("width and height can't be 0. path = " + part.getItem().getFilePath());
        }

        float area_rate = area * 1f / (imageMeta.getWidth() * imageMeta.getHeight());
        int shotType = VEGapUtils.getShotTypeBySubjectRate(area_rate);
        imageMeta.setShotType(getShotTypeString(shotType));

       /* float height_rate = value.height * 1f / imageMeta.getHeight();
        boolean atBottom =  value.top + value.height >= imageMeta.getHeight() - imageMeta.getHeight() * 1f/ 20;
        boolean atTop =  value.top <= imageMeta.getHeight() * 1f/ 20;
        int shotType_h_rate = VEGapUtils.getShotTypeByHeightRate(part, height_rate, value.height * 1f/ value.width, atTop, atBottom);
        int actualShotType;
        if(shotType == MetaInfo.SHOT_TYPE_NONE){
            actualShotType = shotType_h_rate;
            part.addDetail("SubjectRecognize_height_rate(value = "+ height_rate +")");
        }else if(shotType_h_rate == MetaInfo.SHOT_TYPE_NONE){
            actualShotType = shotType;
            part.addDetail("SubjectRecognize_area_rate(value = "+ area_rate +")");
        }else{
            actualShotType = Math.min(shotType, shotType_h_rate);
            part.addDetail("SubjectRecognize_min([area_rate, height_rate] = ["
                    + area_rate + ", " + height_rate + "])");
        }
        imageMeta.setShotType(getShotTypeString(actualShotType));*/
    }

}
