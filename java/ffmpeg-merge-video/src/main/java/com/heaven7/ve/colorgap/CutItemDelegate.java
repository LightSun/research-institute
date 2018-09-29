package com.heaven7.ve.colorgap;


import com.heaven7.utils.Context;
import com.heaven7.ve.cross_os.IMediaResourceItem;

import java.util.List;

/**
 * the cut item delegate
 * @author heaven7
 */
public interface CutItemDelegate {

    /**
     * the image meta of video
     * @return the image meta
     */
    MetaInfo.ImageMeta getImageMeta();

    MediaPartItem asPart(Context context);

    List<FrameTags> getVideoTags();

    IMediaResourceItem getItem();
}
