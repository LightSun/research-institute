package com.heaven7.java.image;

/**
 * @author heaven7
 */
public interface Matrix2Transformer {

    /**
     * transform image matrix to byte array.
     * @param mat the matrix
     * @param imageType the image type. see {@linkplain ImageCons#TYPE_INT_ARGB} and etc.
     * @return the byte array.
     */
    byte[] transform(Matrix2<Integer> mat, int imageType, String format);

}

