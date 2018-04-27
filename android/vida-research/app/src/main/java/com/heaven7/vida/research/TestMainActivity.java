package com.heaven7.vida.research;

import com.heaven7.vida.research.drag.DragActivity;
import com.heaven7.vida.research.sample.TestBaiduFaceActivity;
import com.heaven7.vida.research.sample.TestDiscViewActivity;
import com.heaven7.vida.research.sample.TestRoundViewActivity;
import com.heaven7.vida.research.sample.TestZoomDrawableActivity;

import java.util.List;

/**
 * Created by heaven7 on 2017/12/22.
 */
public class TestMainActivity extends AbsMainActivity {

    @Override
    protected void addDemos(List<ActivityInfo> list) {
        list.add(new ActivityInfo(MainActivity.class));
        list.add(new ActivityInfo(com.heaven7.vida.research.drag.MainActivity.class));
        list.add(new ActivityInfo(DragActivity.class));
        list.add(new ActivityInfo(TestAndroidSpringActivity.class));
        list.add(new ActivityInfo(TestDampScrollView.class));

        list.add(new ActivityInfo(TestRoundViewActivity.class));
        list.add(new ActivityInfo(TestZoomDrawableActivity.class));
        list.add(new ActivityInfo(TestDiscViewActivity.class));
        list.add(new ActivityInfo(TestBaiduFaceActivity.class));
    }

}
