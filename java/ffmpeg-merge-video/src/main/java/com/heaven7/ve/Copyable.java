package com.heaven7.ve;

/**
 * Created by heaven7 on 2018/3/27 0027.
 */

public interface Copyable<T> {

    void setFrom(T t);
    T create();

    T copy();
}
