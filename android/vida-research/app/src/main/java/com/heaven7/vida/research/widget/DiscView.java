package com.heaven7.vida.research.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.FloatProperty;
import android.util.Property;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;

import com.heaven7.core.util.Logger;
import com.heaven7.vida.research.R;
import com.heaven7.vida.research.utils.CycleDiscList;

import java.util.ArrayList;
import java.util.List;

/**
 * 圆盘view
 * Created by heaven7 on 2018/4/21 0021.
 */

public class DiscView extends View implements CycleDiscList.QueryCallback<DiscView.Item>{

    private static final String TAG = "DiscView";

    private static final float CRITICAL_DEGREE      = 60f;
    private static final float DRAW_DEGREE_OFFSET   = -180f;
    private static final float DEFAULT_START_ANGLE  = -180f - DRAW_DEGREE_OFFSET;
    private static final float DEFAULT_CENTER_ANGLE = DEFAULT_START_ANGLE + 90f;


    private static final Property<AnimateHelper, Float> sANIM_PROP = new FloatProperty<AnimateHelper>("disc") {
        @Override
        public Float get(AnimateHelper object) {
            return object.getAngle();
        }
        @Override
        public void setValue(AnimateHelper object, float value) {
            object.setAngle(value);
        }
    };
    private static final Property<AnimateHelper, Float> sANIM_PROP_CYCLE = new FloatProperty<AnimateHelper>("disc_cycle") {
        @Override
        public Float get(AnimateHelper object) {
            return object.getRotateAngle();
        }
        @Override
        public void setValue(AnimateHelper object, float value) {
            object.setRotateAngle(value);
        }
    };

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
    private DiscCallback mDiscCallback;
    private final List<Item> mTempList = new ArrayList<>();
    private final AnimateHelper mAnimHelper = new AnimateHelper();

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

