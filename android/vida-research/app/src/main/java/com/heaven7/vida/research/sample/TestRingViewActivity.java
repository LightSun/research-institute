package com.heaven7.vida.research.sample;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.heaven7.vida.research.BaseActivity;
import com.heaven7.vida.research.R;
import com.heaven7.vida.research.widget.RingView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by heaven7 on 2019/1/4.
 */
public class TestRingViewActivity extends BaseActivity {

    @BindView(R.id.ring)
    RingView mRingView;

    @Override
    protected int getLayoutId() {
        return R.layout.ac_test_ring_view;
    }

    @Override
    protected void onInitialize(Context context, Bundle savedInstanceState) {

    }

    @OnClick(R.id.bt_progress)
    public void onClickChangeProcess(View v){
        int process = mRingView.getProcess();
        int target = process + 20;
        if(target > 100){
            target = 100;
        }
        mRingView.setProgress(target, true);
    }
}
