package com.vida.common.entity;

import java.util.List;

/**
 * batch image line
 * @author heaven7
 */
public class BatchImageLine {

    private String tfrecordPath; //may be rects path
    private List<String> images;

    public String getTfrecordPath() {
        return tfrecordPath;
    }
    public void setTfrecordPath(String tfrecordPath) {
        this.tfrecordPath = tfrecordPath;
    }

    public List<String> getImages() {
        return images;
    }
    public void setImages(List<String> images) {
        this.images = images;
    }
}