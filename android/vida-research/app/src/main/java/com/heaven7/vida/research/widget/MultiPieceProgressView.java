package com.heaven7.vida.research.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.heaven7.vida.research.R;

/**
 * multi piece progress view
 * Created by heaven7 on 2019/2/23.
 */
public class MultiPieceProgressView extends View {

    private final Rect mRect = new Rect();
    private final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
    private int space = 6;
    private int backgroundColor = Color.WHITE;
    private int progressColor = Color.RED;

    private PieceProvider pieceProvider;
    private int frameIndex;

    public MultiPieceProgressView(Context context) {
        this(context, null);
    }

    public MultiPieceProgressView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MultiPieceProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        if(attrs != null){
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MultiPieceProgressView);
            try {
                space = a.getDimensionPixelSize(R.styleable.MultiPieceProgressView_mppv_space, space);
                backgroundColor = a.getColor(R.styleable.MultiPieceProgressView_mppv_background_color, backgroundColor);
                progressColor = a.getColor(R.styleable.MultiPieceProgressView_mppv_progress_color, progressColor);
            }finally {
                a.recycle();
            }
        }

        mPaint.setStyle(Paint.Style.FILL);
    }

    public PieceProvider getPieceProvider() {
        return pieceProvider;
    }
    public void setPieceProvider(PieceProvider pieceProvider) {
        this.pieceProvider = pieceProvider;
    }

    public int getFrameIndex() {
        return frameIndex;
    }
    public void setFrameIndex(int frameIndex) {
        this.frameIndex = frameIndex;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int w = getWidth() - getPaddingLeft() - getPaddingRight();
        int top = getPaddingTop();
        int startX = getPaddingLeft();
        int height = getHeight() - getPaddingTop() - getPaddingBottom();
        int count = pieceProvider.getPieceCount();

        //draw progress
        long totalDuration = pieceProvider.getTotalDuration();
        int excludeSpace = space * (count - 1);
        //
        float unit = (w - excludeSpace) * 1f/ totalDuration;

        int position = Math.round((frameIndex + 1) * unit);
        int tw = 0;
        //bg
        mPaint.setColor(backgroundColor);
        for (int i = 0 ; i < count ; i ++){
            long duration = pieceProvider.getPieceDuration(i);
            int width = Math.round(duration * unit);
            mRect.set(startX, top , startX + width, top + height);
            canvas.drawRect(mRect, mPaint);
            startX += width + space;
            tw += width;
        }
        startX = getPaddingLeft();
        tw = 0;
        //progress
        mPaint.setColor(progressColor);
        for (int i = 0 ; i < count ; i ++){
            long duration = pieceProvider.getPieceDuration(i);
            int width = Math.round(duration * unit); // 需要用这个误差才会小.
            boolean shouldBreak = position <= tw + width;
            mRect.set(startX, top , shouldBreak ?
                            startX + (position - tw): startX + width,
                    top + height);
            canvas.drawRect(mRect, mPaint);
            startX += width + space;
            tw += width;
            if(shouldBreak){
                break;
            }
        }
        /*Logger.d("MultiPieceProgressView", "onDraw", "tw = "
                + tw + " ,(w - excludeSpace) = " +(w - excludeSpace));*/
    }

    public interface PieceProvider{
        int getPieceCount();
        long getPieceDuration(int index);
        long getTotalDuration();
    }
}
