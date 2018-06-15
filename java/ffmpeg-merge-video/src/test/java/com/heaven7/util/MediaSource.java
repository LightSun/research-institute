package com.heaven7.util;

public class MediaSource {

    public static final int TYPE_MP4   = 1;
    public static final int TYPE_JPG   = 2;
    public static final int TYPE_JPEG  = 3;
    public static final int TYPE_PNG   = 4;

    private String filePath;
    private boolean video;
    private int type;

    public String getFilePath() {
        return filePath;
    }
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public boolean isVideo() {
        return video;
    }
    public void setVideo(boolean video) {
        this.video = video;
    }

    public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }
}