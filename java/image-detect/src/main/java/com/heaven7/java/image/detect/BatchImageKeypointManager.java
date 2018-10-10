package com.heaven7.java.image.detect;

import com.heaven7.java.image.utils.TransformUtils;

import java.util.List;

/**
 * @author heaven7
 */
public class BatchImageKeypointManager extends AbstractBatchImageManager<List<KeyPointData>> {

    public BatchImageKeypointManager(List<String> mImages) {
        super(mImages);
    }

    @Override
    protected void onDetect(ImageDetector detector, Callback<List<KeyPointData>> callback, String imgFile, byte[] data) {
        detector.detectKeyPoints(data, new InternalCallback(imgFile));
    }

    @Override
    protected List<KeyPointData> transformData(List<KeyPointData> keyPointData, TransformInfo tInfo) {
        return TransformUtils.transformData(keyPointData, tInfo);
    }
}
