package com.heaven7.vida.research.sample;

import android.Manifest;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;

import com.heaven7.core.util.Logger;
import com.heaven7.core.util.MainWorker;
import com.heaven7.core.util.PermissionHelper;
import com.heaven7.vida.research.R;
import com.heaven7.vida.research.utils.DimenUtil;
import com.heaven7.vida.research.utils.DrawableUtils2;
import com.heaven7.vida.research.widget.DynamicContentView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.heaven7.vida.research.widget.DynamicContentView.TYPE_MONTH;
import static com.heaven7.vida.research.widget.DynamicContentView.TYPE_MONTH_WITH_TIME;
import static com.heaven7.vida.research.widget.DynamicContentView.TYPE_TIME;

/**
 * 测试动态内容view.
 * Created by heaven7 on 2018/11/7 0007.
 */
public class TestDynamicContentActivity extends AppCompatActivity implements DynamicContentView.TextSizeProvider,
        DynamicContentView.FontProvider {

    private static final String TAG = "TestDynamicContent";
    private PermissionHelper mHelper = new PermissionHelper(this);

    @BindView(R.id.dcv1)
    DynamicContentView mDcv1;
    @BindView(R.id.dcv2)
    DynamicContentView mDcv2;
    @BindView(R.id.dcv3)
    DynamicContentView mDcv3;

    @BindView(R.id.vg_cover)
    ViewGroup mVg_cover;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_test_dynamic_content);
        ButterKnife.bind(this);

        mDcv1.setFontProvider(this);
        mDcv1.setTextSizeProvider(this);
        mDcv2.setFontProvider(this);
        mDcv2.setTextSizeProvider(this);
        mDcv3.setFontProvider(this);
        mDcv3.setTextSizeProvider(this);
        requestPermissions();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void requestPermissions() {
        mHelper.startRequestPermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, new int[]{1},
                new PermissionHelper.ICallback() {
                    @Override
                    public void onRequestPermissionResult(String requestPermission, int requestCode, boolean success) {
                        Logger.w(TAG, "onRequestPermissionResult",
                                "success = " + success + " ,permission = " + requestPermission);
                    }
                });

    }

    @OnClick(R.id.bt_toggle_cover)
    public void onClickToggleCover(View view){
       if(mVg_cover.getVisibility() == View.VISIBLE){
           mVg_cover.setVisibility(View.GONE);
       }else{
           mVg_cover.setVisibility(View.VISIBLE);
           saveContent();
       }
    }

    @OnClick(R.id.bt_start)
    public void onClickStart(View v) {
        setContent(mDcv1, TYPE_MONTH);
        setContent(mDcv2, TYPE_TIME);
        setContent(mDcv3, TYPE_MONTH_WITH_TIME);
    }

    @OnClick(R.id.bt_scale)
    public void onClickScale(View v) {
        //倍数不能太大，否则drawingCache 为null.
        mDcv1.scale(5);
        mDcv2.scale(4);
        mDcv3.scale(3);

        //saveContent();
    }

    private void saveContent(){
        MainWorker.postDelay(2000, new Runnable() {
            @Override
            public void run() {
                //save drawing cache. even if is invisible.
                mDcv1.setDrawingCacheEnabled(true);
                mDcv1.buildDrawingCache(true);
                Bitmap bitmap = mDcv1.getDrawingCache();
                DrawableUtils2.saveBitmap(bitmap, "/sdcard/dynamic_content_save.jpg");
                mDcv1.setDrawingCacheEnabled(false);
                Logger.d(TAG, "run", "save bitmap done");
            }
        });
    }

    private void setContent(DynamicContentView view, int type) {
        DynamicContentView.Content content3 = new DynamicContentView.Content.Builder()
                .setDay("02")
                .setMonth("October")
                .setTime("17:39 PM")
                .setType(type)
                .build();
        view.scale(1f);
        view.setContent(content3);
    }

    @Override
    public int getMonthTextSize(DynamicContentView view, int contentType) {
        return DimenUtil.sp2px(getApplicationContext(), 25);
    }

    @Override
    public int getDayTextSize(DynamicContentView view,int contentType) {
        return DimenUtil.sp2px(getApplicationContext(), 54);
    }

    @Override
    public int getTimeTextSize(DynamicContentView view,int contentType) {
        return DimenUtil.sp2px(getApplicationContext(), 14);
    }

    @Override
    public String getMonthFontPath(DynamicContentView view,int contentType) {
        return null;
    }

    @Override
    public String getDayFontPath(DynamicContentView view,int contentType) {
        return null;
    }

    @Override
    public String getTimeFontPath(DynamicContentView view,int contentType) {
        return null;
    }
}
