package com.heaven7.vida.research.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.util.ArrayMap;

/**
 * Created by heaven7 on 2018/11/7 0007.
 */
public class TypefaceCacher {

    private static final ArrayMap<String, Typeface> sCache;

    static {
        sCache = new ArrayMap<>();
    }

    /**
     * get the type face
     * @param context the context
     * @param path the path. can be assets path or absolute file path
     * @return the typeface.
     */
    public static Typeface getTypeface(Context context, String path){
        Typeface typeface = sCache.get(path);
        if(typeface == null) {
            if (isAbsolutePath(path)) {
                typeface = Typeface.createFromFile(path);
            }else{
                typeface = Typeface.createFromAsset(context.getAssets(), path);
            }
            sCache.put(path, typeface);
        }
        return typeface;
    }

    public static void clearCache(){
        sCache.clear();
    }

    private static boolean isAbsolutePath(String path){
        return path.startsWith("/");
    }

}
