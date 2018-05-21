package com.heaven7.utils;

import java.io.File;
import java.util.concurrent.TimeUnit;

public class CommonUtils {

    public static String getFileExtension(File file) {
        String name = file.getName();
        try {
            return name.substring(name.lastIndexOf(".") + 1);
        } catch (Exception e) {
            return null;
        }
    }

    public static float frameToTime(long frames, TimeUnit unit) {
        switch (unit) {
            case SECONDS:
                return frames * 1f / Constants.FPS_VIDEO;

            case MILLISECONDS:
                return frames * 1000f / Constants.FPS_VIDEO;

            case MICROSECONDS:
                return frames * 1000000f / Constants.FPS_VIDEO;

            default:
                throw new UnsupportedOperationException("+" + unit);
        }
    }

    public static long timeToFrame(float time, TimeUnit unit) {
        switch (unit) {
            case SECONDS:
                return (int) (time * Constants.FPS_VIDEO);

            case MILLISECONDS:
                return (int) (time * Constants.FPS_VIDEO / 1000);

            case MICROSECONDS:
                return (int) (time * Constants.FPS_VIDEO / 1000000);

            default:
                throw new UnsupportedOperationException("+" + unit);
        }
    }


    public static boolean isInRange(float val, float start, float end) {
        return val >= start && val < end;
    }
}
