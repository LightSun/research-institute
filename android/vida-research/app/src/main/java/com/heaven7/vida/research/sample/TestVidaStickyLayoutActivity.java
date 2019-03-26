package com.heaven7.vida.research.sample;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.heaven7.adapter.BaseSelector;
import com.heaven7.adapter.ISelectable;
import com.heaven7.adapter.QuickRecycleViewAdapter;
import com.heaven7.android.scroll.IScrollHelper;
import com.heaven7.core.util.Logger;
import com.heaven7.core.util.ViewHelper;
import com.heaven7.vida.research.R;
import com.heaven7.vida.research.widget.VidaStickyLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by heaven7 on 2018/9/28 0028.
 */
public class TestVidaStickyLayoutActivity extends AppCompatActivity {

    @BindView(R.id.rv)
    RecyclerView mRv;

    @BindView(R.id.vsl)
    VidaStickyLayout mVsl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_vida_sticky_layout);
        ButterKnife.bind(this);

        mRv.setLayoutManager(new LinearLayoutManager(this));
        mRv.setNestedScrollingEnabled(true);
        mRv.setAdapter(new QuickRecycleViewAdapter<BaseSelector>(android.R.layout.activity_list_item, createBeans()) {
            @Override
            protected void onBindData(Context context, int position, BaseSelector item, int itemLayoutId, ViewHelper helper) {
                helper.setText(android.R.id.text1, "TestVidaStickyLayoutActivity");
            }
        });
        mVsl.setCallback(new VidaStickyLayout.Callback() {
            @Override
            public int getNestedScrollFlags(VidaStickyLayout vsl, View target, int dx, int dy) {
                if(target == mRv && dy < 0){
                    return VidaStickyLayout.FLAG_PARENT;
                }
                return VidaStickyLayout.FLAG_PARENT | VidaStickyLayout.FLAG_SELF;
            }
        });
        mVsl.addOnScrollChangeListener(new IScrollHelper.OnScrollChangeListener() {
            @Override
            public void onScrollStateChanged(View target, int state) {

            }
            @Override
            public void onScrolled(View target, int dx, int dy) {
                Logger.d("TestVidaStickyLayoutActivity", "onScrolled", "dy = " + dy);
            }
        });
    }

    private List<BaseSelector> createBeans() {
        List<BaseSelector> list = new ArrayList<>();
        for (int i = 0 ; i < 100 ; i ++){
            list.add(new BaseSelector());
        }
        return list;
    }
}
