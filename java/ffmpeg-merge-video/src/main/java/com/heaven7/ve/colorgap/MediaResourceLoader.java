package com.heaven7.ve.colorgap;


import com.heaven7.utils.Context;
import com.heaven7.utils.LoadException;
import com.heaven7.ve.cross_os.IMediaResourceItem;


/**
 * the media resource loader
 * Created by heaven7 on 2018/4/16 0016.
 */

public abstract class MediaResourceLoader {

    /**
     * load the scan result file of media resource
     * @param context the context
     * @param item the media resource item
     * @param filePath the file path of scan result. often is the generate tag/face file
     * @param callback the load callback
     */
    public abstract void load(Context context, IMediaResourceItem item, String filePath, VideoDataLoadUtils.LoadCallback callback)
               throws LoadException;
}
