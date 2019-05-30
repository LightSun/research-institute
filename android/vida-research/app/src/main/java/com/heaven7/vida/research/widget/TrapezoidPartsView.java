package com.heaven7.vida.research.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.heaven7.java.base.util.Predicates;
import com.heaven7.vida.research.utils.DimenUtil;
import com.heaven7.vida.research.utils.DrawingUtils;
import com.heaven7.vida.research.widget.shape.BaseShape;

import java.util.List;

import androidx.annotation.Nullable;

/**
 * 梯形控件
 * Created by heaven7 on 2019/5/30.
 */
public class TrapezoidPartsView extends View {

    private static final boolean DEBUG = false;
    private static final String TAG = "TrapezoidPartsView";
    private final Path mPath = new Path();

    private final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Rect mRect = new Rect();
    private final RectF mRectF = new RectF();
    private boolean mInitialized;

    private final Param mParam = new Param();

    private float mAngle = 68;     //梯形角度

    private int mTextSize = 50;
    private int mTextColor = Color.WHITE;

    private List<TrapezoidPart> parts;

    public TrapezoidPartsView(Context context) {
        this(context, null);
    }

    public TrapezoidPartsView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0, 0);
    }

    public TrapezoidPartsView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public TrapezoidPartsView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        mParam.mPartHeight = DimenUtil.dip2px(context, 66);
        mParam.mSpace = DimenUtil.dip2px(context, 9);
        //TODO init
        mPaint.setTextSize(mTextSize);
        mPaint.setColor(mTextColor);
    }

    public List<TrapezoidPart> getParts() {
        return parts;
    }

    public void setParts(List<TrapezoidPart> parts) {
        if (parts.size() < 2) {
            throw new UnsupportedOperationException();
        }

        mInitialized = false;
        this.parts = parts;
        postDelayed(new Runnable() {
            @Override
            public void run() {
                computeRects();
                mInitialized = true;
                invalidate();
            }
        }, 5);
    }

    private void computeRects() {
        mPath.reset();
        int mPartHeight = mParam.mPartHeight;
        double radians = Math.toRadians(mAngle);
        double tan = Math.tan(radians);
        //short len of left
        int shortLen = (int) (mPartHeight / tan);
        mParam.mShortLength = shortLen;

        int count = parts.size();
        mParam.mTrapezoidMaxLength = (getWidth()- getPaddingLeft() - getPaddingRight()
                - (count - 1) * mParam.mSpace + (count - 1) * shortLen) / count;
        mParam.mTrapezoidSecondLength = mParam.mTrapezoidMaxLength - shortLen;

        int left = getPaddingLeft();
        int top = getPaddingTop();
        int right = getWidth() - getPaddingRight();
        //handle left.(p1,p2,p3, 从左往右。从上到下)
        TrapezoidPart leftPart = parts.get(0);
        Rect rect_left_part = leftPart.getRect();
        rect_left_part.set(left, top, left + mParam.mTrapezoidMaxLength - shortLen, top + mPartHeight);
        Point p1 = new Point(rect_left_part.right, rect_left_part.top);
        Point p2 = new Point(p1.x, p1.y + mPartHeight);
        Point p3 = new Point(left + mParam.mTrapezoidMaxLength, p1.y);
        BaseShape.TriangleRange triangle = leftPart.getRightTriangle();
        triangle.setP1(p1);
        triangle.setP2(p2);
        triangle.setP3(p3);

        if (DEBUG) {
            mRectF.set(rect_left_part);
            mPath.addRect(mRectF, Path.Direction.CW);
            mPath.moveTo(p1.x, p1.y);
            mPath.lineTo(p3.x, p3.y);
            mPath.lineTo(p2.x, p2.y);
        }
        //handle right
        TrapezoidPart rightPart = parts.get(parts.size() - 1);
        Rect rect_right_part = rightPart.getRect();
        rect_right_part.set(right - mParam.mTrapezoidSecondLength, top, right, rect_left_part.bottom);
        triangle = rightPart.getLeftTriangle();

        p2 = new Point(rect_right_part.left, rect_right_part.top);
        p3 = new Point(p2.x, rect_left_part.bottom);
        p1 = new Point(p3.x - shortLen, p3.y);
        triangle.setP2(p2);
        triangle.setP3(p3);
        triangle.setP1(p1);
        if (DEBUG) {
            mRectF.set(rect_right_part);
            mPath.addRect(mRectF, Path.Direction.CW);
            mPath.moveTo(p3.x, p3.y);
            mPath.lineTo(p1.x, p1.y);
            mPath.lineTo(p2.x, p2.y);
        }
        //handle centers
        if (parts.size() > 2) {
            int size = parts.size() - 1;
            //last p2.
            Point lastTailP = leftPart.getRightTriangle().getP2();
            for (int i = 1; i < size; i++) {
                TrapezoidPart part = parts.get(i);
                BaseShape.TriangleRange lt = part.getLeftTriangle();
                Point pp1 = new Point(lastTailP.x + mParam.mSpace, lastTailP.y);
                Point pp3 = new Point(pp1.x + shortLen, pp1.y);
                Point pp2 = new Point(pp3.x, top);
                lt.setP1(pp1);
                lt.setP2(pp2);
                lt.setP3(pp3);

                BaseShape.TriangleRange rt = part.getRightTriangle();
                Point ppp1 = new Point(pp2.x + mParam.mTrapezoidSecondLength - shortLen, pp2.y);
                Point ppp2 = new Point(ppp1.x, ppp1.y + mPartHeight);
                Point ppp3 = new Point(ppp1.x + shortLen, ppp1.y);
                rt.setP1(ppp1);
                rt.setP2(ppp2);
                rt.setP3(ppp3);

                part.getRect().set(pp2.x, pp2.y, ppp2.x, ppp2.y);
                if (DEBUG) {
                    mRectF.set(part.getRect());
                    mPath.addRect(mRectF, Path.Direction.CW);
                    mPath.moveTo(pp3.x, pp3.y);
                    mPath.lineTo(pp1.x, pp1.y);
                    mPath.lineTo(pp2.x, pp2.y);

                    mPath.moveTo(ppp1.x, ppp1.y);
                    mPath.lineTo(ppp3.x, ppp3.y);
                    mPath.lineTo(ppp2.x, ppp2.y);
                }

                lastTailP = ppp3;
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Rect rect = DrawingUtils.measure(mPaint, "PROJECT");
        int height = mParam.mPartHeight + mParam.mTextMarginTop + rect.height() + getPaddingTop() + getPaddingBottom();
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (Predicates.isEmpty(parts) || !mInitialized) {
            return;
        }
        if (DEBUG) {
            mPaint.setColor(Color.RED);
            mPaint.setStyle(Paint.Style.STROKE);
            canvas.drawPath(mPath, mPaint);
        } else {
            mParam.top = getPaddingTop();
            mParam.bottom = getHeight() - getPaddingBottom();
            //draw left
            TrapezoidPart leftPart = parts.get(0);
            leftPart.draw(canvas, mPaint, mRect, mRectF, mParam);

            TrapezoidPart rightPart = parts.get(parts.size() - 1);
            rightPart.draw(canvas, mPaint, mRect, mRectF, mParam);
            if(parts.size() > 2){
                for (int i = 1, size = parts.size() - 1; i < size ; i ++){
                    TrapezoidPart part = parts.get(i);
                    part.draw(canvas, mPaint, mRect, mRectF, mParam);
                }
            }
        }
    }

    public interface OnTrapezoidPartClickListener {
        void onClickTrapezoidPart(TrapezoidPartsView view, TrapezoidPart part);
    }

    static class Param {
        int top;
        int bottom;

        int mTrapezoidMaxLength;    // max length of trapezoid
        int mTrapezoidSecondLength; //second max length of trapezoid
        int mShortLength;           // short length of Triangle

        int mPartHeight = 200;
        int mSpace = 40;
        int mTextMarginTop = 40;
    }

    public static class TrapezoidPart {
        private int type;
        private String text;
        private Drawable icon;
        private Bitmap bgIcon; //should support scale.

        final Rect rect = new Rect();
        BaseShape.TriangleRange leftRange;
        BaseShape.TriangleRange rightRange;

        Rect getRect() {
            return rect;
        }

        BaseShape.TriangleRange getLeftTriangle() {
            if (leftRange == null) {
                leftRange = new BaseShape.TriangleRange();
            }
            return leftRange;
        }

        BaseShape.TriangleRange getRightTriangle() {
            if (rightRange == null) {
                rightRange = new BaseShape.TriangleRange();
            }
            return rightRange;
        }

        public void setType(int type) {
            this.type = type;
        }

        public void setText(String text) {
            this.text = text;
        }

        public void setIcon(Drawable icon) {
            this.icon = icon;
            icon.setBounds(0, 0, icon.getIntrinsicWidth(), icon.getIntrinsicHeight());
        }

        public Bitmap getBgIcon() {
            return bgIcon;
        }

        public void setBgIcon(Bitmap bgIcon) {
            this.bgIcon = bgIcon;
        }

        public int getType() {
            return type;
        }

        public String getText() {
            return text;
        }

        public Drawable getIcon() {
            return icon;
        }

        /*public*/ void drawBackground(Canvas canvas, Rect mRect, Param param) {
            int left;
            if (leftRange != null) {
                left = leftRange.getP1().x;
            } else {
                left = rect.left;
            }
            mRect.left = left;
            mRect.right = left + param.mTrapezoidMaxLength;
            mRect.top = param.top;
            mRect.bottom = mRect.top + param.mPartHeight;
            canvas.drawBitmap(bgIcon, null, mRect, null);
        }

        /*public*/ void drawIcon(Canvas canvas, Rect mRect, Param param) {
            DrawingUtils.drawCenter(canvas, rect, icon);
        }

        /*public*/ void drawText(Canvas canvas, Paint textPaint, Rect mRect, RectF mRectF, Param param) {
            Rect textRange = DrawingUtils.measure(textPaint, text);
            mRect.left = rect.left;
            mRect.right = rect.right;
            mRect.top = rect.bottom + param.mTextMarginTop;
            mRect.bottom = mRect.top + textRange.height();
            DrawingUtils.computeTextDrawingCoordinate(text, textPaint, mRect, mRectF);
            canvas.drawText(text, mRectF.left, mRectF.top - textPaint.ascent(), textPaint);
        }

        /*public*/ void draw(Canvas canvas, Paint textPaint, Rect mRect, RectF mRectF, Param param) {
            drawBackground(canvas, mRect, param);
            drawIcon(canvas, mRect, param);
            drawText(canvas, textPaint, mRect, mRectF, param);
        }
    }
}
