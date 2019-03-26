package com.heaven7.vida.research.sample;

import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Gravity;
import android.widget.FrameLayout;

import com.heaven7.vida.research.widget.CircleImageView;

/**
 * Created by heaven7 on 2018/6/4 0004.
 */
public class TestV7_CircleImageViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FrameLayout fl = new FrameLayout(this);
        CircleImageView civ = new CircleImageView(this, Color.BLUE);
        FrameLayout.LayoutParams flp = new FrameLayout.LayoutParams(300, 300);
        flp.gravity = Gravity.CENTER;
        civ.setLayoutParams(flp);

        fl.addView(civ);
        setContentView(fl);
    }
}
