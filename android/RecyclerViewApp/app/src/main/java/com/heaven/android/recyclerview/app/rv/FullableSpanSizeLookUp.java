package com.heaven.android.recyclerview.app.rv;

import com.heaven7.adapter.FullSpannableStaggeredGridLayoutManager;
import com.heaven7.adapter.ISelectable;
import com.heaven7.adapter.QuickRecycleViewAdapter;

/**
 * Created by heaven7 on 2019/7/22.
 */
public final class FullableSpanSizeLookUp<T extends FullableSpanSizeLookUp.FullSpanItem> implements
        FullSpannableStaggeredGridLayoutManager.ISpanSizeLookupHelper {

    private final QuickRecycleViewAdapter<T> adapter;

    public FullableSpanSizeLookUp(QuickRecycleViewAdapter<T> adapter) {
        this.adapter = adapter;
    }

    @Override
    public boolean shouldFullSpan(int position) {
        if(adapter.isHeader(position)){
            return shouldHeaderFullSpan(position);
        }else if(adapter.isFooter(position)){
            return shouldFooterFullSpan(position);
        }else {
            T item = adapter.getItem(position - adapter.getHeaderSize());
            return  item.shouldFullSpan();
        }
    }
    protected boolean shouldFooterFullSpan(int position) {
        return true;
    }

    protected boolean shouldHeaderFullSpan(int position) {
        return true;
    }
    public interface FullSpanItem extends ISelectable {
        boolean shouldFullSpan();
    }
}
