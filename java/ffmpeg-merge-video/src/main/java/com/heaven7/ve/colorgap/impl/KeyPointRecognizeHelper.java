package com.heaven7.ve.colorgap.impl;

import com.heaven7.java.image.detect.AbstractBatchImageManager;
import com.heaven7.java.image.detect.BatchImageKeypointManager;
import com.heaven7.java.image.detect.KeyPointData;
import com.heaven7.java.visitor.PileVisitor;
import com.heaven7.java.visitor.collection.VisitServices;
import com.heaven7.ve.colorgap.MediaPartItem;

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
    protected void onProcess(MediaPartItem part, List<KeyPointData> value) {
        //set max person
        part.imageMeta.setBodyCount(value.size());

        KeyPointData maxKPD = VisitServices.from(value).pile(new PileVisitor<KeyPointData>() {
            @Override
            public KeyPointData visit(Object o, KeyPointData kp1, KeyPointData kp2) {
                return kp1.getKeyPointCount() > kp2.getKeyPointCount() ? kp1 : kp2;
            }
        });
        part.setKeyPointData(maxKPD);
    }

}
