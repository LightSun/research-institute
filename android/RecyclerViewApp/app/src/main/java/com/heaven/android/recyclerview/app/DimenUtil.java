package com.heaven.android.recyclerview.app;

import android.content.Context;
import android.support.annotation.DimenRes;

/**
 * @author heaven7
 **/
public class DimenUtil {

    public static int sp2px(Context context, float spValue) {
        //TypedValue.applyDimension()
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static int px2sp(Context context, int pxValue) {
        float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static int px2dip(Context context, int pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
    public static int getDimenSize(Context context,@DimenRes int id) {
        return context.getResources().getDimensionPixelSize(id);
    }
}
