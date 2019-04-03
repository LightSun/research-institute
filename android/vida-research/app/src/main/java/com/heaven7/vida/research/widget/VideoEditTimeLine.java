package com.heaven7.vida.research.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.heaven7.core.util.Logger;
import com.heaven7.vida.research.R;
import com.heaven7.vida.research.utils.DrawingUtils;
import com.heaven7.vida.research.utils.ScrollerWrapper;

import androidx.annotation.Nullable;
import androidx.core.view.GestureDetectorCompat;

/**
 * Created by heaven7 on 2019/4/2.
 */
public class VideoEditTimeLine extends View implements ScrollerWrapper.Callback{

    private static final String TAG = "VideoEditTimeLine";
    private static final int MAX_DURATION_IN_SECONDS = 99 * 60;
    private final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Rect mRect = new Rect();
    private final Rect mRange = new Rect();
    private final RectF mRectF = new RectF();

    private TimeFormatter mFormatter = new SimpleFormatter();

    private int mDistance = 300;          //one second indicate
   // private int mSegmentCount;          //one second indicate
    private float mScale = 1f;
    private int mDotRadius = 2;

    private int mOffsetX;
    private int mDuration = 9; // in seconds

    private final ScrollerWrapper mScroller;
    private GestureDetectorCompat mGestureDetector;
    private int mMaxOffsetX;
    private int mMinOffsetX;

    private Callback mCallback;

    public VideoEditTimeLine(Context context) {
        this(context, null);
    }

    public VideoEditTimeLine(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VideoEditTimeLine(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        int textColor = Color.RED;
        int textSize = 45;
        if(attrs != null){
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.VideoEditTimeLine);
            try {
                textColor = a.getColor(R.styleable.VideoEditTimeLine_vetl_text_color, textColor);
                textSize = a.getDimensionPixelSize(R.styleable.VideoEditTimeLine_vetl_text_size, textSize);
                mDotRadius = a.getDimensionPixelSize(R.styleable.VideoEditTimeLine_vetl_dot_radius, mDotRadius);
                mDistance = a.getDimensionPixelSize(R.styleable.VideoEditTimeLine_vetl_time_distance, mDistance);
            }finally {
                a.recycle();
            }
        }
        mPaint.setColor(textColor);
        mPaint.setTextSize(textSize);
        mPaint.setStyle(Paint.Style.FILL);

        mScroller = new ScrollerWrapper(this, this);
        mGestureDetector = new GestureDetectorCompat(context, new GestureHelper());
    }
    public void setCallback(Callback callback){
        this.mCallback = callback;
    }

    public void setDuration(int durationInSeconds){
        if(durationInSeconds >= MAX_DURATION_IN_SECONDS){
            throw new IllegalArgumentException();
        }
        this.mDuration = durationInSeconds;
        invalidate();
    }
    public void setScale(float scale){
        this.mScale = scale;
        invalidate();
    }
    public void setOffsetX(int offsetX){
        this.mOffsetX = offsetX;
        invalidate();
    }

    public void setOffsetPercent(float percent){
        this.mOffsetX = (int) (mMaxOffsetX - (mMaxOffsetX - mMinOffsetX) * percent);
        invalidate();
    }

