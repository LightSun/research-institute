package com.heaven.android.lua.lds.app;

/**
 * Created by heaven7 on 2019/8/16.
 */
public final class LuaDesigner {

    static {
        System.loadLibrary("dummylua");
    }

    public static native void testBase();
    public static native void testSetJmp();
}
