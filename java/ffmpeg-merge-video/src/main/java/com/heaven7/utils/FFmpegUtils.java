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
     * build get create time of video
     * @param videoPath the video path
     * @return the cmds
     */
    public static String[] buildGetCreateTimeCmd(String videoPath){
        //ffmpeg -safe 0 -f concat -i E:\\study\\github\\ffmpeg-merge-video\\concat.txt -c copy concat_output.mp4 -y
        List<String> cmds = new ArrayList<>();
        cmds.add("ffprobe");
        cmds.add("-i");
        cmds.add(videoPath);
        String[] arr = new String[cmds.size()];
        return cmds.toArray(arr);
    }

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
    /** time in seconds */
    public static String transferTime(float time) {
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

    /**
     * build the cmd of extract image from video .
     * @param cmd the image extract cmd
     * @return the cmds
     */
    public static String[] buildImageExtractCmd(ImageExtractCmd cmd, boolean addStartPrefix){
        List<String> cmds = new ArrayList<>();
        //"cmd","/c","start"
        if(addStartPrefix) {
            cmds.add("cmd");
            cmds.add("/c");
            cmds.add("start");
            cmds.add("/wait");
        }
        cmds.add("ffmpeg");
        cmds.add("-i");
        cmds.add(cmd.getVideoPath());
        cmds.add("-r");
        cmds.add(cmd.getCountEverySecond() + "");
        cmds.add("-ss");
        cmds.add(FFmpegUtils.transferTime(cmd.getStartTime()));
        if(cmd.getFrameCount() > 0){
            cmds.add("-vframes");
            cmds.add(cmd.getFrameCount() + "");
        }
        // cmds.add("-f");
        // cmds.add("image2");
        if(!TextUtils.isEmpty(cmd.getResolution())){
            cmds.add("-s");
            cmds.add(cmd.getResolution());
        }
        //5bit int
        if(cmd.getSavePath().contains(".")){//a file path
            cmds.add(cmd.getSavePath());
        }else {
            //jpg and jpeg Lossy for image quality
            cmds.add(cmd.getSavePath() + File.separator + "img_%05d.png");
        }
        cmds.add("-y");

        String[] arr = new String[cmds.size()];
        return cmds.toArray(arr);
    }

    /**
     * the cmds of image extract from video
     * @author heaven7
     */
    public static class ImageExtractCmd{
        /** -i,  MUST */
        private String videoPath;
        /** extract image rate in every second(-r) . MUST*/
        private int countEverySecond = 1;
        /** the image save format(-f) */
        private String imageFormat;

        /** the start time (in seconds) to extract(-ss) */
        private float startTime;
        /** the extract count.(-vframes) */
        private int frameCount;

        /** like '-s 800*600' */
        private String resolution;

        /** can be a jpeg file path or just a dir. MUST */
        private String savePath;

        protected ImageExtractCmd(ImageExtractCmd.Builder builder) {
            this.videoPath = builder.videoPath;
            this.countEverySecond = builder.countEverySecond;
            this.imageFormat = builder.imageFormat;
            this.startTime = builder.startTime;
            this.frameCount = builder.frameCount;
            this.resolution = builder.resolution;
            this.savePath = builder.savePath;
        }

        public void setVideoPath(String videoPath) {
            this.videoPath = videoPath;
        }

        public void setCountEverySecond(int countEverySecond) {
            this.countEverySecond = countEverySecond;
        }
        public void setImageFormat(String imageFormat) {
            this.imageFormat = imageFormat;
        }
        /** the start time (in seconds)*/
        public void setStartTime(float startTime) {
            this.startTime = startTime;
        }
        public void setFrameCount(int frameCount) {
            this.frameCount = frameCount;
        }

        public void setSavePath(String savePath) {
            this.savePath = savePath;
        }

        public void setResolution(String resolution) {
            this.resolution = resolution;
        }

        public String getVideoPath() {
            return this.videoPath;
        }

        public int getCountEverySecond() {
            return this.countEverySecond;
        }

        public String getImageFormat() {
            return this.imageFormat;
        }

        public float getStartTime() {
            return this.startTime;
        }

        public int getFrameCount() {
            return this.frameCount;
        }

        public String getResolution() {
            return this.resolution;
        }

        public String getSavePath() {
            return this.savePath;
        }

        public static class Builder {
            /** -i,  MUST */
            private String videoPath;
            /** extract image rate in every second(-r) . MUST*/
            private int countEverySecond = 1;
            /** the image save format(-f) */
            private String imageFormat;
            /** the start time (in seconds) to extract(-ss) */
            private float startTime = 0f;
            /** the extract count.(-vframes) */
            private int frameCount;
            /** like '-s 800*600' */
            private String resolution;
            /** can be a jpeg file path or just a dir. MUST */
            private String savePath;

            public Builder setVideoPath(String videoPath) {
                this.videoPath = videoPath;
                return this;
            }

            public Builder setCountEverySecond(int countEverySecond) {
                this.countEverySecond = countEverySecond;
                return this;
            }

            public Builder setImageFormat(String imageFormat) {
                this.imageFormat = imageFormat;
                return this;
            }

            public Builder setStartTime(float startTime) {
                this.startTime = startTime;
                return this;
            }

            public Builder setFrameCount(int frameCount) {
                this.frameCount = frameCount;
                return this;
            }

            public Builder setResolution(String resolution) {
                this.resolution = resolution;
                return this;
            }

            public Builder setSavePath(String savePath) {
                this.savePath = savePath;
                return this;
            }

            public ImageExtractCmd build() {
                return new ImageExtractCmd(this);
            }
        }
    }

}
