package com.heaven7.ve.colorgap.impl;

import com.heaven7.java.image.detect.AbstractBatchImageManager;
import com.heaven7.java.image.detect.BatchImageSubjectIdentifyManager;
import com.heaven7.java.image.detect.Location;
import com.heaven7.ve.colorgap.MediaPartItem;
import com.heaven7.ve.colorgap.MetaInfo;

import java.util.List;

import static com.heaven7.ve.colorgap.MetaInfo.getShotTypeString;

/**
 * the subject recognize/identify helper
 * @author heaven7
 */
public abstract class SubjectRecognizeHelper extends BaseRecognizeHelper<Location> {


    public SubjectRecognizeHelper(List<MediaPartItem> mItems) {
       super(mItems);
       //at last default to medium shot.
       for(MediaPartItem item : mItems){
           item.imageMeta.setShotType(getShotTypeString(MetaInfo.SHOT_TYPE_MEDIUM_SHOT));
       }
    }

    @Override
    protected AbstractBatchImageManager<Location> onCreateBatchImageManager(List<String> images) {
        return new BatchImageSubjectIdentifyManager(images);
    }

    @Override
    protected void onProcess(MediaPartItem part, Location value) {
        int area = value.getArea();
        int shotType;
        //fixed to 1920*1080
        float val = area * 1f / 1920 / 1080;
        if(val > 2f / 3){
            shotType = MetaInfo.SHOT_TYPE_CLOSE_UP;
        }else if( val > 1f /3){
            shotType = MetaInfo.SHOT_TYPE_MEDIUM_SHOT;
        }else {
            shotType = MetaInfo.SHOT_TYPE_LONG_SHORT;
        }
        part.imageMeta.setShotType(getShotTypeString(shotType));
    }

}
