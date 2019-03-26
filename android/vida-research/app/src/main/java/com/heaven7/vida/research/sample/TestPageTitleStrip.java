package com.heaven7.vida.research.sample;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.heaven7.adapter.BaseSelector;
import com.heaven7.core.util.ViewHelper;
import com.heaven7.vida.research.BaseActivity;
import com.heaven7.vida.research.R;
import com.heaven7.vida.research.adapter.SimplePageAdapter;
import com.heaven7.vida.research.widget.PagerTitleStrip;
import com.heaven7.vida.research.widget.TwoTextOffsetView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by heaven7 on 2019/3/11.
 */
public class TestPageTitleStrip extends BaseActivity {

    @BindView(R.id.pts)
    PagerTitleStrip mPts;

    @BindView(R.id.ttov)
    TwoTextOffsetView mTtov;

    @BindView(R.id.vp)
    ViewPager mVp;

    @Override
    protected int getLayoutId() {
        return R.layout.ac_test_page_title_strip;
    }

    @Override
    protected void onInitialize(Context context, Bundle savedInstanceState) {
        mVp.setAdapter(new Adapter0(createList(), 10));
        mTtov.concatViewPager(mVp);
    }

    private List<Item> createList() {
        List<Item> list = new ArrayList<>();
        for (int i = 0 ; i < 10 ; i ++){
            list.add(new Item("0" + i));
        }
        return list;
    }

    private class Adapter0 extends SimplePageAdapter<Item>{

        public Adapter0(List<Item> mList, int maxCacheCount) {
            super(mList, maxCacheCount);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return getItem(position).text;
        }
        @Override
        protected int getLayoutId() {
            return android.R.layout.simple_list_item_1;
        }
        @Override
        protected void onBindData(Context context, int pos, Item item, ViewHelper helper) {
            helper.setText(android.R.id.text1, item.text);
        }
    }

    static class Item extends BaseSelector{
        String text;
        public Item(String text) {
            this.text = text;
        }
    }

}
