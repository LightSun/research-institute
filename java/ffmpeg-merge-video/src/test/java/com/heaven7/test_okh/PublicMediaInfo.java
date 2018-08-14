package com.heaven7.test_okh;

public class PublicMediaInfo {

    public static final byte TYPE_IMAGE = 1;
    public static final byte TYPE_VIDEO = 2;
    private Long id;

    private Integer type;
    private String savePath;

    private String filename;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getSavePath() {
        return savePath;
    }

    public void setSavePath(String savePath) {
        this.savePath = savePath;
    }

    public String getFilename() {
        return filename;
    }
    public void setFilename(String filename) {
        this.filename = filename;
    }

}