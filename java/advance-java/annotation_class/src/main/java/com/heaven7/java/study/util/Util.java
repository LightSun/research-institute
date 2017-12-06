package com.heaven7.java.study.util;

/**
 * Created by heaven7 on 2017/8/28 0028.
 */
public final class Util {


    /**
     * get the class name which replace '.' to '$'
     *
     * @param rawSimpleName should not contain package name
     */
    public static String getClassName(String rawSimpleName) {
        return rawSimpleName.replace('.', '$');
    }

    /**
     * get the target class name which will be generated.
     */
    public static String getTargetClassName(String packageName, String fullName) {
        int packageLen = packageName.length() + 1;
        return fullName.substring(packageLen).replace('.', '$');
    }

    /**
     * get the target class name which is only used to reference (impl/extend/return/param/field...)
     * if you should extend another. please use {@linkplain #getTargetClassName}
     */
    public static String getRawTargetClassName(String packageName, String fullName) {
        int packageLen = packageName.length() + 1;
        return fullName.substring(packageLen);
    }

}
