package com.semantive.waveformandroid.waveform.view;

import android.animation.ObjectAnimator;
import android.support.annotation.MainThread;
import android.util.Property;
import android.view.View;

import java.lang.ref.WeakReference;

/**
 * Created by heaven7 on 2019/5/15.
 */
/*public*/ class AnnotatorLine {

    private static final Property<AnnotatorLine, Float> sPROP = new Property<AnnotatorLine, Float>(
            Float.class, "annotator") {
        @Override
        public Float get(AnnotatorLine object) {
            return object.animProcess;
        }
        @Override
        public void set(AnnotatorLine object, Float value) {
            object.animProcess = value;
            object.invalidate();
        }
    };

    int mesc;
    int pix;

    /** draw vertical line or not . default is true .*/
    boolean drawVerticalLine = true;

    /** [0,1] . 0 means no animation. often used for scale animation. */
    float animProcess = 0;
    /** in adjust mode or not. */
    boolean adjustMode;
    /** the start offsetX in adjust mode */
    int mStartOffsetX;

    private WeakReference<ObjectAnimator> weakAnim;
    private View view;

    public AnnotatorLine(int mesc, int pix) {
        this.mesc = mesc;
        this.pix = pix;
    }

    public void resetForAdjust(){
        adjustMode = false;
        mStartOffsetX = 0;
        drawVerticalLine = true;
    }

    public void stopAnimation(){
        if(weakAnim != null){
            ObjectAnimator animator = weakAnim.get();
            if(animator != null){
                animator.cancel();
            }
            weakAnim = null;
        }
    }
    private void invalidate() {
        if(view != null){
            view.invalidate();
        }
    }
    @MainThread
    public void startAnimation(){
        ObjectAnimator animator = ObjectAnimator.ofFloat(this, sPROP, 0, 1);
        animator.setDuration(300);
        weakAnim = new WeakReference<>(animator);
        animator.start();
    }
    @MainThread
    public void startAnimation(View view){
        this.view = view;
        ObjectAnimator animator = ObjectAnimator.ofFloat(this, sPROP, 0, 1);
        animator.setDuration(300);
        weakAnim = new WeakReference<>(animator);
        animator.start();
    }
}
