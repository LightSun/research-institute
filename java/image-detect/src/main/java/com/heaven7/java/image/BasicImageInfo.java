package com.heaven7.java.image;

/**
 * the basic image info of single image.
 * @author heaven7
 */
public class BasicImageInfo {

    /** the public image type . see {@linkplain ImageCons#TYPE_INT_ARGB} and etc.*/
    private int imageType;
    /** the width of used */
    private int width;
    /** the height of used */
    private int height;
    /** the scale rate of width */
    private float widthRate;
    /** the scale rate of height */
    private float heightRate;

    public int getImageType() {
        return imageType;
    }
    public void setImageType(int imageType) {
        this.imageType = imageType;
    }

    public int getWidth() {
        return width;
    }
    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }
    public void setHeight(int height) {
        this.height = height;
    }

    public float getWidthRate() {
        return widthRate;
    }
    public void setWidthRate(float widthRate) {
        this.widthRate = widthRate;
    }

    public float getHeightRate() {
        return heightRate;
    }
    public void setHeightRate(float heightRate) {
        this.heightRate = heightRate;
    }
}
