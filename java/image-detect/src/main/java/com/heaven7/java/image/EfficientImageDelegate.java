package com.heaven7.java.image;

import com.heaven7.java.base.anno.Nullable;

import java.util.List;

/**
 * Efficient Image Delegate which used for batch image
 * @author heaven7
 */
public interface EfficientImageDelegate {

    /**
     * get the image data of multi image
     * @param videoPath the video path
     * @param batchSize the batch size
     * @param startTime the start time as seconds from video. include
     * @param duration the duration of video , in seconds
     * @param frameGap the frame gap .in seconds. often is 1.
     * @param limitInfo the limit image info.
     * @return the batched image info. .can be null
     */
    @Nullable EfficientImageInfo getImageData(String videoPath, int startTime, int duration,
                                              int batchSize, int frameGap, ImageLimitInfo limitInfo);

     class EfficientImageInfo{
         /** merged data of multi image */
         public byte[] mergedData;
         /** the total frame count current used. contains used or skipped by frame gap. */
         public int totalFrameCount;
         /** used frame times from video. such as {3,4,5,6} */
         public List<Integer> times;
         /** the basic info of every frame. */
         public BasicImageInfo baseImageInfo;
     }
}
