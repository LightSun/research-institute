package com.vida.common.ai;

/**
 * ai file info. note:
 * all is relative path
 * @author heaven7
 */
public class AiVideoFileInfo{

    private String tfrecordPath;
    private String tagPath;
    private String facePath;

    public String getTfrecordPath() {
        return tfrecordPath;
    }

    public void setTfrecordPath(String tfrecordPath) {
        this.tfrecordPath = tfrecordPath;
    }

    public String getTagPath() {
        return tagPath;
    }

    public void setTagPath(String tagPath) {
        this.tagPath = tagPath;
    }

    public String getFacePath() {
        return facePath;
    }

    public void setFacePath(String facePath) {
        this.facePath = facePath;
    }
}