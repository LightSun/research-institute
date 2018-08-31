package com.heaven7.ve.test;

import com.heaven7.utils.CmdHelper;
import com.heaven7.utils.CommonUtils;
import com.heaven7.utils.FFmpegUtils;
import com.heaven7.ve.MediaResourceItem;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.TimeUnit;

public class TestUtils {


    public static MediaResourceItem createImageItem(String path) {
        MediaResourceItem item = new MediaResourceItem();
        item.setFilePath(path);

        item.setTime(new File(path).lastModified());
        item.setDuration(0);
        item.setMime("image/jpg");
        return item;
    }
    public static MediaResourceItem createVideoItem(String path, long durationInMills){
       // String path ="F:\\videos\\test_cut\\test_shot_cut\\GP5A0859.mp4";
        MediaResourceItem item = new MediaResourceItem();
        item.setFilePath(path);

        item.setTime(new File(path).lastModified());
        item.setDuration(durationInMills);//03:16
        item.setMime("video/mp4");
        return item;
    }
    public static MediaResourceItem createVideoItem(String path){
        // String path ="F:\\videos\\test_cut\\test_shot_cut\\GP5A0859.mp4";
        String[] cmds = FFmpegUtils.buildGetDurationCmd(path);
        CmdHelper.VideoDurationCallback dc = new CmdHelper.VideoDurationCallback();
        new CmdHelper(cmds).execute(dc);
        long duration = dc.getDuration();
        //System.out.println(duration);

        MediaResourceItem item = new MediaResourceItem();
        item.setFilePath(path);
        item.setDuration(duration);
        item.setTime(new File(path).lastModified());
        item.setMime("video/mp4");
        return item;
    }

    public static void main(String[] args) {
        createVideoItem("F:\\videos\\ClothingWhite\\LM0A0205.mp4");
    }

    //some files is wrong.
    private static long getTime(String path) {
        File file = new File(path);
        BasicFileAttributes bAttributes;
        try {
            bAttributes = Files.readAttributes(file.toPath(),
                    BasicFileAttributes.class);
        } catch (IOException e) {
           throw new RuntimeException(e);
        }
        //String fileName = file.getName();
        return bAttributes.creationTime().toMillis();
    }

    private static long getTime2(String path) {
        CmdHelper.VideoCreateTimeCallback timeCallback = new CmdHelper.VideoCreateTimeCallback();
        new CmdHelper(FFmpegUtils.buildGetCreateTimeCmd(path))
                .execute(timeCallback);
        return timeCallback.getDateTime();
    }
}
