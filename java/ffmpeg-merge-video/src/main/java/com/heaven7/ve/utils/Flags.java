package com.heaven7.ve.utils;

/**
 * @author heaven7
 */
public class Flags {

    public static boolean hasFlags(int totalFlogs, int requireFlags){
        return (totalFlogs & requireFlags) == requireFlags;
    }
}
