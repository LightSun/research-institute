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

import com.heaven7.vida.research.R;
import com.heaven7.vida.research.utils.DrawingUtils;

import androidx.annotation.Nullable;
import androidx.core.view.GestureDetectorCompat;

/**
 * Created by heaven7 on 2019/4/2.
 */
public class VideoEditTimeLine extends View {

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

    private GestureDetectorCompat mGestureDetector;
    private int mHalfTextWidth;
    private int mMaxOffsetX;
    private int mMinOffsetX;

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

        mGestureDetector = new GestureDetectorCompat(context, new GestureHelper());
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

    public void setMaxOffsetX(int max){
        this.mMaxOffsetX = max + getPaddingLeft();
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
        for (int i = 0 ; i < duration ; i++){
            String timeText = mFormatter.format(i);
           // Logger.d("VideoEditTimeLine", "onDraw", " i = " + i + " ,text = " + timeText);
            mPaint.getTextBounds(timeText, 0, timeText.length(), mRect);
            mRange.set(- mRect.width() / 2, 0, mRect.width() / 2, contentHeight);
            mRange.offset(i * s, 0);
            mHalfTextWidth = mRect.width() / 2;

            DrawingUtils.computeTextDrawingCoordinate(timeText, mPaint, mRange, mRectF);
            canvas.drawText(timeText, mRectF.left, mRectF.top - mPaint.ascent(), mPaint);

            if( i != duration -1){
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
    }

    private class GestureHelper extends GestureDetector.SimpleOnGestureListener{
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return super.onScroll(e1, e2, distanceX, distanceY);
        }
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return super.onFling(e1, e2, velocityX, velocityY);
        }
    }

    public interface TimeFormatter{
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
