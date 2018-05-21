package com.heaven7.ve.colorgap;


import com.heaven7.ve.colorgap.impl.TagBasedShotCutter;
import com.heaven7.ve.colorgap.impl.VideoCutterImpl;

import java.util.List;

/**
 * Created by heaven7 on 2018/3/15 0015.
 */

public abstract class VideoCutter {

   public abstract List<MediaPartItem> cut(List<CutInfo.PlaidInfo> musicInfos, List<MediaItem> items);

   public static VideoCutter of(List<MediaItem> items){
       if(hasRawTags(items)){
          return new TagBasedShotCutter();
       }else{
          return new VideoCutterImpl();
       }
   }

   private static boolean hasRawTags(List<MediaItem> items) {
      int tagedCount = 0;
      for (MediaItem item : items) {
         if (item.imageMeta != null && item.imageMeta.hasRawTags()) {
            tagedCount += 1;
         }
      }
      return tagedCount * 1f / items.size() > 0.5f;
   }
}
