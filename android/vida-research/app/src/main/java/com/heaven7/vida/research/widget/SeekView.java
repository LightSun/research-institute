package com.heaven7.vida.research.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.heaven7.vida.research.R;

/**
 * 进度
 * Created by heaven7 on 2018/6/23 0023.
 */
public class SeekView extends View{

    private final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
    private final Rect mRect = new Rect();
    private final RectF mRectF = new RectF();

    private final GestureDetector mGestureDetector;

    /** the select progress color */
    private int mMainColor;
    private int mDefaultProgressColor;
    private int mProgressHeight;

    private int mBallRadius;
    private int mBallColor;

    private int mIndicatorMargin;
    private int mIndicatorRectWidth;
    private int mIndicatorRectHeight;

    private int mProgress;
    private int mMax = 100;
    private boolean mNarrowlyWhenClick = true;


    public SeekView(Context context) {
        this(context, null);
    }

    public SeekView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SeekView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        if(attrs != null){
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SeekView);
            int mTextSize = 0;
            try{
                mMainColor = a.getColor(R.styleable.SeekView_seek_progress_color, mMainColor);
                mDefaultProgressColor = a.getColor(R.styleable.SeekView_seek_default_color, mDefaultProgressColor);
                mBallColor = a.getColor(R.styleable.SeekView_seek_ball_color, mBallColor);

                mProgressHeight = a.getDimensionPixelSize(R.styleable.SeekView_seek_progress_height, mProgressHeight);
                mBallRadius = a.getDimensionPixelSize(R.styleable.SeekView_seek_ball_radius, mBallRadius);

                mIndicatorMargin = a.getDimensionPixelSize(R.styleable.SeekView_seek_indicator_margin_ball, mIndicatorMargin);
                mTextSize = a.getDimensionPixelSize(R.styleable.SeekView_seek_indicator_text_size, mTextSize);
                mIndicatorRectWidth = a.getDimensionPixelSize(R.styleable.SeekView_seek_indicator_rect_width, mIndicatorRectWidth);
                mIndicatorRectHeight = a.getDimensionPixelSize(R.styleable.SeekView_seek_indicator_rect_height, mIndicatorRectHeight);

                mNarrowlyWhenClick = a.getBoolean(R.styleable.SeekView_seek_narrowly_when_click, mNarrowlyWhenClick);

                mPaint.setTextSize(mTextSize);
            }finally {
                a.recycle();
            }
        }
        mPaint.setStyle(Paint.Style.FILL);

        //init touch
        GestureListenerImpl gestureListener = new GestureListenerImpl();
        mGestureDetector = new GestureDetector(context, gestureListener);
    }
    public void setProgress(int progress){
        if(progress < 0 || progress > mMax){
            throw new IllegalArgumentException("progress = " + progress);
        }
        if(progress != this.mProgress) {
            this.mProgress = progress;
            invalidate();
        }
    }
    public void setMaxProgress(int max) {
        this.mMax = max;
        invalidate();
    }
    public boolean isNarrowlyWhenClick() {
        return mNarrowlyWhenClick;
    }
    public void setNarrowlyWhenClick(boolean mNarrowlyWhenClick) {
        this.mNarrowlyWhenClick = mNarrowlyWhenClick;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(mGestureDetector.onTouchEvent(event)){
            return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.save();
        canvas.translate(getPaddingLeft(), getPaddingTop());

        int w = getWidth() - getPaddingLeft() - getPaddingRight();
        int h = getHeight() - getPaddingTop() - getPaddingBottom();

        int top =  h / 2 - mProgressHeight / 2;
        //scale = real / mock
        float scale = w * 1f / (mMax);

        if(mProgress > 0){
            final String text = mProgress + "";
            int dis = (int) (mProgress * scale);
            //draw select
            mRect.set(0, top, dis, top + mProgressHeight);
            mPaint.setColor(mMainColor);
            canvas.drawRect(mRect, mPaint);
            //draw unselct
            mRect.set(dis, top, w, top + mProgressHeight);
            mPaint.setColor(mDefaultProgressColor);
            canvas.drawRect(mRect, mPaint);

            drawBallAndIndicator(canvas, text, top, dis);
        }else{
            //mProgress = 0
            mRect.set(0, top, w, top + mProgressHeight);
            mPaint.setColor(mDefaultProgressColor);
            canvas.drawRect(mRect, mPaint);

            drawBallAndIndicator(canvas, "0", top, 0);
        }

        canvas.restore();
    }

    private void drawBallAndIndicator(Canvas canvas, String text, int top, int centerX){
        //draw ball
        mPaint.setColor(mBallColor);
        canvas.drawCircle(centerX, top + mProgressHeight * 1f/ 2, mBallRadius, mPaint);
        //draw text
        drawIndicator(canvas, text, top - mBallRadius, centerX);
    }

    private void drawIndicator(Canvas canvas, String text, int top, int centerX) {
        mRectF.set(centerX - mIndicatorRectWidth * 1f/ 2,
                top - mIndicatorMargin - mIndicatorRectHeight ,
                centerX + mIndicatorRectWidth * 1f/ 2,
                top - mIndicatorMargin);
        final RectF bounds = mRectF;
        bounds.right = mPaint.measureText(text);
        bounds.bottom = mPaint.descent() - mPaint.ascent();
        bounds.left += (mIndicatorRectWidth - bounds.right) / 2.0f;
        bounds.top += (mIndicatorRectHeight - bounds.bottom) / 2.0f;

        mPaint.setColor(mMainColor);
        canvas.drawText(text, bounds.left, bounds.top - mPaint.ascent(), mPaint);
    }

    private class GestureListenerImpl implements GestureDetector.OnGestureListener {
        @Override
        public boolean onDown(MotionEvent e) {
            //Logger.i("GestureListenerImpl", "onDown", "" + e);
            return true;
        }

        @Override
        public void onShowPress(MotionEvent e) {

        }
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return SeekView.this.onSingleTapUp(e);
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return SeekView.this.onScroll(e1, e2, distanceX, distanceY);
        }

        @Override
        public void onLongPress(MotionEvent e) {
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }
    }

    private boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        setProgressByMotionEvent(e2, false);
        return true;
    }

    private boolean onSingleTapUp(MotionEvent e) {
        setProgressByMotionEvent(e, true);
        return true;
    }

    private void setProgressByMotionEvent(MotionEvent e, boolean click) {
        float x = e.getX() - getPaddingLeft();
        float scale = (getWidth() - getPaddingLeft() - getPaddingRight()) * 1f / (mMax);

        float delta = x;
        float previous = scale * this.mProgress;

        int progress;
        if(click && mNarrowlyWhenClick){
            //judge if is at right/left
            if(delta > previous){
                progress = this.mProgress + 1;
            }else if(delta < previous){
                progress = this.mProgress - 1;
            }else{
                return;
            }
        }else{
            progress = (int) (x / scale);
        }
        //clamp
        if(progress > mMax){
            progress = mMax;
        }else if(progress < 0){
            progress = 0;
        }
        setProgress(progress);
    }
}