    public void setMaxOffsetX(int max){
        int s = (int) (mDistance * mScale);
        this.mMaxOffsetX = max - getPaddingLeft();
        this.mMinOffsetX = mMaxOffsetX - s * mDuration;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(mDuration == 0){
            return;
        }
        final int left = getPaddingLeft();
        final int top = getPaddingTop();
        final int maxLeft = getWidth() - (left + mOffsetX);
        final int s = (int) (mDistance * mScale);
        final int contentHeight = getHeight() - getPaddingTop() - getPaddingBottom();

        canvas.save();
        canvas.translate(left + mOffsetX, top);

        final int duration = this.mDuration;
        for (int i = 0 ; i <= duration ; i++){
            String timeText = mFormatter.format(i);
           // Logger.d("VideoEditTimeLine", "onDraw", " i = " + i + " ,text = " + timeText);
            mPaint.getTextBounds(timeText, 0, timeText.length(), mRect);
            mRange.set(- mRect.width() / 2, 0, mRect.width() / 2, contentHeight);
            mRange.offset(i * s, 0);
           // mHalfTextWidth = mRect.width() / 2;

            DrawingUtils.computeTextDrawingCoordinate(timeText, mPaint, mRange, mRectF);
            canvas.drawText(timeText, mRectF.left, mRectF.top - mPaint.ascent(), mPaint);

            if( i != duration){
                //not the end. draw dot.
                float x = mRange.centerX() + s * 1f/ 2;
                int y = mRange.centerY();
                canvas.drawCircle(x, y, mDotRadius, mPaint);
            }
            //ignore not visible
            if(mRange.left >= maxLeft){
                break;
            }
        }
        canvas.restore();
        //just for debug
        //canvas.drawLine(getWidth() / 2, 0, getWidth() / 2 ,100, mPaint);
    }

    @Override
    public void computeScroll() {
        mScroller.computeScroll();
    }

    @Override
    public boolean onComputeScrolled(ScrollerWrapper wrapper, View view, int deltaX) {
        if(!canScroll(-deltaX)){
            dispatchTimeLineChangeEnd();
            return false;
        }
        mOffsetX += deltaX;
        clampOffsetX();
        invalidate();
        dispatchTimeLineChanged();
        return true;
    }

    @Override
    public void onFinish(ScrollerWrapper wrapper, View view) {
        view.invalidate();
        dispatchTimeLineChangeEnd();
    }

    private boolean canScroll(float dx) {
        if (mOffsetX == mMinOffsetX && dx > 0) {
            return false;
        }
        if (mOffsetX == mMaxOffsetX && dx < 0) {
            return false;
        }
        return true;
    }
    private void clampOffsetX(){
        if(mOffsetX > mMaxOffsetX){
            mOffsetX = mMaxOffsetX;
        }
        if(mOffsetX < mMinOffsetX){
            mOffsetX = mMinOffsetX;
        }
    }
    private void dispatchTimeLineChanged(){
        if(mCallback != null){
            int delta = mOffsetX - mMaxOffsetX;
            float percent = Math.abs(delta) * 1f/ Math.abs(mMaxOffsetX - mMinOffsetX);
            mCallback.onTimeLineChanged(this, percent);
        }
    }
    private void dispatchTimeLineChangeEnd(){
         if(mCallback != null){
             int delta = mOffsetX - mMaxOffsetX;
             float percent = Math.abs(delta) * 1f/ Math.abs(mMaxOffsetX - mMinOffsetX);
             mCallback.onTimeLineChangeEnd(this, percent);
         }
    }

    private class GestureHelper extends GestureDetector.SimpleOnGestureListener{

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            Logger.d(TAG, "onScroll", "distanceX = " + distanceX);
            mOffsetX -= distanceX;
            clampOffsetX();
            invalidate();
            dispatchTimeLineChanged();
            return true;
        }
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            Logger.d(TAG, "onFling", "velocityX = " + velocityX);
            mScroller.startFling(velocityX, velocityY);
            return true;
        }
    }

    public interface Callback{
        void onTimeLineChanged(VideoEditTimeLine view,float percent);
        void onTimeLineChangeEnd(VideoEditTimeLine view ,float percent);
    }

    /*public*/ interface TimeFormatter{
        String format(int seconds);
    }

    private static class SimpleFormatter implements TimeFormatter{
        final StringBuilder sb = new StringBuilder(5);
        @Override
        public String format(int timeInSeconds) {
            sb.delete(0, sb.length());
            int minute = 0;
            int seconds = timeInSeconds;
            if(timeInSeconds >= 60){
                minute = timeInSeconds / 60;
                seconds = timeInSeconds % 60;
            }
            if(minute > 9){
                sb.append(minute).append(":");
            }else {
                sb.append("0").append(minute).append(":");
            }
            if(seconds > 9){
                sb.append(seconds);
            }else {
                sb.append("0").append(seconds);
            }
            return sb.toString();
        }
    }
}
