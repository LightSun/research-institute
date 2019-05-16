package com.semantive.waveformandroid.waveform.view;

import android.content.Context;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;

/**
 * Created by heaven7 on 2019/5/16.
 */
public class EditorWaveformView extends WaveformView {

    private static final boolean DEBUG = true;
    private final static float ADJUSTMENT = 3.5f;

    private final RectF mStartRect = new RectF();
    private final RectF mEndRect = new RectF();


    public EditorWaveformView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWaveformDrawDelegate(new UpWaveformDrawDelegate(this));
        mAP.startDy = 30;
    }

    @Override
    protected GestureDetector.SimpleOnGestureListener createGestureListener() {
        return new Gesture0();
    }

    @Override
    protected void onOffsetMayChanged() {
        float selectStartX = mSelectionStart - mOffsetX;
        float selectEndX = mSelectionEnd - mOffsetX;
        float hW = - mParams.selectStrokeWidth * 1f / 2;

        mStartRect.set(selectStartX - hW - ADJUSTMENT , 0, selectStartX + hW + ADJUSTMENT, getHeight() - mAP.startDy);
        mEndRect.set(selectEndX - hW - ADJUSTMENT , 0, selectEndX + hW + ADJUSTMENT, getHeight() - mAP.startDy);
       /* Logger.d(TAG, "onOffsetMayChanged", "mStartRect = " + mStartRect);
        Logger.d(TAG, "onOffsetMayChanged", "mEndRect = " + mEndRect);*/
    }

    private class Gesture0 extends WaveformView.GestureImpl {
        @Override
        public boolean onDown(MotionEvent e) {
            //Logger.d(TAG, "onDown", "x = " + e.getX() + ", y = " + e.getY());
           /* if(mStartRect.contains(e.getX(), e.getY())){
                mTouchDelegate = new StartTouchDelegate();
                if(DEBUG){
                    Logger.d(TAG, "onDown", "mStartRect is touched.");
                }
            }else if(mEndRect.contains(e.getX(), e.getY())){
                mTouchDelegate = new EndTouchDelegate();
                if(DEBUG){
                    Logger.d(TAG, "onDown", "mEndRect is touched.");
                }
            }*/
            return true;
        }
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return super.onSingleTapUp(e);
        }
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            //滑动时， 中间的杆不滑动。只滑动图。 记得重新设置select start and end.
            return super.onScroll(e1, e2, distanceX, distanceY);
        }
        public boolean onFling(
                MotionEvent e1, MotionEvent e2, float vx, float vy) {
            return super.onFling(e1, e2, vx, vy);
        }
    }
    private interface TouchDelegate{
        boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY);
    }
    private class StartTouchDelegate implements TouchDelegate {
        final int initSelectStart;
        float delta;
        StartTouchDelegate() {
            this.initSelectStart = mSelectionStart;
        }
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY){
            delta -= distanceX;
            mSelectionStart = (int) (initSelectStart + delta);
            onOffsetMayChanged();
            invalidate();
            return true;
        }
    }
    private class EndTouchDelegate implements TouchDelegate {
        final int initSelectEnd;
        float delta;
        EndTouchDelegate() {
            this.initSelectEnd = mSelectionEnd;
        }
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY){
            delta -= distanceX;
            mSelectionEnd = (int) (initSelectEnd + delta);
            onOffsetMayChanged();
            invalidate();
            return true;
        }
    }
}
