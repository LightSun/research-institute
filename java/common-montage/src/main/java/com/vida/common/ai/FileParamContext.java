package com.vida.common.ai;

/**
 * @author heaven7
 */
public interface FileParamContext {

    /**
     * set gen info for video file
     *
     * @param info the ai info
     */
    void setVideoGenInfo(AiVideoFileInfo info);

    /**
     * set gen info for batch image
     *
     * @param info the ai info
     */
    void setImageGenInfo(AiBatchImageFileInfo info);

    /**
     * get the save path
     * @return the save path.
     */
    String getSavePath();
}
