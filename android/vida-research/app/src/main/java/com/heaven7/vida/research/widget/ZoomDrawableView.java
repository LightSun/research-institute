package com.heaven7.vida.research.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.DrawableUtils;
import android.util.AttributeSet;
import android.view.View;

import com.heaven7.vida.research.R;
import com.heaven7.vida.research.utils.DrawableUtils2;

/**
 * Created by heaven7 on 2018/2/8 0008.
 */
public class ZoomDrawableView extends View{

    private Drawable mDrawable;

    public ZoomDrawableView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ZoomDrawableView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        Drawable drawable = context.getResources().getDrawable(R.drawable.ic_test);
        mDrawable = DrawableUtils2.zoomDrawable(context, drawable, 100, 100);
        mDrawable.setBounds(0, 0, 100, 100);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mDrawable.draw(canvas);
    }
}
