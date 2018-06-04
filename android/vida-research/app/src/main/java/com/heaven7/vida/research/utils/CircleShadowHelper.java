package com.heaven7.vida.research.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;

/**
 * Created by heaven7 on 2018/6/4 0004.
 */
public class CircleShadowHelper {

    private static final int KEY_SHADOW_COLOR = 0x1E000000;
    private static final int FILL_SHADOW_COLOR = 0x3D000000;
    // PX
    private static final float X_OFFSET = 0f;
    private static final float Y_OFFSET = 1.75f;
    //private static final float SHADOW_RADIUS = 3.5f;
    //private static final int SHADOW_ELEVATION = 4;

   // private Paint shadowPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
    private Bitmap shadow;

    /**
     * circle shadow helper
     *
     * @param context        the context
     * @param circleRadius   the circle radius
     * @param shadowRadius   the shadow radius. [0-25]
     * @param drawableBounds the bounds of drawable.
     */
    public CircleShadowHelper(Context context, int circleRadius, float shadowRadius, Rect drawableBounds) {
      /*  RadialGradient mRadialGradient = new RadialGradient(circleRadius, circleRadius,
                shadowRadius, new int[] { FILL_SHADOW_COLOR, Color.TRANSPARENT },
                null, Shader.TileMode.CLAMP);
        shadowPaint.setShader(mRadialGradient);*/

        /*float outRadius = circleRadius / 2;
        float offset = 2;
        float innerRadius = outRadius - offset;
        RectF rectF = new RectF(offset, offset, offset, offset);
        float[] innerRadiuss = new float[]{
                innerRadius, innerRadius, innerRadius, innerRadius,
                innerRadius, innerRadius, innerRadius, innerRadius
        };

        RoundRectShape shape = new RoundRectShape(new float[]{
                outRadius, outRadius, outRadius, outRadius, outRadius, outRadius, outRadius, outRadius
        }, rectF, innerRadiuss);*/
        // shadowColor = 0x33000000
     /*   ShapeDrawable drawable = new ShapeDrawable(shape);
        drawable.setColorFilter(new PorterDuffColorFilter(shadowColor, PorterDuff.Mode.SRC_IN));
        drawable.setBounds(drawableBounds);*/
        ShapeDrawable drawable = getShapeDrawable(context, circleRadius, shadowRadius);
        drawable.setBounds(drawableBounds);

        //draw
        Bitmap bitmap = Bitmap.createBitmap(circleRadius * 2, circleRadius * 2, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.draw(canvas);
        //shadow = DrawableUtils2.blurRenderScript(context, bitmap, shadowRadius);
        shadow = bitmap;
    }

    public void draw(Canvas canvas, int r, Rect rect) {
        canvas.drawBitmap(shadow, null, rect, null);
    }

    private ShapeDrawable getShapeDrawable(Context context, int circleRadius, float mShadowRadius){
        final float density = context.getResources().getDisplayMetrics().density;
        final int shadowYOffset = (int) (density * Y_OFFSET);
        final int shadowXOffset = (int) (density * X_OFFSET);

        OvalShape oval = new OvalShadow(circleRadius, mShadowRadius);
        ShapeDrawable circle = new ShapeDrawable(oval);
        circle.getPaint().setShadowLayer(mShadowRadius, shadowXOffset, shadowYOffset,
                KEY_SHADOW_COLOR);
        //final int padding = mShadowRadius;
        // set padding so the inner image sits correctly within the shadow.
        //setPadding(padding, padding, padding, padding);
        return circle;
    }

    private static class OvalShadow extends OvalShape {
        private RadialGradient mRadialGradient;
        private Paint mShadowPaint;
        private float mShadowRadius;
        private final int mCirclrRadius;

        OvalShadow(int circleRadius, float shadowRadius) {
            super();
            mShadowPaint = new Paint();
            mShadowRadius = shadowRadius;
            this.mCirclrRadius = circleRadius;
            updateRadialGradient((int) rect().width());
        }

        @Override
        protected void onResize(float width, float height) {
            super.onResize(width, height);
            updateRadialGradient((int) width);
        }

        @Override
        public void draw(Canvas canvas, Paint paint) {

            int halfW = mCirclrRadius;
            int halfH = mCirclrRadius;
            canvas.drawCircle(halfW, halfH, mCirclrRadius, mShadowPaint);
            int color = paint.getColor();
            paint.setColor(Color.WHITE);
            canvas.drawCircle(halfW, halfH, mCirclrRadius - mShadowRadius, paint);
            paint.setColor(color);
        }

        private void updateRadialGradient(int diameter) {
            mRadialGradient = new RadialGradient(diameter / 2, diameter / 2,
                    mShadowRadius, new int[] { FILL_SHADOW_COLOR, Color.TRANSPARENT },
                    null, Shader.TileMode.CLAMP);
            mShadowPaint.setShader(mRadialGradient);
        }
    }
}
