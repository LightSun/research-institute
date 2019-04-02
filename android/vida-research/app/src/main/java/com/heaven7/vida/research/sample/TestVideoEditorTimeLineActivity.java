package com.heaven7.vida.research.sample;

import android.content.Context;
import android.os.Bundle;

import com.heaven7.vida.research.BaseActivity;
import com.heaven7.vida.research.R;
import com.heaven7.vida.research.widget.VideoEditTimeLine;

import butterknife.BindView;

/**
 * Created by heaven7 on 2019/4/2.
 */
public class TestVideoEditorTimeLineActivity extends BaseActivity {

    @BindView(R.id.tl)
    VideoEditTimeLine mTl;

    @Override
    protected int getLayoutId() {
        return R.layout.ac_test_ve_time_line;
    }

    @Override
    protected void onInitialize(Context context, Bundle savedInstanceState) {
        mTl.setDuration(125);
    }
}
