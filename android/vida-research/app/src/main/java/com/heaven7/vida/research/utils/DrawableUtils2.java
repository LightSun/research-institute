package com.heaven7.vida.research.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptIntrinsicBlur;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by heaven7 on 2018/2/7 0007.
 */
public class DrawableUtils2 {

    /** 居中裁剪 -- 缩放 */
    public static Bitmap clipBitmap(Bitmap src, int requireW, int requireH){
        int w = src.getWidth();
        int h = src.getHeight();
        float scaleW = requireW * 1f / w;
        float scaleH = requireH * 1f / h;
        float scale = Math.min(scaleW, scaleH);

        int resultW = (int) (w * scale);
        int resultH = (int) (h * scale);
        int left = w / 2 - resultW / 2;
        int top = h / 2 - resultH / 2;
        Bitmap bitmap = Bitmap.createBitmap(src, left, top, resultW, resultH);
        Bitmap result = Bitmap.createScaledBitmap(bitmap, requireW, requireH, false);
        bitmap.recycle();
        return result;
    }

    //先缩，再裁
    public static Bitmap clipBitmap2(Bitmap src, int requireW, int requireH){
        int w = src.getWidth();
        int h = src.getHeight();
        float scaleW = requireW * 1f / w;
        float scaleH = requireH * 1f / h;
        float scale = Math.max(scaleW, scaleH);

        Matrix matrix = new Matrix();
        matrix.setScale(scale, scale);
        Bitmap scaled = Bitmap.createBitmap(src, 0, 0, w, h, matrix, false);

        int sWidth = scaled.getWidth();
        int sHeight = scaled.getHeight();
        int left = 0, top = 0;
        if(sWidth > requireW){
            left = sWidth / 2 - requireW / 2;
        }
        if(sHeight > requireH){
            top = sHeight / 2 - requireH/ 2;
        }
        return Bitmap.createBitmap(scaled, left, top, requireW, requireH);
    }

    public static Drawable getDrawable(Context context, @DrawableRes int resId){
        Drawable d = context.getResources().getDrawable(resId);
        if(d == null){
            return null;
        }
        d.setBounds(0, 0 , d.getIntrinsicWidth(), d.getIntrinsicHeight());
        return d;
    }

    public static Bitmap blurRenderScript(Context context,Bitmap input,float radius) {
        Bitmap output = Bitmap.createBitmap(input.getWidth(), input.getHeight(), input.getConfig());
        RenderScript rs = RenderScript.create(context);
        ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        Allocation inAlloc = Allocation.createFromBitmap(rs, input, Allocation.MipmapControl.MIPMAP_NONE, Allocation.USAGE_GRAPHICS_TEXTURE);
        Allocation outAlloc = Allocation.createFromBitmap(rs, output);
        script.setRadius(radius);
        script.setInput(inAlloc);
        script.forEach(outAlloc);
        outAlloc.copyTo(output);
        rs.destroy();
        return output;
    }

    public static byte[] bitmap2Bytes(Bitmap bm) {
        if (bm == null) {
            return null;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    public static Drawable zoomDrawable(Context context, Drawable drawable, int w, int h) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap oldbmp = drawableToBitmap(drawable);
        if(oldbmp == null){
            return null;
        }
        Matrix matrix = new Matrix();
        float scaleWidth = ((float) w / width);
        float scaleHeight = ((float) h / height);
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newbmp = Bitmap.createBitmap(oldbmp, 0, 0, width, height, matrix, true);
        return new BitmapDrawable(context.getResources(), newbmp);
    }

    public static void saveBitmap(Bitmap bitmap , String filePath){
        OutputStream out = null;
        try {
            out = new FileOutputStream(filePath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        }catch (IOException e){
            throw new RuntimeException(e);
        } finally {
            if(out != null){
                try {
                    out.close();
                } catch (IOException e) {
                }
            }
        }
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if(bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        if(width == 0 || height == 0){
            return null;
        }
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);
        return bitmap;
    }

}
