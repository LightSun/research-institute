package com.heaven7.java.image.detect;

/**
 * the data transformer
 * @author heaven7
 */
public interface IDataTransformer<T> {

    T transform(TransformInfo info);
}
