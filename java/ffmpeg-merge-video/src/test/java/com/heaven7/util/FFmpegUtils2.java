package com.heaven7.util;

import com.heaven7.utils.CmdHelper;
import com.heaven7.utils.CommonUtils;
import com.heaven7.utils.FFmpegUtils;
import com.heaven7.utils.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author heaven7
 */
public class FFmpegUtils2 {

    public static void main(String[] args) {
        //mergeVideoAndAudio();
        //String video = "F:\\videos\\wedding\\churchIn\\churchIn_C0044.MP4";
        String audio = "E:\\tmp\\bugfinds\\out_music5\\musics\\2ab0165c6c2f3e378be2c63ce0b6422c.mp3";
        String outDir = "F:\\videos\\wedding\\churchIn\\tmp";
        String[] cmds = FFmpegUtils.buildCutCmd(audio, 0, CommonUtils.timeToFrame(60, TimeUnit.SECONDS), outDir);
        new CmdHelper(cmds).execute(new CmdHelper.InhertIoCallback());
    }

    public static String[] buildTransformFormatCmd(String inVideo, String outVideo) {
        List<String> cmds = new ArrayList<>();
        cmds.add("ffmpeg"); //on android no need this.
        cmds.add("-i");
        cmds.add(inVideo);
        cmds.add("-vcodec");
        cmds.add("copy");

        cmds.add("-acodec");
        cmds.add("copy");
        cmds.add(outVideo);
        cmds.add("-y");
        return cmds.toArray(new String[cmds.size()]);
    }
    public static String[] buildTransformFormatCmd2(String inVideo, String bitRate, String maxBitRate,String outVideo) {
        List<String> cmds = new ArrayList<>();
        cmds.add("ffmpeg"); //on android no need this.
        cmds.add("-i");
        cmds.add(inVideo);

        cmds.add("-b:v");
        cmds.add(bitRate);
        cmds.add("-bufsize");
        cmds.add(bitRate);
        if(maxBitRate != null){
            cmds.add("-maxrate");
            cmds.add(maxBitRate);
        }
        cmds.add(outVideo);
        cmds.add("-y");
        return cmds.toArray(new String[cmds.size()]);
    }

    public static String[] buildMergeVideosCmd(String video1, String musicPath, String outPath) {
        List<String> cmds = new ArrayList<>();
        cmds.add("ffmpeg"); //on android no need this.
        cmds.add("-i");
        cmds.add(video1);
        cmds.add("-i");
        cmds.add(musicPath);

        cmds.add("-c");
        cmds.add("copy");
       /* cmds.add("-movflags");
        cmds.add("+faststart");
        cmds.add("-shortest");*/
        cmds.add(outPath);
        cmds.add("-y");
        return cmds.toArray(new String[cmds.size()]);
    }

    public static String[] buildRemoveAudioForVideo(String video1, String outPath) {
        List<String> cmds = new ArrayList<>();
        cmds.add("ffmpeg"); //on android no need this.
        cmds.add("-i");
        cmds.add(video1);
        cmds.add("-an");
        cmds.add(outPath);
        cmds.add("-y");
        return cmds.toArray(new String[cmds.size()]);
    }

    private static void mergeVideoAndAudio() {
        String vide0 = "F:\\videos\\yunpeng_videos\\v1.MP4";
        String music = "E:\\tmp\\bugfinds\\out_music5\\musics\\1b9b59fccf827701123c154f10f1967e.mp3";
        String fileDir = FileUtils.getFileDir(vide0, 1, true);
        File dst = new File(fileDir, FileUtils.getFileName(vide0) + "__2.mp4");
        //remove audio from video
        CmdHelper cmdHelper = new CmdHelper(buildRemoveAudioForVideo(vide0, dst.getAbsolutePath()));
        System.out.println(cmdHelper.getCmdActually());
        //  cmdHelper.execute(new CmdHelper.InhertIoCallback());
        //merge
        File mergeFile = new File(fileDir, "merged2.mp4");
        cmdHelper = new CmdHelper(buildMergeVideosCmd(dst.getAbsolutePath(), music, mergeFile.getAbsolutePath()));
        System.out.println(cmdHelper.getCmdActually());
        cmdHelper.execute(new CmdHelper.InhertIoCallback());
    }
}
