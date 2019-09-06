package com.github.hiteshsondhi88.sampleffmpeg;

import android.text.TextUtils;

import com.heaven7.java.base.util.FileUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by heaven7 on 2018/11/29 0029.
 */
public class TestUtils {

    //ffmpeg -f concat -safe 0 -i 'vidlist.txt' -i 'music.m4a' -c copy -movflags faststart -shortest -y 'test.mp4'
    public static String[] buildMergeVideosCmd(String concatPath, String musicPath, String outPath) {
        List<String> cmds = new ArrayList<>();
        //cmds.add("ffmpeg"); //on android no need this.
        cmds.add("-f");
        cmds.add("concat");
        cmds.add("-safe");
        cmds.add("0");
        cmds.add("-i");
        cmds.add(concatPath);
        if(!TextUtils.isEmpty(musicPath)){
            cmds.add("-i");
            cmds.add(musicPath);
        }
        cmds.add("-c");
        cmds.add("copy");
        cmds.add("-movflags");
        cmds.add("+faststart");
        //cmds.add("-shortest");
        cmds.add(outPath);
        cmds.add("-y");
        return cmds.toArray(new String[cmds.size()]);
    }

    //ok
    public static String[] buildMergeVideosCmd2(String concatPath, String musicPath, String outPath) {
        List<String> cmds = new ArrayList<>();
        //cmds.add("ffmpeg"); //on android no need this.
        cmds.add("-f");
        cmds.add("concat");
        cmds.add("-safe");
        cmds.add("0");
        cmds.add("-i");
        cmds.add(concatPath);
        if(!TextUtils.isEmpty(musicPath)){
            cmds.add("-i");
            cmds.add(musicPath);
        }
        cmds.add("-acodec");
        cmds.add("copy");
        cmds.add("-vcodec");
        cmds.add("copy");
        cmds.add(outPath);
        cmds.add("-y");
        return cmds.toArray(new String[cmds.size()]);
    }
    public static String[] buildMergeVideosCmd3(String concatPath, String musicPath, String outPath) {
        List<String> cmds = new ArrayList<>();
        //cmds.add("ffmpeg"); //on android no need this.
        cmds.add("-f");
        cmds.add("concat");
        cmds.add("-safe");
        cmds.add("0");
        cmds.add("-i");
        cmds.add(concatPath);
        if(!TextUtils.isEmpty(musicPath)){
            cmds.add("-i");
            cmds.add(musicPath);
        }
        cmds.add("-map");
        cmds.add("0:v:0");
        cmds.add("-map");
        cmds.add("1:a:0");

        cmds.add("-movflags");
        cmds.add("+faststart");
        cmds.add("-shortest");
        /*cmds.add("-c");
        cmds.add("copy");*/
        cmds.add(outPath);
        cmds.add("-y");
        return cmds.toArray(new String[cmds.size()]);
    }

    public static String[] buildMergeVideosCmd4(String video1, String musicPath, String outPath) {
        List<String> cmds = new ArrayList<>();
        //cmds.add("ffmpeg"); //on android no need this.
        cmds.add("-i");
        cmds.add(video1);
        if(!TextUtils.isEmpty(musicPath)){
            cmds.add("-i");
            cmds.add(musicPath);
        }
        cmds.add("-c");
        cmds.add("copy");
        cmds.add(outPath);
        cmds.add("-y");
        return cmds.toArray(new String[cmds.size()]);
    }

    /** mix multi audios */
    public static String[] mix(List<MixParam> mixs, String outFile){
        List<String> cmds = new ArrayList<>();
        //cmds.add("ffmpeg");
        //ffmpeg -i test.aac -i test.mp3 -filter_complex amix=inputs=2:duration=first:dropout_transition=2 -y mix.aac
        for (MixParam mp: mixs){
            cmds.add("-i");
            cmds.add(mp.filepath);
            cmds.add("-ss");
            cmds.add(mp.startTime +"");
            cmds.add("-t");
            cmds.add((mp.endTime - mp.startTime) + "");
        }
        cmds.add("-filter_complex");
        cmds.add("amix=inputs="+ mixs.size() +":duration=first:dropout_transition=2");
        cmds.add("-y");
        cmds.add(outFile);
        return cmds.toArray(new String[cmds.size()]);
    }
    public static String[] extractAudio(String infile, float volume, boolean video){
        //ffmpeg -i 1.mp3 -af "volume=0.1" -y 1_1.mp3
        //ffmpeg -i video.mp4 -acodec mp3 -af "volume=0.1" -y v_1.mp3
        List<String> cmds = new ArrayList<>();
        //cmds.add("ffmpeg");
        cmds.add("-i");
        if(video){
            cmds.add(infile);
            cmds.add("-acodec");
            cmds.add("mp3");
            cmds.add("-af");
            cmds.add("volume=" + volume);
        }else {
            cmds.add(infile);
            cmds.add("-af");
            cmds.add("volume=" + volume);
        }
        cmds.add("-y");
        cmds.add(getExtractAudioFileName(infile, volume));
        return cmds.toArray(new String[cmds.size()]);
    }
    public static String getExtractAudioFileName(String infile, float volume){
        String name = FileUtils.getFileName(infile);
        String dir = FileUtils.getFileDir(infile, 1, true);
        String suffix = String.valueOf(volume).replace(".", "_");
        return dir + "/" + name + "_"+ suffix  +".mp3";
    }
}
