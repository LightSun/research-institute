package com.heaven7.vida.research.widget;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Property;
import android.view.View;

/**
 * 圆环
 * Created by heaven7 on 2019/1/4.
 */
public class RingView extends View {
    private static final Property<RingView,Integer> sPROP = new Property<RingView, Integer>(
            Integer.class, "RingView") {
        @Override
        public Integer get(RingView object) {
            return object.getProcess();
        }
        @Override
        public void set(RingView object, Integer value) {
            object.setProcess(value);
        }
    };
    private final Path mPath = new Path();
    private final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final RectF mRectF = new RectF();

    private final int strokeWidth = 30;
    private int mCoverColor = Color.parseColor("#3f3f3f");
    private int mBackgroundColor = Color.parseColor("#333f3f3f");
    private int mStartAngle = -90;
    private int mSweepAngle = 0;

    public RingView(Context context) {
        super(context);
        init(context, null);
    }

    public RingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(strokeWidth);
    }

    public void setProcess(int process){
        mSweepAngle = process * 360 / 100;
        invalidate();
    }

    public int getProcess(){
        return mSweepAngle * 100 / 360;
    }

    public void setProgress(int process, boolean animate){
        if(!animate){
            setProcess(process);
        }else{
            int curProcess = getProcess();
            ObjectAnimator animator = ObjectAnimator.ofInt(this, sPROP, curProcess, process);
            animator.setDuration(500);
            animator.start();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mRectF.set(strokeWidth / 2, strokeWidth / 2, getWidth() - strokeWidth/2, getHeight() - strokeWidth /2);

        mPath.reset();
        mPath.addArc(mRectF, -90, 360);
        canvas.save();
        canvas.clipPath(mPath);

        int bgStart = mStartAngle + mSweepAngle;
        mPaint.setColor(mBackgroundColor);
        canvas.drawArc(mRectF, bgStart, 360 - bgStart, false, mPaint);
        mPaint.setColor(mCoverColor);
        canvas.drawArc(mRectF, mStartAngle, mSweepAngle, false, mPaint); //use center true , wii make start and end will concat center

        canvas.restore();
    }
}
