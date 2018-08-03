package com.heaven7.ve.colorgap.impl.montage;

import com.heaven7.utils.Context;
import com.heaven7.utils.LoadException;
import com.heaven7.ve.MediaResourceItem;
import com.heaven7.ve.colorgap.MediaResourceLoader;
import com.heaven7.ve.colorgap.MediaResourceScanner;
import com.heaven7.ve.colorgap.VideoDataLoadUtils;

/**
 * @author heaven7
 */
public class MediaTagLoader extends MediaResourceLoader {

    @Override
    public void load(Context context, MediaResourceItem item, String filePath, VideoDataLoadUtils.LoadCallback callback) throws LoadException {
         VideoDataLoadUtils.loadTagData(context, filePath, callback);
    }

}
