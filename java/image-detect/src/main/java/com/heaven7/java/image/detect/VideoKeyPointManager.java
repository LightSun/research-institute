package com.heaven7.java.image.detect;

import java.util.List;

/**
 * video key-point manager
 */
public class VideoKeyPointManager extends AbstractVideoManager<List<KeyPointData>>{

    public VideoKeyPointManager(VideoFrameDelegate vfd, String videoSrc) {
        super(vfd, videoSrc);
    }
    public VideoKeyPointManager(VideoFrameDelegate vfd, String videoSrc, int gap) {
        super(vfd, videoSrc, gap);
    }

    @Override
    protected void onDetect(ImageDetector detector, Callback<List<KeyPointData>> callback, int time, byte[] data) {
        detector.detectKeyPoints(data, new InternalCallback(time));
    }

    @Override
    protected void onDetectBatch(BatchInfo info, ImageDetector detector, Callback<List<KeyPointData>> callback, List<Integer> times, byte[] batchData) {
        detector.detectKeyPointsBatch(info, batchData, new InternalCallback(times));
    }
}
