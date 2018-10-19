package com.heaven7.ve.colorgap.helper;

import com.heaven7.java.image.detect.KeyPointData;
import com.heaven7.java.image.detect.Location;
import com.heaven7.java.visitor.PileVisitor;
import com.heaven7.java.visitor.PredicateVisitor;
import com.heaven7.java.visitor.collection.CollectionVisitService;
import com.heaven7.java.visitor.collection.VisitServices;
import com.heaven7.ve.colorgap.MediaPartItem;
import com.heaven7.ve.colorgap.MetaInfo;
import com.heaven7.ve.colorgap.ShotRecognition;
import com.heaven7.ve.colorgap.VEGapUtils;

import java.util.List;

import static com.heaven7.ve.colorgap.MetaInfoUtils.getShotTypeString;

/**
 * @author heaven7
 */
public class FrameDataUtils {

    public static void applySubject(MediaPartItem part, Location value){
        int area = value.getArea();
        part.imageMeta.setSubjectLocation(value);
        MetaInfo.ImageMeta imageMeta = part.imageMeta;
        if(imageMeta.getWidth() == 0 || imageMeta.getHeight() == 0){
            throw new IllegalStateException("width and height can't be 0. path = " + part.getItem().getFilePath());
        }
        float area_rate = area * 1f / (imageMeta.getWidth() * imageMeta.getHeight());
        int shotType = VEGapUtils.getShotTypeBySubjectRate(area_rate);
        imageMeta.setShotType(getShotTypeString(shotType));
    }

    public static void applyKeyPointData(MediaPartItem part, List<KeyPointData> value) {
        CollectionVisitService<KeyPointData> service = VisitServices.from(value)
                .filter(new PredicateVisitor<KeyPointData>() {
                    @Override
                    public Boolean visit(KeyPointData keyPointData, Object param) {
                        return keyPointData.getKeyPointCount() >= ShotRecognition.THRESOLD_KEY_POINT_COUNT
                                && keyPointData.getLocation() != null;
                    }
                });
        //set body count . if area less than max data/5. ignore
        if (service.size() > 1) {
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
        } else {
            part.imageMeta.setBodyCount(service.size());
        }
        KeyPointData maxKPD = service.pile(new PileVisitor<KeyPointData>() {
            @Override
            public KeyPointData visit(Object o, KeyPointData kp1, KeyPointData kp2) {
                return kp1.getKeyPointCount() > kp2.getKeyPointCount() ? kp1 : kp2;
            }
        });
        if (maxKPD != null) {
            part.setKeyPointData(maxKPD);
        }
    }
}
