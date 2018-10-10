package com.heaven7.java.image.detect;

public class Location {

    public int left, top, width, height;

    public Rect toRect() {
        return new Rect(left, top, left + width, top + height);
    }

    public int getArea(){
        return width * height;
    }

    public void scaleUp(float wRate, float hRate){
        left *= wRate;
        width *= wRate;
        top *= hRate;
        height *= hRate;
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
}