package com.heaven7.ve.colorgap;


import com.heaven7.utils.Context;
import com.heaven7.ve.cross_os.IMediaResourceItem;


/**
 * the media resource scanner
 * Created by heaven7 on 2018/4/16 0016.
 */

public abstract class MediaResourceScanner {

    /**
     * scan the media file and put it to the target ImageMeta.
     *
     * @param context the context
     * @param item    the media file item
     * @param srcDir  the source dir
     * @return the full path of scan result file. eg: rects, tags.
     */
    public abstract String scan(Context context, IMediaResourceItem item, String srcDir);

}
