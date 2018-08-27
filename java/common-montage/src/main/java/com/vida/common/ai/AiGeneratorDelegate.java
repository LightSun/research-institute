package com.vida.common.ai;

/**
 * the ai info generator
 * @author heaven7
 */
public interface AiGeneratorDelegate {

    /**
     * gen face
     * @param io the two elements array. [0] is input(for video is video file path, for batch image is input dir). [1] is out put.
     * @param notifier the ai gen state notifier.
     */
    void genFace(String[] io, AiGenStateNotifier notifier);
    /**
     * gen tfrecord
     * @param io the two elements array. [0] is input(for video is video file path, for batch image is input dir). [1] is out put.
     * @param notifier the ai gen state notifier.
     */
    void genTfRecord(String[] io, AiGenStateNotifier notifier);
    /**
     * gen tag
     * @param io the two elements array. [0] is input(for video is video file path, for batch image is input dir). [1] is out put.
     * @param notifier the ai gen state notifier.
     */
    void genTag(String[] io, AiGenStateNotifier notifier);

}
