package com.heaven7.java.image.detect;

import java.util.List;

/**
 * @author heaven7
 */
public class BatchImageSubjectIdentifyManager extends AbstractBatchImageManager<Location> {

    public BatchImageSubjectIdentifyManager(List<String> mImages) {
        super(mImages);
    }

    @Override
    protected void onDetect(ImageDetector detector, Callback<Location> callback, String imgFile, byte[] data) {
        detector.detectSubjectIdentification(data, new InternalCallback(imgFile));
    }
}
