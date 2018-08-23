package com.vida.common.ai;

/**
 * the ai generate context
 *
 * @author heaven7
 */
public interface AiGenerateContext {

    int STATE_TAG_START = 1;
    int STATE_TAG_TFRECORD_DONE = 2;
    int STATE_TAG_END = 3;
    int STATE_TAG_TFRECORD_FAILED = 4;

    int STATE_DEFAULT = 0;

    int STATE_FACE_START = 11;
    int STATE_FACE_END = 12;

    void genTfRecord();

    void genFace();

    /**
     * gen tag . often called by genTfRecord().
     */
    void genTag();

    boolean hasTask(String path);

    static String getStateString(int state){
        switch (state){
            case STATE_DEFAULT:
                return "STATE_DEFAULT";

            case STATE_FACE_END:
                return "STATE_FACE_END";
            case STATE_FACE_START:
                return "STATE_FACE_START";

            case STATE_TAG_END:
                return "STATE_TAG_END";
            case STATE_TAG_START:
                return "STATE_TAG_START";
            case STATE_TAG_TFRECORD_FAILED:
                return "STATE_TAG_TFRECORD_FAILED";
            case STATE_TAG_TFRECORD_DONE:
                return "STATE_TAG_TFRECORD_DONE";

            default:
                throw new UnsupportedOperationException("wrong state = " + state);
        }
    }
}
