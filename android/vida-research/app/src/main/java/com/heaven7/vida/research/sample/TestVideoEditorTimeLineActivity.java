package com.heaven7.vida.research.sample;

import android.content.Context;
import android.os.Bundle;

import com.heaven7.core.util.Logger;
import com.heaven7.vida.research.BaseActivity;
import com.heaven7.vida.research.R;
import com.heaven7.vida.research.utils.CommonUtils;
import com.heaven7.vida.research.widget.VideoEditTimeLine;

import butterknife.BindView;

/**
 * Created by heaven7 on 2019/4/2.
 */
public class TestVideoEditorTimeLineActivity extends BaseActivity implements VideoEditTimeLine.Callback {

    @BindView(R.id.tl)
    VideoEditTimeLine mTl;

    private static final String TAG = "TestVETimeLine";

    @Override
    protected int getLayoutId() {
        return R.layout.ac_test_ve_time_line;
    }

    @Override
    protected void onInitialize(Context context, Bundle savedInstanceState) {
        int halfWidth = CommonUtils.getDisplayMetrics(context).widthPixels / 2;
        mTl.setDuration(20);
        mTl.setMaxOffsetX(halfWidth);
        mTl.setCallback(this);
    }

    @Override
    public void onTimeLineChanged(VideoEditTimeLine view, float percent) {
        Logger.d(TAG, "onTimeLineChanged", "percent = " + percent);
    }
    @Override
    public void onTimeLineChangeEnd(VideoEditTimeLine view, float percent) {
        Logger.d(TAG, "onTimeLineChangeEnd", "percent = " + percent);
    }
}
