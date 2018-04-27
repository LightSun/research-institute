package com.heaven7.vida.research.retrofit;

import android.os.Environment;

import java.io.File;

public class Utils {

    public static boolean isSdcardExists() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    public static String getRootDirectory() {
        File sdCardDirectory = null;
        if (isSdcardExists()) {
            sdCardDirectory = Environment.getExternalStorageDirectory();
        }
        if (sdCardDirectory != null) {
            return sdCardDirectory.toString();
        } else {
            return "";
        }
    }

}
