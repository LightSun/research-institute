package com.heaven7.vida.research.drawable;

import android.annotation.TargetApi;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.os.Build;

/**
 * Created by heaven7 on 2018/6/2 0002.
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class ShadowDrawable2 extends ShapeDrawable {

    public ShadowDrawable2() {
        super(new RectShape() {
            @Override
            public void draw(Canvas canvas, Paint paint) {
                paint.setColor(Color.WHITE);
                paint.setStyle(Paint.Style.FILL);
                canvas.drawPath(buildConvexPath(), paint);
            }

            @Override
            public void getOutline(Outline outline) {
                outline.setConvexPath(buildConvexPath());
            }

            private Path buildConvexPath() {
                Path path = new Path();
                path.lineTo(rect().left, rect().top);
                path.lineTo(rect().right, rect().top);
                path.lineTo(rect().left, rect().bottom);
                path.close();
                return path;
            }
        });
    }

}
