package com.heaven7.ve.test;

import com.heaven7.ve.BaseMediaResourceItem;
import org.junit.Test;


public class ResourceInitTest {
    private static final String TAG = "ResourceInitTest";

    @Test
    public void test1(){
        String videoPath = "F:\\videos\\wedding\\character\\character_01.mp4";
        String dir = "F:\\videos\\wedding";
        BaseMediaResourceItem item = new BaseMediaResourceItem();
        item.setFilePath(videoPath);
    }
}

