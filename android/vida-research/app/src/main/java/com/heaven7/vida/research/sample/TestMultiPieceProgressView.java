package com.heaven7.vida.research.sample;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.heaven7.vida.research.BaseActivity;
import com.heaven7.vida.research.R;
import com.heaven7.vida.research.widget.MultiPieceProgressView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by heaven7 on 2019/2/23.
 */
public class TestMultiPieceProgressView extends BaseActivity {

    @BindView(R.id.mppv)
    MultiPieceProgressView mMPPV;


    @Override
    protected int getLayoutId() {
        return R.layout.ac_test_multi_piece_pb;
    }

    @Override
    protected void onInitialize(Context context, Bundle savedInstanceState) {
        mMPPV.setPieceProvider(new MultiPieceProgressView.PieceProvider() {
            @Override
            public int getPieceCount() {
                return 5;
            }
            @Override
            public long getPieceDuration(int index) {
                //3.5 3.5 111
                if(index % 2 == 0){
                    return 10;
                }
                return 35;
            }
            @Override
            public long getTotalDuration() {
                return 100;
            }
        });
    }

    @OnClick(R.id.bt_change_frame_index)
    public void onClickChangeFrameIndex(View view){
        mMPPV.setFrameIndex(mMPPV.getFrameIndex() + 1);
    }
}
