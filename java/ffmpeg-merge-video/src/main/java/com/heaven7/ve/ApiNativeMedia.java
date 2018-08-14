package com.heaven7.ve;

/**
 * the api of native media
 * @author heaven7
 */
public interface ApiNativeMedia {

    //beforeIndex  often -1, fileStart (in seconds)
    /**
     * add video part
     * @param videoFile the source video file
     * @param fileStart the file begin , in seconds
     * @param beginFrame the target start position in frames
     * @param countFrames the count of time. in frames
     * @param beforeIndex the before index. default is -1.
     */
   void addVideoPart(String videoFile, float fileStart, int beginFrame, int countFrames, int beforeIndex);

    //insertTime in seconds

    /**
     * add music track
     * @param musicPath the music path
     * @param insertTime the insert time of music file
     * @param startTime the start time
     * @param endTime the end time
     * @param audioType the audio type. current not used.
     */
   void addMusicTrack(String musicPath, float insertTime, float startTime, float endTime, int audioType);

    /**
     * set the write out path .absolute file name
     * @param path the absolute file name. dir must exist.
     */
   void setWriteOutPath(String path);

    /**
     * start gen mc video
     */
    void startGenMcVideo();

    /**
     * pause gen mc video
     */
    void pauseGenMcVideo();

    /**
     * resume gen mc video
     */
    void resumeGenMcVideo();

    /**
     * stop gen mc video.
     */
    void stopGenMcVideo();

    //----------------------------------------------------------

    Callback getCallback();

    void setCallback(Callback callback);

    /**
     * the callback from native.
     */
    interface Callback {
        /**
         * called on preview in time
         *
         * @param frameIndex the frame index
         */
        void onPreviewInTime(int frameIndex);

        /**
         * called on preview end
         */
        void onPreviewEnd();

        /**
         * called on generate in process.
         * @param process the process from 0-1
         */
        void onGenerateInProcess(float process);

        /**
         * called on generate end
         * @param success true if generate ok.
         */
        void onGenerateEnd(boolean success);
    }
}
