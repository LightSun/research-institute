package com.heaven7.vida.research.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ScrollView;

public class DampScrollView extends ScrollView {

    // y方向上当前触摸点的前一次记录位置
    private int previousY = 0;
    // y方向上的触摸点的起始记录位置
    private int startY = 0;
    // y方向上的触摸点当前记录位置
    private int currentY = 0;
    // y方向上两次移动间移动的相对距离
    private int deltaY = 0;

    // 第一个子视图
    private View childView;

    // 用于记录childView的初始位置
    private Rect topRect = new Rect();

    //水平移动搞定距离
    private float moveHeight;

    public DampScrollView(Context context) {
        super(context);
    }

    public DampScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DampScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() > 0) {
            childView = getChildAt(0);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (null == childView) {
            return super.dispatchTouchEvent(event);
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startY = (int) event.getY();
                previousY = startY;

                // 记录childView的初始位置
                topRect.set(childView.getLeft(), childView.getTop(),
                        childView.getRight(), childView.getBottom());
                moveHeight = 0;
                break;
            case MotionEvent.ACTION_MOVE:
                currentY = (int) event.getY();
                deltaY = currentY - previousY;
                previousY = currentY;

                //判定是否在顶部或者滑到了底部，计算是否到底部时，允许有10个像素误差
                if (0 == getScrollY()
                        || childView.getMeasuredHeight() <= getScrollY() + getHeight() + 10) {
                    //计算阻尼
                    float distance = currentY - startY;
                    if (distance < 0) {
                        distance *= -1;
                    }

                    float damping = 0.5f;//阻尼值
                    float height = getHeight();
                    if (height != 0) {
                        if (distance > height) {
                            damping = 0;
                        } else {
                            damping = (height - distance) / height;
                        }
                    }
                    if (currentY - startY < 0) {
                        damping = 1 - damping;
                    }

                    //阻力值限制再0.3-0.5之间，平滑过度
                    damping *= damping * 0.2;
                    damping += 0.3;

                    moveHeight = moveHeight + (deltaY * damping);

                    childView.layout(topRect.left, (int) (topRect.top + moveHeight), topRect.right,
                            (int) (topRect.bottom + moveHeight));
                }
                break;
            case MotionEvent.ACTION_UP:
                if (!topRect.isEmpty()) {
                    //开始回移动画
                    upDownMoveAnimation();
                    // 子控件回到初始位置
                    childView.layout(topRect.left, topRect.top, topRect.right,
                            topRect.bottom);
                }
                //重置一些参数
                startY = 0;
                currentY = 0;
                topRect.setEmpty();
                break;
        }

        return super.dispatchTouchEvent(event);
    }

    // 初始化上下回弹的动画效果
    private void upDownMoveAnimation() {
        TranslateAnimation animation = new TranslateAnimation(0.0f, 0.0f,
                childView.getTop(), topRect.top);
        animation.setDuration(180);
        animation.setFillAfter(true);
        animation.setInterpolator(new AccelerateInterpolator());
        childView.setAnimation(animation);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}