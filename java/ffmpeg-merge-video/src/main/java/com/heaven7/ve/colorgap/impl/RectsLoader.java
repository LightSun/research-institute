package com.heaven7.ve.colorgap.impl;

import com.heaven7.ve.Context;
import com.heaven7.ve.MediaResourceItem;
import com.heaven7.ve.colorgap.MediaResourceLoader;
import com.heaven7.ve.colorgap.VideoDataLoadUtils;

/**
 * Created by heaven7 on 2018/4/16 0016.
 */

/*public*/ class RectsLoader extends MediaResourceLoader {
    @Override
    public void load(Context context, MediaResourceItem item, String filePath, VideoDataLoadUtils.LoadCallback callback) {
        VideoDataLoadUtils.loadRectData(context, filePath, callback);
    }
}
