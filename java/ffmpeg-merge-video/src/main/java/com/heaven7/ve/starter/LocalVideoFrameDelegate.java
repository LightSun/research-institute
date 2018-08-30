package com.heaven7.ve.starter;

import com.heaven7.utils.FileUtils;
import com.heaven7.ve.Constants;

import java.io.File;

/**
 * @author heaven7
 */
public class LocalVideoFrameDelegate extends VidaVideoFrameDelegate {

    @Override
    public String getFrameImagePath(String videoFile, int time) {
        String fileName = FileUtils.getFileName(videoFile);
        String fileDir = FileUtils.getFileDir(videoFile, 1, true);
        //a/b/c.mp4 -> a/b/temp/c/xxx.jpg
        return fileDir + File.separator + Constants.DIR_TEMP + File.separator + fileName
                + File.separator + "img_" + format(time + 1) + ".jpg";
    }
}
