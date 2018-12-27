package com.heaven7.vida.research.sample;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.airbnb.lottie.LottieAnimationView;
import com.heaven7.adapter.BaseSelector;
import com.heaven7.adapter.QuickRecycleViewAdapter;
import com.heaven7.core.util.ViewHelper;
import com.heaven7.core.util.WeakHandler;
import com.heaven7.vida.research.BaseActivity;
import com.heaven7.vida.research.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by heaven7 on 2018/12/26.
 */
public class TestLottieActivity extends BaseActivity {

    private final InternalHandler mHandler = new InternalHandler(this);
    private QuickRecycleViewAdapter<Item> mAdapter;

    @BindView(R.id.lav)
    LottieAnimationView mLav;

    @BindView(R.id.rv)
    RecyclerView mRv;

    @Override
    protected int getLayoutId() {
        return R.layout.ac_test_lottie;
    }

    @Override
    protected void onInitialize(Context context, Bundle savedInstanceState) {
        mRv.setLayoutManager(new LinearLayoutManager(context));
        setAdapter();
    }

    @OnClick(R.id.bt_start)
    public void onClickStart(View view){
        float progress = mLav.getProgress();
        if(progress < 1){
            progress += 0.05f;
            mLav.setProgress(progress);
        }
    }

    private void setAdapter() {
        mRv.setAdapter(mAdapter = new QuickRecycleViewAdapter<Item>(R.layout.item_lottie, createItems()) {
            @Override
            protected void onBindData(Context context, final int position, Item item, int itemLayoutId, final ViewHelper helper) {
                LottieAnimationView view = helper.getView(R.id.lav);
                view.setProgress(item.getProcess());
                helper.setRootOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LottieAnimationView lav = helper.getView(R.id.lav);
                        sendMessage(position, lav);
                    }
                });
            }
        });
    }

    private void sendMessage(int position, LottieAnimationView lav){
        Object tag = lav.getTag(R.id.lav);
        int process = 1;
        if(tag != null){
            process = (int) tag + 1;
        }
        lav.setTag(R.id.lav, process);
        if(process <= 100) {
            //0-30 and 70- 100 should faster.
            Message msg = mHandler.obtainMessage(process, lav);
            msg.arg1 = position;
            int delay = process <= 30 || process >= 71 ? 20 : 30;
            mHandler.sendMessageDelayed(msg, delay);
        }
    }
    private void updateProcess(int position, float process) {
        mAdapter.getItem(position).setProcess(process);
        mAdapter.getAdapterManager().notifyItemChanged(position);
    }

    private List<Item> createItems() {
        List<Item> list = new ArrayList<>();
        for(int i = 0 ; i < 30 ; i ++){
            list.add(new Item());
        }
        return list;
    }

    static class Item extends BaseSelector{
        private float process;

        public float getProcess() {
            return process;
        }
        public void setProcess(float process) {
            this.process = process;
        }
    }

    private static class InternalHandler extends WeakHandler<TestLottieActivity>{

        public InternalHandler(TestLottieActivity testLottieActivity) {
            super(testLottieActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            LottieAnimationView lav = (LottieAnimationView) msg.obj;
           // lav.setProgress(msg.what * 1f / 100);
            get().updateProcess(msg.arg1, msg.what * 1f / 100);
            get().sendMessage(msg.arg1, lav);
        }
    }


}
