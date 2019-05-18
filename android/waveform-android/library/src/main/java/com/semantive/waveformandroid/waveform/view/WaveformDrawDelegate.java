package com.semantive.waveformandroid.waveform.view;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;

import com.heaven7.core.util.Logger;

import java.util.List;

/**
 * Created by heaven7 on 2019/5/14.
 */
/*public*/ abstract class WaveformDrawDelegate {

    static final String TAG = "WaveformDrawDelegate";
    static final boolean DEBUG = true;
    final Callback callback;
    FocusParam focusParam;

    public WaveformDrawDelegate(Callback callback) {
        this.callback = callback;
    }
    public void setFocusParam(FocusParam focusParam) {
        this.focusParam = focusParam;
    }
    //绘制波形
    public abstract void drawWaveform(Canvas canvas, WaveformParam param, AnnotatorParam ap);
    //绘制选择范围的边框
    public abstract void drawSelectBorder(Canvas canvas, Paint paint, WaveformParam param, AnnotatorParam ap);
    //draw time.
    public abstract void drawTime(Canvas canvas, Paint paint, WaveformParam param, AnnotatorParam ap);
    //绘制打点
    public abstract void drawAnnotator(Canvas canvas, Paint paint, WaveformParam wp, AnnotatorParam ap, List<AnnotatorLine> lines);
    //draw center line.
    public void drawCenterLine(Canvas canvas, Paint paint, WaveformParam wp, AnnotatorParam ap) {

    }

    public interface Callback{
        /**
         * get the waveform height
         * @param i the start pix index. start from 0
         * @param param the waveform param
         * @return the waveform height
         */
        int getWaveformHeight(int i, WaveformParam param);

        /**
         * get the select state paint. which used to draw waveform
         * @param select true if is select
         * @return the select state paint
         */
        Paint getSelectStatePaint(boolean select);
        /**
         * get the select state background paint which used to draw waveform background.
         * @param select true if is select
         * @return the select state paint
         */
        Paint getSelectStateBackgroundPaint(boolean select);
    }
}

class UpDownWaveformDrawDelegate extends WaveformDrawDelegate{

    public UpDownWaveformDrawDelegate(Callback callback) {
        super(callback);
    }

    /**
     * a switch for draw background of non-waveform area
     * @return true to draw background
     */
    protected boolean drawNoTimeBackground() {
        return true;
    }
    protected final Paint getSelectStatePaint(final int i, WaveformParam param) {
        int mSelectionStart = param.selectionStart;
        int mSelectionEnd = param.selectionEnd;
        int start = param.offsetX;
        return callback.getSelectStatePaint(i + start >= mSelectionStart && i + start < mSelectionEnd);
    }
    protected final Paint getSelectStateBackgroundPaint(final int i, WaveformParam param) {
        int mSelectionStart = param.selectionStart;
        int mSelectionEnd = param.selectionEnd;
        int start = param.offsetX;
        return callback.getSelectStateBackgroundPaint(i + start >= mSelectionStart && i + start < mSelectionEnd);
    }
    protected final void drawWaveformLine(Canvas canvas, float x, float sy, float ey, Paint paint) {
        sy = wrapStartY(sy);
        ey = wrapEndY(ey);
        canvas.drawLine(x, sy, x, ey, paint);
    }
    protected float wrapStartY(float startY){
        return focusParam != null ? startY + focusParam.focusMarginTopBottom : startY;
    }

    protected float wrapEndY(float endY){
        return focusParam != null ? endY - focusParam.focusMarginTopBottom : endY;
    }

    /**
     * get the content width which used to draw waveform
     * @param param the waveform parameter
     * @return the valid content width
     */
    protected int getContentWidth(WaveformParam param) {
        return param.offsetX >= 0 ? param.width : param.width - param.offsetX;
    }

    protected void drawWaveform(final Canvas canvas, WaveformParam param, AnnotatorParam ap, int i){
        int measuredHeight = param.viewHeight;
        int ctr = param.viewHeight / 2;

        //背景
        Paint bgPaint = getSelectStateBackgroundPaint(i, param);
        drawWaveformLine(canvas, i, 0, measuredHeight, bgPaint);
        //波形高度
        Paint paint = getSelectStatePaint(i, param);
        int h = callback.getWaveformHeight(i, param);
        drawWaveformLine(
                canvas, i,
                ctr - h,
                ctr + h,
                paint);
        //回拨 mOffsetX = 16164 ,width = 540 ,maxPos = 16704
        /*  if (i + start == mPlaybackPos) {
            canvas.drawLine(i, 0, i, measuredHeight, mPlaybackLinePaint);
        }*/
    }

