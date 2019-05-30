package com.heaven7.vida.research.sample;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;

import com.heaven7.vida.research.BaseActivity;
import com.heaven7.vida.research.R;
import com.heaven7.vida.research.widget.TrapezoidPartsView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by heaven7 on 2019/5/30.
 */
public class TestTrapezoidPartsView extends BaseActivity {

    @BindView(R.id.tpv)
    TrapezoidPartsView mTpv;

    @Override
    protected int getLayoutId() {
        return R.layout.ac_test_trapezoid_parts;
    }

    @Override
    protected void onInitialize(Context context, Bundle savedInstanceState) {

    }

    @OnClick(R.id.bt1)
    public void onClick(View view){
        List<TrapezoidPartsView.TrapezoidPart> list = createTrapezoidParts();
        mTpv.setParts(list);
    }

    private List<TrapezoidPartsView.TrapezoidPart> createTrapezoidParts() {
        List<TrapezoidPartsView.TrapezoidPart> list = new ArrayList<>();
        int[] bgs = {
                R.drawable.plan_database_bg,
                R.drawable.plan_music_bg,
                R.drawable.plan_editorz_bg,
        };
        int[] icons = {
                R.drawable.plan_database,
                R.drawable.plan_music,
                R.drawable.plan_editor,
        };
        String[] strs = {
                "Projects",
                "Music",
                "Edit"
        };
        for (int i = 0 ; i < bgs.length ; i ++){
            TrapezoidPartsView.TrapezoidPart part = new TrapezoidPartsView.TrapezoidPart();
            part.setBgIcon(getDrawableBitmap(bgs[i]));
            part.setIcon(getResources().getDrawable(icons[i]));
            part.setText(strs[i]);
            list.add(part);
        }
        return list;
    }
    private Bitmap getDrawableBitmap(int resId){
       BitmapDrawable bd = (BitmapDrawable) getResources().getDrawable(resId);
       return bd.getBitmap();
    }
}
