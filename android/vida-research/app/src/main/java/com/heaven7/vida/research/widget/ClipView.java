package com.heaven7.vida.research.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Region;
import android.util.AttributeSet;
import android.view.View;

public class ClipView extends View {

    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
   // private final TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);

    public ClipView(Context context) {
        super(context);
    }

    public ClipView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ClipView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();

        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);
        canvas.save();
        canvas.clipRect(50, 50, 300, 300, Region.Op.DIFFERENCE); //default is Region.Op.INTERSECT
        canvas.drawRect(0, 0, width, height, paint);
        canvas.restore();

        paint.setColor(Color.RED);
        canvas.drawRect(50, 50, 300, 300, paint);
    }
}
