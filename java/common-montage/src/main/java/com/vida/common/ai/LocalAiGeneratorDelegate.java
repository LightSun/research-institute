package com.vida.common.ai;

import com.heaven7.utils.CmdHelper;
import com.vida.common.PlatformAICmdGenerator;
import com.vida.common.platform.PyDelegate;

/**
 * @author heaven7
 */
public class LocalAiGeneratorDelegate extends ServerAiGeneratorDelegate {

    private final Callback callback;

    public LocalAiGeneratorDelegate( PlatformAICmdGenerator mAiCmdGen, CmdHelper.Callback cllback, Callback callback) {
        super( mAiCmdGen, cllback);
        this.callback = callback;
    }

    @Override
    protected void genTag(String[] io, String[] cmds, String tfRecordPath) {
        //for local can't transform tfrecord to tag. we need request server.
        Runner0 runner = new Runner0(io);
        switch (runner.type){
            case Runner0.TYPE_VIDEO:
                //inputVideo outDir
            case BaseGenerateRunner.TYPE_BATCH_IMAGE_MORE_DIR:
                //outDir file1 file2
            case BaseGenerateRunner.TYPE_BATCH_IMAGE_ONE_DIR:
                //inputDir outDir
                String tagFile = PyDelegate.getDefault().getTagOutputFile(tfRecordPath);
                callback.tfrecordToTag(tfRecordPath, tagFile);
                break;

            default:
                throw new IllegalStateException("wrong type = " + runner.type);
        }
    }

    /**
     * the callback
     */
    public interface Callback{
        /**
         * transform tfrecord file to tag file. use client-server
         * @param tfrecordPath the tfrecord path
         * @param targetTagPath the target tag file.
         */
        void tfrecordToTag(String tfrecordPath, String targetTagPath);
    }
}
