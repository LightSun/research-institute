package com.heaven7.ve.colorgap.impl;

import com.heaven7.utils.ConcurrentUtils;
import com.heaven7.utils.Context;
import com.heaven7.ve.colorgap.MediaItem;

import java.util.List;
import java.util.concurrent.CyclicBarrier;

/**
 * the medium file helper help we scan tags, rects, and load the medium file.
 * @author heaven7
 */
public class ImageMediumFileHelper {

    private final ImageAnalyseHelper mImageHelper = new ImageAnalyseHelper();

    public void cancel(){
        ConcurrentUtils.shutDownNow();
    }

    public void scanAndLoad(Context context, List<MediaItem> items, final CyclicBarrier barrier){
         mImageHelper.scanAndLoad(context, items, barrier);
    }

}
