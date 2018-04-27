package com.heaven7.vida.research.sample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.heaven7.vida.research.R;
import com.heaven7.vida.research.face_bd.FaceRequest;

/**
 * test zoom drawable
 * Created by heaven7 on 2018/2/8 0008.
 */

public class TestBaiduFaceActivity extends AppCompatActivity {

    FaceRequest faceRequest = new FaceRequest();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_test_zoom_drawable);

        faceRequest.startRequest(this, R.drawable.zhaoliyin);

    }

    @Override
    protected void onDestroy() {
        faceRequest.destroy();
        super.onDestroy();
    }
}
