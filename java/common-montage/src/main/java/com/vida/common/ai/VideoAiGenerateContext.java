package com.vida.common.ai;

import com.heaven7.core.util.Logger;
import com.vida.common.platform.PyDelegate;

import static com.heaven7.utils.TextUtils.getRelativePathForPrefix;

/**
 * @author heaven7
 */
public class VideoAiGenerateContext extends BaseAiGenerateContext{

    private final FileParamContext fp;
    private final String videoFile;
    private final String dataDir;

    private final String tfreordPath;
    private final AiVideoFileInfo info = new AiVideoFileInfo();

    public VideoAiGenerateContext(AiGeneratorDelegate delegate, OnGenerateListener l,FileParamContext fp, String videoFile, String dataDir) {
        super(delegate, l);
        this.fp = fp;
        this.videoFile = videoFile;
        this.dataDir = dataDir;

        //dataDir is full data dir
        tfreordPath = PyDelegate.getDefault().getTfrecordPathForVideo(dataDir, videoFile);
    }

    @Override
    public String getTfRecordPath() {
        return tfreordPath;
    }

    @Override
    protected void onGenerateDone() {
        Logger.d(TAG , "onGenerateDone_video", "dataDir is " + dataDir);
        String facePath = PyDelegate.getDefault().getFacePath(videoFile, dataDir);
        String tagPath = PyDelegate.getDefault().getTagOutputFile(tfreordPath);
        info.setTfrecordPath(getRelativePathForPrefix(tfreordPath, dataDir));
        info.setFacePath(getRelativePathForPrefix(facePath, dataDir));
        info.setTagPath(getRelativePathForPrefix(tagPath, dataDir));
        fp.setVideoGenInfo(info);
    }

    @Override
    protected String[] getAiGenIO() {
        return new String[]{videoFile, dataDir};
    }

    @Override
    public boolean hasTask(String path) {
        return videoFile.equals(path);
    }
}
