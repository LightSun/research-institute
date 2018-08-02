package com.vida.common;

import java.util.Properties;

/**
 * @author heaven7
 */
public abstract class Platform {

    private static Platform sInstance;

    public static final byte WINDOWS = 1;
    public static final byte MAC     = 2;
    public static final byte LINUX  = 3;
    public static final byte ANDROID  = 4;
    public static final byte UNKNOWN  = 5;
    private static final byte SYSTEM_TYPE;

    static {
        Properties prop = System.getProperties();
        String os = prop.getProperty("os.name");

        if (os.contains("Windows")) {
            SYSTEM_TYPE = WINDOWS;
        } else if (os.contains("OS X")) {
            SYSTEM_TYPE = MAC;
        } else if (os.contains("linux")){
            SYSTEM_TYPE = LINUX;
        }else if (os.contains("android")){
            SYSTEM_TYPE = ANDROID;
        }else{
            SYSTEM_TYPE = UNKNOWN;
        }

        try {
            Class<?> clazz = Class.forName("com.vida.ai.server.montage.platform.PlatformImpl");
            sInstance = (Platform) clazz.newInstance();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static byte getSystemType(){
        return SYSTEM_TYPE;
    }

    public static boolean isLinux() {
        return SYSTEM_TYPE == LINUX || SYSTEM_TYPE == ANDROID;
    }

    public static String getNewLine() {
        return isLinux() ? "\n" : "\r\n";
    }

    public static Platform getDefault(){
        return sInstance;
    }


    //--------------------------------------

    public abstract FFMpegCmdGenerator getFFMpegCmdGenerator(boolean showWindow);
}
