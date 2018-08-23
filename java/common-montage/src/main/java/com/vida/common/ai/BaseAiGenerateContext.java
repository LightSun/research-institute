package com.vida.common.ai;

import com.heaven7.core.util.Logger;
import com.vida.common.TimeRecorder;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * for gen batch image .they must be in one dir.
 * @author heaven7
 */
public abstract class BaseAiGenerateContext implements AiGenerateContext, AiGenStateNotifier{

    protected final String TAG = getClass().getSimpleName();

    private final TimeRecorder mTr_face = new TimeRecorder("face");
    private final TimeRecorder mTr_tag = new TimeRecorder("tag");

    private final AtomicInteger tagState = new AtomicInteger();
    private final AtomicInteger faceState = new AtomicInteger();
    private final AiGeneratorDelegate mAiGenDelegate;

    public BaseAiGenerateContext(AiGeneratorDelegate delegate) {
        this.mAiGenDelegate = delegate;
    }

    @Override
    public void genTfRecord() {
        mTr_tag.begin();
        mAiGenDelegate.genTfRecord(getAiGenIO(),this);
        //log
        mTr_tag.end();
        log(mTr_tag, "Tfrecord");

        if (tagState.get() == STATE_TAG_TFRECORD_DONE) {
            // AiInfoGenerateManager.getDefault().mService.submit(() -> {
            mTr_tag.begin();
            genTag();
            checkDone();
            mTr_tag.end();
            log(mTr_tag, "Tag");
            // });
        } else {
            checkDone();
        }
    }
    @Override
    public void genFace() {
        mTr_face.begin();
        mAiGenDelegate.genFace(getAiGenIO(),this);
        //log
        mTr_face.end();
        log(mTr_face, "Face");
        //check done
        checkDone();
    }

    @Override
    public void genTag() {
        mAiGenDelegate.genTag(getAiGenIO(),this);
    }

    //==============================================================================

    private void checkDone() {
        int ts = tagState.get();
        boolean tagDone = ts == STATE_TAG_END || ts == STATE_TAG_TFRECORD_FAILED;
        if (faceState.get() == STATE_FACE_END && tagDone) {
            //local done.
            onGenerateDone();
            //aiGen.onMediaGenDone(this);
            mAiGenDelegate.onMediaGenDone(this);
        }
    }
    protected void log(TimeRecorder recorder, String keyword) {
        String[] inOut = getAiGenIO();
        String prefix = String.format("[ Ai-Gen-%s: in = %s, out = %s ]\n", keyword, inOut[0], inOut[1]);
        onWriteAiGenLog(recorder.toString(prefix));
    }

    @Override
    public void notifyState(int mark, int current, int except,String cmd) {
        switch (mark){
            case MARK_FACE:
                if (!faceState.compareAndSet(current, except)) {
                    Logger.w(TAG, "genFace", "change FACE state from " +
                            AiGenerateContext.getStateString(current) + " to " +
                            AiGenerateContext.getStateString(except) +" failed. cmd = " + cmd);
                }
                break;

            case MARK_TAG:
                if (!tagState.compareAndSet(current, except)) {
                    Logger.w(TAG, "genTag", "change TAG state from "
                            + AiGenerateContext.getStateString(current) +" to " +
                            AiGenerateContext.getStateString(except) +" failed. cmd = " + cmd);
                }
                break;

            default:
                throw new UnsupportedOperationException();
        }
    }

    //==================================================================================
    /**
     * on write ai log
     * @param log the log
     */
    protected  void onWriteAiGenLog(String log){
         Logger.d(TAG, "onWriteAiGenLog", log);
    }
    /**
     * called on ai-gen all done
     */
    protected abstract void onGenerateDone();

    /**
     * get ai gen input and output. often used for log
     * @return the input and output.
     */
    protected abstract String[] getAiGenIO();
}
