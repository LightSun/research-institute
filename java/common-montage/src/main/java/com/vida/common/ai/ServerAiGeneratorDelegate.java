package com.vida.common.ai;

import com.heaven7.utils.CmdHelper;
import com.vida.common.PlatformAICmdGenerator;
import com.vida.common.platform.PyDelegate;

import java.io.File;

/**
 * @author heaven7
 */
public class ServerAiGeneratorDelegate extends BaseAiGeneratorDelegate {

    /** the cmd callback. often used to set the env
     * <pre>
     *      <code>
     * ProcessBuilder.directory(new File(mAiGenEnv));
     * </code>
     * </pre>
     */
    private final CmdHelper.Callback mLogCallback;
    protected final PlatformAICmdGenerator mAiCmdGen;

    public ServerAiGeneratorDelegate(PlatformAICmdGenerator mAiCmdGen, CmdHelper.Callback cllback) {
        this.mAiCmdGen = mAiCmdGen;
        this.mLogCallback = cllback;
    }

    @Override
    protected void genTfrecord(String[] io, String[] cmds) {
         new CmdHelper(cmds).execute(mLogCallback);
    }
    @Override
    protected void genTag(String[] io, String[] cmds, String tfRecordPath) {
        new CmdHelper(cmds).execute(mLogCallback);
    }
    @Override
    protected void genFace(String[] io, String[] cmds) {
        new CmdHelper(cmds).execute(mLogCallback);
    }

    @Override
    protected String[] getGenTfRecordCmd(String[] io) {
        Runner0 runner = new Runner0(io);
        switch (runner.type){
            case Runner0.TYPE_VIDEO:
                return mAiCmdGen.generateTfRecordForVideo(runner.getInput().get(0), runner.getOutput());

            case BaseGenerateRunner.TYPE_BATCH_IMAGE_MORE_DIR:
                return mAiCmdGen.generateTfRecordForBatchImages(runner.getInput(), runner.getOutput());

            case BaseGenerateRunner.TYPE_BATCH_IMAGE_ONE_DIR:
                return mAiCmdGen.generateTfRecordForBatchImages(runner.getInput().get(0), runner.getOutput());

            default:
                throw new IllegalStateException("wrong type = " + runner.type);
        }
    }

    @Override
    protected String[] getGenTagCmd(String[] io, String tfrecordPath) {
        String outFile = PyDelegate.getDefault().getTagOutputFile(tfrecordPath);
        return mAiCmdGen.generateTag(tfrecordPath, outFile);
    }

    @Override
    protected String[] getGenFaceCmd(String[] io) {
        Runner0 runner = new Runner0(io);
        switch (runner.type){
            case Runner0.TYPE_VIDEO:
                String framesDir = runner.getParameter(2);
                //gen face from frames which from video.
                if(framesDir!= null && new File(framesDir).isDirectory()){
                    return mAiCmdGen.generateFaceForVideo2(runner.getInput().get(0), framesDir ,runner.getOutput());
                }
                return mAiCmdGen.generateFaceForVideo(runner.getInput().get(0), runner.getOutput());

            case BaseGenerateRunner.TYPE_BATCH_IMAGE_MORE_DIR:
                return mAiCmdGen.generateFaceForBatchImage(runner.getInput(), runner.getOutput());

            case BaseGenerateRunner.TYPE_BATCH_IMAGE_ONE_DIR:
                return mAiCmdGen.generateFaceForBatchImage(runner.getInput().get(0), runner.getOutput());

            default:
                throw new IllegalStateException("wrong type = " + runner.type);
        }
    }

    protected static class Runner0 extends BaseGenerateRunner{
        public Runner0(String[] io) {
            super(io);
        }
        @Override
        protected void onRun(boolean video) {

        }
    }
}
