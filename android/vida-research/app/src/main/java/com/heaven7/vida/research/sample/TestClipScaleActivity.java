package com.heaven7.vida.research.sample;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;

import com.heaven7.vida.research.BaseActivity;
import com.heaven7.vida.research.R;
import com.heaven7.vida.research.utils.DrawableUtils2;

import butterknife.BindView;

/**
 * Created by heaven7 on 2019/1/19.
 */
public class TestClipScaleActivity extends BaseActivity {

    @BindView(R.id.iv)
    ImageView mIv;

    @BindView(R.id.iv2)
    ImageView mIv2;

    @BindView(R.id.iv3)
    ImageView mIv3;

    @Override
    protected int getLayoutId() {
        return R.layout.ac_test_clip_scale;
    }

    @Override
    protected void onInitialize(Context context, Bundle savedInstanceState) {
        BitmapDrawable drawable = (BitmapDrawable) getResources().getDrawable(R.drawable.test_clip);
        Bitmap bitmap = DrawableUtils2.clipBitmap(drawable.getBitmap(), 300, 300);
        Bitmap bitmap2 = DrawableUtils2.clipBitmap2(drawable.getBitmap(), 300, 300);
        mIv.setImageBitmap(bitmap);
        mIv2.setImageBitmap(bitmap2);
        mIv3.setImageBitmap(drawable.getBitmap());
    }
}
