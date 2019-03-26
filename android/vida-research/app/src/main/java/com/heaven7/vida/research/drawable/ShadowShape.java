package com.heaven7.vida.research.drawable;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.shapes.Shape;
import androidx.renderscript.Allocation;
import androidx.renderscript.Element;
import androidx.renderscript.RenderScript;
import androidx.renderscript.ScriptIntrinsicBlur;

/**
 * Created by heaven7 on 2018/6/2 0002.
 */
public class ShadowShape extends Shape {

    Context ctx;
    Drawable source;
    Drawable mask;
    Drawable shadow;
    float shadowRadius;
    Matrix matrix = new Matrix();
    Bitmap bitmap;

    public ShadowShape(Context ctx, Drawable source, Drawable mask, Drawable shadow, float shadowRadius) {
        this.ctx = ctx;
        this.source = source;
        this.mask = mask;
        this.shadow = shadow;
        this.shadowRadius = shadowRadius;
    }

    @Override
    protected void onResize(float width, float height) {
        int intrinsicWidth = source.getIntrinsicWidth();
        int intrinsicHeight = source.getIntrinsicHeight();
        source.setBounds(0, 0, intrinsicWidth, intrinsicHeight);
        mask.setBounds(0, 0, intrinsicWidth, intrinsicHeight);
        shadow.setBounds(0, 0, intrinsicWidth, intrinsicHeight);

        RectF src = new RectF(0, 0, intrinsicWidth, intrinsicHeight);
        RectF dst = new RectF(0, 0, width, height);
        matrix.setRectToRect(src, dst, Matrix.ScaleToFit.CENTER);

        Paint p = new Paint();
        p.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));
        bitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bitmap);

        shadow.draw(c);
        c.saveLayer(null, p, Canvas.ALL_SAVE_FLAG);
        mask.draw(c);
        c.restore();
        bitmap = blurRenderScript(bitmap);

        c = new Canvas(bitmap);
        int count = c.saveLayer(null, null, Canvas.ALL_SAVE_FLAG);
        source.draw(c);
        c.saveLayer(null, p, Canvas.ALL_SAVE_FLAG);
        mask.draw(c);
        c.restoreToCount(count);
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        canvas.drawColor(0xffdddddd);
        canvas.drawBitmap(bitmap, matrix, null);
    }

    private Bitmap blurRenderScript(Bitmap input) {
        Bitmap output = Bitmap.createBitmap(input.getWidth(), input.getHeight(), input.getConfig());
        RenderScript rs = RenderScript.create(ctx);
        ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        Allocation inAlloc = Allocation.createFromBitmap(rs, input, Allocation.MipmapControl.MIPMAP_NONE, Allocation.USAGE_GRAPHICS_TEXTURE);
        Allocation outAlloc = Allocation.createFromBitmap(rs, output);
        script.setRadius(shadowRadius);
        script.setInput(inAlloc);
        script.forEach(outAlloc);
        outAlloc.copyTo(output);
        rs.destroy();
        return output;
    }
}
