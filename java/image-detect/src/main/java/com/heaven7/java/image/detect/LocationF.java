package com.heaven7.java.image.detect;

public class LocationF {

    public float left, top, width, height;

    public RectF toRect() {
        return new RectF(left, top, left + width, top + height);
    }

    public float getArea(){
        return width * height;
    }

    @Override
    public String toString() {
        return "LocationF{" +
                "left=" + left +
                ", top=" + top +
                ", width=" + width +
                ", height=" + height +
                '}';
    }
}