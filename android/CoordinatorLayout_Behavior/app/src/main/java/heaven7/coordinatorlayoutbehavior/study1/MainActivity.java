package heaven7.coordinatorlayoutbehavior.study1;


import java.util.List;

import heaven7.coordinatorlayoutbehavior.study1.sample.TestBehavior1;


/**
 * Created by heaven7 on 2017/7/12 0012.
 */
public class MainActivity extends AbsMainActivity {

    @Override
    protected void addDemos(List<ActivityInfo> list) {
        //just for test multi module

        list.add(new ActivityInfo(TestBehavior1.class, "TestBehavior1"));
    }
}
