package com.heaven7.vida.research.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import androidx.annotation.NonNull;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @author heaven7
 */
public class CommonUtils {


    public static boolean isInRange(float val, float start, float end) {
        return val >= start && val < end;
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }
        Bitmap bitmap;
        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            return null;
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public static DisplayMetrics getDisplayMetrics(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        return dm;
    }

    public static String getAndroidId(Context context) {
        return android.provider.Settings.Secure.getString(context.getContentResolver(),
                android.provider.Settings.Secure.ANDROID_ID);
    }

    public static String getAppVersion(Context context) {
        String versionName = "";
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo packageInfo = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = packageInfo.versionName;
            if (TextUtils.isEmpty(versionName)) {
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionName;
    }

    public static boolean isMainProcess(Context context) {
        ActivityManager am = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE));
        String mainProcessName = context.getPackageName();
        int myPid = android.os.Process.myPid();
        for (ActivityManager.RunningAppProcessInfo info : am.getRunningAppProcesses()) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }

    public static Map<String,String> toMap(@NonNull IRawData data) {
        final Map<String,String> map = new HashMap<String,String>();

        Class<?> clazz = data.getClass();
        populateJsonFromFields(data, clazz, map);
        while ((clazz = clazz.getSuperclass()) != Object.class && clazz != null) {
            populateJsonFromFields(data, clazz, map);
        }
        return map;
    }
    private static void populateJsonFromFields(Object data, Class<?> clazz, Map<String,String> map) {
        final boolean fromSuper = data.getClass() != clazz;
        final Field[] fields = clazz.getDeclaredFields();
        if (fields != null && fields.length > 0) {
            for (Field f : fields) {
                f.setAccessible(true);
                if (fromSuper) {
                    final Expose expose = f.getAnnotation(Expose.class);
                    if (expose == null || !expose.serialize()) {
                        continue;
                    }
                }
                final SerializedName sn = f.getAnnotation(SerializedName.class);
                if (sn == null) {
                    continue;
                }
                try {
                    Object val = f.get(data);
                    map.put(sn.value(), val !=null ? val.toString() : "");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * the tag interface, and the json fields must have annotation {@link com.google.gson.annotations.SerializedName}
     *  and field from super must have annotation {@link com.google.gson.annotations.Expose}.
     * Created by heaven7 on 2016/10/9.
     */

    public  interface IRawData {
    }
}
