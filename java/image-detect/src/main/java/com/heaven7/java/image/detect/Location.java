package com.heaven7.java.image.detect;

public class Location implements IDataTransformer<Location>{

    public int left, top, width, height;

    public Rect toRect() {
        return new Rect(left, top, left + width, top + height);
    }

    public int getArea(){
        return width * height;
    }

    @Override
    public String toString() {
        return "Location{" +
                "left=" + left +
                ", top=" + top +
                ", width=" + width +
                ", height=" + height +
                '}';
    }

    @Override
    public Location transform(TransformInfo info) {
        if(info != null){
            left /= info.widthRate;
            width /= info.widthRate;
            top /= info.heightRate;
            height /= info.heightRate;
        }
        return this;
    }
}