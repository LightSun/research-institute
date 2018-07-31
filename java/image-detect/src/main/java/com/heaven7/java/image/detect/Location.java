package com.heaven7.java.image.detect;

public class Location {

    public int left, top, width, height;

    public Rect toRect() {
        return new Rect(left, top, left + width, top + height);
    }
}