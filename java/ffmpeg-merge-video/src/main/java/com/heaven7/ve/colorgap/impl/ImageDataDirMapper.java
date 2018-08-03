package com.heaven7.ve.colorgap.impl;

/**
 * the image data dir mapper.
 */
public interface ImageDataDirMapper {
    /**
     * map the source dir to data dir.
     *
     * @param imageResDir the image resource dir
     * @return the image data dir
     */
    String mapDataDir(String imageResDir);
}