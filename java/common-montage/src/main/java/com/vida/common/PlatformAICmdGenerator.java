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
    String[] generateTfRecordForBatchImages(String inputDir, String outDir);

    String[] generateFaceForVideo(String videoFile, String outDir);
    String[] generateFaceForBatchImage(String inputDir, String outDir);

    /**
     * generate tag for video tfrecord file
     * @param tfrecordFile the video tfrecord file
     * @param outFile the out file of tag. often is 'xxx_predictions.csv'
     * @return the cmd array for running.
     */
    String[] generateTag(String tfrecordFile, String outFile);
    String[] generateTag(List<String> tfrecordFile, List<String> outFile);

}
