package com.semantive.waveformandroid.waveform.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.heaven7.core.util.Logger;

/**
 * Created by heaven7 on 2019/5/16.
 */
public class EditorWaveformView extends WaveformView {

    private final static float ADJUSTMENT = 10f;

    private final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final RectF mRectF = new RectF();

    private final RectF mLeftRect = new RectF();
    private final RectF mRightRect = new RectF();
    private final RectF mContentRect = new RectF();
    private TouchDelegate mTouchDelegate;

    private final FocusParam mFocusParam = new FocusParam();
    /** indicate is select state or not */
    private boolean mSelected;

    private boolean mFixContentWidth;
    private int mContentWidth;

    public EditorWaveformView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint.setColor(Color.BLUE);
        mPaint.setStyle(Paint.Style.FILL);

        UpWaveformDrawDelegate drawDelegate = new UpWaveformDrawDelegate(this, new EditorWaveformCallbackImpl());
        drawDelegate.setFocusParam(mFocusParam);
        setWaveformDrawDelegate(drawDelegate);

        mFocusParam.blockWidth = 60;
        mFocusParam.blockRoundSize = 20;
        mFocusParam.focusMarginTopBottom = 6;
        mAP.startDy = 30;
        mParams.roundSize = 16;
    }

    public void setMinOffsetX(int minOffsetX){
        this.mMinOffsetX = minOffsetX;
        invalidate();
    }

    /**
     * get the end time after edit.
     * @return the end time. in mill-seconds
     */
    public int getEndTime(){
        return getMaxTime() - pixelsToMillisecs(mTruncateWidth);
    }

    @Override
    protected GestureDetector.SimpleOnGestureListener createGestureListener() {
        return new Gesture0();
    }

    @Override
    protected void onOffsetMayChanged() {
        Logger.d(TAG, "onOffsetMayChanged", "mTruncateWidth = " + mTruncateWidth);

        final int width;
        if(mFixContentWidth && mContentWidth > 0){
            width = this.mContentWidth;
        }else {
            width = maxPosX() - mTruncateWidth;
        }
        mContentRect.set(-mOffsetX, 0, -mOffsetX + width, getHeight() - mAP.startDy);

        mLeftRect.set(mContentRect.left - mFocusParam.blockWidth,
                0,
                mContentRect.left,
                mContentRect.bottom);
        mRightRect.set(mContentRect.right ,
                0,
                mContentRect.right + mFocusParam.blockWidth,
                mContentRect.bottom);
    }

    @Override
    protected void resetTouch() {
        mTouchDelegate = null;
    }

    private boolean contains(RectF rectF, MotionEvent e, float offset){
        mRectF.set(rectF);
        mRectF.inset(-offset, 0);
        return mRectF.contains(e.getX(), e.getY());
    }

    private class Gesture0 extends WaveformView.GestureImpl {
        @Override
        public void onLongPress(MotionEvent e) {
            super.onLongPress(e);
        }

        @Override
        public boolean onDown(MotionEvent e) {
            if(mSelected){
                if(contains(mLeftRect, e, ADJUSTMENT)){
                    mTouchDelegate = new StartTouchDelegate();
                }else if(contains(mRightRect, e, ADJUSTMENT)){
                    mTouchDelegate = new EndTouchDelegate();
                }
            }
            return true;
        }
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            mSelected = !mSelected;
            invalidate();
            return true;
        }
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if(mTouchDelegate != null && mTouchDelegate.onScroll(e1, e2, distanceX, distanceY)){
                return true;
            }
            mFixContentWidth = true;
            return super.onScroll(e1, e2, distanceX, distanceY);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float vx, float vy) {
            mFixContentWidth = true;
            return super.onFling(e1, e2, vx, vy);
        }
    }

    private interface TouchDelegate{
        boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY);
    }
    private class StartTouchDelegate implements TouchDelegate {
        final int initOffset;
        float delta;
        StartTouchDelegate() {
            this.initOffset = mOffsetX;
        }
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY){
            mFixContentWidth = false;
            mContentWidth = maxPosX() - mTruncateWidth;
            delta += distanceX;
            setOffset((int) (initOffset + delta));
            return true;
        }
    }
    private class EndTouchDelegate implements TouchDelegate {
        EndTouchDelegate() {
        }
        //dx < 0 右滑， dy < 0 下滑
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY){
            mFixContentWidth = false;
            mContentWidth = maxPosX() - mTruncateWidth;
          //  Logger.d(TAG, "onScroll", "distanceX = " + distanceX);
            //右滑减小 .truncateWidth
            //左滑增大 truncateWidth
            mTruncateWidth = Math.max(0, (int)(mTruncateWidth + distanceX));
            onOffsetMayChanged();
            invalidate();
            return true;
        }
    }

    private class EditorWaveformCallbackImpl implements EditorWaveformCallback{
        @Override
        public RectF getContentRect() {
            return mContentRect;
        }
        @Override
        public boolean isSelected() {
            return mSelected;
        }
        @Override
        public RectF getTmpRectF() {
            return mRectF;
        }
        @Override
        public Paint getFocusPaint() {
            return mPaint;
        }
        @Override
        public float getTruncateTailWidth() {
            return mTruncateWidth;
        }
    }
}