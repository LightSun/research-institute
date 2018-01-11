package com.heaven7.vida.research;

import com.heaven7.vida.research.drag.DragActivity;

import java.util.List;

/**
 * Created by heaven7 on 2017/12/22.
 */
public class TestMainActivity extends AbsMainActivity {

    @Override
    protected void addDemos(List<ActivityInfo> list) {
        list.add(new ActivityInfo(MainActivity.class));
        list.add(new ActivityInfo(com.heaven7.vida.research.drag.MainActivity.class));
        list.add(new ActivityInfo(DragActivity.class));
    }

}
