package com.heaven7.vida.research.sample;

import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.heaven7.android.util2.LauncherIntent;
import com.heaven7.vida.research.BaseActivity;
import com.heaven7.vida.research.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by heaven7 on 2018/11/24 0024.
 */
public class TestTransitionAnimationActivity1 extends BaseActivity {


    @BindView(R.id.firstSharedView)
    View mAnimView;

    @Override
    protected int getLayoutId() {
        return R.layout.ac_test_transition_anim_1;
    }

    @Override
    protected void onInitialize(Context context, Bundle savedInstanceState) {

    }

    @TargetApi(21)
    @OnClick(R.id.firstSharedView)
    public void onClickShareView(View v){
        Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(this, mAnimView,
                "sharedView").toBundle();
        new LauncherIntent.Builder().setClass(this, TestTransitionAnimationActivity2.class)
                .build().startActivity(bundle);
    }
}
