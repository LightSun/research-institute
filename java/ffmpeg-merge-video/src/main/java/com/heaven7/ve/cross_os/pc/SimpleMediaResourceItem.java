package com.heaven7.ve.cross_os.pc;


import com.heaven7.ve.cross_os.IMediaResourceItem;

/*public*/ class SimpleMediaResourceItem implements IMediaResourceItem {

    private String title;
    private long time; //time of photo/video .the last modified
    private String filePath;
    private String mime;
    private int width;
    private int height;

    private long duration; //in mill seconds

    @Override
    public String getTitle() {
        return title;
    }
    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean isImage() {
        return mime.startsWith("image");
    }

    @Override
    public boolean isVideo() {
        return mime.startsWith("video");
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public void setWidth(int width) {
        this.width = width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public String getMime() {
        return mime;
    }

    @Override
    public void setMime(String mime) {
        this.mime = mime;
    }

    @Override
    public long getTime() {
        return time;
    }

    @Override
    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public String getFilePath() {
        return filePath;
    }

    @Override
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void setDuration(long duration) {
        this.duration = duration;
    }
    @Override
    public long getDuration() {
        return duration;
    }

    @Override
    public String toString() {
        return "IMediaResourceItem{" +
                ", title='" + title + '\'' +
                ", time='" + time + '\'' +
                ", duration='" + duration + '\'' +
                ", filePath='" + filePath + '\'' +
                ", mime='" + mime + '\'' +
                ", width=" + width +
                ", height=" + height +
                '}';
    }

    public SimpleMediaResourceItem() {
    }

    public SimpleMediaResourceItem(SimpleMediaResourceItem item) {
        title = item.title;
        time = item.time;
        filePath = item.filePath;
        mime = item.mime;
        width = item.width;
        height = item.height;
        duration = item.duration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SimpleMediaResourceItem item = (SimpleMediaResourceItem) o;

        return filePath != null ? filePath.equals(item.filePath) : item.filePath == null;
    }

    @Override
    public int hashCode() {
        return filePath.hashCode();
    }

}