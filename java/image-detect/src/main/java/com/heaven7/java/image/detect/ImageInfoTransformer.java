package com.heaven7.java.image.detect;

/**
 * the image info transformer
 * @author heaven7
 */
public interface ImageInfoTransformer<T> {

    T transform(T t, Info info);

    /**
     * the transform info of image .
     */
    class Info{
        /** the scale up rate of width */
        public float widthRate;
        /** the scale up rate of height */
        public float heightRate;

        public static Info of(float widthRate, float heightRate){
            Info info = new Info();
            info.widthRate = widthRate;
            info.heightRate = heightRate;
            return info;
        }
    }
}