    @Override
    public void drawWaveform(Canvas canvas, WaveformParam param, AnnotatorParam ap) {
        final int offsetX = param.offsetX;
        int len = getContentWidth(param);

        //the limit start means there is no time
        int limitStart = offsetX < 0 ? Math.abs(offsetX) : -1;
        if(drawNoTimeBackground()){
            if(limitStart > 0){
                //draw bg for no waveform
                Paint paint = getSelectStateBackgroundPaint(-1, param);
                canvas.drawRect(0,
                        wrapStartY(0),
                        limitStart + 1,
                        wrapEndY(param.viewHeight - ap.startDy),
                        paint);
            }
        }
        //draw right bg.
        if(drawNoTimeBackground()){
            Paint paint = getSelectStateBackgroundPaint(-1, param);
            canvas.drawRect(len,
                    wrapStartY(0),
                    len + param.viewWidth / 2,
                    wrapEndY(param.viewHeight - ap.startDy),
                    paint);
        }

        //0- abs(offsetX) draw nothing.
        for (int i = 0 ; i < len ; i ++){
            if(i <= limitStart){
                continue;
            }
            param.fractionalSecs += param.onePixelInSecs;
            // Draw waveform
            drawWaveform(canvas, param, ap, i);
        }
    }

    @Override
    public void drawSelectBorder(Canvas canvas, Paint paint, WaveformParam param, AnnotatorParam ap) {
        int mSelectionStart = param.selectionStart;
        int mSelectionEnd = param.selectionEnd;
        int bottomY = param.viewHeight - ap.startDy;
        int mOffsetX = param.offsetX;

        drawWaveformLine(canvas, mSelectionStart - mOffsetX + 0.5f, 0, bottomY, paint);
        drawWaveformLine(canvas, mSelectionEnd - mOffsetX + 0.5f, 0, bottomY, paint);
    }

    @Override
    public void drawTime(Canvas canvas, Paint paint, WaveformParam param, AnnotatorParam ap) {
        if(!DEBUG){
            return;
        }
        final int width = getContentWidth(param);
        double fractionalSecs = param.offsetX * param.onePixelInSecs;
        int integerTimecode = (int) (fractionalSecs / param.timecodeIntervalSecs);
        int i = 0;
        while (i < width) {
            i++;
            fractionalSecs += param.onePixelInSecs;
            if(fractionalSecs < 0){
                continue;
            }
            int integerSecs2 = (int) fractionalSecs;
            int integerTimecodeNew = (int) (fractionalSecs / param.timecodeIntervalSecs);
            if (integerTimecodeNew != integerTimecode) {
                integerTimecode = integerTimecodeNew;

                // Turn, e.g. 67 seconds into "1:07"
                String timecodeMinutes = "" + (integerSecs2 / 60);
                String timecodeSeconds = "" + (integerSecs2 % 60);
                if ((integerSecs2 % 60) < 10) {
                    timecodeSeconds = "0" + timecodeSeconds;
                }
                String timecodeStr = timecodeMinutes + ":" + timecodeSeconds;
                float offset = (float) (0.5 * paint.measureText(timecodeStr));
                canvas.drawText(timecodeStr,
                        i - offset,
                        30,
                        paint);
            }
        }
    }
    @Override
    public void drawAnnotator(Canvas canvas, Paint paint, WaveformParam wp, AnnotatorParam ap, List<AnnotatorLine> lines) {
        //播放状态需要绘制动画。包括外面的点击按钮. 时间也要跟随变动。播放声音（按钮和到达打的点）
        for (AnnotatorLine line : lines){
            if(line.pix == -1){
                Logger.d("UpDownWaveformDrawDelegate", "drawAnnotator", "line.pix == -1");
                continue;
            }
            if(line.pix < wp.offsetX){
                continue;
            }
           // Logger.d(TAG, "drawAnnotator", "start draw line.time = " + line.mesc);
            paint.setColor(line.adjustMode ? ap.adjustColor : ap.color);
            int x = line.pix - wp.offsetX;
            //center point
            paint.setStyle(Paint.Style.FILL);
            paint.setAlpha(255);
            paint.setStrokeWidth(0);
            canvas.drawCircle(x, ap.startY, ap.dotMinRadius, paint);
            //for adjust mode. never draw line and circle
            if(!line.drawVerticalLine){
                continue;
            }
            //line
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(ap.lineWidth);
            int lineY = ap.startY + ap.dotLineDistance;
            canvas.drawLine(x, lineY, x, wp.viewHeight - ap.startDy, paint);
            //circle
            if(line.animProcess > 0){
                paint.setStrokeWidth(line.animProcess * 3);
                float radius = line.animProcess * ap.dotMaxRadius;
                int alpha = 255 - (int) (255 * line.animProcess);
                paint.setAlpha(Math.max(0, alpha));
                canvas.drawCircle(x, ap.startY, radius, paint);
            }
        }
    }
}
class MusicStartEndDrawDelegate extends UpDownWaveformDrawDelegate{

