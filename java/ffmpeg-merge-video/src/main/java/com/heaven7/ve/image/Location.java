package com.heaven7.ve.image;

public class Location {

    public int left, top, width, height;

    public Rect toRect() {
        return new Rect(left, top, left + width, top + height);
    }
}