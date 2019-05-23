package com.semantive.waveformandroid.waveform.view;

/*
 * Copyright (C) 2008 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Property;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;

import com.heaven7.core.util.Logger;
import com.semantive.waveformandroid.R;
import com.semantive.waveformandroid.waveform.ScrollerWrapper;
import com.semantive.waveformandroid.waveform.soundfile.CheapSoundFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * WaveformView is an Android view that displays a visual representation
 * of an audio waveform.  It retrieves the frame gains from a CheapSoundFile
 * object and recomputes the shape contour at several zoom levels.
 * <p/>
 * This class doesn't handle selection or any of the touch interactions
 * directly, so it exposes a listener interface.  The class that embeds
 * this view should add itself as a listener and make the view scroll
 * and respond to other events appropriately.
 * <p/>
 * WaveformView doesn't actually handle selection, but it will just display
 * the selected part of the waveform in a different color.
 *
 * Modified by Anna Stępień <anna.stepien@semantive.com>
 * Modified by heaven7
 */
public class WaveformView extends View implements WaveformDrawDelegate.Callback{

    public static final String TAG = "WaveformView";
    static final Property<WaveformView, Integer> sPROP_OFFSET = new Property<WaveformView, Integer>(
            Integer.class, "offsetX") {
        @Override
        public Integer get(WaveformView object) {
            return object.mOffsetX;
        }
        @Override
        public void set(WaveformView object, Integer value) {
            object.mOffsetX = value;
            object.invalidate();
        }
    };

    /*private*/ final WaveformParam mParams = new WaveformParam();
    /*private*/ final AnnotatorParam mAP = new AnnotatorParam();
    protected final ScrollerWrapper mScroller;
    private final GestureDetectorCompat mGestureDetector;
    private WaveformDrawDelegate mDrawDelegate;
    private TimeLineCallback mCallback;

    /*private*/ List<AnnotatorLine> mAnnotatorLines = new ArrayList<>();
    // Colors
    protected Paint mSelectedLinePaint;
    protected Paint mUnselectedLinePaint;
    protected Paint mUnselectedBgLinePaint;
    protected Paint mSelectRangePaint;
    protected Paint mCenterLinePaint;
    protected Paint mTimecodePaint;
    protected Paint mAnnotatorPaint;

    protected CheapSoundFile mSoundFile;

    protected int[] mLenByZoomLevel;
    protected float[] mZoomFactorByZoomLevel;
    protected int mZoomLevel;
    protected int mNumZoomLevels;

    protected int mSampleRate;
    protected int mSamplesPerFrame;

    protected int mOffsetX;
    protected int mSelectionStart;
    protected int mSelectionEnd;

    protected int mPlaybackPos;
    protected float mDensity;

    protected boolean mInitialized;

    protected float range;
    protected float scaleFactor;
    protected float minGain;

    /*private*/ int mMinOffsetX;

    private int mDistancePerSecond = 0;
    /** true to fix the select length. */
    private boolean mFixSelectLength;
    /** the truncate width of tail/end.(this will effect the {@linkplain #getValidWidth()}. */
    /*private*/ int mTruncateWidth;

