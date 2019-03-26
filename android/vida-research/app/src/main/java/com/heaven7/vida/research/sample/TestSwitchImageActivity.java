package com.heaven7.vida.research.sample;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;

import com.heaven7.adapter.BaseSelector;
import com.heaven7.adapter.QuickRecycleViewAdapter;
import com.heaven7.core.util.ViewHelper;
import com.heaven7.core.util.viewhelper.action.IViewGetter;
import com.heaven7.vida.research.R;
import com.heaven7.vida.research.widget.SwitchImage;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by heaven7 on 2018/10/10 0010.
 */
public class TestSwitchImageActivity extends AppCompatActivity {

    @BindView(R.id.rv)
    RecyclerView mRv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_test_switch_image);
        ButterKnife.bind(this);
        setAdapter();
    }

    private void setAdapter() {
        mRv.setLayoutManager(new LinearLayoutManager(this));
        mRv.setAdapter(new QuickRecycleViewAdapter<SwitchItem>(R.layout.item_switch_image, createItems()) {

           final SparseArray<SwitchImage.Item> mMap = new SparseArray<>();
            @Override
            protected void onBindData(Context context, final int position, final SwitchItem item,
                                      int itemLayoutId, final ViewHelper helper) {
                 helper.performViewGetter(R.id.si, new IViewGetter<SwitchImage>() {
                     @Override
                     public void onGotView(SwitchImage view, ViewHelper vp) {
                         view.setItems(item.items);
                         SwitchImage.Item selectItem = mMap.get(position);
                         if(selectItem == null){
                             view.setSelectItem(item.items.get(0));
                            // view.setBackgroundColor(Color.parseColor("#f6f6f6"));
                         }else{
                             view.setSelectItem(selectItem);
                            // view.setBackgroundColor(Color.RED);
                         }
                         vp.setText(R.id.tv_text, view.getSelectItem().getText());
                     }
                 }).setRootOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View v) {
                         SwitchImage si = helper.getView(R.id.si);
                         SwitchImage.Item nextItem = si.selectNextItem();
                         if(nextItem != null) {
                             helper.setText(R.id.tv_text, nextItem.getText());
                             getAdapterManager().notifyItemChanged(position);
                             mMap.put(position, nextItem);
                         }
                     }
                 });
            }
        });
    }

    private List<SwitchItem> createItems() {
        List<SwitchItem> items = new ArrayList<>();
        SwitchImage.Item item;
        for(int i = 0 ; i < 2 ; i ++){
            SwitchItem si = new SwitchItem();
            if(i == 0) {
                item = SwitchImage.Item.of(this, R.drawable.transition_black_fade, 0);
                item.setText("black_fade");
                si.addItem(item);
                item = SwitchImage.Item.of(this, R.drawable.transition_dissolve, 0);
                item.setText("dissolve");
                si.addItem(item);
            }else{
                item = SwitchImage.Item.of(this, R.drawable.transition_left_move, 0);
                item.setText("left_move");
                si.addItem(item);
                item = SwitchImage.Item.of(this, R.drawable.transition_left_move_low, 0);
                item.setText("left_move_low");
                si.addItem(item);
                item = SwitchImage.Item.of(this, R.drawable.transition_left_roller, 0);
                item.setText("left_roller");
                si.addItem(item);
            }
            items.add(si);
        }
        return items;
    }

    static class SwitchItem extends BaseSelector {
        final List<SwitchImage.Item> items = new ArrayList<>();
        boolean disableDot;

        public void addItem(SwitchImage.Item item) {
            items.add(item);
        }
    }
}
