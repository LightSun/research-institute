package com.heaven7.java.image.detect;

public class LocationF implements IDataTransformer<LocationF>{

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

    @Override
    public LocationF transform(TransformInfo info) {
        if(info != null) {
            left /= info.widthRate;
            width /= info.widthRate;
            top /= info.heightRate;
            height /= info.heightRate;
        }
        return this;
    }
}