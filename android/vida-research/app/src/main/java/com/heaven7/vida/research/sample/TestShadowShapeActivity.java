package com.heaven7.vida.research.sample;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.graphics.drawable.shapes.Shape;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import com.heaven7.vida.research.R;
import com.heaven7.vida.research.drawable.ShadowShape;
import com.heaven7.vida.research.utils.CommonUtils;
import com.heaven7.vida.research.utils.DimenUtil;
import com.heaven7.vida.research.utils.DrawableUtils2;

/**
 * 给任意的drawable加阴影
 * Created by heaven7 on 2018/6/2 0002.
 */
public class TestShadowShapeActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.ac_test_shadow);
        //View v = new View(this);
        View v = findViewById(R.id.view);

        //shadow1(v);
        shadow2(v);
    }

    private void shadow2(View v) {
        int wh = DimenUtil.dip2px(this, 200);
        int value = DimenUtil.dip2px(this, 100);
        Rect rect = new Rect(10, 10, wh - 10, wh - 10);

        float innerRaius = 15;
        RoundRectShape shape = new RoundRectShape(new float[]{
                value, value,value,value,value,value,value,value
        },
                null/*new RectF(20,20, 20, 20)*/,
                /*new float[]{
                        innerRaius, innerRaius,innerRaius,innerRaius,
                        innerRaius,innerRaius,innerRaius,innerRaius
                }*/ null);
        ShapeDrawable drawable = new ShapeDrawable(shape);
        drawable.setColorFilter(new PorterDuffColorFilter(0x33000000, PorterDuff.Mode.SRC_IN));
        drawable.setBounds(rect);

        innerRaius += 20;
        RoundRectShape shape2 = new RoundRectShape(new float[]{
                value, value,value,value,value,value,value,value
        },
                null,
               /* new float[]{
                        innerRaius, innerRaius,innerRaius,innerRaius,
                        innerRaius,innerRaius,innerRaius,innerRaius
                }*/ null );
        ShapeDrawable drawable2 = new ShapeDrawable(shape2);
        drawable2.setColorFilter(new PorterDuffColorFilter(Color.RED, PorterDuff.Mode.SRC_IN));
        int offset = 20;
        drawable2.setBounds(new Rect(offset ,offset, wh - offset, wh -offset));
       //convert to bitmap and blur
        Bitmap bitmap = Bitmap.createBitmap(wh, wh, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.draw(canvas);
        //drawable2.draw(canvas);
        bitmap = DrawableUtils2.blurRenderScript(this, bitmap, 16);
       //draw blur and target
        rect.set(0, 0, wh, wh);
        Bitmap result = Bitmap.createBitmap(wh, wh, Bitmap.Config.ARGB_8888);
        canvas = new Canvas(result);
        canvas.drawBitmap(bitmap, null, rect, null);
        drawable2.draw(canvas);

        v.setBackground(new BitmapDrawable(getResources(), result));
    }
//can apply shadow to any drawable
    private void shadow1(View v) {
           /* int value = DimenUtil.dip2px(this, 200);
        Rect rect = new Rect(0, 0, value, value);*/
        Drawable source = getResources().getDrawable(R.drawable.chrome);
        // source.setBounds(rect);
        //Drawable source = getResources().getDrawable(R.drawable.huge);
        Drawable mask = getResources().getDrawable(R.drawable.chrome_mask);
        // mask.setBounds(rect);
        int[] colors = {
                Color.RED, Color.BLACK
        };
        Drawable shadow = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, colors);
        ShadowShape s = new ShadowShape(this, source, mask, shadow, 8);
        v.setBackground(new ShapeDrawable(s));
    }
}
