package com.heaven7.ve;

/**
 * @author heaven7
 */
public interface IMediaResourceItem {

    String getTitle();

    void setTitle(String title);

    boolean isImage();

    boolean isVideo();

    int getWidth();

    void setWidth(int width);

    int getHeight();

    void setHeight(int height);

    String getMime();

    void setMime(String mime);

    /**
     * in mills
     */
    long getTime();

    void setTime(long time);

    String getFilePath();

    void setFilePath(String filePath);

    /**
     * in mill seconds
     */
    void setDuration(long duration);

    /**
     * in mill seconds
     */
    long getDuration();

    float getRatio();

    void setRatio(float ratio);

    @Override
    boolean equals(Object o);

    @Override
    int hashCode();
}
