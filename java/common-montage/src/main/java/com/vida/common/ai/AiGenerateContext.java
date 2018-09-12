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

    /**
     * gen face by read video steam.
     */
    void genFace();

    /**
     * gen tag . often called by genTfRecord().
     */
    void genTag();

    boolean hasTask(String path);

    /**
     * set data. which may be used as temp store.
     * @param data the data
     */
    void setData(Object data);

    /**
     * get the data
     * @return the data
     */
    Object getData();

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

    /**
     * on generate listener
     */
    interface OnGenerateListener{
        /**
         * called on generate done
         * @param context the context.
         */
        void onGenerateDone(AiGenerateContext context);
    }
}
