package com.heaven7.ve.cross_os.pc;

import com.heaven7.ve.SimpleCopyDelegate;
import com.heaven7.ve.cross_os.CopyHelper;
import com.heaven7.ve.cross_os.ITimeTraveller;
import com.heaven7.ve.cross_os.TimeTravellerHelper;

/*public*/ class SimpleTimeTraveller extends SimpleCopyDelegate implements ITimeTraveller{

    /** duration in frames */
    public long getDuration(){
        return TimeTravellerHelper.getDuration(this);
    }
    public long increaseStartTime(long delta){
        return TimeTravellerHelper.increaseStartTime(this, delta);
    }

    public long decreaseStartTime(long delta){
        return TimeTravellerHelper.decreaseStartTime(this, delta);
    }
    public long increaseEndTime(long delta){
        return TimeTravellerHelper.increaseEndTime(this, delta);
    }
    public long decreaseEndTime(long delta){
        return TimeTravellerHelper.decreaseEndTime(this, delta);
    }
    public long clampEndTime() {
        return TimeTravellerHelper.clampEndTime(this);
    }
    public void adjustByLimit(){
        TimeTravellerHelper.adjustByLimit(this);
    }

    /**
     * adjust time by target center time.
     * @param focusTime the focus time in frames
     * @since 1.0.1 */
    public void adjustTime(long focusTime, long expectDuration) {
        TimeTravellerHelper.adjustTime(this, focusTime, expectDuration);
    }

    /** adjust the start and end times by expect duration as center.  */
    public void adjustTimeAsCenter(long expectDuration) {
        TimeTravellerHelper.adjustTimeAsCenter(this, expectDuration);
    }

    @Override
    public void setFrom(ITimeTraveller src) {
        CopyHelper.copyTimeTraveller(this, src);
    }

    @Override
    public final void setFrom(SimpleCopyDelegate sc) {
        if(sc instanceof ITimeTraveller){
            setFrom((ITimeTraveller)sc);
        }
    }

    @Override
    public String toString() {
        return "ITimeTraveller{" +
                "startTime=" + getStartTime() +
                ", endTime=" + getEndTime() +
                ", maxEndTime=" + getMaxDuration() +
                '}';
    }
    public String toString2() {
        return TimeTravellerHelper.toString2(this);
    }


    //---------------------------------------------------------------------------

    private long startTime;
    private long endTime;
    private long maxDuration;

    public long getStartTime() {
        return startTime;
    }
    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }
    public long getEndTime() {
        return endTime;
    }
    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }
    public void setMaxDuration(long maxDuration) {
        this.maxDuration = maxDuration;
    }
    public long getMaxDuration() {
        return maxDuration;
    }

}
