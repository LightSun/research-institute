package com.heaven7.ve.colorgap.impl;

import com.heaven7.java.image.detect.KeyPointData;
import com.heaven7.java.image.detect.Location;
import com.heaven7.java.visitor.PredicateVisitor;
import com.heaven7.java.visitor.collection.VisitServices;
import com.heaven7.ve.colorgap.*;
import com.heaven7.ve.colorgap.helper.FrameDataUtils;

import java.util.List;

/**
 * @author heaven7
 */
public class SimpleShotRecognizer implements IShotRecognizer {

    //private static final String TAG = "SimpleShotRecognizer";

    @Override
    public void requestKeyPoint(ColorGapContext context, List<MediaPartItem> parts, Callback callback) {
        MediaResourceConfiguration config = context.getMediaResourceConfiguration();
        final FrameDataDelegate<List<KeyPointData>> delegate;
        if(config == null || (delegate = config.getKeyPointDelegate()) == null){
            new KeyPointRecognizeHelper(parts){
                @Override
                protected void onDone() {
                    callback.onRecognizeDone(parts);
                }
            }.start();
        }else{
            //requetItems : need to request  data
            List<MediaPartItem> requestItems = VisitServices.from(parts).filter(new PredicateVisitor<MediaPartItem>() {
                @Override
                public Boolean visit(MediaPartItem item, Object param) {
                    String path = item.getKeyFrameImagePath();
                    List<KeyPointData> data = delegate.getFrameData(item, path);
                    //null means need request.
                    if(data != null){
                        FrameDataUtils.applyKeyPointData(item, data);
                    }
                    return data == null;
                }
            }).getAsList();
            new KeyPointRecognizeHelper(requestItems){
                @Override
                protected void onDone() {
                    callback.onRecognizeDone(parts);
                }
            }.start();
        }
    }

    @Override
    public void requestSubject(ColorGapContext context, List<MediaPartItem> parts, Callback callback) {
        MediaResourceConfiguration config = context.getMediaResourceConfiguration();
        final FrameDataDelegate<Location> delegate;
        if(config == null || (delegate = config.getSubjectDelegate()) == null){
            new SubjectRecognizeHelper(parts){
                @Override
                protected void onDone() {
                    callback.onRecognizeDone(parts);
                }
            }.start();
        }else{
            //requetItems : need to request  data
            List<MediaPartItem> requestItems = VisitServices.from(parts).filter(new PredicateVisitor<MediaPartItem>() {
                @Override
                public Boolean visit(MediaPartItem item, Object param) {
                    String path = item.getKeyFrameImagePath();
                    Location data = delegate.getFrameData(item, path);
                    //null means need request.
                    if(data != null){
                        FrameDataUtils.applySubject(item, data);
                    }
                    return data == null;
                }
            }).getAsList();
            new SubjectRecognizeHelper(requestItems){
                @Override
                protected void onDone() {
                    callback.onRecognizeDone(parts);
                }
            }.start();
        }
    }

    @Override
    public int recognizeShotCategory(MediaPartItem item) {
        return ShotRecognition.recognizeShotCategory(item);
    }

    @Override
    public int getShotType(MediaPartItem item) {
        return ShotRecognition.getShotType(item);
    }
}
