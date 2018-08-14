package com.heaven7.ve;

import com.heaven7.utils.CommonUtils;

import java.util.concurrent.TimeUnit;

/**
 * 时间性的. in frames
 * Created by heaven7 on 2018/1/16 0016.
 */
public class TimeTraveller extends SimpleCopyDelegate {

    public static final int NTYPE_DEFAULT        = 1;
    public static final int NTYPE_PATH           = 2;
    public static final int NTYPE_SPECIAL_EFFECT = 3;
    public static final int NTYPE_FILTER      = 4;
    public static final int NTYPE_TRANSITION  = 5;

    public static final int NTYPE_AUDIO_TRACK = 6;
    public static final int NTYPE_VIDEO_TRACK = 7;
    public static final int NTYPE_IMAGE_INFO  = 9;

    private static final String TAG = "TimeTraveller";
    private final long ptr;
    private boolean mDestroied;

    public TimeTraveller() {
        this.ptr = nCreate(getNativeType());
    }

    public static TimeTraveller of(long start, long end, long maxDuration){
        TimeTraveller tt = new TimeTraveller();
        tt.setStartTime(start);
        tt.setEndTime(end);
        tt.setMaxDuration(maxDuration);
        return tt;
    }

    protected int getNativeType(){
        return NTYPE_DEFAULT;
    }


    /** duration in frames */
    public long getDuration(){
        long val = getEndTime() - getStartTime();
        if(val <= 0 ){
            throw new IllegalStateException("endTime = " + getEndTime() + " ,startTime = " + getStartTime());
        }
        return val;
    }

    public long increaseStartTime(long delta){
        long startTime = getStartTime() + delta;
        setStartTime(startTime);
        //logIfNeed("increaseStartTime", delta);
        if(startTime >= getEndTime()){
            throw new IllegalArgumentException("startTime = " + startTime + " , endTime = " + getEndTime());
        }
        return 0;
    }

    public long decreaseStartTime(long delta){
        //decrease start time . of not enough , add end time.
        long startTime = getStartTime();
        long endTime = getEndTime();
        long maxEndTime = getMaxDuration();
        long result = 0;
        if(startTime >= delta){
            startTime -= delta;
        }else{
            //delta > startTime
            endTime += delta - startTime;
            startTime = 0;
            if(endTime > maxEndTime){
                result = endTime - maxEndTime;
                endTime = maxEndTime;
            }
        }
        setStartTime(startTime);
        setEndTime(endTime);
        //logIfNeed("decreaseStartTime", delta);
        return result;
    }
    public long increaseEndTime(long delta){
        //increase end time . of not enough , decrease start time.
        long startTime = getStartTime();
        long endTime = getEndTime();
        long maxEndTime = getMaxDuration();
        long result = 0;

        if((endTime + delta) > maxEndTime){
            startTime -= (endTime + delta - maxEndTime);
            endTime = maxEndTime;
            if(startTime < 0){
                result = Math.abs(startTime);
                startTime = 0;
            }
        }else{
            endTime += delta;
        }
        setStartTime(startTime);
        setEndTime(endTime);
       // logIfNeed( "increaseEndTime",delta);
        return result;
    }
    public long decreaseEndTime(long delta){
        long endTime = getEndTime() - delta;
        setEndTime(endTime);
        //logIfNeed("decreaseEndTime", delta);
        if(getStartTime() >= endTime){
            throw new IllegalArgumentException("startTime = " + getStartTime() + " , endTime = " + endTime);
        }
        return 0;
    }
    public long clampEndTime() {
        long endTime = getEndTime();
        long maxEndTime = getMaxDuration();
        if(endTime > maxEndTime){
            long result = (endTime - maxEndTime);
            setEndTime(maxEndTime);
            return result;
        }
        return 0;
    }

    /**
     * adjust time by target center time.
     * @param focusTime the focus time in frames
     * @since 1.0.1 */
    public void adjustTime(long focusTime, long expectDuration) {
        final long half = expectDuration / 2;
        long start = focusTime - half;
        long end = start + expectDuration;
        if(start < 0){
            end += Math.abs(start);
            start = 0;
        }else if(end > getMaxDuration()){
            //end is too large . move start left. end left
            start -= end - getMaxDuration();
            end = getMaxDuration();
        }
        setStartTime(start);
        setEndTime(end);
    }

