package com.heaven7.test;

import com.heaven7.utils.CmdHelper;
import com.heaven7.utils.FFmpegUtils;

/**
 * @author heaven7
 */
public class TestProcess_InhertIO {

    public static void main(String[] args) {
        startNormal();
    }

    private static void startNormal(){
        String concatFile = "E:\\study\\github\\research-institute\\java\\ffmpeg-merge-video\\cut_videos\\story3_dinner\\concat.txt";
        String out1 = "E:\\study\\github\\research-institute\\java\\ffmpeg-merge-video\\cut_videos\\story3_dinner\\normal.mp4";
        String[] strs = FFmpegUtils.buildMergeVideoCmd(concatFile, out1);
        new CmdHelper(strs).execute(new CmdHelper.LogCallback(){
            @Override
            public void beforeStartCmd(CmdHelper helper, ProcessBuilder pb) {
                super.beforeStartCmd(helper, pb);
                pb.inheritIO();
            }
            @Override
            public void collect(CmdHelper helper, String line) {
                super.collect(helper, line);
                System.out.println("line: " + line);
            }
        });
    }


}
