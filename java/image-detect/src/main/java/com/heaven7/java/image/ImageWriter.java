package com.heaven7.java.image;

import java.io.File;

/** @author heaven7 */
public interface ImageWriter {

    /**
     * write the matrix to file
     *
     * @param mat the image matrix
     * @param dst the dst file
     * @param imageType the image type .see {@linkplain ImageCons#TYPE_INT_ARGB} and etc.
     * @param format the format of generate, if null , will guess by file name.
     * @return true if write success.
     */
     boolean write(Matrix2<Integer> mat, File dst, int imageType, String format);
    /**
     * write the matrix to file
     *
     * @param imageData the image data
     * @param dst the dst file
     * @param format the format of generate, if null , will guess by file name.
     * @return true if write success.
     */
     boolean write(byte[] imageData, File dst, String format);
}
