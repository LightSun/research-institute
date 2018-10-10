package com.heaven7.java.image.detect;

import com.heaven7.java.image.utils.TransformUtils;

import java.util.List;

/**
 * @author heaven7
 */
public class BatchImageHighLightManager extends AbstractBatchImageManager<List<IHighLightData>> {

    public BatchImageHighLightManager(List<String> mImages) {
        super(mImages);
    }

    @Override
    protected void onDetect(ImageDetector detector, Callback<List<IHighLightData>> callback, String imgFile, byte[] data) {
        detector.detectHighLight(data, new InternalCallback(imgFile));
    }

    @Override
    protected List<IHighLightData> transformData(List<IHighLightData> list, TransformInfo tInfo) {
        return TransformUtils.transformData(list, tInfo);
    }
}
