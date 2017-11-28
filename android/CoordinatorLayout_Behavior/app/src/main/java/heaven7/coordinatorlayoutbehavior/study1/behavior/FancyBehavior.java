package heaven7.coordinatorlayoutbehavior.study1.behavior;

import android.content.Context;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.WindowInsetsCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * study behavior: like SwipeDismissBehavior
 * 拦截Window Insets

 * Created by heaven7 on 2017/11/28 0028.
 */

public class FancyBehavior<V extends View> extends CoordinatorLayout.Behavior<V>{
    public FancyBehavior() {
    }
    public FancyBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
        // Extract any custom attributes out
        // preferably prefixed with behavior_ to denote they
        // belong to a behavior
    }

    @Override
    public Parcelable onSaveInstanceState(CoordinatorLayout parent, V child) {
        return super.onSaveInstanceState(parent, child);
    }

    @Override
    public void onRestoreInstanceState(CoordinatorLayout parent, V child, Parcelable state) {
        super.onRestoreInstanceState(parent, child, state);
    }

    @Override
    public boolean onInterceptTouchEvent(CoordinatorLayout parent, V child, MotionEvent ev) {
        return super.onInterceptTouchEvent(parent, child, ev);
    }
    @Override
    public boolean onTouchEvent(CoordinatorLayout parent, V child, MotionEvent ev) {
        return super.onTouchEvent(parent, child, ev);
    }

    @Override
    public boolean onMeasureChild(CoordinatorLayout parent, V child, int parentWidthMeasureSpec, int widthUsed, int parentHeightMeasureSpec, int heightUsed) {
        return super.onMeasureChild(parent, child, parentWidthMeasureSpec, widthUsed, parentHeightMeasureSpec, heightUsed);
    }
    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, V child, int layoutDirection) {
        return super.onLayoutChild(parent, child, layoutDirection);
    }

    @NonNull
    @Override
    public WindowInsetsCompat onApplyWindowInsets(CoordinatorLayout coordinatorLayout, V child, WindowInsetsCompat insets) {
        /**
         * 假设你读了Why would I want to fitsSystemWindows? blog。那里深入讨论了fitsSystemWindows到底干什么的，
         但是它归纳为：window insets 需要避免在 system windows（比如status bar 和 navigation bar）的下面绘制。
         Behaviors在这里也有拦截的机会 － 如果你的View是fitsSystemWindows=“true”的，
         那么任何依附着的Behavior都将得到onApplyWindowInsets()调用，且优先级高于View自身。
         */
        return super.onApplyWindowInsets(coordinatorLayout, child, insets);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, V child, View dependency) {
        return super.layoutDependsOn(parent, child, dependency);
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, V child, View dependency) {
        return super.onDependentViewChanged(parent, child, dependency);
    }

    @Override
    public void onDependentViewRemoved(CoordinatorLayout parent, V child, View dependency) {
        super.onDependentViewRemoved(parent, child, dependency);
    }
    @Override
    public void onDetachedFromLayoutParams() {
        super.onDetachedFromLayoutParams();
    }
}

