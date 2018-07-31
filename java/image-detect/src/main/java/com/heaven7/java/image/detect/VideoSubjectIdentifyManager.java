package com.heaven7.java.image.detect;

import java.util.List;

/**
 * subject identify video manager
 * @author heaven7
 */
public class VideoSubjectIdentifyManager extends AbstractVideoManager<Location> {

    public VideoSubjectIdentifyManager(VideoFrameDelegate vfd, String videoSrc) {
        super(vfd, videoSrc);
    }

    public VideoSubjectIdentifyManager(VideoFrameDelegate vfd, String videoSrc, int gap, ImageDetector detector) {
        super(vfd, videoSrc, gap, detector);
    }

    @Override
    protected void onDetect(ImageDetector detector, Callback<Location> callback, int time, byte[] data) {
        detector.detectSubjectIdentification(data,  new InternalCallback(time));
    }

    @Override
    protected void onDetectBatch(int batchSize, ImageDetector detector, Callback<Location> callback, List<Integer> times, byte[] batchData) {
        detector.detectSubjectIdentificationBatch(batchSize, batchData, new InternalCallback(times));
    }
}
