package com.heaven7.vida.research.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.FloatRange;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

import com.heaven7.vida.research.R;

/**
 * 1-0.4f
 * Created by heaven7 on 2018/11/21 0021.
 */
public class PageTipView extends View {

    //the offset directions. view pager scroll->left. means offset->right.
    public static final byte OFFSET_NONE = 1;
    public static final byte OFFSET_LEFT = 2;
    public static final byte OFFSET_RIGHT = 3;

    private static final float MIN_ALPHA = 0.4f;

    private final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Path mPath = new Path();
    private final RectF mRectF = new RectF();

    private int mTipHeight;
    private int mTipMinWidth;
    private int mTipMaxWidth;
    private int mTipColor;

    private int mRoundSize;
    private int mCount = 5;
    private int mSpace;
    //changed.
    private int mSelectPosition = 0;
    private float mPositionOffset = 0f;          // [ 0,1]
    private byte mOffsetDirection = OFFSET_NONE;

    public PageTipView(Context context) {
        this(context, null, 0);
    }

    public PageTipView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PageTipView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if(attrs != null){
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PageTipView);
            try {
                mTipHeight = a.getDimensionPixelSize(R.styleable.PageTipView_ptv_tip_height, mTipHeight);
                mTipMinWidth = a.getDimensionPixelSize(R.styleable.PageTipView_ptv_tip_min_width, mTipMinWidth);
                mTipMaxWidth = a.getDimensionPixelSize(R.styleable.PageTipView_ptv_tip_max_width, mTipMaxWidth);
                mSpace = a.getDimensionPixelSize(R.styleable.PageTipView_ptv_tip_space, mSpace);
                mRoundSize = a.getDimensionPixelSize(R.styleable.PageTipView_ptv_tip_round_size, mTipHeight / 2);
                mTipColor = a.getColor(R.styleable.PageTipView_ptv_tip_color, mTipColor);
                mCount = a.getInteger(R.styleable.PageTipView_ptv_tip_count, mCount);
            }finally {
                a.recycle();
            }
        }
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setDither(true);
        mPaint.setColor(mTipColor);
    }

    public void setTipCount(int count){
        if(count != mCount) {
            this.mCount = count;
            requestLayout();
            invalidate();
        }
    }

    public void setPositionOffset(@FloatRange(from = 0f, to = 1f) float offset){
        if(mPositionOffset != offset) {
            mPositionOffset = offset;
            invalidate();
        }
    }
    public void setOffsetDirection(byte direction){
        if(mOffsetDirection != direction) {
            mOffsetDirection = direction;
            invalidate();
        }
    }

    public void setSelectPosition(int position){
        if(mSelectPosition != position) {
            mSelectPosition = position;
            invalidate();
        }
    }

    public void concatViewPager(ViewPager vp){
        vp.addOnPageChangeListener(new PageTipListener(vp, this));
    }

    private void setPaintAlpha(float val){
        mPaint.setAlpha((int) (val * 255));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
       //super.onMeasure(widthMeasureSpec, heightMeasureSpec);
       int width = (mCount - 1) * mTipMinWidth + mTipMaxWidth + (mCount - 1) * mSpace
               + getPaddingLeft() + getPaddingRight();
       int height = mTipHeight + getPaddingTop() + getPaddingBottom();
       setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //0 - transparent  255 non-transparent
        canvas.save();
        canvas.translate(getPaddingLeft(), getPaddingTop());
        switch (mOffsetDirection){
            case OFFSET_NONE:
                drawByNone(canvas);
                break;

            case OFFSET_LEFT:
                drawByLeft(canvas);
                break;

            case OFFSET_RIGHT:
                drawByRight(canvas);
                break;
        }
        canvas.restore();
    }

    private void drawByRight(Canvas canvas) {
        float adjustOffset = (float) mapValueFromRangeToRange(mPositionOffset, 0, 1, 1f, 0.4f);
        float offset_receiver = (float) mapValueFromRangeToRange(mPositionOffset, 0, 1, 0.4f, 1f);
        int count = this.mCount;
        int lastStartX = 0;
        int top = 0;
        int bottom = mTipHeight;
        for (int i = 0 ; i < count ; i ++){
            int w;
            if(i == mSelectPosition - 1){
                setPaintAlpha(adjustOffset);
                w = (int) (mTipMaxWidth - mPositionOffset * (mTipMaxWidth - mTipMinWidth));
            }else if(i == mSelectPosition){
                setPaintAlpha(offset_receiver);
                w = (int) (mTipMinWidth + mPositionOffset * (mTipMaxWidth - mTipMinWidth));
            }else{
                setPaintAlpha(MIN_ALPHA);
                w = mTipMinWidth;
            }
            mRectF.set(lastStartX, top, lastStartX + w, bottom);
            mPath.reset();
            mPath.addRoundRect(mRectF, mRoundSize, mRoundSize, Path.Direction.CW);
            //draw
            canvas.save();
            canvas.clipPath(mPath);
            canvas.drawRect(mRectF, mPaint);
            canvas.restore();
            lastStartX += w + mSpace;
        }
    }

    private void drawByLeft(Canvas canvas) {
        float adjustOffset = (float) mapValueFromRangeToRange(mPositionOffset, 0, 1, 1f, 0.4f);
        float offset_receiver = (float) mapValueFromRangeToRange(mPositionOffset, 0, 1, 0.4f, 1f);
        int count = this.mCount;
        int lastStartX = 0;
        int top = 0;
        int bottom = mTipHeight;
        for (int i = 0 ; i < count ; i ++){
            int w;
            if(i == mSelectPosition){
                setPaintAlpha(adjustOffset);
                w = (int) (mTipMaxWidth - mPositionOffset * (mTipMaxWidth - mTipMinWidth));
            }else if(i == mSelectPosition + 1){
                setPaintAlpha(offset_receiver);
                w = (int) (mTipMinWidth + mPositionOffset * (mTipMaxWidth - mTipMinWidth));
            }else{
                setPaintAlpha(MIN_ALPHA);
                w = mTipMinWidth;
            }
            mRectF.set(lastStartX, top, lastStartX + w, bottom);
            mPath.reset();
            mPath.addRoundRect(mRectF, mRoundSize, mRoundSize, Path.Direction.CW);
            //draw
            canvas.save();
            canvas.clipPath(mPath);
            canvas.drawRect(mRectF, mPaint);
            canvas.restore();
            lastStartX += w + mSpace;
        }
    }

    private void drawByNone(Canvas canvas) {
        int count = this.mCount;
        int lastStartX = 0;
        int top = 0;
        int bottom = mTipHeight;
        for (int i = 0 ; i < count ; i ++){
            int w;
            if(i == mSelectPosition){
                setPaintAlpha(1);
                w = mTipMaxWidth;
            }else{
                setPaintAlpha(MIN_ALPHA);
                w = mTipMinWidth;
            }
            mRectF.set(lastStartX, top, lastStartX + w, bottom);
            mPath.reset();
            mPath.addRoundRect(mRectF, mRoundSize, mRoundSize, Path.Direction.CW);
            //draw
            canvas.save();
            canvas.clipPath(mPath);
            canvas.drawRect(mRectF, mPaint);
            canvas.restore();
            lastStartX += w + mSpace;
        }
    }

    private static double mapValueFromRangeToRange(
            double value,
            double fromLow,
            double fromHigh,
            double toLow,
            double toHigh) {
        double fromRangeSize = fromHigh - fromLow;
        double toRangeSize = toHigh - toLow;
        double valueScale = (value - fromLow) / fromRangeSize;
        return toLow + (valueScale * toRangeSize);
    }

    public static class PageTipListener implements ViewPager.OnPageChangeListener{

        private final ViewPager mVp;
        private final PageTipView mPTV;
        private int mScrollState;
        private float mLastKnownPositionOffset = -1;

        public PageTipListener(ViewPager mVp,PageTipView mPageTipView) {
            this.mVp = mVp;
            this.mPTV = mPageTipView;
        }

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
                mPTV.setSelectPosition(getRealPosition(position));

                final float offset = mLastKnownPositionOffset >= 0 ? mLastKnownPositionOffset : 0;
                updatePosition(mVp.getCurrentItem(), offset, true);
            }
        }
        @Override
        public void onPageScrollStateChanged(int state) {
            mScrollState = state;
        }

        private void updatePosition(int position, float positionOffset, boolean force) {
            position = getRealPosition(position);
            mPTV.setOffsetDirection(PageTipView.OFFSET_LEFT);
            mPTV.setSelectPosition(position);
            mPTV.setPositionOffset(positionOffset);
        }
        protected int getRealPosition(int position){
            return position;
        }
    }
}
