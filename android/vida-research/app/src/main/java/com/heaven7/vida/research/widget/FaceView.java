package com.heaven7.vida.research.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.heaven7.core.util.Logger;

import java.util.List;

/**
 * Created by heaven7 on 2018/4/28 0028.
 */

public class FaceView extends AppCompatImageView {

    private final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private List<FaceDrawInfo> mFaceRects;

    public FaceView(Context context) {
        this(context, null, 0);
    }

    public FaceView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mPaint.setStrokeWidth(5);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.RED);
    }

    public void setFaceAreas(List<FaceDrawInfo> rects){
        mFaceRects = rects;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(mFaceRects != null){
          /*  Logger.d("FaceView", "onDraw", "width = " + getWidth()
                    + " ,height = " + getHeight());*/
            for(FaceDrawInfo fdi : mFaceRects) {
               // Logger.d("FaceView", "onDraw", "" + fdi.toString());
                canvas.save();
                canvas.rotate(fdi.rotateAngle, fdi.rect.left, fdi.rect.top);
                canvas.drawRect(fdi.rect, mPaint);
                canvas.restore();
            }
        }
    }

    public static class FaceDrawInfo{
        Rect rect;
        float rotateAngle;

        public FaceDrawInfo(Rect rect, float rotateAngle) {
            this.rect = rect;
            this.rotateAngle = rotateAngle;
        }

        @Override
        public String toString() {
            return "FaceDrawInfo{" +
                    "rect=" + rect +
                    ", rotateAngle=" + rotateAngle +
                    '}';
        }
    }
}
