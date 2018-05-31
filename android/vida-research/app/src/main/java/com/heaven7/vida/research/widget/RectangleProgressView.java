package com.heaven7.vida.research.widget;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.FloatProperty;
import android.util.IntProperty;
import android.util.Property;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.heaven7.core.util.Logger;
import com.heaven7.vida.research.R;

import java.lang.ref.WeakReference;

/**
 * 矩形进度view
 * Created by heaven7 on 2018/5/28 0028.
 */

public class RectangleProgressView extends View {

    private static final String TAG = "RectangleProgress";
    private static final Property<RectangleProgressView, Integer> sANIM_PROP = new IntProperty<RectangleProgressView>("mDistance") {
        @Override
        public void setValue(RectangleProgressView object, int value) {
            object.mDistance = value;
            object.invalidate();
        }

        @Override
        public Integer get(RectangleProgressView object) {
            return object.mDistance;
        }
    };
    private final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Rect mRect = new Rect();

    private int mPHeight = 10;
    private int mDistance;
    private WeakReference<Animator> mWeakAnim;

    public RectangleProgressView(Context context) {
        this(context, null);
    }

    public RectangleProgressView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RectangleProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RectangleProgressView);
        int mPColor = Color.RED;
        try {
            mPHeight = a.getDimensionPixelSize(R.styleable.RectangleProgressView_rect_progress_height, mPHeight);
            mPColor = a.getColor(R.styleable.RectangleProgressView_rect_progress_color, mPColor);
        } finally {
            a.recycle();
        }
        mPaint.setColor(mPColor);
        mPaint.setStyle(Paint.Style.FILL);

        int offset = mPHeight / 2;
        // setPadding(offset, offset, offset, offset);
    }

    public void startProgressAnimation() {
        int width = getWidth() - getPaddingLeft() - getPaddingRight();
        int height = getHeight() - getPaddingTop() - getPaddingBottom();
        int distance = width * 2 + height * 2;
        ObjectAnimator animator = ObjectAnimator.ofInt(this, sANIM_PROP, 0, distance);
        animator.setDuration(1000);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.start();
        mWeakAnim = new WeakReference<Animator>(animator);
    }

    public void cancelProgressAnimation() {
        if (mWeakAnim != null && mWeakAnim.get() != null) {
            mWeakAnim.get().cancel();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mDistance == 0) {
            return;
        }
        //include padding
        // for make rect in center of view. we need adjust.
        // draw width = width + mPHeight
        // draw height = height + mPHeight
        int width = getWidth() - getPaddingLeft() - getPaddingRight();
        int height = getHeight() - getPaddingTop() - getPaddingBottom();

        int offsetW = (getWidth() - width - mPHeight) / 2;
        int offsetH = (getHeight() - height - mPHeight) / 2;

        if (mDistance <= width) {
            //draw only top.
            mRect.set(mPHeight, 0, mDistance + mPHeight, mPHeight);
            mRect.offset(offsetW, offsetH);
            canvas.drawRect(mRect, mPaint);
        } else if (mDistance <= width + height) {
            //draw  top and right.
            mRect.set(mPHeight, 0, width + mPHeight, mPHeight);
            mRect.offset(offsetW, offsetH);
            canvas.drawRect(mRect, mPaint);
            //right = last right, top = last bottom
            int hOffset = mDistance - width;
            mRect.set(mRect.right - mPHeight, mRect.bottom, mRect.right, mRect.bottom + hOffset);
            canvas.drawRect(mRect, mPaint);
            Logger.d(TAG, "onDraw", "draw  top and right");
        } else if (mDistance <= width * 2 + height) {
            //draw  top,right,bottom.
            mRect.set(mPHeight, 0, width + mPHeight, mPHeight);
            mRect.offset(offsetW, offsetH);
            canvas.drawRect(mRect, mPaint);

            //right = last right, top = last bottom
            mRect.set(mRect.right - mPHeight, mRect.bottom, mRect.right, mRect.bottom + height);
            canvas.drawRect(mRect, mPaint);

            int wOffset = mDistance - width - height;
            //right = last left, bottom = last bottom
            mRect.set(mRect.left - wOffset, mRect.bottom - mPHeight, mRect.left, mRect.bottom);
            canvas.drawRect(mRect, mPaint);
            Logger.d(TAG, "onDraw", "draw  top,right,bottom");
        } else if (mDistance <= width * 2 + height * 2) {
            //draw  left,top,right,bottom.
            mRect.set(mPHeight, 0, width + mPHeight, mPHeight);
            mRect.offset(offsetW, offsetH);
            canvas.drawRect(mRect, mPaint);

            //right = last right, top = last bottom
            mRect.set(mRect.right - mPHeight, mRect.bottom, mRect.right, mRect.bottom + height);
            canvas.drawRect(mRect, mPaint);

            //right = last left, bottom = last bottom
            mRect.set(mRect.left - width, mRect.bottom - mPHeight, mRect.left, mRect.bottom);
            canvas.drawRect(mRect, mPaint);

            int hOffset = mDistance - width * 2 - height;
            //bottom = last top, left = last left
            mRect.set(mRect.left, mRect.top - hOffset, mRect.left + mPHeight, mRect.top);
            canvas.drawRect(mRect, mPaint);
            Logger.d(TAG, "onDraw", "draw  left,top,right,bottom");
        } else {
            throw new IllegalStateException("distance is error");
        }
    }
}
