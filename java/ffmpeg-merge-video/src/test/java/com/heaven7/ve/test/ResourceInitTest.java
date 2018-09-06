package com.heaven7.ve.test;

import com.heaven7.java.base.util.DefaultPrinter;
import com.heaven7.ve.BaseMediaResourceItem;
import com.heaven7.ve.colorgap.ResourceInitializer;
import org.junit.Test;


public class ResourceInitTest {
    private static final String TAG = "ResourceInitTest";

    @Test
    public void test1(){
        String videoPath = "F:\\videos\\wedding\\character\\character_01.mp4";
        String dir = "F:\\videos\\wedding";
        BaseMediaResourceItem item = new BaseMediaResourceItem();
        item.setFilePath(videoPath);
        ResourceInitializer.init(null);
        String path = ResourceInitializer.getFilePathOfTags(item, dir);
        DefaultPrinter.getDefault().debug(TAG, "test1",  path);


       /* try {
            FileInputStream in = new FileInputStream(path);
            in.read();
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }
}

