package com.heaven7.utils;

import com.heaven7.java.base.anno.Nullable;
import com.heaven7.java.base.util.DefaultPrinter;
import com.heaven7.ve.colorgap.MediaPartItem;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * ffmpeg utils
 * @author heaven7
 */
public class FFmpegUtils {

    /**
     * build merge video cmd
     * @param concatPath the concat path
     * @param outVideoPath the out video path
     * @return the cmds
     */
    public static String[] buildMergeVideoCmd(String concatPath, String outVideoPath){
        //ffmpeg -safe 0 -f concat -i E:\\study\\github\\ffmpeg-merge-video\\concat.txt -c copy concat_output.mp4 -y
        List<String> cmds = new ArrayList<>();
        cmds.add("ffmpeg");
        cmds.add("-safe");
        cmds.add("0");
        cmds.add("-f");
        cmds.add("concat");
        cmds.add("-i");
        cmds.add(concatPath);
        cmds.add("-c");
        cmds.add("copy");
        cmds.add(outVideoPath);
        cmds.add("-y");
        String[] arr = new String[cmds.size()];
        return cmds.toArray(arr);
    }

    /**
     * build get video duration cmd
     * @param videoPath the video path
     * @return the cmds
     */
    public static String[] buildGetDurationCmd(String videoPath){
        List<String> cmds = new ArrayList<>();
        cmds.add("ffprobe");
        cmds.add("-i");
        cmds.add(videoPath);
        cmds.add("-show_entries");
        cmds.add("format=duration");
        cmds.add("-v");
        cmds.add("quiet");
        cmds.add("-of");
        cmds.add("csv=\"p=0\"");
        String[] arr = new String[cmds.size()];
        return cmds.toArray(arr);
    }
    public static String[] buildCutCmd(MediaPartItem item,String outDir){
        return buildCutCmd(item, outDir, null);
    }
    //ffmpeg  -i  F:\\videos\\wedding\\churchIn\\churchIn_C0006.mp4  -vcodec copy  -acodec copy -ss 00:00:25 -to 00:00:30 .cutout.mp4 -y
    //ffmpeg  -i  F:\\videos\\wedding\\churchIn\\churchIn_C0006.mp4  -vcodec copy  -acodec copy -ss 00:00:25.25 -to 00:00:29.75 .cutout2.mp4 -y
    /**
     * build cut video cmd. must use string[] . if you concat string. may cause bugs.
     * @param item the media item
     * @param outDir  the out dir
     * @param outPathArr  the out path to save to
     * @return the string array of cmds.
     */
    public static String[] buildCutCmd(MediaPartItem item,String outDir,@Nullable String[] outPathArr){
        File file = new File(outDir);
        if(!file.exists() && !file.mkdirs()){
            DefaultPrinter.getDefault().warn("FFmpegUtils", "buildCutCmd",
                    "mkdir failed. outDir = " + outDir);
        }

        float start = CommonUtils.frameToTime(item.videoPart.getStartTime(), TimeUnit.SECONDS);
        float end = CommonUtils.frameToTime(item.videoPart.getEndTime(), TimeUnit.SECONDS);
        String filePath = item.item.getFilePath();
        int index1 = filePath.lastIndexOf(File.separator);
        int index2 = filePath.lastIndexOf(".");
        String fileName = filePath.substring(index1 + 1, index2);

        String outPath = outDir + File.separator + fileName
                + "_" + String.format("%.2f", start) + "__"
                + String.format("%.2f", end) + filePath.substring(index2) ;
        if(outPathArr != null && outPathArr.length > 0) {
            outPathArr[0] = outPath;
        }

        List<String> cmds = new ArrayList<>();
        cmds.add("ffmpeg");  //不能有空格
        cmds.add("-i");
        cmds.add(item.item.getFilePath());
        cmds.add("-vcodec");
        cmds.add("copy");
        cmds.add("-acodec");
        cmds.add("copy");
        cmds.add("-ss");
        cmds.add(transferTime(start));
        cmds.add("-to");
        cmds.add(transferTime(end));
        cmds.add(outPath);
        cmds.add("-y");
        String[] arr = new String[cmds.size()];
        return cmds.toArray(arr);

       /* StringBuilder sb = new StringBuilder();
        sb.append("ffmpeg -i ")
                .append(item.item.getFilePath()).append(" ")
                .append("-vcodec copy -acodec copy -ss ")
                .append(transferTime(start)).append(" -to ")
                .append(transferTime(end)).append(" ")
                .append(outPath).append(" -y");
        return sb.toString();*/
    }
    //整数s, 小数ms
    private static String transferTime(float time) {
        String str = String.format("%.2f", time);
        int second = (int) time;
        int minute = 0;
        if(second >= 60){
            minute = second / 60;
            second -= minute * 60;
        }
        int hour = 0;
        if(minute > 60){
            hour = minute / 60;
            minute -= hour * 60;
        }
        return new StringBuilder()
                .append(hour > 9 ? hour : ("0" + hour)).append(":")
                .append(minute > 9 ? minute : ("0" + minute)).append(":")
                .append(second > 9 ? second : ("0" + second))
                .append(str.substring(str.lastIndexOf(".")))
                .toString();
    }

}
