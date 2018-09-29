package com.heaven7.ve.cross_os;

import com.heaven7.ve.Copyable;
import com.heaven7.ve.SimpleCopyDelegate;

/**
 * the all time unit is in frames.
 * @author heaven7
 */
public interface ITimeTraveller extends Copyable<SimpleCopyDelegate> {

    long getDuration();
    long increaseStartTime(long delta);
    long decreaseStartTime(long delta);
    long increaseEndTime(long delta);
    long decreaseEndTime(long delta);
    long clampEndTime();
    void adjustByLimit();

    String toString2();

    /**
     * adjust time by target center time.
     * @param focusTime the focus time in frames
     * @since 1.0.1 */
    void adjustTime(long focusTime, long expectDuration);

    /** adjust the start and end times by expect duration as center.  */
    void adjustTimeAsCenter(long expectDuration);


    //--------------------------------------------------------
    long getStartTime();
    void setStartTime(long startTime);
    long getEndTime();
    void setEndTime(long endTime);
    void setMaxDuration(long maxDuration);
    long getMaxDuration();
}
