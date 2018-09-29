package com.heaven7.ve.test;


import com.heaven7.ve.cross_os.IMediaResourceItem;
import com.heaven7.ve.cross_os.VEFactory;
import org.junit.Test;


public class ResourceInitTest {
    private static final String TAG = "ResourceInitTest";

    @Test
    public void test1(){
        String videoPath = "F:\\videos\\wedding\\character\\character_01.mp4";
        String dir = "F:\\videos\\wedding";
        IMediaResourceItem item = VEFactory.getDefault().newMediaResourceItem();
        item.setFilePath(videoPath);
    }
}

