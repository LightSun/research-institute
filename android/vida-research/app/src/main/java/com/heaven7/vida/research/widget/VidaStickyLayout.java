package com.heaven7.vida.research.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingChildHelper;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.NestedScrollingParentHelper;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.OverScroller;

import com.heaven7.android.scroll.IScrollHelper;
import com.heaven7.android.scroll.NestedScrollFactory;
import com.heaven7.android.scroll.NestedScrollHelper;
import com.heaven7.vida.research.R;

import java.util.Arrays;

/**
 * Created by heaven7 on 2018/9/28 0028.
 */
public class VidaStickyLayout extends LinearLayout implements NestedScrollingChild, NestedScrollingParent {

    /** indicate that can process nest-scroll self. */
    public static final int FLAG_SELF    = 1;
    /** indicate that can process nest-scroll by parent.*/
    public static final int FLAG_PARENT  = 2;

    private final NestedScrollingParentHelper mNestedScrollingParentHelper;
    private final NestedScrollingChildHelper mNestedScrollingChildHelper;
    private final NestedScrollHelper mNestedHelper;

    private final int[] mParentScrollConsumed = new int[2];
    private final int[] mParentOffsetInWindow = new int[2];

    private int mMaxScrollY;
    private boolean mEnableStickyTouch = true;
    private boolean mKeepVisibleHeight = true;
    private boolean mAutoFitScroll;
    private float mAutoFitPercent = 0.6f;

    private final OverScroller mScroller;
    private int mInitHeight; // the init height of this view. used for keep visible height.

    private Callback mCallback;

