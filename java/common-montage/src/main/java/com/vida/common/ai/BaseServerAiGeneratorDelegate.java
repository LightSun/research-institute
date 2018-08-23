package com.vida.common.ai;

import com.heaven7.utils.CmdHelper;
import com.vida.common.PlatformAICmdGenerator;
import com.vida.common.platform.PyDelegate;

/**
 * @author heaven7
 */
public abstract class BaseServerAiGeneratorDelegate extends BaseAiGeneratorDelegate {

    /** the cmd callback. often used to set the env
     * <pre>
     *      <code>
     * ProcessBuilder.directory(new File(mAiGenEnv));
     * </code>
     * </pre>
     */
    private final CmdHelper.Callback mLogCallback;
    private final PlatformAICmdGenerator mAiCmdGen;

    public BaseServerAiGeneratorDelegate(PlatformAICmdGenerator mAiCmdGen, CmdHelper.Callback cllback) {
        this.mAiCmdGen = mAiCmdGen;
        this.mLogCallback = cllback;
    }

    @Override
    protected void genTfrecord(String[] io, String[] cmds) {
         new CmdHelper(cmds).execute(mLogCallback);
    }
    @Override
    protected void genTag(String[] io, String[] cmds) {
        new CmdHelper(cmds).execute(mLogCallback);
    }
    @Override
    protected void genFace(String[] io, String[] cmds) {
        new CmdHelper(cmds).execute(mLogCallback);
    }

    @Override
    protected String[] getGenTfRecordCmd(String[] io) {
        String input = io[0];
        String output = io[1];
        if(isVideo(input)){
            return mAiCmdGen.generateTfRecordForVideo(input, output);
        }else{
            return mAiCmdGen.generateTfRecordForBatchImages(input, output);
        }
    }

    @Override
    protected String[] getGenTagCmd(String[] io, String tfrecordPath) {
        String outFile = PyDelegate.getDefault().getTagOutputFile(tfrecordPath);
        return mAiCmdGen.generateTag(tfrecordPath, outFile);
    }

    @Override
    protected String[] getGenFaceCmd(String[] io) {
        String input = io[0];
        String output = io[1];
        if(isVideo(input)){
            return mAiCmdGen.generateFaceForVideo(input, output);
        }else{
            return mAiCmdGen.generateFaceForBatchImage(input, output);
        }
    }

}
