package com.github.hiteshsondhi88.sampleffmpeg;

import android.text.TextUtils;

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
}
