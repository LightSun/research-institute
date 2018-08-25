package com.vida.common.ai;

import com.heaven7.utils.TextUtils;

import java.io.File;

import static com.vida.common.ai.AiGenerateContext.*;

/**
 * @author heaven7
 */
public abstract class BaseAiGeneratorDelegate implements AiGeneratorDelegate {

    @Override
    public final void genFace(String[] io, AiGenStateNotifier notifier) {
        final String[] cmds = getGenFaceCmd(io);
        final String cmdString = toCmdString(cmds);
        notifier.notifyState(AiGenStateNotifier.MARK_FACE, STATE_DEFAULT, STATE_FACE_START, cmdString);
        //start
        genFace(io, cmds);
        //end
        notifier.notifyState(AiGenStateNotifier.MARK_FACE, STATE_FACE_START, STATE_FACE_END, cmdString);
    }

    @Override
    public final void genTfRecord(String[] io, AiGenStateNotifier notifier) {
        //start
        final String[] cmds = getGenTfRecordCmd(io);
        final String cmdString = toCmdString(cmds);
        notifier.notifyState(AiGenStateNotifier.MARK_TAG, STATE_DEFAULT, STATE_TAG_START, cmdString);
        genTfrecord(io, cmds);
        //end
        String tfRecordPath = notifier.getTfRecordPath();
        final int targetState;
        if (TextUtils.isEmpty(tfRecordPath)) {
            targetState = STATE_TAG_TFRECORD_FAILED;
        } else {
            targetState = new File(tfRecordPath).exists() ? STATE_TAG_TFRECORD_DONE : STATE_TAG_TFRECORD_FAILED;
        }
        notifier.notifyState(AiGenStateNotifier.MARK_TAG, STATE_TAG_START, targetState, cmdString);
    }

    @Override
    public final void genTag(String[] io, AiGenStateNotifier notifier) {
        String[] cmds = getGenTagCmd(io, notifier.getTfRecordPath());
        final String cmdString = toCmdString(cmds);
        genTag(io, cmds, notifier.getTfRecordPath());
        //post
        notifier.notifyState(AiGenStateNotifier.MARK_TAG, STATE_TAG_TFRECORD_DONE, STATE_TAG_END, cmdString);
    }

    public static String toCmdString(String[] cmds){
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for(int size = cmds.length; i < size; ++i) {
            sb.append(cmds[i]);
            if (i != size - 1) {
                sb.append(" ");
            }
        }
        return sb.toString();
    }

    public static boolean isVideo(String input){
        File file = new File(input);
        return file.exists() && file.isFile();
    }

    protected abstract void genTfrecord(String[] io, String[] cmds);
    protected abstract void genTag(String[] io, String[] cmds, String tfRecordPath);
    protected abstract void genFace(String[] io, String[] cmds);

    protected abstract String[] getGenTfRecordCmd(String[] io);
    protected abstract String[] getGenTagCmd(String[] io, String tfrecordPath);
    protected abstract String[] getGenFaceCmd(String[] io);
}
