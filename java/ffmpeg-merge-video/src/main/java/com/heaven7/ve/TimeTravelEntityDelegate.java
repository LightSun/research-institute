package com.heaven7.ve;

import com.heaven7.java.base.anno.Hide;

/**
 * the all time unit from this is in frames.
 * @author heaven7
 */
public interface TimeTravelEntityDelegate {

    @Hide
    long nCreate(int nativeType);
    @Hide
    void nRelease(long ptr);

    /** in frames */
    long getStartTime0();
    void setStartTime0(long startTime);

    /** in frames */
    long getEndTime0();
    void setEndTime0(long endTime);

    /** set max duration in frames*/
    void setMaxDuration0(long maxDuration);
    long getMaxDuration0();

}
