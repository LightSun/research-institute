package com.heaven7.utils;

import com.heaven7.java.base.anno.Nullable;

public class TextUtils {

    public static boolean isEmpty(@Nullable CharSequence str) {
        return str == null || str.length() == 0;
    }
}
