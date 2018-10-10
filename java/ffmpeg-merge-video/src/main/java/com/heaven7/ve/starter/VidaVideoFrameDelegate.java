package com.heaven7.ve.starter;

import com.heaven7.java.base.util.Logger;
import com.heaven7.java.image.ImageLimitInfo;
import com.heaven7.java.image.ImageReader;
import com.heaven7.java.image.Matrix2;
import com.heaven7.java.image.detect.AbstractVideoManager;
import com.heaven7.utils.CmdHelper;
import com.heaven7.utils.FileUtils;
import com.heaven7.ve.Constants;
import com.vida.common.FFMpegCmdGenerator;
import com.vida.common.Platform;

import java.io.File;

/**
 * @author heaven7
 */
public class VidaVideoFrameDelegate implements AbstractVideoManager.VideoFrameDelegate {

    private final JavaImageReader mReader = JavaImageReader.DEFAULT;
    private final FFMpegCmdGenerator mCmdGen = Platform.getDefault().getFFMpegCmdGenerator(false);

    @Override
    public ImageReader.ImageInfo getFrame(String videoFile, int timeInSeconds, ImageLimitInfo info) {
        String imagePath = getFrameImagePath(videoFile, timeInSeconds);
        return mReader.readBytes(imagePath, "jpg", info);
    }
    @Override
    public ImageReader.ImageInfo getFrameMatrix(String videoFile, int timeInSeconds, ImageLimitInfo info) {
        String imagePath = getFrameImagePath(videoFile, timeInSeconds);
        return mReader.readMatrix(imagePath, info);
    }

    @Override
    public int getDuration(String videoFile) {
        String[] cmds = mCmdGen.buildGetDurationCmd(videoFile);
        CmdHelper.VideoDurationCallback callback = new CmdHelper.VideoDurationCallback();
        new CmdHelper(cmds).execute(callback);
        return (int) (callback.getDuration() / 1000);
    }
    @Override
    public String getFrameImagePath(String videoFile, int time) {
        if(time == -1){
            Logger.w("VidaVideoFrameDelegate", "getFrameImagePath",
                    "time = -1, videoFile = " + videoFile);
        }
        //image from 00001. time from 0
        String fileName = FileUtils.getFileName(videoFile);
        //videofile =xxx/projectid/resource/xxx.mp4
        //temp = xxx/projectid/temp/xxx/xxx.jpg
        String fileDir = FileUtils.getFileDir(videoFile, 2, true);
        return fileDir + File.separator + Constants.DIR_TEMP + File.separator + fileName + File.separator
                + "img_" + format(time + 1) + ".jpg";
    }
    public static String format(int time) {
        switch (String.valueOf(time).length()) {
            case 1:
                return "0000" + time;

            case 2:
                return "000" + time;
            case 3:
                return "00" + time;
            default:
                throw new UnsupportedOperationException();
        }
    }
}
