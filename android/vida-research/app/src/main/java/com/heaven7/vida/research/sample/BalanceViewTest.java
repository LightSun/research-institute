package com.heaven7.vida.research.sample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.heaven7.vida.research.R;
import com.heaven7.vida.research.widget.BalanceView;

/**
 * Created by heaven7 on 2018/6/23 0023.
 */
public class BalanceViewTest extends AppCompatActivity {

    BalanceView mBV;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_test_balance_view);

        mBV = findViewById(R.id.balanceView);
    }
}
