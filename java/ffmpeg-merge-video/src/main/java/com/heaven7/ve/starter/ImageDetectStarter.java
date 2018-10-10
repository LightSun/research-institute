package com.heaven7.ve.starter;

import com.heaven7.java.base.anno.Platform;
import com.heaven7.java.image.ImageFactory;
import com.heaven7.java.image.ImageInitializer;
import com.heaven7.java.image.detect.AbstractVideoManager;
import com.heaven7.java.image.detect.ImageDetector;
import com.heaven7.utils.Context;
import com.heaven7.ve.colorgap.ColorGapContext;
import com.heaven7.ve.colorgap.VEGapUtils;
import com.heaven7.ve.configs.BootStrapData;

/**
 * @author heaven7
 */
@Platform()
public class ImageDetectStarter implements IStarter{

    @Override
    public void init(Context context, Object param) {
        assert param instanceof String;
        ImageDetector detector;
        try {
            detector = (ImageDetector) Class.forName(param.toString()).newInstance();
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            throw new RuntimeException(e);
        }
        int testType = VEGapUtils.asColorGapContext(context).getTestType();

        ImageFactory.setImageInitializer(new ImageInitializer.Builder()
                .setImageReader(JavaImageReader.DEFAULT)
                .setMatrix2Transformer(new JavaMatrix2Transformer())
                .setImageDetector(detector)
                .setVideoFrameDelegate(createVideoFrameDelegate(testType))
                .setImageWriter(JavaImageWriter.DEFAULT)
                .setImageTypeTransformer(new JavaImageTypeTransformer())
                .setImageLimitInfo(BootStrapData.get(context).getImageLimitInfo())
                .build());
    }

    private AbstractVideoManager.VideoFrameDelegate createVideoFrameDelegate(int testType) {
        return testType == ColorGapContext.TEST_TYPE_LOCAL ? new LocalVideoFrameDelegate() : new VidaVideoFrameDelegate();
    }

}
