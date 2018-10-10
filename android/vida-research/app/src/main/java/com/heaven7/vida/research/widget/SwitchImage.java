package com.heaven7.vida.research.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.view.View;

import com.heaven7.java.base.util.Predicates;
import com.heaven7.java.visitor.PredicateVisitor;
import com.heaven7.java.visitor.collection.VisitServices;
import com.heaven7.vida.research.R;
import com.heaven7.vida.research.utils.DrawableUtils2;

import java.util.List;

/**
 * Created by heaven7 on 2018/10/10 0010.
 */
public class SwitchImage extends View {

    private final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int mSelectColor = Color.BLACK;
    private int mUnselectColor = Color.GRAY;
    /** the margin between dot and icon */
    private int mDotMarginTop;
    /** the space between dots */
    private int mDotSpace;
    /** the every dot size */
    private int mDotSize;

    private List<Item> mItems;
    private Item mSelectItem;
    private boolean mDotEnabled = true;

    public SwitchImage(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwitchImage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SwitchImage);
        try {
            mSelectColor = a.getColor(R.styleable.SwitchImage_si_dot_select_color, mSelectColor);
            mUnselectColor = a.getColor(R.styleable.SwitchImage_si_dot_unselect_color, mUnselectColor);
            mDotMarginTop = a.getDimensionPixelSize(R.styleable.SwitchImage_si_dot_margin_top, mDotMarginTop);
            mDotSpace = a.getDimensionPixelSize(R.styleable.SwitchImage_si_dot_space, mDotSpace);
            mDotSize = a.getDimensionPixelSize(R.styleable.SwitchImage_si_dot_size, mDotSize);
            mDotEnabled = a.getBoolean(R.styleable.SwitchImage_si_dot_enabled, mDotEnabled);
        }finally {
            a.recycle();
        }
        mPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (Predicates.isEmpty(mItems) || mSelectItem == null) {
            return;
        }
        int width = getWidth() - getPaddingLeft() - getPaddingRight();
        int height = getHeight() - getPaddingTop() - getPaddingBottom();
        //draw icon in center
        int iconWidth = mSelectItem.icon.getIntrinsicWidth();
        int iconHeight = mSelectItem.icon.getIntrinsicHeight();
        if (iconWidth > width || iconHeight > height) {
            throw new IllegalStateException("wrong icon width and height");
        }
        canvas.save();
        //draw icon
        int x = (width - iconWidth) / 2;
        int y = (height - iconHeight) / 2;
        canvas.translate(x, y);
        mSelectItem.icon.draw(canvas);
        canvas.translate(-x, -y);
        //draw dots
        int size = mItems.size();
        int dotsWidth = size * mDotSize + (size - 1) * mDotSpace;
        x = (width - dotsWidth) / 2;
        y += (iconHeight + mDotMarginTop);
        canvas.translate(x, y);
        int cx = x + mDotSize / 2, cy = y + mDotSize / 2;
        for (int i = 0 ; i < size ; i ++){
            boolean select = mItems.get(i) == mSelectItem;
            mPaint.setColor(select ? mSelectColor : mUnselectColor);
            canvas.drawCircle(cx, cy, mDotSize / 2, mPaint);
            cx += mDotSpace + mDotSize;
        }
        canvas.restore();
    }

    public void setDotEnabled(boolean enabled) {
        this.mDotEnabled = enabled;
    }

    public boolean hasItems(){
        return !Predicates.isEmpty(mItems);
    }

    /** return select item , null means no changed. */
    public Item selectNextItem(){
        final Item oldItem = mSelectItem;
        int index = mItems.indexOf(oldItem);
        index += 1;
        if(index >= mItems.size()){
            index = 0;
        }
        mSelectItem = mItems.get(index);
        if(mSelectItem != oldItem){
            invalidate();
            return mSelectItem;
        }
        return null;
    }
    public void setItems(List<Item> mItems) {
        this.mItems = mItems;
        this.mSelectItem = mItems.get(0);
    }
    public void setSelectType(final int selectType) {
        this.mSelectItem = VisitServices.from(mItems).visitForQuery(new PredicateVisitor<Item>() {
            @Override
            public Boolean visit(Item item, Object param) {
                return item.type == selectType;
            }
        });
        invalidate();
    }
    public Item getSelectItem(){
        return mSelectItem;
    }

    public void setSelectItem(Item selectItem) {
        if(mItems.indexOf(selectItem) >=0){
            this.mSelectItem = selectItem;
        }else{
            throw new IllegalArgumentException();
        }
    }

    public static final class Item {
        Drawable icon;
        int type;
        String text;

        public static Item of(Context context, @DrawableRes int iconResId, int type) {
            Item item = new Item();
            item.icon = DrawableUtils2.getDrawable(context, iconResId);
            item.type = type;
            return item;
        }

        public String getText() {
            return text;
        }
        public void setText(String text) {
            this.text = text;
        }
    }

}
