package com.vida.common;

import java.util.List;

/**
 * the cmd generator of platform
 * @author heaven7
 */
public interface PlatformAICmdGenerator {

    /** the proxy python for running any python cmd. */
    String CMD_PROXY_FILENAME = "cmd_proxy.py";
    String PREFIX_INPUT  = "-input=";
    String PREFIX_OUTPUT = "-output=";

    //process(video_file, out_dir)
    String[] generateTfRecordForVideo(String videoFile, String outDir);
    /**
     * generate tfrecord for batch images .which must be in one dir.
     * @param inputDir the input dir
     * @param outDir the out dir
     * @return the cmd array
     */
    String[] generateTfRecordForBatchImages(String inputDir, String outDir);

    /**
     * generate tfrecord for batch images .which can in different dirs.
     * @param images the image paths
     * @param outDir the out dir
     * @return the cmd array
     */
    String[] generateTfRecordForBatchImages(List<String> images,String outDir);

    String[] generateFaceForVideo(String videoFile, String outDir);

    /**
     * gen face from frames which from video
     * @param videoFile the video file
     * @param framesDir the frames dir
     * @param outDir the output dir
     * @return the cmd array
     */
    String[] generateFaceForVideo2(String videoFile, String framesDir,String outDir);
    /**
     * generate face for batch images .which must be in one dir.
     * @param inputDir the input dir
     * @param outDir the out dir
     * @return the cmd array
     */
    String[] generateFaceForBatchImage(String inputDir, String outDir);
    /**
     * generate face for batch images .which can in different dirs.
     * @param images the image paths
     * @param outDir the out dir
     * @return the cmd array
     */
    String[] generateFaceForBatchImage(List<String> images,String outDir);

    /**
     * generate tag for video tfrecord file
     * @param tfrecordFile the video tfrecord file
     * @param outFile the out file of tag. often is 'xxx_predictions.csv'
     * @return the cmd array for running.
     */
    String[] generateTag(String tfrecordFile, String outFile);
    String[] generateTag(List<String> tfrecordFile, List<String> outFile);

}
