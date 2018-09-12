package com.heaven7.test;

import com.heaven7.utils.CmdHelper;
import com.heaven7.utils.FFmpegUtils;
import org.junit.Test;

public class FFmpegTest {

    @Test
    public void testGetCreateTime(){
        String path = "F:\\videos\\story4\\storyTest\\C0013.mp4";
        new CmdHelper(FFmpegUtils.buildGetCreateTimeCmd(path)).execute(new CmdHelper.VideoCreateTimeCallback());

        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testExtractImages(){
       String video = "F:\\videos\\ClothingWhite\\LM0A0215.mp4";
       String outDir = "F:\\videos\\temp_works\\ClothingWhite_none\\215";
       String cmd = "ffmpeg -i " + video + " -r 1 " + outDir + "/img-%05d.bmp -y";
       new CmdHelper(getCmds(cmd)).execute();
    }

    private static String[] getCmds(String cmd){
        return cmd.split(" ");
    }

}
