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
import com.heaven7.vida.research.utils.LinearEquation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.BaseStream;

/**
 * 圆盘view
 * Created by heaven7 on 2018/4/21 0021.
 */

public class DiscView extends View {

    private static final String TAG = "DiscView";

    public static final int FLAG_LEFT = 0x0001;
    public static final int FLAG_TOP = 0x0002;
    public static final int FLAG_RIGHT = 0x0004;
    public static final int FLAG_BOTTOM = 0x0008;
    public static final int FLAG_ALL = FLAG_LEFT | FLAG_TOP | FLAG_RIGHT | FLAG_BOTTOM;

    private static final boolean DEBUG = true;
    private static final float CRITICAL_DEGREE = 60f;
    private static final float DEFAULT_START_ANGLE = -180f;

    private final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final RectF mRectF = new RectF();
    private final Path mPath = new Path();
    private final int mCircleColor;

  //  private final ShadowHelper mShadowHelper = new ShadowHelper();

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
            // mShadowHelper.init(arr);
        } finally {
            arr.recycle();
        }

        mCircleCenter.set(mScreenWidth / 2, mDiscSize / 2);
        mPaint.setStyle(Paint.Style.STROKE);
        //shadow
        // mShadowHelper.setUpShadowPaint();

        //init touch
        GestureListenerImpl gestureListener = new GestureListenerImpl();
        mGestureDetector = new GestureDetector(context, gestureListener);
    }

    public void setDiscs(List<Disc> discs) {
        this.mDiscs = discs;
        requestLayout();
    }

    private void preProcessVisibleItems(List<Disc> discs) {
        for (Disc disc : discs) {
            if (disc.loop) {
                //compute the all degree to decide which will be visible.
                recomputeVisibleItems(disc, true);
                //if all item is visible . we just mark the loop to false.
                if (disc.visibleItems.size() == disc.items.size()) {
                    disc.loop = false;
                }
            }
        }
    }

    private void recomputeVisibleItems(Disc disc, boolean first) {
        disc.visibleItems.clear();
        if (first) {
            disc.startAngle = mStartVisibleAngle;
        }
        final float wholeStartAngle = disc.startAngle % 360;
        final List<Item> items = disc.items;
        final int size = items.size();
        //if rotated. we
        final float minAngle = mStartVisibleAngle;
        final float maxCircleAngle = 360f - Math.abs(minAngle);
        final float mStartVisibleAngle = this.mStartVisibleAngle;
        final float mEndVisibleAngle = this.mEndVisibleAngle;

        float lastAngle = wholeStartAngle;
        float startAngel, endAngel;
        int minVisibleIndex = -1;
        int maxVisibleIndex = -1;

        //startVisibleAngle ~ endVisibleAngle
        for (int i = 0; i < size; i++) {
            Item item = items.get(i);
            if (item.degree == 0f) {
                item.degree = mProvider.provideDegree(item, mPaint);
            }
            startAngel = lastAngle;
            endAngel = lastAngle + item.degree;
            lastAngle = endAngel;

            if (mCircleMode) {
                if (endAngel >= maxCircleAngle) {
                    //out of range
                    break;
                } else {
                    item.setAngles(startAngel, endAngel);
                    item.setNormal(true);
                    disc.visibleItems.add(item);
                    if (minVisibleIndex == -1) {
                        minVisibleIndex = i;
                    }
                    maxVisibleIndex = i;
                }
            } else {
                //half
                if (endAngel <= mStartVisibleAngle) {
                    //
                } else if (startAngel >= mEndVisibleAngle) {
                    break;
                } else {
                    item.setNormal(true);
                    disc.visibleItems.add(item);
                    if (minVisibleIndex == -1) {
                        minVisibleIndex = i;
                    }
                    maxVisibleIndex = i;
                }
            }
        }

        Logger.d(TAG, "recomputeVisibleItems", "minVisibleIndex = "
                + minVisibleIndex + " , maxVisibleIndex = " + maxVisibleIndex + " ,wholeStartAngle = " + wholeStartAngle);
        //前补,  -180 ~ mStartVisibleAngle . 后面的item补前面。
        float preAngle = wholeStartAngle;//-170
        float minAngleForSupplier = Math.abs(lastAngle) - 360f;
        //TODO 反接有bug
        if (preAngle > minAngleForSupplier) {
            if (mCircleMode) {
                for (int i = size - 1; i > maxVisibleIndex; i--) {
                    //desc. check can visible or not.
                    Item item = items.get(i);

                    if (item.degree == 0f) {
                        item.degree = mProvider.provideDegree(item, mPaint);
                    }
                    startAngel = preAngle - item.degree;
                    endAngel = preAngle;

                    if (startAngel >= minAngleForSupplier) {
                        item.setAngles(startAngel, endAngel);
                        item.setNormal(false);
                        disc.visibleItems.add(0, item);
                    } else {
                        break;
                    }
                    preAngle = startAngel;
                }
            } else {
                //旋转后的角度 > 可见角度
                if (wholeStartAngle > mStartVisibleAngle) {
                    for (int i = size - 1; i > maxVisibleIndex; i--) {
                        //desc. check can visible or not.
                        Item item = items.get(i);

                        if (item.degree == 0f) {
                            item.degree = mProvider.provideDegree(item, mPaint);
                        }
                        startAngel = preAngle - item.degree;
                        endAngel = preAngle;
                        Logger.v(TAG, "recomputeVisibleItems",
                                String.format(item.rows.get(0).text + " <<< (mStartVisibleAngle, wholeStartAngle) is (%.6f, %.6f), startAngel = "
                                        + startAngel + " ,endAngel = " + endAngel, mStartVisibleAngle, wholeStartAngle));

                        if (endAngel > mStartVisibleAngle && endAngel <= wholeStartAngle) {
                            if (startAngel < mEndVisibleAngle) {
                                item.setAngles(startAngel, endAngel);
                                item.setNormal(false);
                                disc.visibleItems.add(0, item);
                            } else {
                                Logger.v(TAG, "recomputeVisibleItems", " >>> not matched");
                            }
                        } else {
                            break;
                        }
                        preAngle = startAngel;
                    }
                }
            }
        }
        disc.startAngle = wholeStartAngle;
        disc.startDrawAngle = preAngle;
        Logger.d(TAG, "recomputeVisibleItems", "visible items: " + disc.visibleItems);
        Logger.d(TAG, "recomputeVisibleItems", "startAngle = " + wholeStartAngle
                + " ,startDrawAngle = " + disc.startDrawAngle);
    }

    private void computeStartAndEndAngle() {
        final int h = getHeight();
        //compute visible angle(start-end)
        int val = h - mCircleCenter.y;
        if (val > 0) {
            float degree = distanceToDegree(mCircleCenter.y, Math.abs(val));
            float leftOffsetDegree = 90f - degree;
            mStartVisibleAngle = DEFAULT_START_ANGLE - leftOffsetDegree;
            mEndVisibleAngle = leftOffsetDegree;
        } else if (val < 0) {
            float degree = distanceToDegree(mCircleCenter.y, Math.abs(val));
            float leftOffsetDegree = 90f - degree;
            mStartVisibleAngle = DEFAULT_START_ANGLE + leftOffsetDegree;
            mEndVisibleAngle = -leftOffsetDegree;
        } else {
            //=
            mStartVisibleAngle = DEFAULT_START_ANGLE;
            mEndVisibleAngle = 0f;
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
            // mShadowHelper.drawShadow(canvas, r);
            canvas.drawCircle(0, 0, r, mPaint);
            float startDegree = disc.startDrawAngle;
            if (disc.start_vOffset == -1) {
                disc.start_vOffset = mProvider.provideVOffset(disc, i, r);
            }

            final List<Item> items = disc.loop ? disc.visibleItems : disc.items;
            for (int i1 = 0, size1 = items.size(); i1 < size1; i1++) {
                Item item = items.get(i1);
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
        Logger.d(TAG, "onScroll", "--------------");
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
        if (dx < 0) {
            //to right. first + and -
            if (mLastX > mInitX && x < mInitX) {
                LinearEquation le = new LinearEquation(mLastX, mLastY, x, y);
                float tempY = le.getY(mInitX);

                float distance = computeDistance(mLastX, mLastY, mInitX, tempY);
                mFocusDisc.startAngle += distanceToDegree(distance);
                distance = computeDistance(mInitX, tempY, x, y);
                mFocusDisc.startAngle -= distanceToDegree(distance);
                mInitY = tempY;
                Logger.d(TAG, "onScroll", "to right >>> first + and -");
            } else {
                float distance = computeDistance(mLastX, mLastY, x, y);
                float degree = distanceToDegree(distance);
                //right of init position
                if (x > mInitX) {
                    mFocusDisc.startAngle += degree;
                } else {
                    mFocusDisc.startAngle -= degree;
                }
                Logger.d(TAG, "onScroll", "to right");
            }
        } else {
            //to left。first - and +
            if (mLastX < mInitX && x > mInitX) {
                LinearEquation le = new LinearEquation(mLastX, mLastY, x, y);
                float tempY = le.getY(mInitX);
                float distance = computeDistance(mLastX, mLastY, mInitX, tempY);
                mFocusDisc.startAngle -= distanceToDegree(distance);
                distance = computeDistance(mInitX, tempY, x, y);
                mFocusDisc.startAngle += distanceToDegree(distance);
                mInitY = tempY;
                Logger.d(TAG, "onScroll", "to left >>> first - and +");
            } else {
                float distance = computeDistance(mLastX, mLastY, x, y);
                float degree = distanceToDegree(distance);
                if (x < mInitX) {
                    mFocusDisc.startAngle -= degree;
                } else {
                    mFocusDisc.startAngle += degree;
                }
                Logger.d(TAG, "onScroll", "to left");
            }
        }
        //adjust visible items and start angle.
        if (mFocusDisc.loop) {
            recomputeVisibleItems(mFocusDisc, false);
        } else {
            mFocusDisc.startDrawAngle = mFocusDisc.startAngle;
        }
       /* float distance = computeDistance(mLastX, mLastY, e2.getX(), e2.getY());

        float degree = distanceToDegree(distance);
        if (dx < 0) {
            mFocusDisc.startAngle += degree;
        } else {
            mFocusDisc.startAngle -= degree;
        }*/
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

    private static class ShadowHelper {

        final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        int flags;
        float shadowRadius;
        float shadowDx;
        float shadowDy;
        int shadowColor;

        void init(TypedArray arr) {
            flags = arr.getInt(R.styleable.DiscView_disc_shadowFlags, FLAG_ALL);
            shadowRadius = arr.getDimension(R.styleable.DiscView_disc_shadowRadius, 0);
            shadowDx = arr.getDimension(R.styleable.DiscView_disc_shadowDx, 0);
            shadowDy = arr.getDimension(R.styleable.DiscView_disc_shadowDy, 0);
            shadowColor = arr.getColor(R.styleable.DiscView_disc_shadowColor, Color.BLACK);
        }

        void setUpShadowPaint() {
            mPaint.reset();
            mPaint.setAntiAlias(true);
            mPaint.setColor(Color.TRANSPARENT);
            mPaint.setShadowLayer(shadowRadius, shadowDx,
                    shadowDy, shadowColor);
        }

        void drawShadow(Canvas canvas, float circleRadius) {
            canvas.drawCircle(0, 0, circleRadius, mPaint);
        }

    }

    /**
     * indicate one disc.
     */
    public static class Disc {

        final List<Item> items = new ArrayList<>();
        /**
         * visible items for loop
         */
        final List<Item> visibleItems = new ArrayList<>();
        /** no visible items for loop */
        //final List<Item> noVisibleItems = new ArrayList<>();
        /**
         * the step between current and next disc
         */
        public int step;

        /**
         * when loop is true, no matter how many items. the all items will loop
         */
        public boolean loop;

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

        private boolean normal = true;

        //just used internal
        float endAngel;
        float startAngel;

        public void addRow(Row row) {
            rows.add(row);
        }

        @Override
        public String toString() {
            return "Item{" +
                    "text=" + rows.get(0).text +
                    ", degree=" + degree +
                    ", backup=" + !normal +
                    '}';
        }

        void setNormal(boolean normal) {
            this.normal = normal;
        }

        void setAngles(float startAngel, float endAngel) {
            this.startAngel = startAngel;
            this.endAngel = endAngel;
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
