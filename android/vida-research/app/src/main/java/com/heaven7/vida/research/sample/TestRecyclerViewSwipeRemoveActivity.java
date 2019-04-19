package com.heaven7.vida.research.sample;

import android.content.Context;
import android.os.Bundle;

import com.heaven7.vida.research.BaseActivity;
import com.heaven7.vida.research.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

/**
 * Created by heaven7 on 2019/4/19.
 */
public class TestRecyclerViewSwipeRemoveActivity extends BaseActivity {

    @BindView(R.id.rv)
    RecyclerView mRv;

    @Override
    protected int getLayoutId() {
        return R.layout.ac_test_rv_swipe_remove;
    }

    @Override
    protected void onInitialize(Context context, Bundle savedInstanceState) {
        mRv.setLayoutManager(new LinearLayoutManager(context));
    }

    private class TouchCallbackImpl extends ItemTouchHelper.SimpleCallback{

        public TouchCallbackImpl(int dragDirs, int swipeDirs) {
            super(dragDirs, swipeDirs);
        }

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder,
                              @NonNull RecyclerView.ViewHolder target) {
            int pos = viewHolder.getAdapterPosition();
            int pos2 = target.getAdapterPosition();
            //swap data
            return true;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

        }
    }
}
