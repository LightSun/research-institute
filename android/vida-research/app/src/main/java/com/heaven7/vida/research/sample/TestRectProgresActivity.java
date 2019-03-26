package com.heaven7.vida.research.sample;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import com.heaven7.vida.research.R;
import com.heaven7.vida.research.widget.RectangleProgressView;

/**
 * Created by heaven7 on 2018/5/31 0031.
 */
public class TestRectProgresActivity extends AppCompatActivity {

    RectangleProgressView mRectView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_rect_progress);

        mRectView = findViewById(R.id.rectProView);
    }

    public void startRectAnim(View v){
        mRectView.startProgressAnimation();
    }
}
