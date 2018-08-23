package com.vida.common.ai;

/**
 * ai batch image file info
 * <p>Note: all is relative path</p>
 * @author heaven7
 */
public class AiBatchImageFileInfo extends AiVideoFileInfo {

    private String tfsConfigPath;     //tfs_config.txt
    private String faceConfigPath;    //face_config.txt

    public String getTfsConfigPath() {
        return tfsConfigPath;
    }
    public void setTfsConfigPath(String tfsConfigPath) {
        this.tfsConfigPath = tfsConfigPath;
    }

    public String getFaceConfigPath() {
        return faceConfigPath;
    }
    public void setFaceConfigPath(String faceConfigPath) {
        this.faceConfigPath = faceConfigPath;
    }

}