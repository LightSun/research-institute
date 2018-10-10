package com.heaven7.java.image;

/**
 * help transform between native image type and public image type.
 *
 * @author heaven7
 */
public interface ImageTypeTransformer {

    /**
     * transform public image type to native image type.
     *
     * @param type the type .see {@linkplain ImageCons#TYPE_INT_ARGB} and etc.
     * @return the transformed native image type .which is the running platform's image type.
     */
    int publicToNative(int type);

    /**
     * transform native image type to public image type.
     *
     * @param type native image type .which is the running platform's image type.
     * @return the public image type .see {@linkplain ImageCons#TYPE_INT_ARGB} and etc.
     */
    int nativeToPublic(int type);
}
