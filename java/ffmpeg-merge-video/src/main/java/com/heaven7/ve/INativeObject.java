package com.heaven7.ve;

/**
 * the native object delegate . which can use java object to manage the native c++ object.
 * Created by heaven7 on 2018/3/28 0028.
 */

public interface INativeObject {

    /**
     * get the native pointer, this is called in native.
     * @return the native pointer
     */
    long getNativePointer();

    /**
     * this method should called in {@linkplain Object#finalize()}.
     */
    void destroyNative();
}
