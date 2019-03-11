package com.heaven7.vida.research.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.heaven7.core.util.ViewHelper;
import com.heaven7.java.base.util.ObservableList;
import com.heaven7.memory.util.Cacher;

import java.util.Collection;
import java.util.List;

/**
 * Created by heaven7 on 2018/11/22 0022.
 */
public abstract class SimplePageAdapter<T> extends PagerAdapter {

    private final Cacher<View, ViewGroup> mCacher;
    private final ObservableList<T> mList;

    public SimplePageAdapter(List<T> mList, int maxCacheCount) {
        this.mList = new ObservableList<T>(mList, new DataCallbackImpl());
        this.mCacher = new Cacher<View, ViewGroup>(maxCacheCount) {
            @Override
            public View create(ViewGroup container) {
                return LayoutInflater.from(container.getContext()).inflate(getLayoutId(),
                        container, false);
            }
        };
    }

    public void prepareRightNow(ViewGroup parent){
        mCacher.prepare(parent);
    }

    public T getItem(int index){
        return mList.get(index);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = mCacher.obtain(container);
        int pos = getActualPosition(position);
        container.addView(view);
        ViewHelper helper = new ViewHelper(view);
        onBindData(container.getContext(), pos, mList.get(pos), helper);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        View v = (View) object;
        container.removeView(v);
        mCacher.recycle(v);
    }
    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == o;
    }
    @Override
    public int getCount() {
        return mList.size();
    }

    protected int getActualPosition(int position){
        return position;
    }

    protected abstract int getLayoutId();

    protected abstract void onBindData(Context context, int pos, T item, ViewHelper helper);


    private class DataCallbackImpl implements ObservableList.Callback<T>{
        @Override
        public void onRemove(ObservableList<T> origin, int index, T ele) {
              notifyDataSetChanged();
        }
        @Override
        public void onAdd(ObservableList<T> origin, int index, T ele) {
            notifyDataSetChanged();
        }
        @Override
        public void onSet(ObservableList<T> origin, int index, T old, T newE) {
            notifyDataSetChanged();
        }
        @Override
        public void onClear(ObservableList<T> origin, List<T> old) {
            notifyDataSetChanged();
        }
        @Override
        public void onBatchRemove(ObservableList<T> origin, Collection<T> batch) {
            notifyDataSetChanged();
        }
        @Override
        public void onBatchAdd(ObservableList<T> origin, int startIndex, Collection<T> batch) {
            notifyDataSetChanged();
        }
        @Override
        public void onUpdate(ObservableList<T> origin, int index, T ele) {
            notifyDataSetChanged();
        }

        @Override
        public void onBatchChanged(ObservableList<T> origin, List<T> batch) {
            notifyDataSetChanged();
        }
        @Override
        public void onAllChanged(ObservableList<T> origin) {
            notifyDataSetChanged();
        }
    }
}
