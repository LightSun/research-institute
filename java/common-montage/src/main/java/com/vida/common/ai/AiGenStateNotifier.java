package com.vida.common.ai;

/**
 * @author heaven7
 */
public interface AiGenStateNotifier {

    int MARK_TAG    = 1;
    int MARK_FACE   = 2;
    /**
     * notify state
     * @param mark  the mark of this notify
     * @param current the current state
     * @param except the excepted state.
     * @param cmd the cmd
     */
    void notifyState(int mark, int current, int except, String cmd);

    /**
     * get the tfrecord path
     * @return the tfrecord path
     */
    String getTfRecordPath();
}
