package com.heaven7.java.image;

/**
 * @author heaven7
 */
public class ImageDetectFactory {

    private static ImageDetectInitializer sInitializer;

    public static void setImageInitializer(ImageDetectInitializer initializer) {
        ImageDetectFactory.sInitializer = initializer;
    }
    public static ImageDetectInitializer getImageInitializer(){
        return sInitializer;
    }
}
