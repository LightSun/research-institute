package com.semantive.waveformandroid.waveform.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.heaven7.core.util.MainWorker;

/**
 * Created by heaven7 on 2019/5/15.
 */
//播放的时候加动画。
public class MusicAnnotatorView extends WaveformView {

    private static final float ADJUSTMENT = 20;
    private final RectF mRectF = new RectF();
    private AnnotatorLine mFocusLine;

    public MusicAnnotatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWaveformDrawDelegate(new UpDownWaveformDrawDelegate(this));
    }

    @Override
    protected GestureDetector.SimpleOnGestureListener createGestureListener() {
        return new Gesture0();
    }

    @Override
    protected void onPreDraw() {
        mMinOffsetX = -getWidth() / 2;
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(mFocusLine != null ){
            int delta = mOffsetX - mFocusLine.mStartOffsetX;
            //not need
            if(delta == 0){
                return;
            }
            int x = mFocusLine.pix - mOffsetX + delta;
            //delta > 0  左滑，否则右滑
            final Paint paint = mAnnotatorPaint;
            final AnnotatorParam ap = this.mAP;

            paint.setColor(ap.adjustColor);
            paint.setStyle(Paint.Style.FILL);
            paint.setAlpha(255);
            paint.setStrokeWidth(0);
            canvas.drawCircle(x, ap.startY, ap.dotMinRadius, paint);

            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(ap.lineWidth);
            int lineY = ap.startY + ap.dotLineDistance;
            canvas.drawLine(x , lineY, x, mParams.viewHeight - ap.startDy, paint);
        }else {
            //draw center line
            final AnnotatorParam ap = this.mAP;
            final Paint paint = mAnnotatorPaint;

            paint.setColor(ap.color);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(ap.lineWidth);
            paint.setAlpha(255);

            float x = mParams.width / 2;
            int lineY = ap.startY + ap.dotLineDistance;
            canvas.drawLine(x, lineY, x, mParams.viewHeight - ap.startDy, paint);
        }
    }

    /**
     * jump to previous annotator.
     * @return true if success
     */
    public boolean previousAnnotator(){
        if(mFocusLine != null){
            int index = mAnnotatorLines.indexOf(mFocusLine);
            if(index > 0){
                AnnotatorLine line = mAnnotatorLines.get(index - 1);
                startAnnotatorLine(line);
                return true;
            }
        }
        return false;
    }
    /**
     * jump to next annotator.
     * @return true if success
     */
    public boolean nextAnnotator(){
        if(mFocusLine != null){
            int index = mAnnotatorLines.indexOf(mFocusLine);
            if(index < mAnnotatorLines.size() - 1){
                AnnotatorLine line = mAnnotatorLines.get(index + 1);
                startAnnotatorLine(line);
                return true;
            }
        }
        return false;
    }

    /**
     * called this to finish adjust mode.
     */
    public void finishAdjustMode(){
        if(mFocusLine != null){
            finishAdjustCurrentAnnotatorLine();
            mFocusLine = null;
            invalidate();
        }
    }

    /**
     * called this to clear adjust mode. that means the annotator data not changed.
     */
    public void clearAdjustMode(){
        if(mFocusLine != null){
            mFocusLine.resetForAdjust();
            mFocusLine = null;
            invalidate();
        }
    }

    //-----------------------------------------------------------

    private void startAnnotatorLine(AnnotatorLine line){
        //reset last.;
        if(mFocusLine != null){
            finishAdjustCurrentAnnotatorLine();
            mFocusLine = null;
        }
        // start anim
        startLeanAnim(line.pix - mOffsetX - getWidth()/2, line);
    }
    private void finishAdjustCurrentAnnotatorLine() {
        int delta = mOffsetX - mFocusLine.mStartOffsetX;
        //finish change the time-point of annotator line
        mFocusLine.pix += delta;
        mFocusLine.mesc = pixelsToMillisecs(mFocusLine.pix);
        mFocusLine.resetForAdjust();
    }

    private AnnotatorLine findAnnotatorLine(MotionEvent e) {
        int by = mParams.viewHeight - mAP.startDy;
        for (AnnotatorLine line : mAnnotatorLines){
            float tx = line.pix - mOffsetX;
            mRectF.set(tx - ADJUSTMENT, 0, tx + ADJUSTMENT, by);
            if(mRectF.contains(e.getX(), e.getY())){
                return line;
            }
        }
        return null;
    }

    private void startLeanAnim(int delta, AnnotatorLine line) {
        ObjectAnimator anim = ObjectAnimator.ofInt(this, sPROP_OFFSET, mOffsetX, delta + mOffsetX);
        anim.setDuration(300);
        anim.addListener(new LeanAnimListener(line));
        anim.start();
    }

    private class Gesture0 extends GestureImpl{
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            AnnotatorLine line = findAnnotatorLine(e);
            if(line != null){
                startAnnotatorLine(line);
                return true;
            }
            return false;
        }
    }

    private class LeanAnimListener extends AnimatorListenerAdapter {

        private final AnnotatorLine line;

        public LeanAnimListener(AnnotatorLine line) {
            this.line = line;
        }
        @Override
        public void onAnimationEnd(Animator animation) {
            line.adjustMode = true;
            line.mStartOffsetX = mOffsetX;
            MainWorker.postDelay(10, new Runnable() {
                @Override
                public void run() {
                    mFocusLine = line;
                    line.drawVerticalLine = false;
                }
            });
        }
    }

}
