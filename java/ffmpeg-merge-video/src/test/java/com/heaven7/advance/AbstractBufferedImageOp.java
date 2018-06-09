package com.heaven7.advance;

import java.awt.image.BufferedImage;

public abstract class AbstractBufferedImageOp {

    public abstract BufferedImage filter(BufferedImage src, BufferedImage dest);

    public static void setRGB(BufferedImage dest, int startX, int startY, int width, int height, int[] outPixels) {
        dest.setRGB(startX, startY, width, height, outPixels, 0, width);
    }

    public static void getRGB(BufferedImage src, int startX, int startY, int width, int height, int[] inPixels) {
        src.getRGB(startX, startY, width, height, inPixels, 0, width);
    }

    public BufferedImage createCompatibleDestImage(BufferedImage src, String suffix) {

        return null;
    }

    public static int clamp(int value) {
        return value > 255 ? 255 :
                (value < 0 ? 0 : value);
    }

}
