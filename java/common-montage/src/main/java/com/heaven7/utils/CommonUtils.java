package com.heaven7.utils;


import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.concurrent.TimeUnit;

public class CommonUtils {

    private static final int FPS_VIDEO = 30;
    private static final char[] ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

    /**
     * indicate the path is relative path or absolute path.
     * @param path the path to judge
     * @return true if is relative path
     */
    public static boolean isRelativePath(String path){
        if(path.startsWith("/")){
            return false;
        }
        for (char ch : ALPHABET){
            if(path.startsWith(ch + ":")){
                return false;
            }
        }
        return true;
    }

    public static String urlEncode(String  str){
        try {
            return URLEncoder.encode(str, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return str;
        }
    }
    public static String urlDecode(String  str){
        try {
            return URLDecoder.decode(str, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return str;
        }
    }

    public static boolean strEquals(String str1, String str2){
        if(str1 == null){
            return str2 == null;
        }
        return str1.equals(str2);
    }

    public static float frameToTime(long frames, TimeUnit unit) {
        switch (unit) {
            case SECONDS:
                return frames * 1f / FPS_VIDEO;

            case MILLISECONDS:
                return frames * 1000f / FPS_VIDEO;

            case MICROSECONDS:
                return frames * 1000000f / FPS_VIDEO;

            default:
                throw new UnsupportedOperationException("+" + unit);
        }
    }

    public static long timeToFrame(float time, TimeUnit unit) {
        switch (unit) {
            case SECONDS:
                return (int) (time * FPS_VIDEO);

            case MILLISECONDS:
                return (int) (time * FPS_VIDEO / 1000);

            case MICROSECONDS:
                return (int) (time * FPS_VIDEO / 1000000);

            default:
                throw new UnsupportedOperationException("+" + unit);
        }
    }

    public static boolean isInRange(float val, float start, float end) {
        return val >= start && val < end;
    }
}
