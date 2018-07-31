package com.heaven7.java.image.detect;

import java.util.List;

/**
 * @author heaven7
 */
public class BatchImageHighLightManager extends AbstractBatchImageManager<List<IHighLightData>> {

    public BatchImageHighLightManager(List<String> mImages, ImageDetector detector) {
        super(mImages, detector);
    }

    @Override
    protected void onDetect(ImageDetector detector, Callback<List<IHighLightData>> callback, String imgFile, byte[] data) {
        detector.detectHighLight(data, new InternalCallback(imgFile));
    }


}
