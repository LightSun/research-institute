package com.heaven7.vida.research.sample;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.heaven7.core.util.Logger;
import com.heaven7.core.util.MainWorker;
import com.heaven7.core.util.PermissionHelper;
import com.heaven7.vida.research.BaseActivity;
import com.heaven7.vida.research.R;
import com.heaven7.vida.research.service.TestAccessibilityService;
import com.heaven7.vida.research.utils.AccessibilityOperator;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * using accessibility service.
 * Created by heaven7 on 2019/2/11.
 */
public class TestAutoClickActivity extends BaseActivity {

    private static final String TAG = "TestAutoClickActivity";

    @BindView(R.id.tv)
    TextView mTv;

    @Override
    protected int getLayoutId() {
        return R.layout.ac_test_auto_click;
    }

    @Override
    protected void onInitialize(Context context, Bundle savedInstanceState) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    }

    @OnClick(R.id.normal_sample_back)
    public void onClickBack(View view){
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        postDelayed(new Runnable() {
            @Override
            public void run() {
                // simulationClickByText();
                simulationClickById();
            }
        }, 2000);
    }

    private void postDelayed(Runnable runnable , long delay){
        MainWorker.postDelay(delay, runnable);
    }

    private void printLog(String msg){
        Logger.d("TestAutoClickActivity", "printLog", msg);
    }

    private void simulationClickByText() {
        boolean result = AccessibilityOperator.getInstance().clickByText("复选框开关");
        printLog(result ? "复选框模拟点击成功" : "复选框模拟点击失败");
        postDelayed(new Runnable() {
            @Override
            public void run() {
                boolean result = AccessibilityOperator.getInstance().clickByText("单选按钮");
                printLog(result ? "单选按钮模拟点击成功" : "单选按钮模拟点击失败");
            }
        }, 2000);
        postDelayed(new Runnable() {
            @Override
            public void run() {
                boolean result = AccessibilityOperator.getInstance().clickByText("OFF");
                printLog(result ? "OnOff开关模拟点击成功" : "OnOff开关模拟点击失败");
            }
        }, 4000);
        postDelayed(new Runnable() {
            @Override
            public void run() {
                boolean result = AccessibilityOperator.getInstance().clickByText("退出本页面");
                printLog(result ? "退出本页面模拟点击成功" : "退出本页面模拟点击失败");
            }
        }, 6000);
    }

    private void simulationClickById() {
        final String pkg = getPackageName();
        boolean result = AccessibilityOperator.getInstance().clickById(pkg + ":id/normal_sample_checkbox");
        printLog(result ? "复选框模拟点击成功" : "复选框模拟点击失败");
        postDelayed(new Runnable() {
            @Override
            public void run() {
                boolean result = AccessibilityOperator.getInstance().clickById(pkg + ":id/normal_sample_radiobutton");
                printLog(result ? "单选按钮模拟点击成功" : "单选按钮模拟点击失败");
            }
        }, 2000);
        postDelayed(new Runnable() {
            @Override
            public void run() {
                boolean result = AccessibilityOperator.getInstance().clickById(pkg + ":id/normal_sample_togglebutton");
                printLog(result ? "OnOff开关模拟点击成功" : "OnOff开关模拟点击失败");
            }
        }, 4000);
        postDelayed(new Runnable() {
            @Override
            public void run() {
//                boolean result = AccessibilityOperator.getInstance().clickById("com.accessibility:id/normal_sample_back");
//                printLog(result ? "退出本页面模拟点击成功" : "退出本页面模拟点击失败");
                // 下面这个模拟点击系统返回键
                boolean result = AccessibilityOperator.getInstance().clickBackKey();
                printLog(result ? "返回键模拟点击成功" : "返回键模拟点击失败");
            }
        }, 6000);
    }
}
