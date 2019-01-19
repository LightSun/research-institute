package com.heaven7.vida.research;

import com.heaven7.vida.research.drag.DragActivity;
import com.heaven7.vida.research.sample.BalanceViewTest;
import com.heaven7.vida.research.sample.NativeSurfaceActivity;
import com.heaven7.vida.research.sample.TestBaiduFace2Activity;
import com.heaven7.vida.research.sample.TestBaiduFaceActivity;
import com.heaven7.vida.research.sample.TestClipScaleActivity;
import com.heaven7.vida.research.sample.TestDiscViewActivity;
import com.heaven7.vida.research.sample.TestDynamicContentActivity;
import com.heaven7.vida.research.sample.TestLottieActivity;
import com.heaven7.vida.research.sample.TestPageTipViewActivity;
import com.heaven7.vida.research.sample.TestRectProgresActivity;
import com.heaven7.vida.research.sample.TestRecyclerViewSwipeActivity;
import com.heaven7.vida.research.sample.TestRingViewActivity;
import com.heaven7.vida.research.sample.TestRoundViewActivity;
import com.heaven7.vida.research.sample.TestScaleDrawableActivity;
import com.heaven7.vida.research.sample.TestShadowShapeActivity;
import com.heaven7.vida.research.sample.TestSwitchImageActivity;
import com.heaven7.vida.research.sample.TestTransitionAnimationActivity1;
import com.heaven7.vida.research.sample.TestV7_CircleImageViewActivity;
import com.heaven7.vida.research.sample.TestVidaStickyLayoutActivity;
import com.heaven7.vida.research.sample.TestVidaTransDrawable;
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

        list.add(new ActivityInfo(NativeSurfaceActivity.class));
        list.add(new ActivityInfo(TestRectProgresActivity.class));
        list.add(new ActivityInfo(TestShadowShapeActivity.class));
        list.add(new ActivityInfo(TestV7_CircleImageViewActivity.class));
        list.add(new ActivityInfo(BalanceViewTest.class));
        list.add(new ActivityInfo(TestBaiduFace2Activity.class));

        list.add(new ActivityInfo(TestVidaStickyLayoutActivity.class));
        list.add(new ActivityInfo(TestSwitchImageActivity.class));
        list.add(new ActivityInfo(TestRecyclerViewSwipeActivity.class));
       // list.add(new ActivityInfo(TestReadAssetsActivity.class));
        list.add(new ActivityInfo(TestVidaTransDrawable.class));
        //list.add(new ActivityInfo(TestGetCityActivity.class));
        list.add(new ActivityInfo(TestDynamicContentActivity.class));
        list.add(new ActivityInfo(TestPageTipViewActivity.class));
        list.add(new ActivityInfo(TestTransitionAnimationActivity1.class));
        list.add(new ActivityInfo(TestScaleDrawableActivity.class));
        list.add(new ActivityInfo(TestLottieActivity.class));
        list.add(new ActivityInfo(TestRingViewActivity.class));
        list.add(new ActivityInfo(TestClipScaleActivity.class));
    }

}
