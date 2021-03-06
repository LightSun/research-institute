package com.heaven7.vida.research.widget;

import android.content.Context;
import android.content.res.TypedArray;
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
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.heaven7.java.base.util.Predicates;
import com.heaven7.vida.research.R;
import com.heaven7.vida.research.utils.DimenUtil;
import com.heaven7.vida.research.utils.DrawingUtils;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.core.view.GestureDetectorCompat;

/**
 * 梯形控件
 * @author heaven7
 */
public class TrapezoidPartsView extends View {

    //for debug
    private boolean mDebug = false;
    private Path mPath;

    private final BaseShape.RectangleShape mRectShape = new BaseShape.RectangleShape();
    private final BaseShape.TriangleShape mTriangleShape = new BaseShape.TriangleShape();

    private final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Rect mRect = new Rect();
    private final RectF mRectF = new RectF();

    private final Param mParam = new Param();

    private float mAngle = 68;
    private float mImageRatio = 488 * 1f/ 264;

    private int mTouchAlpha = 179;
    private GestureDetectorCompat mGesture;

    private OnTrapezoidPartClickListener mListener;
    private List<TrapezoidPart> mParts;

    private TrapezoidPart mFocusPart;

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

        int mTextSize = 50;
        int mTextColor = Color.WHITE;
        if(attrs != null){
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TrapezoidPartsView);
            try {
                float alpha = a.getFloat(R.styleable.TrapezoidPartsView_tpv_touch_alpha, 0.7f);
                mTouchAlpha = (int) (alpha * 255);
                mParam.mSpace = a.getDimensionPixelSize(R.styleable.TrapezoidPartsView_tpv_space, mParam.mSpace);
                mParam.mTextMarginTop = a.getDimensionPixelSize(R.styleable.TrapezoidPartsView_tpv_text_margin_top, mParam.mTextMarginTop);
                mAngle = a.getFloat(R.styleable.TrapezoidPartsView_tpv_angle, mAngle);
                mImageRatio = a.getFloat(R.styleable.TrapezoidPartsView_tpv_image_aspect_ratio, mImageRatio);
                mDebug = a.getBoolean(R.styleable.TrapezoidPartsView_tpv_debug, mDebug);
                mTextSize = a.getDimensionPixelSize(R.styleable.TrapezoidPartsView_tpv_text_size, mTextSize);
                mTextColor = a.getColor(R.styleable.TrapezoidPartsView_tpv_text_color, mTextColor);
            }finally {
                a.recycle();
            }
        }else {
            mParam.mSpace = DimenUtil.dip2px(context, 16);
            mParam.mTextMarginTop = 40;
            mTouchAlpha = 179;
        }
        mPaint.setTextSize(mTextSize);
        mPaint.setColor(mTextColor);

        mGesture = new GestureDetectorCompat(context, new GestureListener());
        if(mDebug){
            mPath = new Path();
        }
    }

    /**
     * set on click TrapezoidPart listener.
     * @param mListener the listener
     */
    public void setOnTrapezoidPartClickListener(OnTrapezoidPartClickListener mListener) {
        this.mListener = mListener;
    }

    /**
     * get the all trapezoid parts
     * @return the pars
     */
    public List<TrapezoidPart> getParts() {
        return mParts;
    }

    /**
     * set the trapezoid parts
     * @param parts the parts
     */
    public void setParts(List<TrapezoidPart> parts) {
        if (parts.size() < 2) {
            throw new UnsupportedOperationException();
        }
        this.mParts = parts;
        computeAll();
        requestLayout();
        postInvalidate();
    }
    private void computeTrapezoidParameters(Context context) {
        double tan = Math.tan(Math.toRadians(mAngle));
        int count = mParts.size();
        int wholeWidth = getWidth() - getPaddingLeft() - getPaddingRight();
        int val = wholeWidth - (count - 1 ) * mParam.mSpace;
        double a = count - (count - 1) / (tan * mImageRatio);
        mParam.mTrapezoidMaxLength = (int) (val / a);
        mParam.mPartHeight = (int) (mParam.mTrapezoidMaxLength / mImageRatio);
        mParam.mShortLength = (int) (mParam.mPartHeight / tan);
        mParam.mTrapezoidSecondLength = mParam.mTrapezoidMaxLength - mParam.mShortLength;
    }

    private void computeAll() {
        if(Predicates.isEmpty(mParts)){
            return;
        }
        if(mDebug){
            mPath.reset();
        }
        computeTrapezoidParameters(getContext());
        int mPartHeight = mParam.mPartHeight;
        int shortLen = mParam.mShortLength;

        int left = getPaddingLeft();
        int top = getPaddingTop();
        //handle left.(p1,p2,p3, 从左往右。从上到下)
        TrapezoidPart leftPart = mParts.get(0);
        Rect rect_left_part = leftPart.getRect();
        rect_left_part.set(left, top, left + mParam.mTrapezoidMaxLength - shortLen, top + mPartHeight);
        Point p1 = new Point(rect_left_part.right, rect_left_part.top);
        Point p2 = new Point(p1.x, p1.y + mPartHeight);
        Point p3 = new Point(left + mParam.mTrapezoidMaxLength, p1.y);
        BaseShape.TriangleRange triangle = leftPart.getRightTriangle();
        triangle.setP1(p1);
        triangle.setP2(p2);
        triangle.setP3(p3);

        if (mDebug) {
            mRectF.set(rect_left_part);
            mPath.addRect(mRectF, Path.Direction.CW);
            mPath.moveTo(p1.x, p1.y);
            mPath.lineTo(p3.x, p3.y);
            mPath.lineTo(p2.x, p2.y);
        }
        //handle centers
        Point lastTailP = leftPart.getRightTriangle().getP2();
        if (mParts.size() > 2) {
            int size = mParts.size() - 1;
            //last p2.
            for (int i = 1; i < size; i++) {
                TrapezoidPart part = mParts.get(i);
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
                if (mDebug) {
                    mRectF.set(part.getRect());
                    mPath.addRect(mRectF, Path.Direction.CW);
                    mPath.moveTo(pp3.x, pp3.y);
                    mPath.lineTo(pp1.x, pp1.y);
                    mPath.lineTo(pp2.x, pp2.y);

                    mPath.moveTo(ppp1.x, ppp1.y);
                    mPath.lineTo(ppp3.x, ppp3.y);
                    mPath.lineTo(ppp2.x, ppp2.y);
                }

                lastTailP = ppp2;
            }
        }

        //handle right
        TrapezoidPart rightPart = mParts.get(mParts.size() - 1);
        triangle = rightPart.getLeftTriangle();

        p1 = new Point(lastTailP.x + mParam.mSpace, lastTailP.y);
        p3 = new Point(p1.x + shortLen, p1.y);
        p2 = new Point(p3.x, top);
        triangle.setP2(p2);
        triangle.setP3(p3);
        triangle.setP1(p1);

        rightPart.getRect().set(p2.x, p2.y, p2.x + mParam.mTrapezoidSecondLength, rect_left_part.bottom);
        if (mDebug) {
            mRectF.set(rightPart.getRect());
            mPath.addRect(mRectF, Path.Direction.CW);
            mPath.moveTo(p3.x, p3.y);
            mPath.lineTo(p1.x, p1.y);
            mPath.lineTo(p2.x, p2.y);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        boolean result = mGesture.onTouchEvent(e);

        switch (e.getActionMasked()){
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                findFocusPart(e);
                if(mFocusPart != null){
                    mFocusPart.alpha = mTouchAlpha;
                    invalidate();
                }
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                if(mFocusPart != null){
                    mFocusPart.alpha = 255;
                    mFocusPart = null;
                    invalidate();
                }
                break;
        }
        return result;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Rect rect = DrawingUtils.measure(mPaint, "PROJECT");
        if(Predicates.isEmpty(mParts)){
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }else {
            int height = mParam.mPartHeight + mParam.mTextMarginTop + rect.height() + getPaddingTop() + getPaddingBottom();
          /*  int width = size * mParam.mTrapezoidMaxLength - (size - 1) * (mParam.mShortLength - mParam.mSpace)
                    + getPaddingLeft() + getPaddingRight();*/
            super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY));
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (Predicates.isEmpty(mParts)) {
            return;
        }
        if (mDebug) {
            mPaint.setColor(Color.RED);
            mPaint.setStyle(Paint.Style.STROKE);
            canvas.drawPath(mPath, mPaint);
        } else {
            mParam.top = getPaddingTop();
            mParam.bottom = getHeight() - getPaddingBottom();
            //draw left
            TrapezoidPart leftPart = mParts.get(0);
            leftPart.draw(canvas, mPaint, mRect, mRectF, mParam);

            TrapezoidPart rightPart = mParts.get(mParts.size() - 1);
            rightPart.draw(canvas, mPaint, mRect, mRectF, mParam);
            if(mParts.size() > 2){
                for (int i = 1, size = mParts.size() - 1; i < size ; i ++){
                    TrapezoidPart part = mParts.get(i);
                    part.draw(canvas, mPaint, mRect, mRectF, mParam);
                }
            }
        }
    }
    private class GestureListener extends GestureDetector.SimpleOnGestureListener{

        @Override
        public boolean onDown(MotionEvent e) {
            return !Predicates.isEmpty(mParts);
        }
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            if(mFocusPart != null){
                if(mListener != null){
                    mListener.onClickTrapezoidPart(TrapezoidPartsView.this, mFocusPart);
                }
                return true;
            }
            return false;
        }
    }

    private void findFocusPart(MotionEvent e) {
        int x = (int) e.getX();
        int y = (int) e.getY();
        for (TrapezoidPart part : mParts){
            if(mRectShape.isPointIn(part.rect, x, y)){
                mFocusPart = part;
                break;
            }else if(part.leftRange != null && mTriangleShape.isPointIn(part.leftRange, x, y)){
                mFocusPart = part;
                break;
            }else if(part.rightRange != null && mTriangleShape.isPointIn(part.rightRange, x, y)){
                mFocusPart = part;
                break;
            }
        }
    }
    private static class Param {
        int top;
        int bottom;
        int mTrapezoidMaxLength;    // max length of trapezoid

        int mTrapezoidSecondLength; //second max length of trapezoid
        int mShortLength;           // short length of Triangle
        int mPartHeight;            // the height of bg image

        int mSpace = 40;
        int mTextMarginTop = 40;
    }

    /**
     * the click listener of TrapezoidPart
     * @author heaven7
     */
    public interface OnTrapezoidPartClickListener {

        /**
         * called on click TrapezoidPart
         * @param view the view
         * @param part the TrapezoidPart
         */
        void onClickTrapezoidPart(TrapezoidPartsView view, TrapezoidPart part);
    }

    /**
     * the trapezoid part
     * @author heaven7
     */
    public static class TrapezoidPart {
        int alpha = 255;
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

        /*public*/ void drawBackground(Canvas canvas, Paint paint, Rect mRect, Param param) {
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
            canvas.drawBitmap(bgIcon, null, mRect, paint);
        }

        /*public*/ void drawIcon(Canvas canvas, Rect mRect, Param param) {
            mRect.set(rect);
            if(leftRange == null){
                mRect.right += param.mShortLength;
            }else if(rightRange == null){
                mRect.left -= param.mShortLength;
            }
            icon.setAlpha(this.alpha);
            DrawingUtils.drawCenter(canvas, mRect, icon);
        }

        /*public*/ void drawText(Canvas canvas, Paint textPaint, Rect mRect, RectF mRectF, Param param) {
            Rect textRange = DrawingUtils.measure(textPaint, text);
            mRect.left = rect.left;
            mRect.right = rect.right;
            mRect.top = rect.bottom + param.mTextMarginTop;
            mRect.bottom = mRect.top + textRange.height();
            if(leftRange == null){
                mRect.right += param.mShortLength;
            }else if(rightRange == null){
                mRect.left -= param.mShortLength;
            }
            DrawingUtils.computeTextDrawingCoordinate(text, textPaint, mRect, mRectF);
            canvas.drawText(text, mRectF.left, mRectF.top - textPaint.ascent(), textPaint);
        }

        /*public*/ void draw(Canvas canvas, Paint textPaint, Rect mRect, RectF mRectF, Param param) {
            textPaint.setAlpha(this.alpha);
            drawBackground(canvas, textPaint, mRect, param);
            drawIcon(canvas, mRect, param);
            drawText(canvas, textPaint, mRect, mRectF, param);
        }
    }
}
