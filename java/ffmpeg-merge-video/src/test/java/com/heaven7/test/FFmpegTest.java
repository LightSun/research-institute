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
}
