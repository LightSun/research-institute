package com.heaven7.java.image;

/**
 * @author heaven7
 */
public class ImageFactory {

    private static ImageInitializer sInitializer;

    public static void setImageInitializer(ImageInitializer initializer) {
        ImageFactory.sInitializer = initializer;
    }
    public static ImageInitializer getImageInitializer(){
        return sInitializer;
    }
}
