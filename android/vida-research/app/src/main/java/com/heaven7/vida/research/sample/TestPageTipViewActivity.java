package com.heaven7.vida.research.sample;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.heaven7.core.util.Logger;
import com.heaven7.vida.research.R;
import com.heaven7.vida.research.widget.PageTipView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by heaven7 on 2018/11/22 0022.
 */
public class TestPageTipViewActivity extends AppCompatActivity {

    private static final String TAG = "TestPageTipView";
    private static final int[] IMAGE_RESS = {R.drawable.a, R.drawable.b, R.drawable.c, R.drawable.d,
            R.drawable.e, R.drawable.a, R.drawable.b, R.drawable.c};

    @BindView(R.id.pageTipView)
    PageTipView mPTV;

    @BindView(R.id.vp)
    ViewPager mVp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_test_page_tip);
        ButterKnife.bind(this);

        mPTV.setTipCount(IMAGE_RESS.length);
        mVp.setPageMargin(40);
        // setPageListener();
        mVp.addOnPageChangeListener(new PageListenerImpl());
        setAdapter();
    }

    private void setPageListener() {
        mVp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            int mDraggingIndex ;
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                final int realPosition = getRealPosition(position);
                Logger.d(TAG, "onPageScrolled", "position = " + realPosition
                        + " ,positionOffset = " + positionOffset + " ,positionOffsetPixels = " + positionOffsetPixels);
                if(positionOffset == 0f){
                    Logger.w(TAG, "onPageScrolled", "positionOffset == 0 ... now...");
                    mPTV.setSelectPosition(realPosition);
                    mPTV.setOffsetDirection(PageTipView.OFFSET_NONE);
                }else if(mDraggingIndex == realPosition){
                    //to left
                    Logger.d(TAG, "onPageScrolled", "to left");
                    mPTV.setOffsetDirection(PageTipView.OFFSET_LEFT);
                }else{
                    Logger.d(TAG, "onPageScrolled", "to right");
                    mPTV.setOffsetDirection(PageTipView.OFFSET_RIGHT);
                }
                mPTV.setPositionOffset(positionOffset);
            }

            @Override
            public void onPageSelected(int position) {
                position = getRealPosition(position);
                Logger.d(TAG, "onPageSelected", "position = " + position);
               // mPTV.setSelectPosition(position);
               // mPTV.setOffsetDirection(PageTipView.OFFSET_NONE);
            }
            @Override
            public void onPageScrollStateChanged(int state) {
                if(state == ViewPager.SCROLL_STATE_DRAGGING){
                    mDraggingIndex = getRealPosition(mVp.getCurrentItem());
                }else if(state == ViewPager.SCROLL_STATE_IDLE){
                   // mPTV.setSelectPosition(getRealPosition(mVp.getCurrentItem()));
                    mPTV.setOffsetDirection(PageTipView.OFFSET_NONE);
                }
            }
        });
    }

    private void setAdapter() {
        mVp.setAdapter(new PagerAdapter() {
            @Override
            public Object instantiateItem(final ViewGroup container, final int position) {
                ImageView view = new ImageView(container.getContext());
                view.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                final int realPosition = getRealPosition(position);
                view.setImageResource(IMAGE_RESS[realPosition]);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(container.getContext(), "click position= " + realPosition, Toast.LENGTH_SHORT).show();
                        mVp.setCurrentItem(position, true);
                    }
                });
                container.addView(view);
                return view;
            }


            @Override
            public int getItemPosition(Object object) {
                return POSITION_NONE;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }

            @Override
            public int getCount() {
               // return Integer.MAX_VALUE;
                return IMAGE_RESS.length;
            }

            @Override
            public boolean isViewFromObject(View view, Object o) {
                return view == o;
            }

            @Override
            public float getPageWidth(int position) {
                return 0.35f;
            }

          /*  @Override
            public void startUpdate(ViewGroup container) {
                super.startUpdate(container);
                ViewPager viewPager = (ViewPager) container;
                int position = viewPager.getCurrentItem();
                if (position == 0) {
                    position = getFirstItemPosition();
                } else if (position == getCount() - 1) {
                    position = getLastItemPosition();
                }
                viewPager.setCurrentItem(position, false);
            }*/
        });
        mVp.setCurrentItem(0);
    }

    private int getRealCount() {
        return IMAGE_RESS.length;
    }

    private int getRealPosition(int position) {
        return position % getRealCount();
    }

    private int getFirstItemPosition() {
        return Integer.MAX_VALUE / getRealCount() / 2 * getRealCount();
    }

    private int getLastItemPosition() {
        return Integer.MAX_VALUE / getRealCount() / 2 * getRealCount() - 1;
    }

    private class PageListenerImpl implements ViewPager.OnPageChangeListener{

        private int mScrollState;
        private float mLastKnownPositionOffset = -1;

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            if (positionOffset > 0.5f) {
                // Consider ourselves to be on the next page when we're 50% of the way there.
               // position++;
            }
            updatePosition(position, positionOffset, false);
            mLastKnownPositionOffset = positionOffset;
        }

        @Override
        public void onPageSelected(int position) {
            if (mScrollState == ViewPager.SCROLL_STATE_IDLE) {
                // Only update the text here if we're not dragging or settling.
                // updateText(mPager.getCurrentItem(), mPager.getAdapter());
                mPTV.setSelectPosition(getRealPosition(position));

                final float offset = mLastKnownPositionOffset >= 0 ? mLastKnownPositionOffset : 0;
                updatePosition(mVp.getCurrentItem(), offset, true);
            }
        }
        @Override
        public void onPageScrollStateChanged(int state) {
            mScrollState = state;
        }

        private void updatePosition(int position, float positionOffset, boolean force) {
            position = getRealPosition(position);
            mPTV.setOffsetDirection(PageTipView.OFFSET_LEFT);
            mPTV.setSelectPosition(position);
            mPTV.setPositionOffset(positionOffset);
        }
    }
}
