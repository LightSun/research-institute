package heaven7.coordinatorlayoutbehavior.study1.sample;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;

import butterknife.OnClick;
import heaven7.coordinatorlayoutbehavior.study1.BaseActivity;
import heaven7.coordinatorlayoutbehavior.study1.R;

/**
 * Created by heaven7 on 2017/11/28 0028.
 */

public class TestBehavior1 extends BaseActivity {
    @Override
    protected int getLayoutId() {
        return R.layout.test_behavior1;
    }

    @Override
    protected void onInit(Context context, Bundle savedInstanceState) {
    }

    @OnClick(R.id.fab)
    public void onClickFab(View view) {
        Snackbar.make(view, "FAB", Snackbar.LENGTH_LONG)
                .setAction("cancel", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //这里的单击事件代表点击消除Action后的响应事件

                    }
                })
                .show();
    }
}
