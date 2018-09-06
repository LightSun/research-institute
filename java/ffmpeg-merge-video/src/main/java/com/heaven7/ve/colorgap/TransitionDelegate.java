package com.heaven7.ve.colorgap;

/**
 * @author heaven7
 */
public interface TransitionDelegate {

    /**
     * get the duration of transition type. must be '2n'
     * @param transitionType the transition type
     * @return the duration.
     */
    long getDuration(int transitionType);

}