    public MusicStartEndDrawDelegate(Callback callback) {
        super(callback);
    }
    @Override
    public void drawSelectBorder(Canvas canvas, Paint paint, WaveformParam param, AnnotatorParam ap) {
       // super.drawSelectBorder(canvas, paint, param, ap);
    }
    @Override
    public void drawCenterLine(Canvas canvas, Paint paint, WaveformParam wp, AnnotatorParam ap) {
        float startX = wp.viewWidth * 1f / 2 - wp.selectStrokeWidth * 1f / 2;
        float endX = wp.viewWidth * 1f / 2 + wp.selectStrokeWidth * 1f / 2;
        canvas.drawLine(startX,
                wrapStartY(0),
                endX,
                wrapEndY(wp.viewHeight - ap.startDy), paint);
    }
}
class UpWaveformDrawDelegate extends UpDownWaveformDrawDelegate{

    private final Path mPath = new Path();
    private final EditorWaveformCallback mEWC;

    public UpWaveformDrawDelegate(Callback callback, EditorWaveformCallback ewc) {
        super(callback);
        this.mEWC = ewc;
    }

    @Override
    protected int getContentWidth(WaveformParam param) {
        return (int) mEWC.getContentRect().width() - param.offsetX;
    }

    @Override
    protected void drawWaveform(Canvas canvas, WaveformParam param, AnnotatorParam ap, int i) {
        int bottomY = param.viewHeight - ap.startDy;
        //bg
        Paint bgPaint = getSelectStateBackgroundPaint(i, param);
        drawWaveformLine(canvas, i, 0,  bottomY, bgPaint);
        //波形高度
        Paint paint = getSelectStatePaint(i, param);
        int h = callback.getWaveformHeight(i, param);//may be 0

        int by = bottomY - focusParam.focusMarginTopBottom;
        canvas.drawLine(i, by - h,  i,  by,  paint);
    }

    @Override
    public void drawWaveform(Canvas canvas, WaveformParam param, AnnotatorParam ap) {
        RectF contentRect = mEWC.getContentRect();
        if(mEWC.isSelected()){
            RectF rectF = mEWC.getTmpRectF();
            rectF.set(contentRect.left - focusParam.blockWidth,
                    contentRect.top,
                    contentRect.right + focusParam.blockWidth,
                    contentRect.bottom);
            canvas.drawRoundRect(rectF, focusParam.blockRoundSize, focusParam.blockRoundSize, mEWC.getFocusPaint());
            super.drawWaveform(canvas, param, ap);
        }else {
            mPath.reset();
            mPath.addRoundRect(contentRect, param.roundSize, param.roundSize, Path.Direction.CW);
            canvas.save();
            canvas.clipPath(mPath);
            super.drawWaveform(canvas, param, ap);
            canvas.restore();
        }
    }

    @Override
    public void drawAnnotator(Canvas canvas, Paint paint, WaveformParam wp, AnnotatorParam ap, List<AnnotatorLine> lines) {
        for (AnnotatorLine line : lines){
            if(line.pix == -1){
                Logger.d("UpDownWaveformDrawDelegate", "drawAnnotator", "line.pix == -1");
                continue;
            }
            if(line.pix < wp.offsetX){
                continue;
            }
           // Logger.d(TAG, "drawAnnotator", "start draw line.time = " + line.mesc);
            int x = line.pix - wp.offsetX;
            //center point at bottom
            paint.setStyle(Paint.Style.FILL);
            paint.setAlpha(255);
            paint.setStrokeWidth(0);
            canvas.drawCircle(x, wp.viewHeight - ap.startDy / 2, ap.dotMinRadius, paint);
        }
    }
    @Override
    protected boolean drawNoTimeBackground() {
        return false;
    }
}