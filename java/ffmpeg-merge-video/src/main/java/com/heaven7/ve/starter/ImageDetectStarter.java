package com.heaven7.ve.starter;

import com.heaven7.java.image.ImageFactory;
import com.heaven7.java.image.ImageInitializer;
import com.heaven7.java.image.detect.ImageDetector;
import com.heaven7.utils.Context;

/**
 * @author heaven7
 */
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
        ImageFactory.setImageInitializer(new ImageInitializer.Builder()
                .setImageReader(JavaImageReader.DEFAULT)
                .setMatrix2Transformer(new JavaMatrix2Transformer())
                .setImageDetector(detector)
                .setVideoFrameDelegate(new VidaVideoFrameDelegate())
                .setImageWriter(JavaImageWriter.DEFAULT)
                .build());
    }
}
