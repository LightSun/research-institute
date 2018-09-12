package com.vida.common.ai;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * the video ai generate context . which use video frames dir to scan face.
 * @author heaven7
 */
public class VideoAiGenerateContext2 extends VideoAiGenerateContext {

    private final String videoFrameDir;

    public VideoAiGenerateContext2(AiGeneratorDelegate delegate, OnGenerateListener l, FileParamContext fp,
                                   String videoFile, String dataDir, String videoFrameDir) {
        super(delegate, l, fp, videoFile, dataDir);
        this.videoFrameDir = videoFrameDir;
    }

    @Override
    protected String[] getAiGenIO() {
        List<String> list = new ArrayList<>(Arrays.asList(super.getAiGenIO()));
        list.add(videoFrameDir);
        return list.toArray(new String[list.size()]);
    }
}
