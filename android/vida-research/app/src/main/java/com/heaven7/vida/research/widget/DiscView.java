package com.heaven7.vida.research.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.heaven7.core.util.Logger;
import com.heaven7.vida.research.R;
import com.heaven7.vida.research.utils.DiscList;
import com.heaven7.vida.research.utils.LinearEquation;

import java.util.ArrayList;
import java.util.List;

/**
 * 圆盘view
 * Created by heaven7 on 2018/4/21 0021.
 */

public class DiscView extends View {

    private static final String TAG = "DiscView";

    private static final boolean DEBUG = true;
    private static final float CRITICAL_DEGREE = 60f;
    private static final float DRAW_DEGREE_OFFSET = -180f;
    private static final float DEFAULT_START_ANGLE = -180f - DRAW_DEGREE_OFFSET;

    private final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final RectF mRectF = new RectF();
    private final Path mPath = new Path();
    private final int mCircleColor;

    private final GestureDetector mGestureDetector;

    private int mScreenWidth;
    private int mScreenHeight;
    /**
     * the max disc size in pix(width = height), which is circle
     */
    private final int mDiscSize;

    private final int mRowDistance;

    /**
     * the discs from out to inner
     */
    private List<Disc> mDiscs;
    private DiscProvider mProvider;
    private final List<Item> mTempList = new ArrayList<>();

    //handle event
    private float mLastX = -1;
    private float mLastY = -1;
    /**
     * the init locus of start position x
     */
    private float mInitX = -1;
    /**
     * the init locus of start position y
     */
    private float mInitY = -1;

    private final Point mCircleCenter = new Point();
    private Disc mFocusDisc;

    private float mStartVisibleAngle;
    private float mEndVisibleAngle;

