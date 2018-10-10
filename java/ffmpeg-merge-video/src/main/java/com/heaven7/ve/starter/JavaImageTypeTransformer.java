package com.heaven7.ve.starter;

import com.heaven7.java.image.ImageCons;
import com.heaven7.java.image.ImageTypeTransformer;

import java.awt.image.BufferedImage;

/**
 * @author heaven7
 */
public class JavaImageTypeTransformer implements ImageTypeTransformer {

    @Override
    public int publicToNative(int i) {
        switch (i){
            case ImageCons.TYPE_INT_ARGB:
                return BufferedImage.TYPE_INT_ARGB;

            case ImageCons.TYPE_INT_RGB:
                return BufferedImage.TYPE_INT_RGB;
        }
        return BufferedImage.TYPE_INT_ARGB;
    }

    @Override
    public int nativeToPublic(int i) {
        switch (i){
            case BufferedImage.TYPE_INT_ARGB:
                return ImageCons.TYPE_INT_ARGB;

            case BufferedImage.TYPE_INT_RGB:
                return ImageCons.TYPE_INT_RGB;
        }
        return ImageCons.TYPE_INT_ARGB;
    }
}
