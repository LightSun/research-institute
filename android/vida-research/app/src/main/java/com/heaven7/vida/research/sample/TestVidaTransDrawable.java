package com.heaven7.vida.research.sample;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.heaven7.vida.research.R;
import com.heaven7.vida.research.drawable.VidaTransDrawable;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by heaven7 on 2018/11/6 0006.
 */
public class TestVidaTransDrawable extends AppCompatActivity {

    @BindView(R.id.iv)
    ImageView mIv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_test_trans_drawable);
        ButterKnife.bind(this);

        VidaTransDrawable vtd = new VidaTransDrawable();
        vtd.setBackgroundColor(Color.RED);
        vtd.setSize(80);
        vtd.setMiniDrawable(getResources().getDrawable(R.drawable.transition_dissolve));
       /* vtd.setTint(Color.LTGRAY);
        vtd.setTintMode(PorterDuff.Mode.SRC_IN);*/
        mIv.setImageDrawable(vtd);
    }
}
