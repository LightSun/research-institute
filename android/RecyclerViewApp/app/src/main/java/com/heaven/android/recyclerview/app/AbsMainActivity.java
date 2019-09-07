package com.heaven.android.recyclerview.app;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.heaven7.adapter.ISelectable;
import com.heaven7.adapter.QuickAdapter;
import com.heaven7.core.util.ViewHelper;

import java.util.ArrayList;
import java.util.List;

import butterknife.internal.DebouncingOnClickListener;


public abstract class AbsMainActivity extends ListActivity {

    protected final List<ActivityInfo> mInfos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, options));
        addDemos(mInfos);
        setListAdapter(new QuickAdapter<ActivityInfo>(android.R.layout.simple_list_item_1, mInfos) {
            @Override
            protected void onBindData(Context context, int position, final ActivityInfo item,
                                      int itemLayoutId, ViewHelper helper) {
                helper.setText(android.R.id.text1, item.desc)
                        .setRootOnClickListener(new DebouncingOnClickListener() {
                            @Override
                            public void doClick(View v) {
                                if(item.forResult){
                                    startActivityForResult(new Intent(AbsMainActivity.this, item.clazz), 0);
                                }else {
                                    startActivity(new Intent(AbsMainActivity.this, item.clazz));
                                }
                            }
                        });
            }
        });
    }

    protected abstract void addDemos(List<ActivityInfo> list);

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {

    }

    public static class ActivityInfo implements ISelectable {
        final String desc;
        final Class<? extends Activity> clazz;
        boolean forResult;

        public ActivityInfo(Class<? extends Activity> clazz, String desc) {
            this.clazz = clazz;
            this.desc = desc;
        }

        public ActivityInfo(Class<? extends Activity> clazz) {
            this(clazz, clazz.getSimpleName());
        }
        public ActivityInfo(Class<? extends Activity> clazz, boolean forResult) {
            this(clazz, clazz.getSimpleName());
            this.forResult = forResult;
        }

        public ActivityInfo(Context context, Class<? extends Activity> clazz, int stringResId) {
            this.clazz = clazz;
            this.desc = context.getResources().getString(stringResId);
        }

        @Override
        public void setSelected(boolean selected) {
        }
        @Override
        public boolean isSelected() {
            return false;
        }
    }
}