package com.heaven7.ve.image;

/**
 * image detect manager for image and video.
 */
public class ImageDetectManager {

    private final MediaDataMapper mediaDataMapper;

    public ImageDetectManager(MediaDataMapper mediaDataMapper) {
        this.mediaDataMapper = mediaDataMapper;
    }

    //TODO gen and load.

    public interface MediaDataMapper{
        /**
         * map the source dir to data dir.
         * @param imageResDir the image resource dir
         * @return the image data dir
         */
        String mapBatchImageDir(String imageResDir);
        /**
         * map the source dir to data dir.
         * @param videoDir the video resource dir
         * @return the video data dir
         */
        String mapVideoDataDir(String videoDir);
    }
}
