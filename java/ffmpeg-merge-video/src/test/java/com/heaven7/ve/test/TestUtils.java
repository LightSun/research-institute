package com.heaven7.ve.test;

import com.heaven7.ve.MediaResourceItem;

import java.io.File;

public class TestUtils {


    public static MediaResourceItem createVideoItem(String path, long durationInMills){
       // String path ="F:\\videos\\test_cut\\test_shot_cut\\GP5A0859.mp4";
        MediaResourceItem item = new MediaResourceItem();
        item.setFilePath(path);
        item.setTime(new File(path).lastModified());
        item.setDuration(durationInMills);//03:16
        item.setMime("video/mp4");
        return item;
    }

}
