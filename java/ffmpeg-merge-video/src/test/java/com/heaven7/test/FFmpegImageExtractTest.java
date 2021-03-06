package com.heaven7.test;

import com.heaven7.utils.CmdHelper;
import com.heaven7.utils.FFmpegUtils;
import com.vida.common.ImageExtractCmd;
import org.junit.Test;

/**
 * extract image from video. which use ffmpeg
 */
public class FFmpegImageExtractTest extends BaseTest{


    // ffmpeg -i F:\\videos\\jiege_1\\褂子\\VID_20180602_124118.mp4 -r 1 -f image2 image-%05d.jpeg
    //ffmpeg -i F:\\videos\\jiege_1\\褂子\\VID_20180602_124118.mp4 -r 1 -ss 00:00:08 -vframes 3 F:\\videos\\jiege_1\褂子\\imgs\\img-%05d.jpeg

    @Test
    public void test1(){
        //extract -> gen highlight info -> key-point ->
        ImageExtractCmd cmd = new ImageExtractCmd.Builder()
                .setVideoPath("F:\\videos\\jiege_1\\褂子\\VID_20180602_124118.mp4")
                .setCountEverySecond(1)
                .setFrameCount(3)
                .setStartTime(8)
                .setSavePath("F:\\videos\\jiege_1\\褂子\\imgs")
                .build();
        String[] cmds = FFmpegUtils.buildImageExtractCmd(cmd, true);
        new CmdHelper(cmds).execute();
        System.out.println("Done");
    }

    @Test
    public void test2(){
        ImageExtractCmd cmd = new ImageExtractCmd.Builder()
                .setVideoPath("F:\\videos\\ClothingWhite\\LM0A0215.mp4")
                .setCountEverySecond(1)
                //.setFrameCount(3)
               // .setStartTime(8)
                .setSavePath("F:\\videos\\ClothingWhite\\temp\\LM0A0215")
                .setJpg(false)
                .build();
        String[] cmds = FFmpegUtils.buildImageExtractCmd(cmd, true);
        new CmdHelper(cmds).execute();
        System.out.println("Done");
    }
}
