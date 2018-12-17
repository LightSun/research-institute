package com.heaven7.vida.research.sample;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;

import com.heaven7.vida.research.BaseActivity;
import com.heaven7.vida.research.R;

import butterknife.BindView;

/**
 * Created by heaven7 on 2018/12/17 0017.
 */
public class TestScaleDrawableActivity extends BaseActivity {

    @BindView(R.id.iv)
    ImageView mIv;

    @Override
    protected int getLayoutId() {
        return R.layout.ac_test_scale_drawable;
    }

    @Override
    protected void onInitialize(Context context, Bundle savedInstanceState) {
        final Drawable scaleDrawable = mIv.getDrawable();

        ValueAnimator valueAnimator =  ValueAnimator.ofInt(1, 10000);
        valueAnimator.setDuration(5000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                scaleDrawable.setLevel((int)animation.getAnimatedValue());
            }
        });
        valueAnimator.start();
    }
}
