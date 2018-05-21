package com.heaven7.ve.colorgap;


import com.heaven7.ve.Context;
import com.heaven7.ve.MediaResourceItem;

import java.util.List;
import java.util.concurrent.CyclicBarrier;

/**
 * Created by heaven7 on 2018/3/17 0017.
 */

public interface MediaAnalyser {

    List<MediaItem> analyse(Context context, List<MediaResourceItem> items, CyclicBarrier barrier);

    void cancel();

}
