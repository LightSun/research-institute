package com.heaven7.vida.research;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.heaven7.vida.research.drag.DragActivity;
import com.heaven7.vida.research.sample.BalanceViewTest;
import com.heaven7.vida.research.sample.NativeSurfaceActivity;
import com.heaven7.vida.research.sample.TestAutoClickActivity;
import com.heaven7.vida.research.sample.TestBaiduFace2Activity;
import com.heaven7.vida.research.sample.TestBaiduFaceActivity;
import com.heaven7.vida.research.sample.TestClipScaleActivity;
import com.heaven7.vida.research.sample.TestDiscViewActivity;
import com.heaven7.vida.research.sample.TestDynamicContentActivity;
import com.heaven7.vida.research.sample.TestGLSurfaceViewActivity;
import com.heaven7.vida.research.sample.TestLottieActivity;
import com.heaven7.vida.research.sample.TestMultiPieceProgressView;
import com.heaven7.vida.research.sample.TestPageTipViewActivity;
import com.heaven7.vida.research.sample.TestPageTitleStrip;
import com.heaven7.vida.research.sample.TestRectProgresActivity;
import com.heaven7.vida.research.sample.TestRecyclerViewSwipeActivity;
import com.heaven7.vida.research.sample.TestRhinoActivity;
import com.heaven7.vida.research.sample.TestRingViewActivity;
import com.heaven7.vida.research.sample.TestRoundViewActivity;
import com.heaven7.vida.research.sample.TestScaleDrawableActivity;
import com.heaven7.vida.research.sample.TestShadowShapeActivity;
import com.heaven7.vida.research.sample.TestSwitchImageActivity;
import com.heaven7.vida.research.sample.TestTransitionAnimationActivity1;
import com.heaven7.vida.research.sample.TestV7_CircleImageViewActivity;
import com.heaven7.vida.research.sample.TestVidaStickyLayoutActivity;
import com.heaven7.vida.research.sample.TestVidaTransDrawable;
import com.heaven7.vida.research.sample.TestVideoEditorTimeLineActivity;
import com.heaven7.vida.research.sample.TestZoomDrawableActivity;
import com.heaven7.vida.research.service.TestAccessibilityService;
import com.heaven7.vida.research.utils.AccessibilityHelper;

import java.io.IOException;
import java.net.URL;
import java.util.List;

/**
 * Created by heaven7 on 2017/12/22.
 */
public class TestMainActivity extends AbsMainActivity {

    @Override
    protected void addDemos(List<ActivityInfo> list) {
        //System.getProperties().put("dsfdsfsfs", "");
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

        list.add(new ActivityInfo(TestRhinoActivity.class));
        list.add(new ActivityInfo(TestAutoClickActivity.class));
        list.add(new ActivityInfo(TestMultiPieceProgressView.class));
        list.add(new ActivityInfo(TestPageTitleStrip.class));
        list.add(new ActivityInfo(TestGLSurfaceViewActivity.class));
        list.add(new ActivityInfo(TestVideoEditorTimeLineActivity.class));

        testLoadImageFromJar();
    }

    //test ok
    private void testLoadImageFromJar() {
        //from aop.jar.
        //jar:file:/data/app/com.heaven7.vida.research-xdQ48mUgjcWppqlAcca0kg==/base.apk!/res/ic_photo.png
        try{
            URL url = TestMainActivity.class.getClassLoader().getResource("res/ic_photo.png");
            if(url != null) {
                Bitmap btmp = BitmapFactory.decodeStream(url.openStream(), null, null);
                System.out.println(btmp);
            }else {
                int resourceID = getResources().getIdentifier("ic_photo", "drawable", getPackageName());
                Bitmap btmp = BitmapFactory.decodeResource(getResources(), resourceID, null);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
       /* stopService(new Intent(this, TestAccessibilityService.class));
        AccessibilityOperator.getInstance().updateEvent(null, null);*/
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
       // startTestServices();
    }

    private void startTestServices() {
        if(!AccessibilityHelper.isAccessibilitySettingsOn(this,
                TestAccessibilityService.class.getCanonicalName())) {
            AccessibilityHelper.jumpToSettingPage(this);
        }
       // startService(new Intent(this, TestAccessibilityService.class));
    }

}
