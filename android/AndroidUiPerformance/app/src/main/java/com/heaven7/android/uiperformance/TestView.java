package com.heaven7.android.uiperformance;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by heaven7 on 2019/6/19.
 */
public final class TestView extends View {

    private final Paint mPaint = new Paint();
    private boolean mStartInvalidate;

    public TestView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TestView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint.setColor(Color.MAGENTA);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();
        canvas.drawCircle(width / 2, height / 2, 60, mPaint);

        int color = mPaint.getColor();
        color -= 5;
        if(color <= 0){
            color = Color.MAGENTA;
        }
        mPaint.setColor(color);
        if(mStartInvalidate){
            postDelayed(mTask, 31);
        }
    }

    public void start(){
        if(!mStartInvalidate){
            mStartInvalidate = true;
            invalidate();
        }
    }

    private final Runnable mTask = new Runnable() {
        @Override
        public void run() {
             invalidate();
        }
    };

}