    /**
     * the loop mode: circle mode or height(view height) mode
     */
    private boolean mCircleMode = true;

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
            if (mDiscSize == 0 || mRowDistance == 0) {
                throw new IllegalStateException();
            }
            mCircleColor = arr.getColor(R.styleable.DiscView_disc_circle_color, Color.RED);
        } finally {
            arr.recycle();
        }

        mCircleCenter.set(mScreenWidth / 2, mDiscSize / 2);
        mPaint.setStyle(Paint.Style.STROKE);

        //init touch
        GestureListenerImpl gestureListener = new GestureListenerImpl();
        mGestureDetector = new GestureDetector(context, gestureListener);
    }

    public void setDiscs(List<Disc> discs) {
        this.mDiscs = discs;
        computeDegree(discs);
        requestLayout();
    }

    private void computeDegree(List<Disc> discs) {
        for(Disc disc : discs){
            //decide: total degree >360 or not.
            disc.loop = disc.computeDegree(mProvider, mPaint) > 360;
        }
    }

    private void preProcessVisibleItems(List<Disc> discs) {
        for (Disc disc : discs) {
            if (disc.loop) {
                DiscList<Item> discList = disc.getDiscList();
                discList.setVisibleAngles(mStartVisibleAngle, mEndVisibleAngle);
                discList.setItems(disc.items);
            }
        }
    }

    private void computeStartAndEndAngle() {
        final int h = getHeight();
        //compute visible angle(start-end)
        int val = h - mCircleCenter.y;
        if (val > 0) {
            float degree = distanceToDegree(mCircleCenter.y, Math.abs(val));
            float leftOffsetDegree = 90f - degree;
            mStartVisibleAngle = DEFAULT_START_ANGLE - leftOffsetDegree;
            mEndVisibleAngle = DEFAULT_START_ANGLE + 180f + leftOffsetDegree;
        } else if (val < 0) {
            float degree = distanceToDegree(mCircleCenter.y, Math.abs(val));
            float leftOffsetDegree = 90f - degree;
            mStartVisibleAngle = DEFAULT_START_ANGLE + leftOffsetDegree;
            mEndVisibleAngle = DEFAULT_START_ANGLE + 180f - leftOffsetDegree;
        } else {
            mStartVisibleAngle = DEFAULT_START_ANGLE;
            mEndVisibleAngle = DEFAULT_START_ANGLE + 180f;
        }
        Logger.d(TAG, "computeStartAndEndAngle", "mStartVisibleAngle = " + mStartVisibleAngle
                + ", mEndVisibleAngle = " + mEndVisibleAngle);
    }

    public void setDiscProvider(DiscProvider provider) {
        this.mProvider = provider;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (mDiscs != null && mStartVisibleAngle == 0 && mEndVisibleAngle == 0) {
            computeStartAndEndAngle();
            preProcessVisibleItems(mDiscs);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mDiscs == null || mDiscs.size() == 0) {
            return super.onTouchEvent(event);
        }
        boolean result = mGestureDetector.onTouchEvent(event);

        switch (event.getActionMasked()) {

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                resetTouch();
                break;
        }

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
        int r = mCircleCenter.y;
        canvas.translate(mCircleCenter.x, r);
        mRectF.set(-r, -r, r, r);
        //from out to inner
        for (int i = 0, size = mDiscs.size(); i < size; i++) {
            Disc disc = mDiscs.get(i);
            if (disc.radius == 0) {
                disc.radius = r;
            }

            mPaint.setColor(mCircleColor);
            canvas.drawCircle(0, 0, r, mPaint);

            float startDegree = disc.startDrawAngle + DRAW_DEGREE_OFFSET;
            if (disc.start_vOffset == -1) {
                disc.start_vOffset = mProvider.provideVOffset(disc, i, r);
            }
            final List<Item> items = disc.loop ? disc.getDiscList().getLayoutItems(mTempList) : disc.items;
            for (int i1 = 0, size1 = items.size(); i1 < size1; i1++) {
                Item item = items.get(i1);
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
            mTempList.clear();

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

    private void resetTouch() {
        mFocusDisc = null;
        mLastX = -1;
        mLastY = -1;
        mInitX = -1;
        mInitY = -1;
    }

    protected boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    //轮盘放不下item的问题？
    protected boolean onScroll(@NonNull MotionEvent e1, @NonNull MotionEvent e2,
                               float dx, float dy) {
       // Logger.d(TAG, "onScroll", "--------------");
        if (dx == 0) {
            return false;
        }
        if (mFocusDisc == null) {
            return false;
        }
        //dx < 0 右滑， dy < 0 下滑
        if (mLastX == -1) {
            mInitX = mLastX = e1.getX();
        }
        if (mLastY == -1) {
            mInitY = mLastY = e1.getY();
        }
        float x = e2.getX();
        float y = e2.getY();

        final float deltaDegree;
        if (dx < 0) {
            //to right. first + and -
            if (mLastX > mInitX && x < mInitX) {
                LinearEquation le = new LinearEquation(mLastX, mLastY, x, y);
                float tempY = le.getY(mInitX);

                float distance = computeDistance(mLastX, mLastY, mInitX, tempY);
                float degree1 = distanceToDegree(distance);
                mFocusDisc.startAngle += degree1;
                distance = computeDistance(mInitX, tempY, x, y);
                float degree2 = distanceToDegree(distance);
                mFocusDisc.startAngle -= degree2;
                mInitY = tempY;
                deltaDegree = degree1 - degree2;
               // Logger.d(TAG, "onScroll", "to right >>> first + and -");
            } else {
                float distance = computeDistance(mLastX, mLastY, x, y);
                float degree = distanceToDegree(distance);
                //right of init position
                if (x > mInitX) {
                    mFocusDisc.startAngle += degree;
                    deltaDegree = degree;
                } else {
                    mFocusDisc.startAngle -= degree;
                    deltaDegree = -degree;
                }
               // Logger.d(TAG, "onScroll", "to right");
            }
        } else {
            //to left。first - and +
            if (mLastX < mInitX && x > mInitX) {
                LinearEquation le = new LinearEquation(mLastX, mLastY, x, y);
                float tempY = le.getY(mInitX);
                float distance = computeDistance(mLastX, mLastY, mInitX, tempY);
                float degree1 = distanceToDegree(distance);
                mFocusDisc.startAngle -= degree1;
                distance = computeDistance(mInitX, tempY, x, y);
                float degree2 = distanceToDegree(distance);
                mFocusDisc.startAngle += degree2;
                mInitY = tempY;
                deltaDegree = degree2 - degree1;
               // Logger.d(TAG, "onScroll", "to left >>> first - and +");
            } else {
                float distance = computeDistance(mLastX, mLastY, x, y);
                float degree = distanceToDegree(distance);
                if (x < mInitX) {
                    mFocusDisc.startAngle -= degree;
                    deltaDegree = -degree;
                } else {
                    mFocusDisc.startAngle += degree;
                    deltaDegree = degree;
                }
               // Logger.d(TAG, "onScroll", "to left");
            }
        }
        //adjust visible items and start angle.
        if (mFocusDisc.loop) {
            Logger.d(TAG, "onScroll", "deltaDegree = " + deltaDegree);
            DiscList<Item> discList = mFocusDisc.getDiscList();
            discList.rotate(Math.abs(deltaDegree), deltaDegree > 0);
            mFocusDisc.startDrawAngle = discList.getStartDrawAngle();
        } else {
            mFocusDisc.startDrawAngle = mFocusDisc.startAngle;
        }
        mLastX = x;
        mLastY = y;
        postInvalidate();
        return true;
    }

    /**
     * compute distance
     */
    private float computeDistanceFromCircleCenter(float x, float y) {
        return computeDistance(x, y, mCircleCenter.x, mCircleCenter.y);
    }

    private static float computeDistance(float x, float y, float x2, float y2) {
        float dx = x2 - x;
        float dy = y2 - y;
        return (float) Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * according to sin and asin.(2 times)
     */
    private float distanceToDegree(float distance) {
        float sinVal = distance / 2 / mFocusDisc.radius;
        float asinVal = (float) Math.asin(sinVal);
        float degree = (float) (asinVal * 180 * 2 / Math.PI);
        return degree;
    }

    /**
     * according to cos and acos.
     */
    private static float distanceToDegree(float radius, float distance) {
        float cosVal = distance / radius;
        float acosVal = (float) Math.acos(cosVal);
        float degree = (float) (acosVal * 180 / Math.PI);
        return degree;
    }

    private class GestureListenerImpl implements GestureDetector.OnGestureListener {
        @Override
        public boolean onDown(MotionEvent e) {
            //Logger.i("GestureListenerImpl", "onDown", "" + e);
            float distance = computeDistanceFromCircleCenter(e.getX(), e.getY());
            for (int size = mDiscs.size(), i = size - 1; i >= 0; i--) {
                Disc disc = mDiscs.get(i);
                if (distance <= disc.radius) {
                    mFocusDisc = disc;
                    break;
                }
            }
            Logger.d(TAG, "onDown", "has mFocusDisc = " + (mFocusDisc != null));
            return mFocusDisc != null;
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
            Logger.d(TAG, "onFling", "");
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
         *
         * @param disc   the disc
         * @param index  the index of disc. start from out to inner
         * @param radius the radius of circle
         * @return the vOffset
         */
        float provideVOffset(Disc disc, int index, float radius);
    }

    /**
     * indicate one disc.
     */
    public static class Disc {

        private DiscList<Item> mDiscList;
        final List<Item> items = new ArrayList<>();

        /**
         * when loop is true, no matter how many items. the all items will loop
         */
        boolean loop;

        /**
         * the step between current and next disc
         */
        public int step;
        /**
         * the select text color
         */
        public int selectTextColor;

        public int textColor;
        /**
         * the start vOffset
         */
        float start_vOffset = -1;

        /**
         * the start angle of first item
         */
        float startAngle = DEFAULT_START_ANGLE;

        /**
         * the start angle of draw.
         */
        float startDrawAngle = DEFAULT_START_ANGLE;
        /**
         * the r of circle
         */
        float radius;

        public void addItem(Item item) {
            items.add(item);
        }

        /*public*/ float computeDegree(DiscProvider provider, Paint paint) {
            float total = 0f;
            for(Item item : items){
                item.degree = provider.provideDegree(item, paint);
                total += item.degree;
            }
            return total;
        }
        /*public*/ DiscList<Item> getDiscList() {
            if(mDiscList == null){
                mDiscList = new DiscList<>();
            }
            return mDiscList;
        }
    }

    /**
     * one item indicate sector area. which may contains multi rows.
     */
    public static class Item extends DiscList.BaseDiscItem{

        public final List<Row> rows = new ArrayList<>();
        /**
         * the degree of this
         */
        float degree;

        public void addRow(Row row) {
            rows.add(row);
        }

        String getDefaultText() {
            return rows.get(0).text;
        }
        @Override
        public float getDegree() {
            return degree;
        }
        @Override
        public String getLogText() {
            return getDefaultText() + " ," + toString();
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