    /** adjust the start and end times by expect duration as center.  */
    public void adjustTimeAsCenter(long expectDuration) {
        long startTime = getStartTime();
        long endTime = getEndTime();

        int delta = (int) (getDuration() - expectDuration);
        int diff = Math.abs(delta / 2);
        if(delta > 0){
            // decrease
            endTime -= diff;
            startTime = endTime - expectDuration;
        }else if( delta < 0){
            //increase
            increaseEndTime(diff);
            endTime = getEndTime();
            startTime = endTime - expectDuration;
        }
        //start time is invalid. adjust
        if(startTime < 0){
            endTime += Math.abs(startTime);
            startTime = 0;
            if(endTime > getMaxDuration()){
                throw new IllegalStateException("adjust failed for target/expect duration is too long.");
            }
        }
        setStartTime(startTime);
        setEndTime(endTime);
    }

    @Override
    public final void setFrom(SimpleCopyDelegate sc) {
        if(sc instanceof TimeTraveller){
            setFrom((TimeTraveller)sc);
        }
    }
    public void setFrom(TimeTraveller src) {
        setStartTime(src.getStartTime());
        setEndTime(src.getEndTime());
        setMaxDuration(src.getMaxDuration());
    }

    @Override
    public String toString() {
        return "TimeTraveller{" +
                "startTime=" + getStartTime() +
                ", endTime=" + getEndTime() +
                ", maxEndTime=" + getMaxDuration() +
                '}';
    }
    public String toString2() {
        return "TimeTraveller{" +
                "startTime=" + CommonUtils.frameToTime(getStartTime(), TimeUnit.SECONDS) +
                ", endTime=" +  CommonUtils.frameToTime(getEndTime(), TimeUnit.SECONDS) +
                ", maxEndTime=" +  CommonUtils.frameToTime(getMaxDuration(), TimeUnit.SECONDS) +
                '}';
    }

    /** in frames */
    public long getStartTime(){
       // Logger.d(TAG, "getStartTime", "in");
        long result = getStartTime0();
       // Logger.d(TAG, "getStartTime", "out");
        return result;
    }
    public void setStartTime(long startTime){
       // Logger.d(TAG, "setStartTime", "in");
        setStartTime0(startTime);
        // Logger.d(TAG, "setStartTime", "out");
    }

    /** in frames */
    public long getEndTime(){
       // Logger.d(TAG, "getEndTime", "in");
        long result = getEndTime0();
       // Logger.d(TAG, "getEndTime", "out");
        return result;
    }
    public void setEndTime(long endTime){
       // Logger.d(TAG, "setEndTime", "in");
        setEndTime0(endTime);
       // Logger.d(TAG, "setEndTime", "out");
    }

    /** set max duration in frames*/
    public void setMaxDuration(long maxDuration){
       // Logger.d(TAG, "setMaxDuration", "in");
        setMaxDuration0(maxDuration);
       // Logger.d(TAG, "setMaxDuration", "out");
    }
    public long getMaxDuration(){
       // Logger.d(TAG, "getMaxDuration", "in");
        long result = getMaxDuration0();
       // Logger.d(TAG, "getMaxDuration", "out");
        return result;
    }

    public void destroyNative(){
        if(!mDestroied && ptr != 0 ){
           // Logger.d(TAG, "destroy", "start release pointer = " + getNativePointer());
            nRelease(ptr);
            mDestroied = true;
        }
    }

    @Override
    protected void finalize() throws Throwable {
        destroyNative();
        super.finalize();
    }

    private long nCreate(int nativeType){
        return 0;
    }
    private void nRelease(long ptr){

    }

    /** in frames */
    private long getStartTime0(){
        return startTime;
    }
    private void setStartTime0(long startTime){
        this.startTime = startTime;
    }

    /** in frames */
    private long getEndTime0(){
        return endTime;
    }
    private void setEndTime0(long endTime){
        this.endTime = endTime;
    }

    /** set max duration in frames*/
    private void setMaxDuration0(long maxDuration){
        this.maxDuration = maxDuration;
    }
    private long getMaxDuration0(){
        return maxDuration;
    }

    private long startTime;
    private long endTime;
    private long maxDuration;

}
