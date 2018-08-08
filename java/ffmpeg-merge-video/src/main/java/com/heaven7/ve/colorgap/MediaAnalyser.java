package com.heaven7.ve.colorgap;


import com.heaven7.utils.Context;
import com.heaven7.ve.MediaResourceItem;

import java.util.List;
import java.util.concurrent.CyclicBarrier;

/**
 * Created by heaven7 on 2018/3/17 0017.
 */

public interface MediaAnalyser {

    List<MediaItem> analyse(Context context, List<MediaResourceItem> items, CyclicBarrier barrier);

    /**
     * get the async module count .
     * @return the async module count
     */
    int getAsyncModuleCount();

    void cancel();

    /**
     * pre load data.
     * @param param the extra param
     */
    void preLoadData(ColorGapParam param);
}
