package com.heaven7.vida.research.sample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.heaven7.core.util.Logger;
import com.heaven7.vida.research.R;
import com.heaven7.vida.research.face_bd.BodyAttrData;
import com.heaven7.vida.research.face_bd.BodyData;
import com.heaven7.vida.research.face_bd.FaceRequest;
import com.heaven7.vida.research.face_bd.MainClassifyBean;
import com.heaven7.vida.research.retrofit.AbstractRetrofitCallback;

/**
 * 百度ai主体内容识别
 * Created by heaven7 on 2018/2/8 0008.
 */

public class TestBaiduFace2Activity extends AppCompatActivity {

    private static final String TAG = "TestBaiduFaceActivity";
    FaceRequest faceRequest = new FaceRequest();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_test_face_bd);

       // startRequest(R.drawable.zhaoliyin);
      //  startRequest(R.drawable.zhaoliyin2);
        startRequestBody(R.drawable.test_body2);
    }

    //(width, cx, cy, angle) = (366, 370, 472, 0 )
    private void startRequest(int resid) {
        faceRequest.startMainClassify(this, resid, new AbstractRetrofitCallback<MainClassifyBean>() {
            @Override
            public void onSuccess(MainClassifyBean result) {
                 Logger.i(TAG, "startMainClassify", "" + result.toString());
            }
        });
    }
    private void startRequestBody(int resid) {
        faceRequest.getBodyAnalyse(this, resid, new AbstractRetrofitCallback<BodyData>() {
            @Override
            public void onSuccess(BodyData result) {
                Logger.i(TAG, "getBodyAnalyse", "" + result.toString());
            }
        });
        faceRequest.getBodyAttr(this, resid, new AbstractRetrofitCallback<BodyAttrData>() {
            @Override
            public void onSuccess(BodyAttrData result) {
                Logger.i(TAG, "getBodyAttr", "" + result.toString());
            }
        });
    }

    @Override
    protected void onDestroy() {
        faceRequest.destroy();
        super.onDestroy();
    }
}
