package com.heaven7.ve.colorgap.impl;

import com.heaven7.java.image.detect.AbstractBatchImageManager;
import com.heaven7.java.image.detect.BatchImageKeypointManager;
import com.heaven7.java.image.detect.KeyPointData;
import com.heaven7.java.visitor.PileVisitor;
import com.heaven7.java.visitor.PredicateVisitor;
import com.heaven7.java.visitor.collection.CollectionVisitService;
import com.heaven7.java.visitor.collection.VisitServices;
import com.heaven7.ve.colorgap.MediaPartItem;
import com.heaven7.ve.colorgap.ShotRecognition;

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
        CollectionVisitService<KeyPointData> service = VisitServices.from(value)
                .filter(new PredicateVisitor<KeyPointData>() {
            @Override
            public Boolean visit(KeyPointData keyPointData, Object param) {
                return keyPointData.getKeyPointCount() >= ShotRecognition.THRESOLD_KEY_POINT_COUNT
                        && keyPointData.getLocation() != null;
            }
        });
        //set body count . if area less than max data/5. ignore
        if(service.size() > 1) {
            final KeyPointData maxAreaData = service.pile(new PileVisitor<KeyPointData>() {
                @Override
                public KeyPointData visit(Object o, KeyPointData kp1, KeyPointData kp2) {
                    return kp1.getLocation().getArea() > kp2.getLocation().getArea() ? kp1 : kp2;
                }
            });
            int matchSize = service.filter(new PredicateVisitor<KeyPointData>() {
                @Override
                public Boolean visit(KeyPointData kpd, Object param) {
                    return kpd.getLocation().getArea() / maxAreaData.getLocation().getArea() > 0.2f;
                }
            }).size();
            part.imageMeta.setBodyCount(matchSize);
        }else{
            part.imageMeta.setBodyCount(service.size());
        }
        KeyPointData maxKPD = service.pile(new PileVisitor<KeyPointData>() {
            @Override
            public KeyPointData visit(Object o, KeyPointData kp1, KeyPointData kp2) {
                return kp1.getKeyPointCount() > kp2.getKeyPointCount() ? kp1 : kp2;
            }
        });
        if(maxKPD != null) {
            part.setKeyPointData(maxKPD);
        }
    }

}