    public WaveformView(Context context, AttributeSet attrs) {
        super(context, attrs);

        int dashWidth = 10;
        int dashSpace = 5;
        int selectBorderColor = Color.parseColor("#7e7e7e");
        int selectLineColor = Color.parseColor("#7e7e7f");
        int unselectLineColor = Color.parseColor("#2f2f34");
        int unselectBgColor = Color.parseColor("#171718");
        if(attrs != null){
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.WaveformView);
            try {
                mAP.lineWidth = a.getDimensionPixelSize(R.styleable.WaveformView_wv_ap_line_width, 3);
                mAP.dotLineDistance = a.getDimensionPixelSize(R.styleable.WaveformView_wv_ap_dot_line_distance, 12);
                mAP.dotMinRadius = a.getDimensionPixelSize(R.styleable.WaveformView_wv_ap_dot_min_radius, 3);
                mAP.dotMaxRadius = a.getDimensionPixelSize(R.styleable.WaveformView_wv_ap_dot_max_radius, 30);
                mAP.startY = a.getDimensionPixelSize(R.styleable.WaveformView_wv_ap_start_y, 30);
                mAP.color = a.getColor(R.styleable.WaveformView_wv_ap_default_color, Color.RED);
                mAP.adjustColor = a.getColor(R.styleable.WaveformView_wv_ap_adjust_color, Color.parseColor("#58c9b9"));

                mParams.roundSize = a.getDimensionPixelSize(R.styleable.WaveformView_wv_waveform_round_size, 50);
                dashWidth = a.getDimensionPixelSize(R.styleable.WaveformView_wv_waveform_select_range_dash_width, dashWidth);
                dashSpace = a.getDimensionPixelSize(R.styleable.WaveformView_wv_waveform_select_range_dash_space, dashSpace);

                selectBorderColor = a.getColor(R.styleable.WaveformView_wv_waveform_select_range_color, selectBorderColor);
                selectLineColor = a.getColor(R.styleable.WaveformView_wv_waveform_select_line_color, selectLineColor);
                unselectLineColor = a.getColor(R.styleable.WaveformView_wv_waveform_unselect_line_color, unselectLineColor);
                unselectBgColor = a.getColor(R.styleable.WaveformView_wv_waveform_unselect_bg_color, unselectBgColor);
            }finally {
                a.recycle();
            }
            mParams.selectStrokeWidth = mAP.lineWidth;
        }else {
            mAP.lineWidth = 3;
            mAP.dotMinRadius = 3;
            mAP.dotMaxRadius = 30;
            mAP.startY = mAP.dotMaxRadius + 2;
            mAP.dotLineDistance = 12;
            mAP.color = Color.RED;
            mAP.adjustColor = Color.parseColor("#58c9b9");

            mParams.selectStrokeWidth = 3;
            mParams.roundSize = 16;
        }

        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(metrics);
        mDensity = metrics.density;
        //Logger.d(TAG, "WaveformView", "widthPixels = " + metrics.widthPixels);

        mSelectedLinePaint = new Paint();
        mSelectedLinePaint.setAntiAlias(false);
        mSelectedLinePaint.setColor(selectLineColor);

        mUnselectedLinePaint = new Paint();
        mUnselectedLinePaint.setAntiAlias(false);
        mUnselectedLinePaint.setColor(unselectLineColor);

        mUnselectedBgLinePaint = new Paint();
        mUnselectedBgLinePaint.setAntiAlias(false);
        mUnselectedBgLinePaint.setColor(unselectBgColor);

        mSelectRangePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mSelectRangePaint.setPathEffect(new DashPathEffect(new float[]{dashWidth, dashSpace}, 0.0f));
        mSelectRangePaint.setStrokeWidth(mParams.selectStrokeWidth);
        mSelectRangePaint.setColor(selectBorderColor);

        mCenterLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCenterLinePaint.setStrokeWidth(mParams.selectStrokeWidth);
        mCenterLinePaint.setColor(mAP.color);

        mTimecodePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTimecodePaint.setTextSize(12 * mDensity);
        mTimecodePaint.setColor(Color.BLUE);

        mAnnotatorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        mScroller = new ScrollerWrapper(this, createScrollerCallback());
        mGestureDetector = new GestureDetectorCompat(context, createGestureListener());

        mSoundFile = null;
        mLenByZoomLevel = null;
        mOffsetX = 0;
        mPlaybackPos = -1;
        mSelectionStart = -1;
        mSelectionEnd = -1;
        mInitialized = false;

        getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                getViewTreeObserver().removeOnPreDrawListener(this);
                WaveformView.this.onPreDraw();
                return true;
            }
        });
    }

    /**
     * called on pre draw this view.
     */
    protected void onPreDraw() {

    }

    /**
     * called in {@linkplain #clampOffsetX()}. used to do something when offset may changed.
     */
    protected void onOffsetMayChanged() {

    }

    /**
     * called on action up
     */
    protected void resetTouch() {

    }
    /** make the target time point line to the center line. */
    public void seekToCenter(int millsecs){
        mOffsetX = millisecsToPixels(millsecs) - getWidth() / 2;
        clampOffsetX();
        postInvalidate();
    }

    /**
     * get the center time in mill-seconds
     * @return the center time in mill-seconds.
     */
    public int getCenterTime(){
        return pixelsToMillisecs(mOffsetX + getWidth() / 2);
    }

    /**
     * set truncate width
     * @param width the width in pixes
     */
    public void setTruncateWidth(int width){
        mTruncateWidth = width;
        onOffsetMayChanged();
        invalidate();
    }

    /**
     * add annotator at target time
     * @param msec the time in mill-seconds
     */
    public void addAnnotator(int msec){
        if(isInitialized()){
            mAnnotatorLines.add(new AnnotatorLine(msec, millisecsToPixels(msec)));
        }else {
            mAnnotatorLines.add(new AnnotatorLine(msec, -1));
        }
        invalidate();
    }
    @VisibleForTesting
    public void addAnnotatorWidthAnim(int msec){
        addAnnotator(msec);
        mAnnotatorLines.get(mAnnotatorLines.size() - 1).startAnimation(this);
    }

    /**
     * get the annotators which used to save all annotators.
     * @return the annotator
     */
    public List<AnnotatorLine> getAnnotators() {
        return mAnnotatorLines;
    }
    /**
     * set the annotators which used to restore all annotators.
     * @param list the annotators
     */
    public void setAnnotators(List<AnnotatorLine> list){
        if(list == null){
            throw new NullPointerException();
        }
        this.mAnnotatorLines = list;
        invalidate();
    }

    public void clearAnnotator(){
        mAnnotatorLines.clear();
        invalidate();
    }
    public void setWaveformDrawDelegate(WaveformDrawDelegate delegate) {
        this.mDrawDelegate = delegate;
    }

    protected GestureDetector.SimpleOnGestureListener createGestureListener(){
        return new GestureImpl();
    }

    protected ScrollerWrapper.Callback createScrollerCallback(){
        return new ScrollCallbackImpl();
    }

    protected boolean onScroll(@NonNull MotionEvent e1, @NonNull MotionEvent e2,
                               float dx, float dy) {
        if(mFixSelectLength && mSelectionEnd == maxPosX() && dx > 0){
            return true;
        }
        //dx < 0 右滑， dy < 0 下滑
        mOffsetX += dx;
        clampOffsetX();
        dispatchTimeLineChanged();
        invalidate();
        return true;
    }
    @Override
    public void computeScroll() {
        mScroller.computeScroll();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean result = mGestureDetector.onTouchEvent(event);

        switch (event.getActionMasked()) {

            case MotionEvent.ACTION_UP:
                Logger.d(TAG, "onTouchEvent", "ACTION_UP");
                //if fling wait fling finish
                if(!mScroller.isFling()) {
                    dispatchTimeLineChangeEnd();
                }

            case MotionEvent.ACTION_CANCEL:
                Logger.d(TAG, "onTouchEvent", "ACTION_CANCEL");
                resetTouch();
                break;
        }
        return result;
    }

    public void setFixSelectLength(boolean mFixSelectLength) {
        this.mFixSelectLength = mFixSelectLength;
    }

    public void setTimeLineCallback(TimeLineCallback mCallback) {
        this.mCallback = mCallback;
    }

    public void setSoundFile(CheapSoundFile soundFile) {
        mSoundFile = soundFile;
        mSampleRate = mSoundFile.getSampleRate();
        mSamplesPerFrame = mSoundFile.getSamplesPerFrame();
        computeDoublesForAllZoomLevels();
        mAnnotatorLines.clear();
        //call the first time.
        onOffsetMayChanged();

        postInvalidate();
    }

    //--------------------------------------------------------------
    public boolean hasSoundFile() {
        return mSoundFile != null;
    }

    public boolean isInitialized() {
        return mInitialized;
    }

    public int getZoomLevel() {
        return mZoomLevel;
    }

    public void setZoomLevel(int zoomLevel) {
        mZoomLevel = zoomLevel;
    }

    public boolean canZoomIn() {
        return (mZoomLevel < mNumZoomLevels - 1);
    }

    public void zoomIn() {
        if (canZoomIn()) {
            mZoomLevel++;
            float factor = mLenByZoomLevel[mZoomLevel] / (float) mLenByZoomLevel[mZoomLevel - 1];
            mSelectionStart *= factor;
            mSelectionEnd *= factor;
            int offsetCenter = mOffsetX + (int) (getMeasuredWidth() / factor);
            offsetCenter *= factor;
            mOffsetX = offsetCenter - (int) (getMeasuredWidth() / factor);
            if (mOffsetX < 0)
                mOffsetX = 0;
            invalidate();
        }
    }

    public boolean canZoomOut() {
        return (mZoomLevel > 0);
    }

    public void zoomOut() {
        if (canZoomOut()) {
            mZoomLevel--;
            float factor = mLenByZoomLevel[mZoomLevel + 1] / (float) mLenByZoomLevel[mZoomLevel];
            mSelectionStart /= factor;
            mSelectionEnd /= factor;
            int offsetCenter = (int) (mOffsetX + getMeasuredWidth() / factor);
            offsetCenter /= factor;
            mOffsetX = offsetCenter - (int) (getMeasuredWidth() / factor);
            if (mOffsetX < 0)
                mOffsetX = 0;
            invalidate();
        }
    }

    //最大能够滑动的位置。
    public int maxPosX() {
        return mLenByZoomLevel[mZoomLevel];
    }
    /** get the max time in mill-seconds */
    public int getMaxTime(){
        return pixelsToMillisecs(maxPosX());
    }
    /** 默认最大能够滑动的位置 */
    public int maxOffsetX(){
        return maxPosX() - getWidth() / 2;
    }

    private void clampOffsetX(){
        //右边留半屏
        if(mOffsetX > maxOffsetX()){
            mOffsetX = maxOffsetX();
        }
        if(mOffsetX < mMinOffsetX){
            mOffsetX = mMinOffsetX;
        }
        //should keep the select length
        if(mFixSelectLength && mSelectionEnd > 0){
            int expect_mSelectionStart = mOffsetX - mMinOffsetX;
            int expect_mSelectionEnd = expect_mSelectionStart + (mSelectionEnd - mSelectionStart);
            if(expect_mSelectionEnd > maxPosX()){
                int dx = expect_mSelectionEnd - maxPosX();
                mSelectionStart = expect_mSelectionStart - dx;
                mSelectionEnd = expect_mSelectionEnd - dx;
                mOffsetX -= dx;
            }else {
                mSelectionStart = expect_mSelectionStart;
                mSelectionEnd = expect_mSelectionEnd;
            }
        }
        onOffsetMayChanged();
    }

    public int secondsToFrames(double seconds) {
        return (int) (1.0 * seconds * mSampleRate / mSamplesPerFrame + 0.5);
    }

    public int secondsToPixels(double seconds) {
        double z = mZoomFactorByZoomLevel[mZoomLevel];
        return (int) (z * seconds * mSampleRate / mSamplesPerFrame + 0.5);
    }

    public double pixelsToSeconds(int pixels) {
        double z = mZoomFactorByZoomLevel[mZoomLevel];
        return (pixels * (double) mSamplesPerFrame / (mSampleRate * z));
    }

    public int millisecsToPixels(int msecs) {
        double z = mZoomFactorByZoomLevel[mZoomLevel];
        return (int) ((msecs * 1.0 * mSampleRate * z) / (1000.0 * mSamplesPerFrame) + 0.5);
    }

    public int pixelsToMillisecs(int pixels) {
        double z = mZoomFactorByZoomLevel[mZoomLevel];
        return (int) (pixels * (1000.0 * mSamplesPerFrame) / (mSampleRate * z) + 0.5);
    }

    /**
     * set the select range.
     * @param start start in pix
     * @param end  end in pix
     */
    public void setSelectRange(int start, int end) {
        Logger.i(TAG, "setSelectRange", "start = " + start + " ,end = " + end);
        mSelectionStart = start;
        mSelectionEnd = end;
        postInvalidate();
    }

    public void setOffset(int offset) {
        mOffsetX = offset;
        clampOffsetX();
        postInvalidate();
    }

    public int getStart() {
        return mSelectionStart;
    }

    public int getEnd() {
        return mSelectionEnd;
    }

    public int getOffset() {
        return mOffsetX;
    }

    public void setPlayback(int pos) {
        mPlaybackPos = pos;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mSoundFile == null)
            return;
        if(mDrawDelegate == null){
            throw new NullPointerException();
        }

         //一个像素多少秒
        double onePixelInSecs = pixelsToSeconds(1);
        // 偏移的时间
        double fractionalSecs = mOffsetX * onePixelInSecs;
        // 间隔的时间.second
        double timecodeIntervalSecs = 1.0;
       // Logger.d(TAG, "onDraw", "timecodeIntervalSecs = " + timecodeIntervalSecs);

        int factor = 1;
        while (timecodeIntervalSecs / onePixelInSecs < 50) {
            timecodeIntervalSecs = 5.0 * factor;
            factor++;
        }

       // int integerTimecode = (int) (fractionalSecs / timecodeIntervalSecs);
        mParams.minOffsetX = mMinOffsetX;
        mParams.viewHeight = getHeight();
        mParams.viewWidth = getWidth();
        mParams.offsetX = mOffsetX;
        //the valid visible width of scaled.
        mParams.width = getValidWidth();
       /* Logger.d(TAG, "onDraw", "mOffsetX = " + mOffsetX + " ,width = "
                + mParams.width + " ,maxPos = " + maxPosX());*/

        mParams.selectionStart = mSelectionStart;
        mParams.selectionEnd = mSelectionEnd;
        mParams.fractionalSecs = fractionalSecs;
        mParams.onePixelInSecs = onePixelInSecs;
        mParams.timecodeIntervalSecs = timecodeIntervalSecs;

        mDrawDelegate.drawWaveform(canvas, mParams, mAP);
        if(mParams.selectionStart >= 0){
            mDrawDelegate.drawSelectBorder(canvas, mSelectRangePaint, mParams, mAP);
        }
        mDrawDelegate.drawTime(canvas, mTimecodePaint, mParams, mAP);
        mDrawDelegate.drawAnnotator(canvas, mAnnotatorPaint, mParams, mAP, mAnnotatorLines);
        mDrawDelegate.drawCenterLine(canvas, mCenterLinePaint, mParams, mAP);
    }
    protected int getValidWidth(){
        if(mOffsetX < 0){
            return Math.min(maxPosX(), getWidth()) - mTruncateWidth;
        }else {
            return Math.min(maxPosX() - mOffsetX, getWidth()) - mTruncateWidth;
        }
    }

    /**
     * Called once when a new sound file is added
     */
    protected void computeDoublesForAllZoomLevels() {
        int numFrames = mSoundFile.getNumFrames();

        // Make sure the range is no more than 0 - 255
        float maxGain = 1.0f;
        for (int i = 0; i < numFrames; i++) {
            float gain = getGain(i, numFrames, mSoundFile.getFrameGains());
            if (gain > maxGain) {
                maxGain = gain;
            }
        }
        scaleFactor = 1.0f;
        if (maxGain > 255.0) {
            scaleFactor = 255 / maxGain;
        }

        // Build histogram of 256 bins and figure out the new scaled max
        maxGain = 0;
        int gainHist[] = new int[256];
        for (int i = 0; i < numFrames; i++) {
            int smoothedGain = (int) (getGain(i, numFrames, mSoundFile.getFrameGains()) * scaleFactor);
            if (smoothedGain < 0)
                smoothedGain = 0;
            if (smoothedGain > 255)
                smoothedGain = 255;

            if (smoothedGain > maxGain)
                maxGain = smoothedGain;

            gainHist[smoothedGain]++;
        }

        // Re-calibrate the min to be 5%
        minGain = 0;
        int sum = 0;
        while (minGain < 255 && sum < numFrames / 20) {
            sum += gainHist[(int) minGain];
            minGain++;
        }

        // Re-calibrate the max to be 99%
        sum = 0;
        while (maxGain > 2 && sum < numFrames / 100) {
            sum += gainHist[(int) maxGain];
            maxGain--;
        }

        range = maxGain - minGain;

        mNumZoomLevels = 4;
        mLenByZoomLevel = new int[4];
        mZoomFactorByZoomLevel = new float[4];

        float ratio = getMeasuredWidth() / (float) numFrames;

        if (ratio < 1) {
            mLenByZoomLevel[0] = Math.round(numFrames * ratio);
            mZoomFactorByZoomLevel[0] = ratio;

            mLenByZoomLevel[1] = numFrames;
            mZoomFactorByZoomLevel[1] = 1.0f;

            mLenByZoomLevel[2] = numFrames * 2;
            mZoomFactorByZoomLevel[2] = 2.0f;

            mLenByZoomLevel[3] = numFrames * 3;
            mZoomFactorByZoomLevel[3] = 3.0f;

            mZoomLevel = 0;
        } else {
            mLenByZoomLevel[0] = numFrames;
            mZoomFactorByZoomLevel[0] = 1.0f;

            mLenByZoomLevel[1] = numFrames * 2;
            mZoomFactorByZoomLevel[1] = 2f;

            mLenByZoomLevel[2] = numFrames * 3;
            mZoomFactorByZoomLevel[2] = 3.0f;

            mLenByZoomLevel[3] = numFrames * 4;
            mZoomFactorByZoomLevel[3] = 4.0f;

            mZoomLevel = 0;
            for (int i = 0; i < 4; i++) {
                if (mLenByZoomLevel[mZoomLevel] - getMeasuredWidth() > 0) {
                    break;
                } else {
                    mZoomLevel = i;
                }
            }
        }

        //set max zoom level
        mZoomLevel = mNumZoomLevels - 1;
        if(mDistancePerSecond > 0){
            //added by heaven7: make the distance as out expect.
            mLenByZoomLevel[mZoomLevel] *= (mDistancePerSecond * 1f / secondsToPixels(1));
            mZoomFactorByZoomLevel[mZoomLevel] = mLenByZoomLevel[mZoomLevel] * 1f / numFrames;

            float average = mZoomFactorByZoomLevel[mZoomLevel] / mNumZoomLevels;
            for (int i = 0; i < mNumZoomLevels ; i ++){
                float value = (i + 1) * average;
                mZoomFactorByZoomLevel[i] = value;
                mLenByZoomLevel[i] = (int) (value * numFrames);
            }
        }

        mInitialized = true;
    }

    //i = [0, numFrames - 1]. frameGains.len = numFrames
    protected float getGain(int i, int numFrames, int[] frameGains) {
        int x = Math.min(i, numFrames - 1);
        if (numFrames < 2) {
            return frameGains[x];
        } else {
            if (x == 0) {
                return (frameGains[0] / 2.0f) + (frameGains[1] / 2.0f);
            } else if (x == numFrames - 1) {
                return (frameGains[numFrames - 2] / 2.0f) + (frameGains[numFrames - 1] / 2.0f);
            } else {
                return (frameGains[x - 1] / 3.0f) + (frameGains[x] / 3.0f) + (frameGains[x + 1] / 3.0f);
            }
        }
    }

    protected float getHeight(int i, int numFrames, int[] frameGains, float scaleFactor, float minGain, float range) {
        float value = (getGain(i, numFrames, frameGains) * scaleFactor - minGain) / range;
        if (value < 0.0)
            value = 0.0f;
        if (value > 1.0)
            value = 1.0f;
        return value;
    }

    protected float getZoomedInHeight(float zoomLevel, int i) {
        int f = (int) zoomLevel;
        if (i == 0) {
            return 0.5f * getHeight(0, mSoundFile.getNumFrames(), mSoundFile.getFrameGains(), scaleFactor, minGain, range);
        }
        if (i == 1) {
            return getHeight(0, mSoundFile.getNumFrames(), mSoundFile.getFrameGains(), scaleFactor, minGain, range);
        }
        if (i % f == 0) {
            float x1 = getHeight(i / f - 1, mSoundFile.getNumFrames(), mSoundFile.getFrameGains(), scaleFactor, minGain, range);
            float x2 = getHeight(i / f, mSoundFile.getNumFrames(), mSoundFile.getFrameGains(), scaleFactor, minGain, range);
            return 0.5f * (x1 + x2);
        } else if ((i - 1) % f == 0) {
            return getHeight((i - 1) / f, mSoundFile.getNumFrames(), mSoundFile.getFrameGains(), scaleFactor, minGain, range);
        }
        return 0;
    }

    protected float getZoomedOutHeight(float zoomLevel, int i) {
        int f = (int) (i / zoomLevel);
        float x1 = getHeight(f, mSoundFile.getNumFrames(), mSoundFile.getFrameGains(), scaleFactor, minGain, range);
        float x2 = getHeight(f + 1, mSoundFile.getNumFrames(), mSoundFile.getFrameGains(), scaleFactor, minGain, range);
        return 0.5f * (x1 + x2);
    }

    protected float getNormalHeight(int i) {
        return getHeight(i, mSoundFile.getNumFrames(), mSoundFile.getFrameGains(), scaleFactor, minGain, range);
    }

    protected float getScaledHeight(float zoomLevel, int i) {
        if (zoomLevel == 1.0) {
            return getNormalHeight(i);
        } else if (zoomLevel < 1.0) {
            return getZoomedOutHeight(zoomLevel, i);
        }
        return getZoomedInHeight(zoomLevel, i);
    }
    private boolean canScroll(float dx) {
        if (mOffsetX == mMinOffsetX && dx < 0) {
            return false;
        }
        if (mOffsetX == maxOffsetX() && dx > 0) {
            return false;
        }
        return true;
    }
    private void dispatchTimeLineChanged(){
        if(mCallback != null){
            int delta = mOffsetX - maxOffsetX();
            float percent = Math.abs(delta) * 1f/ Math.abs(maxOffsetX() - mMinOffsetX);
            mCallback.onTimeLineChanged(this, percent);
        }
    }
    private void dispatchTimeLineChangeEnd(){
        if(mCallback != null){
            int delta = mOffsetX - maxOffsetX();
            float percent = Math.abs(delta) * 1f/ Math.abs(maxOffsetX() - mMinOffsetX);
            mCallback.onTimeLineChangeEnd(this, percent);
        }
    }

    @Override
    public int getWaveformHeight(int i, WaveformParam param) {
        if(mDistancePerSecond > 0){
            return  (int) (getZoomedOutHeight(mZoomFactorByZoomLevel[mZoomLevel], param.offsetX + i) * param.viewHeight/ 2);
        }
        return (int) (getScaledHeight(mZoomFactorByZoomLevel[mZoomLevel], param.offsetX + i) * param.viewHeight/ 2);
    }
    @Override
    public Paint getSelectStatePaint(boolean select) {
        return select ? mSelectedLinePaint : mUnselectedLinePaint;
    }
    @Override
    public Paint getSelectStateBackgroundPaint(boolean select) {
        return mUnselectedBgLinePaint;
    }

    protected class GestureImpl extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return super.onSingleTapUp(e);
        }
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return WaveformView.this.onScroll(e1, e2, distanceX, distanceY);
        }

        public boolean onFling(
                MotionEvent e1, MotionEvent e2, float vx, float vy) {
            //velocityX > 0 ? left -> right  : right to left
            if(mFixSelectLength && mSelectionEnd == maxPosX()){
                return true;
            }
            if(mScroller.isFling()){
                mScroller.abortIfNeed();
            }
            mScroller.startFling(vx, 0);
            return true;
        }
    }

    protected class ScrollCallbackImpl implements ScrollerWrapper.Callback{
        @Override
        public boolean onComputeScrolled(ScrollerWrapper wrapper, View view, int deltaX) {
            if(!canScroll(deltaX)){
                dispatchTimeLineChangeEnd();
                return false;
            }
            mOffsetX -= deltaX;
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
    }

    public interface TimeLineCallback {
        void onTimeLineChanged(WaveformView view, float percent);
        void onTimeLineChangeEnd(WaveformView view, float percent);
    }
}

