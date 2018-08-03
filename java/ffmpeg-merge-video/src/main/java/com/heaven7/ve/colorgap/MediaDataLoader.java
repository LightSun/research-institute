package com.heaven7.ve.colorgap;

import com.heaven7.utils.Context;
import com.heaven7.ve.MediaResourceItem;

/**
 * @author heaven7
 */
public interface MediaDataLoader {

    /**
     * load media data from target data path. like high-light data
     * @param context the context
     * @param item the media resource item
     * @param dataPath the data path.  like high-light data path
     * @return the whole data
     */
    Object load(Context context, MediaResourceItem item, String dataPath);
}
