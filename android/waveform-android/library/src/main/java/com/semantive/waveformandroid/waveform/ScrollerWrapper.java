package com.semantive.waveformandroid.waveform;

import android.view.View;
import android.widget.OverScroller;

import com.heaven7.core.util.Logger;

/**
 * Created by heaven7 on 2018/5/8 0008.
 */

public class ScrollerWrapper {

    private static final boolean DEBUG = false;
    private static final String TAG = "ScrollerWrapper";

    private final OverScroller mScroller;
    private final View mView;
    private final Callback mCallback;

    private int mLastX;
    private boolean mTrigged;

    public ScrollerWrapper(View view, Callback callback) {
        this.mView = view;
        this.mScroller = new OverScroller(view.getContext());
        this.mCallback = callback;
    }

    public OverScroller getScroller(){
        return mScroller;
    }

    public boolean isFinished() {
        return mScroller.isFinished();
    }

    public void forceFinished(){
        mScroller.forceFinished(true);
    }

    public void abortIfNeed() {
        if (!mScroller.isFinished()) {
            mScroller.abortAnimation();
        }
    }

    public boolean isFling(){
        return mTrigged && !mScroller.isFinished();
    }

    private int getDeltaX() {
        final int deltaX = mScroller.getCurrX() - mLastX;
        if(DEBUG){
            Logger.v(TAG, "getDeltaX", "(startX, currX) = ("+
                    mScroller.getStartX()+ ", " + mScroller.getCurrX() +")" );
        }
        mLastX = mScroller.getCurrX();
        return deltaX;
    }

    //velocityX > 0 ? LEFT_TO_RIGHT  : right to left
    public void startFling(float velocityX, float velocityY){
        forceFinished();
        mTrigged = true;
        mScroller.fling(0, 0, (int) velocityX, (int) velocityY,
                Integer.MIN_VALUE, Integer.MAX_VALUE, 0, 0);
        //must call this to computeScroll or else may cause bug(never call computeScroll)
        mView.postInvalidateOnAnimation();
    }

    public void computeScroll() {
        if(!mTrigged){
            return;
        }
        if(mScroller.computeScrollOffset()){
            //not finished
            int deltaX = getDeltaX();
            Logger.d(TAG, "computeScroll", "not finished,  deltaX = " + deltaX);
            if(!mCallback.onComputeScrolled(this, mView, deltaX)){
                abortIfNeed();
            }
        }else{
            Logger.d(TAG, "computeScroll", "finished");
            mTrigged = false;
            mLastX = 0;
            //finished
            mCallback.onFinish(this, mView);
        }
    }

    public interface Callback{

        /**
         * called on compute scroll
         * @param wrapper the wrapper
         * @param view the view
         * @param deltaX the delta x. >0 means scroll to right.
         * @return true if it is expect. false other wise..
         */
        boolean onComputeScrolled(ScrollerWrapper wrapper, View view, int deltaX);

        void onFinish(ScrollerWrapper wrapper, View view);
    }
}
