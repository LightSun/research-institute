package com.heaven7.ve.colorgap.impl;

import com.heaven7.java.base.util.Logger;
import com.heaven7.utils.CommonUtils;
import com.heaven7.utils.Context;
import com.heaven7.ve.colorgap.*;
import com.heaven7.ve.cross_os.IPlaidInfo;
import com.heaven7.ve.cross_os.ITimeTraveller;
import com.heaven7.ve.cross_os.VEFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * the Average Split Shot Cutter
 * Created by heaven7 on 2018/3/16 0016.
 */

public class VideoCutterImpl extends VideoCutter {

    @Override
    public List<MediaPartItem> cut(Context mContext, List<IPlaidInfo> plaids, List<MediaItem> items) {
        //if video src.count > music.bag.size
        // -> color
        /*
         * 1, 计算缺少的视频段数。
         * 2, 按照视频从长到短，一段一段切割
         * 3, 校正视频part长度直到满足要求。可以左右挪动 (应该在填充的时候再去校正)
         */
        int lackCount = 0;
        final int itemSize = items.size();
        for (int i = 0 , size = plaids.size() ; i < size ; i ++){
            IPlaidInfo info = plaids.get(i);
            if(i >= itemSize){
                lackCount ++;
            }else {
                MediaItem item = items.get(i);
                if(item.item.isImage()){
                    continue;
                }
                //for first, only have source video. no parts.
                if (info.getDuration() > CommonUtils.timeToFrame(item.item.getDuration(), TimeUnit.MILLISECONDS)) {
                    //not enough. should cut.
                    item.addFlags(MediaItem.FLAG_INVALID);
                    lackCount ++;
                }
            }
        }
        //after mark leakCount. we remove the invalid video
        for(Iterator<MediaItem> it = items.iterator() ; it.hasNext(); ){
            final MediaItem item = it.next();
            if(!item.isValid()){
                it.remove();
                Logger.d("VideoCuttrtImpl", "cut", "start remove" +
                        " the invalid video item, len =" + item.item.getDuration()
                        + " ,path = " + item.item.getFilePath());
            }
        }
        //start handle lack video part
        if(lackCount > 0){
            List<MediaItem> opItems = new ArrayList<>(items);
            //for cut. we can only cut video, so remove image
            for(Iterator<MediaItem> it = opItems.iterator() ; it.hasNext(); ){
                if(it.next().item.isImage()){
                    it.remove();
                }
            }
            //sort duration desc.
            Collections.sort(opItems, new Comparator<MediaItem>() {
                @Override
                public int compare(MediaItem o1, MediaItem o2) {
                    return Long.compare(o2.item.getDuration(), o1.item.getDuration());
                }
            });
            final int sizeOfVideo = opItems.size();
            if(sizeOfVideo > 0) {
                //cut
                //计算每个视频需要切割的 次数s
                int count = lackCount / sizeOfVideo;
                int remainder = lackCount % sizeOfVideo; //余数
                int maxCount = remainder > 0 ? count + 1 : count;//每个视频源切割的最多次数

                int lastCutIndex = 0;
                //剩余切割次数 ， 按照最长的开始切
                int leftLackCount = lackCount;
                for (int i = lackCount - 1; i >= 0 && lastCutIndex < sizeOfVideo; i--) {
                    MediaItem item = opItems.get(lastCutIndex);
                    int curCount = Math.min(leftLackCount, maxCount);
                    cut(item, curCount);
                    leftLackCount -= curCount;
                    lastCutIndex += 1;
                    if (leftLackCount <= 0) {
                        //no need
                        break;
                    }
                }
                if (leftLackCount > 0) {
                    throw new IllegalStateException("cut failed.");
                }
            }
        }else{
            //no need cut
            Logger.d("VideoCutterImpl", "cut", "no need cut");
        }
        //populate to new parts.
        List<MediaPartItem> newItems = new ArrayList<>();
        for(MediaItem item : items){
            List<ITimeTraveller> parts = item.getVideoParts();
            //no parts . may be image
            if(parts == null || parts.size() == 0){
                long maxDuration = CommonUtils.timeToFrame(item.item.getDuration(), TimeUnit.MILLISECONDS);
                newItems.add(new MediaPartItem(mContext, (MetaInfo.ImageMeta) item.imageMeta.copy(), item.item,
                        VEFactory.getDefault().createTimeTraveller(0, maxDuration, maxDuration)));
            }else {
                for (ITimeTraveller part : parts) {
                    newItems.add(new MediaPartItem(mContext, (MetaInfo.ImageMeta) item.imageMeta.copy(), item.item, part));
                }
            }
        }
        return newItems;
    }

    //count: cut the media into the target count.
    private static void cut(MediaItem mediaItem, int cutCount) {
        //max duration in frames
        final long maxDuration = CommonUtils.timeToFrame(mediaItem.item.getDuration(), TimeUnit.MILLISECONDS);
        final int count = cutCount + 1;
        //split (partDuration in frames)
        long partDuration = maxDuration / count;
        for(int i = 0 ; i < count ; i++){
            long start = i * partDuration;
            ITimeTraveller tt = VEFactory.getDefault().createTimeTraveller(start, start + partDuration, maxDuration);
            mediaItem.addVideoPart(tt);
        }
        mediaItem.dump();
    }
}
