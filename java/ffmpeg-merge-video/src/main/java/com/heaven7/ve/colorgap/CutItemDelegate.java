package com.heaven7.ve.colorgap;


import com.heaven7.ve.MediaResourceItem;

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

    MediaPartItem asPart();

    List<FrameTags> getVideoTags();

    MediaResourceItem getItem();
}
