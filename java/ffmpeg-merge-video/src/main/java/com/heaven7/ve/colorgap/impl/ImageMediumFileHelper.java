package com.heaven7.ve.colorgap.impl;

import com.heaven7.utils.ConcurrentUtils;
import com.heaven7.ve.Context;
import com.heaven7.ve.colorgap.MediaItem;

import java.util.List;
import java.util.concurrent.CyclicBarrier;

/**
 * the medium file helper help we scan tags, rects, and load the medium file.
 * @author heaven7
 */
public class ImageMediumFileHelper {

    public static final int MODE_SINGLE_TAG   = 1;
    public static final int MODE_TAGS         = 2;
    public static final int MODE_SINGLE_RECT  = 1 << 8;
    public static final int MODE_RECTS        = 2 << 8;

    private final boolean singleTag;
    private final boolean singleRect;
    private final ImageAnalyseHelper mImageHelper = new ImageAnalyseHelper(new MockImageRectsScanner(),
            new MockImageTagsScanner());

    public ImageMediumFileHelper(int mMode) {
        singleTag = (mMode & 0xff) == MODE_SINGLE_TAG ;
        singleRect = (mMode & 0xff00) == MODE_SINGLE_RECT;
    }

    public void cancel(){
        ConcurrentUtils.shutDownNow();
    }

    public void scanAndLoad(Context context, List<MediaItem> items, final CyclicBarrier barrier){
         mImageHelper.scanAndLoad(context, items, singleTag, singleRect, barrier);
    }

}
