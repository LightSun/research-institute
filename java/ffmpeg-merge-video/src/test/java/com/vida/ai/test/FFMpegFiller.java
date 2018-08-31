package com.vida.ai.test;

import com.heaven7.core.util.Logger;
import com.heaven7.ve.colorgap.ColorGapManager;
import com.heaven7.ve.gap.GapManager;
import com.heaven7.ve.test.util.FFmpegVideoHelper;

import java.util.List;

/**
 * @author heaven7
 */
public class FFMpegFiller implements ColorGapManager.FillCallback {

    private static final String TAG = "FFMpegFiller";
    private final String outDir;

    public FFMpegFiller(String outDir) {
        this.outDir = outDir;
    }

    @Override
    public void onFillFinished(ColorGapManager.FillResult result) {
        if(result == null){
            Logger.w(TAG, "onFillFinished", "fill failed.");
        }else{
            List<GapManager.GapItem> gapItems = result.nodes;
//ffmpeg -safe 0 -f concat -i E:\\study\\github\research-institute\\java\\ffmpeg-merge-video\\cut_videos\\story2\\concat.txt -c copy concat_output.mp4 -y
            FFmpegVideoHelper.buildVideo(result.resultTemplate, gapItems, outDir);
            Logger.d(TAG, "onFillFinished", "fill success. dir is " + outDir);
        }
    }

}
