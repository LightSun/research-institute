package com.heaven.android.recyclerview.app;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.ViewGroup;

import com.heaven.android.recyclerview.app.rv.FullableSpanSizeLookUp;
import com.heaven7.adapter.AdapterManager;
import com.heaven7.adapter.BaseSelector;
import com.heaven7.adapter.QuickRecycleViewAdapter;
import com.heaven7.adapter.util.ViewHelper2;
import com.heaven7.core.util.Logger;
import com.heaven7.java.visitor.ResultVisitor;
import com.heaven7.java.visitor.collection.VisitServices;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.rv)
    RecyclerView mRv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_1);
        ButterKnife.bind(this);

        Adapter0 adapter = new Adapter0(createItems());
    /*    FullSpannableStaggeredGridLayoutManager gm = new FullSpannableStaggeredGridLayoutManager(
                4, GridLayoutManager.VERTICAL);
        gm.setSpanSizeLookupHelper(new FullableSpanSizeLookUp<>(adapter));*/
        StaggeredGridLayoutManager gm = new StaggeredGridLayoutManager( 4, GridLayoutManager.VERTICAL);
        mRv.setLayoutManager(gm);
        mRv.setAdapter(adapter);

        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchCallbackImpl());
        helper.attachToRecyclerView(mRv);
    }

    private List<Item> createItems() {
        List<Item> list = VisitServices.from(TestUtils.FILES).map(new ResultVisitor<String, Item>() {
            @Override
            public Item visit(String s, Object param) {
                Item item = new Item();
                item.url = s;
                item.fullSpan = false;
                return item;
            }
        }).getAsList();

        Item item = new Item();
        item.title = "Stand-by";
        item.fullSpan = true;

        list.add(6, item);
        return list;
    }

    private static class Adapter0 extends QuickRecycleViewAdapter<Item>{

        GlideImageLoader mLoader = new GlideImageLoader();

        public Adapter0(List<Item> mDatas) {
            super(R.layout.item_image, mDatas);
        }
        @Override
        protected void onBindData(Context context, int position, Item item, int itemLayoutId, ViewHelper2 helper) {
           if(item.title != null){
               helper.setVisibility(R.id.iv, false)
                   .setVisibility(R.id.tv, true)
                       .setText(R.id.tv,  item.title);
           }else {
               helper.setVisibility(R.id.iv, true)
                       .setVisibility(R.id.tv, false)
                       .setImageUrl(R.id.iv, item.url, mLoader);
           }
        }
        @Override
        public void onViewAttachedToWindow(@NonNull RecyclerView.ViewHolder holder) {
            super.onViewAttachedToWindow(holder);
            if(isStaggeredGridLayout(holder)){
                int pos = holder.getLayoutPosition();
                if(isHeader(pos) || isFooter(pos)){
                    return;
                }
                Item item = getItem(pos - getHeaderSize());
                if(item.fullSpan){
                    StaggeredGridLayoutManager.LayoutParams lp = (StaggeredGridLayoutManager.LayoutParams)
                            holder.itemView.getLayoutParams();
                    lp.setFullSpan(true);
                }
            }
        }
        private boolean isStaggeredGridLayout(RecyclerView.ViewHolder holder) {
            ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
            return layoutParams instanceof StaggeredGridLayoutManager.LayoutParams;
        }
    }

    static class Item extends BaseSelector implements FullableSpanSizeLookUp.FullSpanItem {
        String url;
        boolean fullSpan;
        String title;
        @Override
        public boolean shouldFullSpan() {
            return fullSpan;
        }
    }

    private class ItemTouchCallbackImpl extends ItemTouchHelper.Callback{
        private static final String TAG = "ItemTouchCallbackImpl";
        @Override
        public int getMovementFlags(@NonNull RecyclerView rv, @NonNull RecyclerView.ViewHolder viewHolder) {
            QuickRecycleViewAdapter<Item> adapter = (QuickRecycleViewAdapter<Item>) rv.getAdapter();
            int pos = viewHolder.getAdapterPosition();
            Item item = adapter.getItem(pos - adapter.getHeaderSize());
            if(item.fullSpan){
                //can't drag for full span item
                Logger.d(TAG, "getMovementFlags", "full span. can't drag");
                return 0;
            }

            final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN
                    | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
            return makeMovementFlags(dragFlags, 0);
        }
        @Override
        public boolean onMove(@NonNull RecyclerView rv, @NonNull RecyclerView.ViewHolder viewHolder,
                              @NonNull RecyclerView.ViewHolder target) {
            QuickRecycleViewAdapter<Item> adapter = (QuickRecycleViewAdapter<Item>) rv.getAdapter();
            int fromPosition = viewHolder.getAdapterPosition() - adapter.getHeaderSize();
            int toPosition = target.getAdapterPosition() - adapter.getHeaderSize();
            if(fromPosition < 0 || toPosition < 0){
                return false;
            }
            //swap data and notify
            Item fromItem = adapter.getItem(fromPosition);
            Item toItem = adapter.getItem(toPosition);
            if(fromItem.fullSpan || toItem.fullSpan){
                //can't drag for full span item
                Logger.d(TAG, "getMovementFlags", "full span. can't drag.(from-to) = " +
                        fromItem.fullSpan + " - " + toItem.fullSpan);
                return false;
            }
            AdapterManager<Item> am = adapter.getAdapterManager();
            am.moveItem(fromPosition, toPosition);

            Logger.d(TAG, "onMove", "from = " + fromPosition + " ,to = " + toPosition);
            return true;
        }

        @Override
        public boolean isItemViewSwipeEnabled() {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

        }

        @Override
        public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
            super.onSelectedChanged(viewHolder, actionState);
        }
        @Override
        public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
            super.clearView(recyclerView, viewHolder);
        }
    }
}
