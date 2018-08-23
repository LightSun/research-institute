package com.heaven7.utils;

import com.heaven7.java.base.anno.Nullable;

public class TextUtils {

    public static boolean isEmpty(@Nullable CharSequence str) {
        return str == null || str.length() == 0;
    }

    /**
     * get the relative path for prefix path. eg:  absPath = 'e:/data/xxx/aaa',  prefix ='e:/data/', so result is 'xxx/aaa'
     *
     * @param absPath the absolute path
     * @param prefixPath the prefix path of abspath
     * @return the relative path
     */
    public static String getRelativePathForPrefix(String absPath, String prefixPath) {
        int index = absPath.indexOf(prefixPath);
        if (index == -1) {
            throw new IllegalArgumentException("can't getRelativePath for absPath = "
                    + absPath + " ,prefixPath = " + prefixPath);
        }
        String result = absPath.substring(index + prefixPath.length());
        if(result.startsWith("/")){
            result = result.substring(1);
        }else if(result.startsWith("\\")){
            result = result.substring(1);
        }
        return result;
    }

}
