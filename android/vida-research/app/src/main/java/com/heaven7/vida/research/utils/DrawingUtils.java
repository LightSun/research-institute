package com.heaven7.vida.research.utils;

import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

/**
 * Created by heaven7 on 2018/11/7 0007.
 */
public class DrawingUtils {

    private static final Rect sRect = new Rect();

    public static Rect measure(Paint p, String text){
        p.getTextBounds(text, 0, text.length(), sRect);
        return sRect;
    }

    //draw text in center of rect
    public static void computeTextDrawingCoordinate(String text, Paint paint, Rect srcRange, RectF out){
        out.set(srcRange);
        out.right = paint.measureText(text);
        out.bottom = paint.descent() - paint.ascent();
        out.left += (srcRange.width() - out.right) / 2.0f;
        out.top += (srcRange.height() - out.bottom) / 2.0f;
        //  canvas.drawText(text, bounds.left, bounds.top - mPaint.ascent(), mPaint);
    }

    /** make the small rect center in big rect. */
    public static void center(Rect small, Rect big){
        int bigW = big.width();
        int bigH = big.height();
        int smallW = small.width();
        int smallH = small.height();

        int offsetX = 0;
        int offsetY = 0;
        if(bigW > smallW){
            offsetX = (bigW - smallW) / 2;
        }
        if(bigH > smallH){
            offsetY = (bigH - smallH) / 2;
        }
        small.offset(offsetX, offsetY);
    }

}
