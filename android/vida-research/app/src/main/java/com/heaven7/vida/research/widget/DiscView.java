package com.heaven7.vida.research.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.WindowManager;

import com.heaven7.vida.research.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 圆盘view
 * Created by heaven7 on 2018/4/21 0021.
 */

public class DiscView extends View {

    private static final float CRITICAL_DEGREE = 60f;
    private final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final RectF mRectF = new RectF();
    private final Path mPath = new Path();
    private final int mCircleColor;

    private final GestureDetector mGestureDetector;

    private int mScreenWidth;
    private int mScreenHeight;
    /**
     * the max disc size in pix
     */
    private final int mDiscSize;

    private final int mRowDistance;
    /**
     * the whole offset Y
     */
    private final int mOffsetY;

    private List<Disc> mDiscs;
    private DiscProvider mProvider;

    //handle event
    private float mLastX = -1;
    private float mLastY = -1;
    private Disc mFocusDisc;

    public DiscView(Context context) {
        this(context, null, 0);
    }

    public DiscView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DiscView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (wm != null) {
            wm.getDefaultDisplay().getMetrics(dm);
            mScreenWidth = dm.widthPixels;
            mScreenHeight = dm.heightPixels;
        }

        TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.DiscView, defStyleAttr, 0);
        try {
            mDiscSize = arr.getDimensionPixelSize(R.styleable.DiscView_disc_max_width_height, 0);
            mRowDistance = arr.getDimensionPixelSize(R.styleable.DiscView_disc_row_distance, 0);
            mOffsetY = arr.getDimensionPixelSize(R.styleable.DiscView_disc_whole_offset_y, 0);
            if (mDiscSize == 0 || mRowDistance == 0) {
                throw new IllegalStateException();
            }
            mCircleColor = arr.getColor(R.styleable.DiscView_disc_circle_color, Color.RED);
        } finally {
            arr.recycle();
        }

        mPaint.setStyle(Paint.Style.STROKE);

        //init touch
        GestureListenerImpl gestureListener = new GestureListenerImpl();
        mGestureDetector = new GestureDetector(context, gestureListener);
    }

    public void setDiscs(List<Disc> discs) {
        this.mDiscs = discs;
        invalidate();
    }

    public void setDiscProvider(DiscProvider provider) {
        this.mProvider = provider;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean result = mGestureDetector.onTouchEvent(event);

        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mScreenWidth == 0 || mScreenHeight == 0) {
            return;
        }
        if (mDiscs == null || mDiscs.size() == 0) {
            return;
        }
        //注意空白， 放不下。滑动等问题
        int r = mDiscSize / 2;
        canvas.translate(mScreenWidth / 2, r);
        mRectF.set(-r, -r, r, r);
        //from out to inner
        for (int i = 0, size = mDiscs.size(); i < size; i++) {
            Disc disc = mDiscs.get(i);
            if(disc.radius == 0){
                disc.radius = r;
            }

            mPaint.setColor(mCircleColor);
            canvas.drawCircle(0, 0, r, mPaint);
            float startDegree = disc.startAngle;
            if(disc.start_vOffset == -1){
                disc.start_vOffset = mProvider.provideVOffset(disc,i, r);
            }
            for (int i1 = 0, size1 = disc.items.size(); i1 < size1; i1++) {
                Item item = disc.items.get(i1);
                if (item.degree == 0) {
                    item.degree = mProvider.provideDegree(item, mPaint);
                }
                float degree = item.degree;
                float vOffset = disc.start_vOffset;
                for (int i2 = 0, size2 = item.rows.size(); i2 < size2; i2++) {
                    Row row = item.rows.get(i2);
                    mPaint.setColor(row.textColor);
                    mPaint.setTextSize(row.textSize);

                    drawText(canvas, row.text, r, startDegree, degree, vOffset, mRectF, mPaint);
                    vOffset += mRowDistance;
                }
                startDegree += degree;
            }

            r -= disc.step;
            if (r <= 0) {
                throw new IllegalStateException("r must > 0 .");
            }
            mRectF.set(-r, -r, r, r);
        }
    }

    /**
     * draw text
     *
     * @param canvas
     * @param text
     * @param radius
     * @param vOffset    the vertical offset
     * @param startAngle start angle
     * @param mAngle     the degree of sector
     * @param rect       area of circle
     * @param paint      paint.
     */
    private void drawText(Canvas canvas, String text, float radius,
                          float startAngle, float mAngle, float vOffset,
                          RectF rect, Paint paint) {
        //have a bug when degree is over a limit
        if (mAngle > CRITICAL_DEGREE) {
            startAngle += (mAngle - CRITICAL_DEGREE) / 2;
            mAngle = CRITICAL_DEGREE;
        }
        mPath.reset();
        mPath.addArc(rect, startAngle, mAngle);

        float textWidth = paint.measureText(text);
        float hOffset = (float) (Math.sin(mAngle / 2 / 180 * Math.PI) * radius) - textWidth / 2;
        canvas.drawTextOnPath(text, mPath, hOffset, vOffset, paint);
    }

    protected boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    protected boolean onScroll(@NonNull MotionEvent e1, @NonNull MotionEvent e2,
                                float dx, float dy) {
        if(dx == 0){
            return false;
        }
        if(mFocusDisc == null){
            return false;
        }
        //dx < 0 右滑， dy < 0 下滑
        if(mLastX == -1){
            mLastX = e1.getX();
        }
        if(mLastY == -1){
            mLastY = e1.getY();
        }
        float x = e2.getX();
        float y = e2.getY();
        //handle radian
        float distance = Math.abs(x - mLastX);
        float sinVal = distance/ 2 / mFocusDisc.radius;
        float asinVal = (float) Math.asin(sinVal);
        float degree = (float) (asinVal * 180 * 2 / Math.PI);
        if(dx < 0){
            mFocusDisc.startAngle += degree;
        }else{
            mFocusDisc.startAngle -= degree;
        }
        invalidate();
        mLastX = x;
        mLastY = y;
        return false;
    }

    private class GestureListenerImpl implements GestureDetector.OnGestureListener{
        @Override
        public boolean onDown(MotionEvent e) {
            //Logger.i("GestureListenerImpl", "onDown", "" + e);
            return true;
        }

        @Override
        public void onShowPress(MotionEvent e) {

        }
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return DiscView.this.onSingleTapUp(e);
        }
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return DiscView.this.onScroll(e1, e2, distanceX, distanceY);
        }

        @Override
        public void onLongPress(MotionEvent e) {
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return true;
        }
    }

    /**
     * the degree provider of item.
     */
    public interface DiscProvider {
        float provideDegree(Item item, Paint p);

        /**
         * provide vOffset which will used to draw text
         * @param disc the disc
         * @param index the index of disc. start from out to inner
         * @param radius the radius of circle
         * @return the vOffset
         */
        float provideVOffset(Disc disc, int index, float radius);
    }

    /**
     * indicate one disc.
     */
    public static class Disc {
        public final List<Item> items = new ArrayList<>();
        /**
         * the step between current and next disc
         */
        public int step;

        /** the select text color */
        public int selectTextColor;
        /**
         * the start vOffset
         */
        float start_vOffset = -1;

        /**
         * the start angle
         */
        float startAngle = -180f;
        /** the r of circle */
        float radius;

        public void addItem(Item item) {
            items.add(item);
        }
    }

    /**
     * one item indicate sector area. which may contains multi rows.
     */
    public static class Item {
        public final List<Row> rows = new ArrayList<>();
        /**
         * the degree of this
         */
        float degree;

        public void addRow(Row row) {
            rows.add(row);
        }
    }

    /**
     * indicate a row of sector area.
     */
    public static class Row {
        public String text;
        public float textSize;
        public int textColor;
    }
}
