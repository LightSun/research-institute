package com.heaven7.ve.colorgap.impl;


import com.heaven7.utils.Context;
import com.heaven7.utils.LoadException;

import com.heaven7.ve.colorgap.MediaResourceLoader;
import com.heaven7.ve.colorgap.VideoDataLoadUtils;
import com.heaven7.ve.cross_os.IMediaResourceItem;

/**
 * only used for video
 * Created by heaven7 on 2018/4/16 0016.
 */

/*public*/ class TagsLoader extends MediaResourceLoader {

    @Override
    public void load(Context context, IMediaResourceItem item, String filePath,
                     VideoDataLoadUtils.LoadCallback callback) throws LoadException {
        VideoDataLoadUtils.loadTagData(context, filePath, callback);
    }
}
