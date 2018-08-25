package com.heaven7.ve.colorgap.impl;

/**
 * the image data dir mapper.
 */
@Deprecated
public interface ImageDataDirMapper {
    /**
     * map the source dir to data dir.
     *
     * @param imageResDir the image resource dir
     * @return the image data dir
     */
    String mapDataDir(String imageResDir);

    /**
     * map the source dir to high-light dir.
     *
     * @param imageResDir the image resource dir
     * @return the image data dir
     */
    String mapHighLightDir(String imageResDir);
}