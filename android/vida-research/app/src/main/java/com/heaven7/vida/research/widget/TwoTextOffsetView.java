package com.heaven7.vida.research.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

import com.heaven7.vida.research.R;

/**
 * 俩个文本左右偏移
 * Created by heaven7 on 2019/3/11.
 */
public class TwoTextOffsetView extends View {

    private static final byte DIR_NONE = 0;
    private static final byte DIR_LEFT = 1;
    private static final byte DIR_RIGHT = 2;
    private final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
    private final PagerListener mPagerListener = new PagerListener();
    private ViewPager mPager;

    private final Rect mRect = new Rect();
    private final RectF mRectf = new RectF();

    private int mTextSpace = 20;
    private String mSampleText = "01";

    private int mCurrentPosition;
    private float mOffsetPercent;
    private byte mDir;

    public TwoTextOffsetView(Context context) {
        this(context, null, 0);
    }

    public TwoTextOffsetView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TwoTextOffsetView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        int textSize = 20;
        int textColor = Color.BLACK;
        if(attrs != null){
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TwoTextOffsetView);
            try {
                mTextSpace = a.getDimensionPixelSize(R.styleable.TwoTextOffsetView_ttov_text_space, mTextSpace);
                textSize = a.getDimensionPixelSize(R.styleable.TwoTextOffsetView_ttov_text_size, textSize);
                textColor = a.getColor(R.styleable.TwoTextOffsetView_ttov_text_color, textColor);
                mSampleText = a.getString(R.styleable.TwoTextOffsetView_ttov_sample_text);
            }finally {
                a.recycle();
            }
        }
        mPaint.setColor(textColor);
        mPaint.setTextSize(textSize);
        mPaint.setStyle(Paint.Style.STROKE);
    }

    public void concatViewPager(ViewPager pager){
        this.mPager = pager;
        pager.addOnPageChangeListener(mPagerListener);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
       // super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mPaint.getTextBounds(mSampleText, 0, mSampleText.length(), mRect);
        int w = MeasureSpec.makeMeasureSpec(mRect.width(), MeasureSpec.EXACTLY);
        int h = MeasureSpec.makeMeasureSpec(mRect.height(), MeasureSpec.EXACTLY);
        setMeasuredDimension(w, h);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if(mPager != null){
            mPager.removeOnPageChangeListener(mPagerListener);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        PagerAdapter adapter = mPager.getAdapter();
        if(adapter == null){
            return;
        }
        mRect.set(getPaddingLeft(), getPaddingTop(),
                getWidth() - getPaddingLeft()- getPaddingRight(),
                getHeight() - getPaddingTop() - getPaddingBottom());
        switch (mDir){
            case DIR_LEFT:
                drawScrollToLeft(canvas, mRect);
                break;

            case DIR_RIGHT:
                drawScrollToRight(canvas, mRect);
                break;
            case DIR_NONE:
                drawNone(canvas, mRect);
                break;
        }
    }

    private void drawNone(Canvas canvas, Rect rect) {
        int pos = this.mCurrentPosition;
        PagerAdapter adapter = mPager.getAdapter();
        String title = adapter.getPageTitle(pos).toString();
        computeTextDrawingCoordinate(title, mPaint, rect, mRectf);
        mPaint.setAlpha(255);
        canvas.drawText(title, mRectf.left, mRectf.top - mPaint.ascent(), mPaint);
    }

    private void drawScrollToRight(Canvas canvas, Rect mRect) {
        //center and left -> offset right
        int pos = this.mCurrentPosition;
        if(pos == 0){
            drawNone(canvas, mRect);
            return;
        }
        PagerAdapter adapter = mPager.getAdapter();
        String leftText = adapter.getPageTitle(pos - 1).toString();
        String curText = adapter.getPageTitle(pos).toString();

        int offsetX = (int) (getWidth() * mOffsetPercent);
        int curAlpha = (int) (255 * (1 - mOffsetPercent));
        int leftAlpha = (int) (255 * mOffsetPercent);
        //start draw cur and left
        mRect.offset(offsetX, 0);
        drawText(canvas, mRect,curText, curAlpha);

        mRect.offset( - getWidth() - mTextSpace, 0);
        drawText(canvas, mRect, leftText, leftAlpha);
    }

    private void drawScrollToLeft(Canvas canvas, Rect mRect) {
        //center and right -> offset left
        int pos = this.mCurrentPosition;
        PagerAdapter adapter = mPager.getAdapter();
        if(pos == adapter.getCount() - 1){
            drawNone(canvas, mRect);
            return;
        }

        String rightText = adapter.getPageTitle(pos + 1).toString();
        String curText = adapter.getPageTitle(pos).toString();

        int offsetX = (int) ((getWidth() + mTextSpace) * mOffsetPercent);
        int curAlpha = (int) (255 * (1 - mOffsetPercent));
        int rightAlpha = (int) (255 * mOffsetPercent);
        //start draw cur and right
        mRect.offset(-offsetX, 0);
        drawText(canvas, mRect, curText, curAlpha);

        mRect.offset(  getWidth() + mTextSpace, 0);
        drawText(canvas, mRect, rightText, rightAlpha);
    }

    private void drawText(Canvas canvas, Rect range, String text, int alpha) {
        mPaint.setAlpha(alpha);
        computeTextDrawingCoordinate(text, mPaint, range, mRectf);
        canvas.drawText(text, mRectf.left, mRectf.top - mPaint.ascent(), mPaint);
    }

    private static void computeTextDrawingCoordinate(String text, Paint paint, Rect srcRange, RectF out){
        out.set(srcRange);
        out.right = paint.measureText(text);
        out.bottom = paint.descent() - paint.ascent();
        out.left += (srcRange.width() - out.right) / 2.0f;
        out.top += (srcRange.height() - out.bottom) / 2.0f;
        //  canvas.drawText(text, bounds.left, bounds.top - mPaint.ascent(), mPaint);
    }

    private class PagerListener implements ViewPager.OnPageChangeListener{

        private int mScrollState;
        private float mLastKnownPositionOffset;

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            updatePosition(position, positionOffset, false);
            mLastKnownPositionOffset = positionOffset;
        }

        @Override
        public void onPageSelected(int position) {
            if (mScrollState == ViewPager.SCROLL_STATE_IDLE) {
                // Only update the text here if we're not dragging or settling.
                // updateText(mPager.getCurrentItem(), mPager.getAdapter());
               // mPTV.setSelectPosition(getRealPosition(position));

                final float offset = mLastKnownPositionOffset >= 0 ? mLastKnownPositionOffset : 0;
                //updatePosition(mPager.getCurrentItem(), offset, true);
                updatePosition(position, offset, true);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            mScrollState = state;
        }

        private void updatePosition(int position, float positionOffset, boolean force) {
            mCurrentPosition = position;
            mDir = DIR_LEFT;
            mOffsetPercent = positionOffset;
            postInvalidate();
        }
    }
}
