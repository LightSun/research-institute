package com.vida.common;

import com.heaven7.java.base.anno.Nullable;
import com.heaven7.java.base.anno.Platform;

/**
 * @author heaven7
 */
@Platform
public interface FFMpegCmdGenerator {
    /**
     * build get video duration cmd
     * @param videoPath the video path
     * @return the cmds
     */
    String[] buildGetDurationCmd(String videoPath);//cmd response is too simple . if use 'cmd /c start /wait' also need '/b'.

    /**
     * build the cmd of extract image from video .
     *
     * @param cmd the image extract cmd
     * @return the cmds
     */
    String[] buildImageExtractCmd(ImageExtractCmd cmd);

    /**
     * build cut video cmd. must use string[] . if you concat string. may cause bugs.
     * @param videoPath the video path
     * @param start the start time in frames
     * @param end the end time in frames
     * @param outDir  the out dir
     * @param outPathArr  the out path to save to
     * @return the string array of cmds.
     */
    String[] buildCutCmd(String videoPath, long start, long end, String outDir, @Nullable String[] outPathArr);
    /**
     * build merge video cmd
     * @param concatPath the concat path
     * @param outVideoPath the out video path
     * @return the cmds
     */
    String[] buildMergeVideoCmd(String concatPath, String outVideoPath);
}
