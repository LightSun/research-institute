package com.heaven7.java.image.detect;

/**
 * the transform info of image .
 */
public class TransformInfo {
    /**
     * the scale rate of width. often comes from {@linkplain com.heaven7.java.image.utils.ImageUtils#computeScaleRate(int, int, int, int)}
     */
    public float widthRate;
    /**
     * the scale rate of height. often comes from {@linkplain com.heaven7.java.image.utils.ImageUtils#computeScaleRate(int, int, int, int)}
     */
    public float heightRate;

    public static TransformInfo of(float widthRate, float heightRate) {
        TransformInfo info = new TransformInfo();
        info.widthRate = widthRate;
        info.heightRate = heightRate;
        return info;
    }
}