    public void setDiscProvider(DiscProvider provider) {
        this.mProvider = provider;
    }
    public void setDiscCallback(DiscCallback callback) {
        this.mDiscCallback = callback;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (mDiscs != null) {
            if(mStartVisibleAngle == 0 && mEndVisibleAngle == 0) {
                computeStartAndEndAngle();
            }
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
                makeItemInCenter();
                resetTouch();
                break;

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

            float startDegree = disc.getStartDrawAngle() + DRAW_DEGREE_OFFSET;
            if (disc.start_vOffset == -1) {
                disc.start_vOffset = mProvider.provideVOffset(disc, i, r);
            }
            final List<Item> items = disc.loop ? disc.getDiscList().getLayoutItems(mTempList) : disc.items;
            Item selectItem = disc.selectItem;
            for (int i1 = 0, size1 = items.size(); i1 < size1; i1++) {
                Item item = items.get(i1);

                float degree = item.degree;
                float vOffset = disc.start_vOffset;
                for (int i2 = 0, size2 = item.rows.size(); i2 < size2; i2++) {
                    Row row = item.rows.get(i2);
                    mPaint.setColor(row.textColor == Color.TRANSPARENT ?
                            (item == selectItem ? disc.selectTextColor : disc.textColor) : row.textColor);
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

    private void makeItemInCenter() {
        if (mFocusDisc != null && mFocusDisc.makeItemInCenter(mDiscCallback)) {
            invalidate();
        }
    }

    private void resetTouch() {
        mFocusDisc = null;
        mLastX = -1;
        mLastY = -1;
        mInitX = -1;
        mInitY = -1;
    }

    protected boolean onSingleTapUp(MotionEvent e) {
        //先判断在第几个象限。计算距离-》角度->选择item
        if (mFocusDisc == null) {
            return false;
        }
        float x = e.getX();
        float y = e.getY();
        float radius = getRadius(mFocusDisc);
        float topCy = mCircleCenter.y - radius;
        //当前 只考虑 1 ,2 象限
        if (y <= mCircleCenter.y) {
            final float targetAngle;
            float degree = distanceToDegree(computeDistance(x, y, mCircleCenter.x, topCy));
            if (x <= mCircleCenter.x) {
                // 2 象限
                targetAngle = 90f - degree;
            } else {
                // 1 象限
                targetAngle = 90f + degree;
            }
            //find item
            final float centerAngle = DEFAULT_START_ANGLE + 90f;
            if(mFocusDisc.loop){
                return mFocusDisc.getDiscList().queryItem(centerAngle, targetAngle, this);
            }
            float startDegree = mFocusDisc.startAngle % 360;
            Item clickItem = null;
            float startAngle = 0f;

            List<Item> items = mFocusDisc.items;
            for (int i1 = 0, size1 = items.size(); i1 < size1; i1++) {
                Item item = items.get(i1);
                //contains . break
                if (targetAngle >= startDegree && targetAngle <= startDegree + item.degree) {
                    startAngle = startDegree;
                    clickItem = item;
                    break;
                }
                startDegree += item.degree;
            }
            if (clickItem != null) {
                float expectStartDegree = centerAngle - clickItem.getDegree() / 2;
                float delta = expectStartDegree - startAngle;
                if(delta != 0) {
                    mAnimHelper.start(mFocusDisc, clickItem, delta);
                }
                Logger.d(TAG, "onSingleTapUp", "delta = " + delta + " , click item = " + clickItem);
                return true;
            }
            return false;
        } else {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public void onQueryResult(Item item, float deltaAngle) {
        if(item != null && deltaAngle != 0) {
            mAnimHelper.startCycle(mFocusDisc, item, deltaAngle);
        }
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
        float distance = computeDistance(mLastX, mLastY, x, y);
        float degree = distanceToDegree(distance);

        final float deltaDegree;
        if (dx < 0) {
            deltaDegree = degree;
            mFocusDisc.startAngle += degree;
        } else {
            deltaDegree = -degree;
            mFocusDisc.startAngle -= degree;
        }
       /* if (dx < 0) {
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
        }*/
        //adjust visible items and start angle.
        if (mFocusDisc.loop) {
            Logger.d(TAG, "onScroll", "deltaDegree = " + deltaDegree);
            CycleDiscList<Item> discList = mFocusDisc.getDiscList();
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
    private float getRadius(Disc target) {
        int radius = mCircleCenter.y;
        for (Disc disc : mDiscs) {
            if (target == disc) {
                return radius;
            }
            radius -= disc.step;
        }
        return radius;
    }

    private void computeDegree(List<Disc> discs) {
        for (Disc disc : discs) {
            //decide: total degree >360 or not.
            disc.loop = disc.computeDegree(mProvider, mPaint) > 360;
        }
    }

    private void preProcessVisibleItems(List<Disc> discs) {
        for (Disc disc : discs) {
            if (disc.loop) {
                CycleDiscList<Item> discList = disc.getDiscList();
                discList.setVisibleAngles(mStartVisibleAngle, mEndVisibleAngle);
                discList.setItems(disc.items);
            }
            disc.makeItemInCenter(mDiscCallback);
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

    private boolean isAnimating(){
        return mAnimHelper != null && mAnimHelper.animating;
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

    private class AnimateHelper extends AnimatorListenerAdapter{

        private Disc disc;
        private Item focusItem;
        boolean animating;

        void start(Disc disc, Item focusItem, float deltaAngle){
            this.disc = disc;
            this.focusItem = focusItem;
            ObjectAnimator animator = ObjectAnimator.ofFloat(this, sANIM_PROP,
                    disc.startAngle, disc.startAngle + deltaAngle);
            animator.setInterpolator(new LinearInterpolator());
            animator.setDuration(250);
            animator.addListener(this);
            animator.start();
        }
        void startCycle(Disc disc, Item focusItem, float deltaAngle){
            if(!disc.loop){
                throw new IllegalStateException("the disc must be cycled.");
            }
            this.disc = disc;
            this.focusItem = focusItem;
            float startRotateDegree = disc.getDiscList().getRotatedDegree();
            ObjectAnimator animator = ObjectAnimator.ofFloat(this, sANIM_PROP_CYCLE,
                    startRotateDegree, startRotateDegree + deltaAngle);
            animator.setInterpolator(new LinearInterpolator());
            animator.setDuration(250);
            animator.addListener(this);
            animator.start();
        }

        void setAngle(float value) {
            disc.startDrawAngle = disc.startAngle = value;
            invalidate();
        }
        float getAngle() {
            return disc.startAngle;
        }
        void setRotateAngle(float value) {
            disc.getDiscList().rotateTo(value);
            invalidate();
        }
        Float getRotateAngle() {
            return disc.getDiscList().getRotatedDegree();
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            disc.selectItem = focusItem;
            animating = false;
            if (mDiscCallback != null) {
                mDiscCallback.onClickItem(mFocusDisc, focusItem);
            }
        }
        @Override
        public void onAnimationStart(Animator animation) {
            animating = true;
        }
        @Override
        public void onAnimationCancel(Animator animation) {
            animating = false;
        }

    }

    /**
     * the degree provider of item.
     *
     * @author heaven7
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
     *
     * @author heaven7
     */
    public static class Disc {

        private CycleDiscList<Item> mDiscList;
        final List<Item> items = new ArrayList<>();
        /**
         * the step between current and next disc
         */
        public int step;
        /**
         * the select text color
         */
        public int selectTextColor;

        /**
         * normal text color
         */
        public int textColor;
//--------------------------------------------------------------------------------
        /**
         * when loop is true, no matter how many items. the all items will loop
         */
        boolean loop;

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

        /**
         * the select item(as centered )
         */
        Item selectItem;

        public void addItem(Item item) {
            items.add(item);
        }

        /*public*/ float computeDegree(DiscProvider provider, Paint paint) {
            float total = 0f;
            for (Item item : items) {
                item.degree = provider.provideDegree(item, paint);
                total += item.degree;
            }
            return total;
        }

        /*public*/ CycleDiscList<Item> getDiscList() {
            if (mDiscList == null) {
                mDiscList = new CycleDiscList<>();
            }
            return mDiscList;
        }

        /*public*/ boolean makeItemInCenter(DiscCallback callback) {
            final Item oldItem = this.selectItem;
            final Item newItem ;
            if (loop) {
                newItem = mDiscList.makeItemInCenter(DEFAULT_CENTER_ANGLE);
                this.selectItem = newItem;
            } else {
                final List<Item> items = this.items;
                // 规范化start angle. (-360 ~ 360)
                float startDegree = startAngle % 360;

                float start = 0f;
                //距离中心点的最小距离
                final float centerAngle = DEFAULT_CENTER_ANGLE;
                float minDistance = Float.MAX_VALUE;
                float distanceStart, distanceEnd;
                Item nearItem = null;
                for (int i1 = 0, size1 = items.size(); i1 < size1; i1++) {
                    Item item = items.get(i1);
                    distanceStart = Math.abs(centerAngle - startDegree);
                    distanceEnd = Math.abs(centerAngle - (startDegree + item.degree));
                    float tmp = Math.min(distanceStart, distanceEnd);
                    //contains . break
                    if (centerAngle >= startDegree && centerAngle <= startDegree + item.degree) {
                        start = startDegree;
                        nearItem = item;
                        break;
                    } else if (tmp < minDistance) {
                        start = startDegree;
                        minDistance = tmp;
                        nearItem = item;
                    }
                    startDegree += item.degree;
                }
                if (nearItem != null) {
                    float expectStartDegree = centerAngle - nearItem.getDegree() / 2;
                    float delta = expectStartDegree - start;
                    this.startAngle += delta;
                    this.startDrawAngle = startAngle;
                    this.selectItem = nearItem;
                    Logger.d(TAG, "makeItemInCenter", "delta = " + delta + " ,selectItem = " + nearItem.getDefaultText());
                }
                newItem = nearItem;
            }
            if(callback != null){
                callback.onSelectItem(this, oldItem, newItem);
            }
            return newItem != null;
        }

        float getStartDrawAngle() {
            return loop ? getDiscList().getStartDrawAngle() : startDrawAngle;
        }
    }

    /**
     * one item indicate sector area. which may contains multi rows.
     *
     * @author heaven7
     */
    public static class Item extends CycleDiscList.BaseDiscItem {

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
        public String toString() {
            return getDefaultText() + " , start = " + getStartAngle() + " ,end = " + getEndAngle();
        }
    }

    /**
     * indicate a row of sector area.
     *
     * @author heaven7
     */
    public static class Row {
        public String text;
        public float textSize;
        public int textColor = Color.TRANSPARENT;
    }

    /**
     * the callback of whole disc view.
     *
     * @author heaven7
     */
    public interface DiscCallback {
        /**
         * called on select item, often from scroll .
         *
         * @param disc    the disc
         * @param oldItem the old item from disc.
         * @param newItem the new item from disc.
         */
        void onSelectItem(Disc disc, Item oldItem, Item newItem);

        /**
         * called on click item
         *
         * @param disc the disc
         * @param item the item from disc
         */
        void onClickItem(Disc disc, Item item);
    }
}
