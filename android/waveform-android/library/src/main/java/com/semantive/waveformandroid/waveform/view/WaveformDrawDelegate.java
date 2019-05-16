package com.semantive.waveformandroid.waveform.view;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.heaven7.core.util.Logger;

import java.util.List;

/**
 * Created by heaven7 on 2019/5/14.
 */
/*public*/ abstract class WaveformDrawDelegate {

    static final String TAG = "WaveformDrawDelegate";
    static final boolean DEBUG = true;
    protected final Callback callback;

    public WaveformDrawDelegate(Callback callback) {
        this.callback = callback;
    }

    //绘制波形
    public abstract void drawWaveform(Canvas canvas, WaveformParam param, AnnotatorParam ap);
    //绘制选择范围的边框
    public abstract void drawSelectBorder(Canvas canvas, Paint paint, WaveformParam param, AnnotatorParam ap);
    //draw time.
    public abstract void drawTime(Canvas canvas, Paint paint, WaveformParam param, AnnotatorParam ap);
    //绘制打点
    public abstract void drawAnnotator(Canvas canvas, Paint paint, WaveformParam wp, AnnotatorParam ap, List<AnnotatorLine> lines);

    public interface Callback{
        int getWaveformHeight(int i, WaveformParam param);
        Paint getSelectStatePaint(boolean select);
        Paint getSelectStateBackgroundPaint(boolean select);
    }
}

class UpDownWaveformDrawDelegate extends WaveformDrawDelegate{

    public UpDownWaveformDrawDelegate(Callback callback) {
        super(callback);
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

    protected final void drawWaveformLine(Canvas canvas, int x, int y0, int y1, Paint paint) {
        canvas.drawLine(x, y0, x, y1, paint);
    }

    protected void drawWaveform(final Canvas canvas, WaveformParam param, AnnotatorParam ap, int i){
        int measuredHeight = param.viewHeight - ap.startDy;
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
                ctr + 1 + h,
                paint);
        //回拨
        /*  if (i + start == mPlaybackPos) {
            canvas.drawLine(i, 0, i, measuredHeight, mPlaybackLinePaint);
        }*/
    }

    @Override
    public void drawWaveform(Canvas canvas, WaveformParam param, AnnotatorParam ap) {
        int i = 0;
        while (i < param.width) {
            param.fractionalSecs += param.onePixelInSecs;
            // Draw waveform
            drawWaveform(canvas, param, ap, i);

            i++;
        }
    }
    @Override
    public void drawSelectBorder(Canvas canvas, Paint paint, WaveformParam param, AnnotatorParam ap) {
        int mSelectionStart = param.selectionStart;
        int mSelectionEnd = param.selectionEnd;
        int bottomY = param.viewHeight - ap.startDy;
        int mOffsetX = param.offsetX;
        canvas.drawLine(
                mSelectionStart - mOffsetX + 0.5f, 0,
                mSelectionStart - mOffsetX + 0.5f, bottomY,
                paint);
        canvas.drawLine(
                mSelectionEnd - mOffsetX + 0.5f, 0,
                mSelectionEnd - mOffsetX + 0.5f, bottomY,
                paint);
    }

    @Override
    public void drawTime(Canvas canvas, Paint paint, WaveformParam param, AnnotatorParam ap) {
        if(!DEBUG){
            return;
        }
        double fractionalSecs = param.offsetX * param.onePixelInSecs;
        int integerTimecode = (int) (fractionalSecs / param.timecodeIntervalSecs);
        int i = 0;
        while (i < param.width) {
            i++;
            fractionalSecs += param.onePixelInSecs;
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
            Logger.d(TAG, "drawAnnotator", "start draw line.time = " + line.mesc);
            int x = line.pix - wp.offsetX;
            //center point
            paint.setStyle(Paint.Style.FILL);
            paint.setAlpha(255);
            paint.setStrokeWidth(0);
            canvas.drawCircle(x, ap.startY, ap.dotMinRadius, paint);
            //line
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(ap.lineWidth);
            int lineY = ap.startY + ap.dotLineDistance;
            canvas.drawLine(x, lineY, x, wp.viewHeight, paint);
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
class UpWaveformDrawDelegate extends UpDownWaveformDrawDelegate{

    public UpWaveformDrawDelegate(Callback callback) {
        super(callback);
    }
    @Override
    protected void drawWaveform(Canvas canvas, WaveformParam param, AnnotatorParam ap, int i) {
        //bg
        Paint bgPaint = getSelectStateBackgroundPaint(i, param);
        drawWaveformLine(canvas, i, 0,  param.viewHeight - ap.startDy, bgPaint);
        //波形高度
        Paint paint = getSelectStatePaint(i, param);
        int h = callback.getWaveformHeight(i, param);
        int bottomY = param.viewHeight - ap.startDy;
        drawWaveformLine(
                canvas, i,
                bottomY - h,
                bottomY,
                paint);
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
            Logger.d(TAG, "drawAnnotator", "start draw line.time = " + line.mesc);
            int x = line.pix - wp.offsetX;
            //center point at bottom
            paint.setStyle(Paint.Style.FILL);
            paint.setAlpha(255);
            paint.setStrokeWidth(0);
            canvas.drawCircle(x, wp.viewHeight - ap.startDy / 2, ap.dotMinRadius, paint);
        }
    }
}