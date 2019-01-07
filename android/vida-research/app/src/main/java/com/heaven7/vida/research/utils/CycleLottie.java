package com.heaven7.vida.research.utils;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;

import com.airbnb.lottie.LottieAnimationView;
import com.heaven7.android.component.lifecycle.LifeCycleComponent;
import com.heaven7.core.util.MainWorker;

/**
 * Created by heaven7 on 2018/12/18 0018.
 */
public class CycleLottie implements Animator.AnimatorListener, Runnable,
        LifeCycleComponent, ValueAnimator.AnimatorUpdateListener {

    private LottieAnimationView mAnimView;
    private long interval = -1;
    private float startFrame = - 1; // 0-1
    private float endFrame = -1;     // 0-1
    private boolean keyFrameLoopEnabled;
    private int frameRate = 30;

    private CycleLottie(CycleLottie.Builder builder) {
        this.mAnimView = builder.mAnimView;
        this.interval = builder.interval;
        this.startFrame = builder.startFrame;
        this.endFrame = builder.endFrame;
        this.keyFrameLoopEnabled = builder.keyFrameLoopEnabled;
        this.frameRate = builder.frameRate;
        init();
    }

    public static CycleLottie ofInterval(LottieAnimationView view, long interval){
        return new Builder()
                .setAnimView(view)
                .setInterval(interval)
                .build();
    }

    public static CycleLottie ofLoopKeyFrame(LottieAnimationView view, int startFrame,
                                             int endFrame){
        //view.getDuration() in ms
        return new Builder()
                .setAnimView(view)
                .setStartFrame(startFrame * 1000f / 30 / view.getDuration())
                .setEndFrame(endFrame * 1000f / 30 / view.getDuration())
                .setKeyFrameLoopEnabled(true)
                .build();
    }

    public void setKeyFrameLoopEnabled(boolean keyFrameLoopEnabled){
        this.keyFrameLoopEnabled = keyFrameLoopEnabled;
    }

    public void setLoopStartFrame(int startFrame){
        this.startFrame = startFrame;
    }
    public void setLoopEndFrame(int endFrame){
        this.endFrame = endFrame;
    }

    public void start(){
        mAnimView.playAnimation();
    }

    @Override
    public void onAnimationStart(Animator animation) {

    }
    @Override
    public void onAnimationEnd(Animator animation) {
        if(interval >= 0){
            MainWorker.postDelay(interval, this);
        }
    }
    @Override
    public void onAnimationCancel(Animator animation) {

    }
    @Override
    public void onAnimationRepeat(Animator animation) {

    }
    @Override
    public void run() {
        start();
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        if(keyFrameLoopEnabled && endFrame > 0 && startFrame >= 0){
            float process = (float) animation.getAnimatedValue();
            if(process >= endFrame){
                mAnimView.setProgress(startFrame);
            }
        }
    }

    @Override
    public void onLifeCycle(Context context, int lifeCycle) {
        switch (lifeCycle){
            case ON_PAUSE:
                mAnimView.pauseAnimation();
                break;

            case ON_RESUME:
                mAnimView.resumeAnimation();
                break;

            case ON_DESTROY:
                MainWorker.remove(this);
                break;
        }
    }
    private void init(){
        mAnimView.addAnimatorUpdateListener(this);
        mAnimView.addAnimatorListener(this);
    }

    public LottieAnimationView getAnimView() {
        return this.mAnimView;
    }

    public long getInterval() {
        return this.interval;
    }

    public float getStartFrame() {
        return this.startFrame;
    }

    public float getEndFrame() {
        return this.endFrame;
    }

    public boolean isKeyFrameLoopEnabled() {
        return this.keyFrameLoopEnabled;
    }

    public int getFrameRate() {
        return this.frameRate;
    }

    public static class Builder {
        private LottieAnimationView mAnimView;
        private long interval = -1;
        private float startFrame = - 1; // 0-1
        private float endFrame = -1;     // 0-1
        private boolean keyFrameLoopEnabled;
        private int frameRate = 30;

        public Builder setAnimView(LottieAnimationView mAnimView) {
            this.mAnimView = mAnimView;
            return this;
        }

        public Builder setInterval(long interval) {
            this.interval = interval;
            return this;
        }

        public Builder setStartFrame(float startFrame) {
            this.startFrame = startFrame;
            return this;
        }

        public Builder setEndFrame(float endFrame) {
            this.endFrame = endFrame;
            return this;
        }

        public Builder setKeyFrameLoopEnabled(boolean keyFrameLoopEnabled) {
            this.keyFrameLoopEnabled = keyFrameLoopEnabled;
            return this;
        }

        public Builder setFrameRate(int frameRate) {
            this.frameRate = frameRate;
            return this;
        }

        public CycleLottie build() {
            return new CycleLottie(this);
        }
    }
}
