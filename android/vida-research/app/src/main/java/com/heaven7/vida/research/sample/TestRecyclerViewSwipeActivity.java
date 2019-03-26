package com.heaven7.vida.research.sample;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;

import com.heaven7.adapter.BaseSelector;
import com.heaven7.adapter.QuickRecycleViewAdapter;
import com.heaven7.core.util.ViewHelper;
import com.heaven7.vida.research.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * test recycler view drag and swipe
 * Created by heaven7 on 2018/10/18 0018.
 */
public class TestRecyclerViewSwipeActivity extends AppCompatActivity {

    @BindView(R.id.rv)
    RecyclerView mRv;

    private QuickRecycleViewAdapter<Item> mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_test_rv_swipe);
        ButterKnife.bind(this);

        init();
    }

    private void init() {
        mRv.setLayoutManager(new LinearLayoutManager(this));
        mRv.setHasFixedSize(true);
        ItemTouchHelper helper = new ItemTouchHelper(new RecyclerViewCallback());
        helper.attachToRecyclerView(mRv);
        mRv.setAdapter(mAdapter = new QuickRecycleViewAdapter<Item>(android.R.layout.simple_list_item_1, createItems()) {
            @Override
            protected void onBindData(Context context, int position, Item item, int itemLayoutId, ViewHelper helper) {
                helper.setText(android.R.id.text1, item.text);
            }
        });
    }

    private List<Item> createItems() {
        List<Item> items = new ArrayList<>();
        for (int i = 0 ; i < 30 ; i ++){
            items.add(new Item("text_heaven7_" + i));
        }
        return items;
    }

    private static class Item extends BaseSelector{
        String text;
        public Item(String text) {
            this.text = text;
        }
    }

    //支持拖拽交换位置和swipe
    private class RecyclerViewCallback extends ItemTouchHelper.SimpleCallback {

        private int draggingFromPosition;
        private int draggingToPosition;

        public RecyclerViewCallback() {
            super(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.START | ItemTouchHelper.END);
            draggingFromPosition = -1;
            draggingToPosition = -1;
        }

        @Override
        public boolean onMove(RecyclerView list, RecyclerView.ViewHolder origin,
                              RecyclerView.ViewHolder target) {
            int fromPosition = origin.getAdapterPosition();
            int toPosition = target.getAdapterPosition();
            if (draggingFromPosition == -1) {
                // A drag has started, but changes to the media queue will be reflected in clearView().
                draggingFromPosition = fromPosition;
            }
            draggingToPosition = toPosition;
            mAdapter.getAdapterManager().notifyItemMoved(fromPosition, toPosition);
            return true;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            mAdapter.getAdapterManager().notifyItemRemoved(position);
          /*  if (playerManager.removeItem(position)) {
                mediaQueueListAdapter.notifyItemRemoved(position);
            }*/
        }

        @Override
        public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            super.clearView(recyclerView, viewHolder);
            if (draggingFromPosition != -1) {
                /*if (!playerManager.moveItem(draggingFromPosition, draggingToPosition)) {
                    mediaQueueListAdapter.notifyDataSetChanged();
                }*/
            }
            draggingFromPosition = -1;
            draggingToPosition = -1;
        }

    }
}
