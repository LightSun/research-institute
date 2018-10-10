package com.heaven7.java.image.detect;

import com.heaven7.java.image.utils.TransformUtils;

import java.util.List;

/**
 * video key-point manager
 */
public class VideoKeyPointManager extends AbstractVideoManager<List<KeyPointData>>{

    public VideoKeyPointManager( String videoSrc) {
        super(videoSrc);
    }
    public VideoKeyPointManager(String videoSrc, int gap) {
        super( videoSrc, gap);
    }

    @Override
    protected void onDetect(ImageDetector detector, Callback<List<KeyPointData>> callback, int time, byte[] data) {
        detector.detectKeyPoints(data, new InternalCallback(time));
    }

    @Override
    protected void onDetectBatch(BatchInfo info, ImageDetector detector, Callback<List<KeyPointData>> callback, List<Integer> times, byte[] batchData) {
        detector.detectKeyPointsBatch(info, batchData, new InternalCallback(times));
    }

    @Override
    protected List<KeyPointData> transformData(List<KeyPointData> list, TransformInfo tInfo) {
        return TransformUtils.transformData(list, tInfo);
    }
}
