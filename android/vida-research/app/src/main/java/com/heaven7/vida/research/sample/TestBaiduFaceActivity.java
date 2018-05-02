package com.heaven7.vida.research.sample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.heaven7.core.util.Logger;
import com.heaven7.java.visitor.ResultVisitor;
import com.heaven7.java.visitor.Visitors;
import com.heaven7.java.visitor.collection.VisitServices;
import com.heaven7.vida.research.R;
import com.heaven7.vida.research.face_bd.FaceInfo;
import com.heaven7.vida.research.face_bd.FaceRequest;
import com.heaven7.vida.research.retrofit.AbstractRetrofitCallback;
import com.heaven7.vida.research.widget.FaceView;

import java.util.ArrayList;
import java.util.List;

/**
 * test zoom drawable
 * Created by heaven7 on 2018/2/8 0008.
 */

public class TestBaiduFaceActivity extends AppCompatActivity {

    private static final String TAG = "TestBaiduFaceActivity";
    FaceView mFaceView;
    FaceView mFaceView2;
    FaceView mFaceView3;
    FaceRequest faceRequest = new FaceRequest();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_test_face_bd);

        mFaceView = findViewById(R.id.faceView);
        mFaceView2 = findViewById(R.id.faceView2);
        mFaceView3 = findViewById(R.id.faceView3);

        startRequest(R.drawable.zhaoliyin, mFaceView);
        startRequest(R.drawable.zhaoliyin2, mFaceView2);
        startRequest(R.drawable.huge, mFaceView3);
    }

    //(width, cx, cy, angle) = (366, 370, 472, 0 )
    private void startRequest(int resid,final FaceView receiver) {
        faceRequest.startRequest(this,
                resid,
                new AbstractRetrofitCallback<ArrayList<FaceInfo>>() {
            @Override
            public void onSuccess(ArrayList<FaceInfo> result) {
                Logger.i(TAG, "onSuccess", "" + result);
                final List<FaceView.FaceDrawInfo> rects = VisitServices.from(result).visitForResultList(
                        Visitors.truePredicateVisitor(), new ResultVisitor<FaceInfo, FaceView.FaceDrawInfo>() {
                    @Override
                    public FaceView.FaceDrawInfo visit(FaceInfo faceInfo, Object param) {
                        return new FaceView.FaceDrawInfo(faceInfo.getLocation().toRect(), faceInfo.getRotation_angle());
                    }
                }, null);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        receiver.setFaceAreas(rects);
                    }
                });
            }
        });
    }

    @Override
    protected void onDestroy() {
        faceRequest.destroy();
        super.onDestroy();
    }
}
