package com.heaven7.java.image.detect;

import java.util.List;

/**
 * @author heaven7
 */
public class BatchImageKeypointManager extends AbstractBatchImageManager<List<KeyPointData>> {

    public BatchImageKeypointManager(List<String> mImages, ImageDetector detector) {
        super(mImages, detector);
    }

    @Override
    protected void onDetect(ImageDetector detector, Callback<List<KeyPointData>> callback, String imgFile, byte[] data) {
        detector.detectKeyPoints(data, new InternalCallback(imgFile));
    }
}