    public VidaStickyLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public VidaStickyLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.VidaStickyLayout);
        mMaxScrollY = a.getDimensionPixelSize(R.styleable.VidaStickyLayout_vsl_max_scroll_height, 0);
        mAutoFitScroll = a.getBoolean(R.styleable.VidaStickyLayout_vsl_auto_fit_enabled, false);
        mKeepVisibleHeight = a.getBoolean(R.styleable.VidaStickyLayout_vsl_keep_visible_height, false);
        mAutoFitPercent = a.getFloat(R.styleable.VidaStickyLayout_vsl_auto_fit_percent, mAutoFitPercent);
        a.recycle();

        mScroller = new OverScroller(context);
        mNestedScrollingParentHelper = new NestedScrollingParentHelper(this);
        mNestedScrollingChildHelper = new NestedScrollingChildHelper(this);
        mNestedHelper = NestedScrollFactory.create(this, mScroller, new NestedScrollHelper.NestedScrollCallback() {
            @Override
            public boolean canScrollHorizontally(View target) {
                return false;
            }
            @Override
            public boolean canScrollVertically(View target) {
                return true;
            }
            @Override
            public int getMaximumYScrollDistance(View target) {
                return mMaxScrollY;
            }
        });

        getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                getViewTreeObserver().removeOnPreDrawListener(this);
                mInitHeight = getMeasuredHeight();
                return true;
            }
        });
        setNestedScrollingEnabled(true);
    }
    public void setCallback(Callback mCallback) {
        this.mCallback = mCallback;
    }

    public boolean isEnableStickyTouch() {
        return mEnableStickyTouch;
    }
    public void setEnableStickyTouch(boolean mEnableStickyTouch) {
        this.mEnableStickyTouch = mEnableStickyTouch;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(!mEnableStickyTouch){
            return super.onTouchEvent(event);
        }
        return mNestedHelper.onTouchEvent(event);
    }


    /**
     * Begin a standard fling with an initial velocity along each axis in pixels per second.
     * If the velocity given is below the system-defined minimum this method will return false
     * and no fling will occur.
     *
     * @param velocityX Initial horizontal velocity in pixels per second
     * @param velocityY Initial vertical velocity in pixels per second
     * @return true if the fling was started, false if the velocity was too low to fling or
     *  does not support scrolling in the axis fling is issued.
     */
    public boolean fling(int velocityX, int velocityY) {
        return mNestedHelper.fling(velocityX , velocityY);
    }

    /**
     * Like {@link #scrollTo}, but scroll smoothly instead of immediately.
     *
     * @param x the position where to scroll on the X axis
     * @param y the position where to scroll on the Y axis
     */
    public final void smoothScrollTo(int x, int y) {
        mNestedHelper.smoothScrollBy(x - getScrollX(), y - getScrollY());
    }

    /**
     * Like {@link View#scrollBy}, but scroll smoothly instead of immediately.
     *
     * @param dx the number of pixels to scroll by on the X axis
     * @param dy the number of pixels to scroll by on the Y axis
     */
    public final void smoothScrollBy(int dx, int dy) {
        mNestedHelper.smoothScrollBy(dx , dy);
    }


    public void addOnScrollChangeListener(IScrollHelper.OnScrollChangeListener l) {
        mNestedHelper.addOnScrollChangeListener(l);
    }

    public void removeOnScrollChangeListener(IScrollHelper.OnScrollChangeListener l) {
        mNestedHelper.removeOnScrollChangeListener(l);
    }
    @Override
    public void computeScroll() {
        mNestedHelper.computeScroll();
        if(!mScroller.computeScrollOffset() && mKeepVisibleHeight){
            final int expectHeight = mInitHeight + getScrollY();
            post(new Runnable() {
                @Override
                public void run() {
                    final ViewGroup.LayoutParams lp = getLayoutParams();
                    lp.height = expectHeight;
                    setLayoutParams(lp);
                }
            });
        }
    }


    //========================  NestedScrollingParent begin ========================
    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return isEnabled() && mEnableStickyTouch && (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    @Override
    public void onNestedScrollAccepted(View child, View target, int nestedScrollAxes) {
        // Reset the counter of how much leftover scroll needs to be consumed.
        mNestedScrollingParentHelper.onNestedScrollAccepted(child, target, nestedScrollAxes);
        // Dispatch up to the nested parent
        startNestedScroll(nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL);
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        int flags = FLAG_SELF | FLAG_PARENT;
        if(mCallback != null){
            flags = mCallback.getNestedScrollFlags(this, target, dx, dy);
        }
        //pre scroll if permit
        if((flags & FLAG_SELF) == FLAG_SELF) {
            mNestedHelper.nestedScroll(dx, dy, consumed, true);
        }

        // Now let our nested parent consume the leftovers
        if((flags & FLAG_PARENT) == FLAG_PARENT) {
            final int[] parentConsumed = mParentScrollConsumed;
            Arrays.fill(parentConsumed, 0);
            if (dispatchNestedPreScroll(dx - consumed[0], dy - consumed[1], parentConsumed, null)) {
                consumed[0] += parentConsumed[0];
                consumed[1] += parentConsumed[1];
            }
        }
    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed,
                               int dxUnconsumed, int dyUnconsumed) {
        // Dispatch up to the nested parent first
        dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed,
                mParentOffsetInWindow);
    }

    @Override
    public void onStopNestedScroll(View target) {
        mNestedScrollingParentHelper.onStopNestedScroll(target);
        checkAutoFitScroll();

        // Dispatch up our nested parent
        stopNestedScroll();
    }

    private void checkAutoFitScroll() {
        //check auto fit scroll
        if (mAutoFitScroll) {
            //check whole gesture.
            final float scrollY = getScrollY();
            if(scrollY >= mMaxScrollY * mAutoFitPercent){
                smoothScrollTo(0, mMaxScrollY);
            }else{
                smoothScrollTo(0, 0);
            }
        }
    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        return dispatchNestedFling(velocityX, velocityY, consumed);
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        return dispatchNestedPreFling(velocityX, velocityY);
    }

    @Override
    public int getNestedScrollAxes() {
        return mNestedScrollingParentHelper.getNestedScrollAxes();
    }
    //========================  NestedScrollingParent end ========================

    //========================  NestedScrollingChild begin ========================
    public void setNestedScrollingEnabled(boolean enabled) {
        mNestedScrollingChildHelper.setNestedScrollingEnabled(enabled);
    }

    public boolean isNestedScrollingEnabled() {
        return mNestedScrollingChildHelper.isNestedScrollingEnabled();
    }

    public boolean startNestedScroll(int axes) {
        return mNestedScrollingChildHelper.startNestedScroll(axes);
    }

    public void stopNestedScroll() {
        mNestedScrollingChildHelper.stopNestedScroll();
    }

    public boolean hasNestedScrollingParent() {
        return mNestedScrollingChildHelper.hasNestedScrollingParent();
    }

    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed,
                                        int dxUnconsumed, int dyUnconsumed, int[] offsetInWindow) {
        return mNestedScrollingChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed,
                dxUnconsumed, dyUnconsumed, offsetInWindow);
    }

    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow) {
        return mNestedScrollingChildHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
    }

    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
        return mNestedScrollingChildHelper.dispatchNestedFling(velocityX, velocityY, consumed);
    }

    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
        return mNestedScrollingChildHelper.dispatchNestedPreFling(velocityX, velocityY);
    }
    //======================== end NestedScrollingChild =====================

    public interface Callback{
        /**
         * get the next scroll flags ,which will decide the behaviour(nest-scroll) of VidaStickyLayout and it's parent.
         * @param vsl the layout
         * @param target the view to trigger this event
         * @param dx the delta x , gesture left dx > 0, gesture right dy <0 .
         * @param dy the delta y, gesture up dy >0 , gesture down dy < 0
         * @return the flags used to transport nested scroll
         */
        int getNestedScrollFlags(VidaStickyLayout vsl, View target, int dx, int dy);
    }


}
