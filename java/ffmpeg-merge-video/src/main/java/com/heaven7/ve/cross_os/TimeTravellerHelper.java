package com.heaven7.ve.cross_os;

import com.heaven7.utils.CommonUtils;

import java.util.concurrent.TimeUnit;

/**
 * @author heaven7
 */
public class TimeTravellerHelper {

    /** duration in frames */
    public static long getDuration(ITimeTraveller tt){
        long val = tt.getEndTime() - tt.getStartTime();
        if(val <= 0 ){
            throw new IllegalStateException("endTime = " + tt.getEndTime() + " ,startTime = " + tt.getStartTime());
        }
        return val;
    }

    public static long increaseStartTime(ITimeTraveller tt, long delta){
        long startTime = tt.getStartTime() + delta;
        tt.setStartTime(startTime);
        //logIfNeed("increaseStartTime", delta);
        if(startTime >= tt.getEndTime()){
            throw new IllegalArgumentException("startTime = " + startTime + " , endTime = " + tt.getEndTime());
        }
        return 0;
    }

    public static long decreaseStartTime(ITimeTraveller tt, long delta){
        //decrease start time . of not enough , add end time.
        long startTime = tt.getStartTime();
        long endTime = tt.getEndTime();
        long maxEndTime = tt.getMaxDuration();
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
        tt.setStartTime(startTime);
        tt.setEndTime(endTime);
        //logIfNeed("decreaseStartTime", delta);
        return result;
    }
    public static long increaseEndTime(ITimeTraveller tt,long delta){
        //increase end time . of not enough , decrease start time.
        long startTime = tt.getStartTime();
        long endTime = tt.getEndTime();
        long maxEndTime = tt.getMaxDuration();
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
        tt.setStartTime(startTime);
        tt.setEndTime(endTime);
        // logIfNeed( "increaseEndTime",delta);
        return result;
    }
    public static long decreaseEndTime(ITimeTraveller tt, long delta){
        long endTime = tt.getEndTime() - delta;
        tt.setEndTime(endTime);
        //logIfNeed("decreaseEndTime", delta);
        if(tt.getStartTime() >= endTime){
            throw new IllegalArgumentException("startTime = " + tt.getStartTime() + " , endTime = " + endTime);
        }
        return 0;
    }
    public static long clampEndTime(ITimeTraveller tt) {
        long endTime = tt.getEndTime();
        long maxEndTime = tt.getMaxDuration();
        if(endTime > maxEndTime){
            long result = (endTime - maxEndTime);
            tt.setEndTime(maxEndTime);
            return result;
        }
        return 0;
    }

    public static void adjustByLimit(ITimeTraveller tt){
        long endTime = tt.getEndTime();
        final long maxEndTime = tt.getMaxDuration();
        if(endTime > maxEndTime){
            long result = (endTime - maxEndTime);
            tt.setEndTime(maxEndTime);
            tt.setStartTime(tt.getStartTime() - result);
        }else {
            long startTime = tt.getStartTime();
            if (startTime < 0) {
                tt.setEndTime(tt.getEndTime() + Math.abs(startTime));
                tt.setStartTime(0);
            }
        }
        //check
        if(tt.getStartTime() < 0 ){
            throw new IllegalStateException("time error for start time = " + tt.getStartTime());
        }
        if(tt.getEndTime() > maxEndTime){
            throw new IllegalStateException("time error for end time over maxEndTime,  endTime = "
                    + tt.getEndTime() + " ,maxEndTime =" + maxEndTime);
        }
    }

    /**
     * adjust time by target center time.
     * @param focusTime the focus time in frames
     * @since 1.0.1 */
    public static void adjustTime(ITimeTraveller tt, long focusTime, long expectDuration) {
        final long half = expectDuration / 2;
        long start = focusTime - half;
        long end = start + expectDuration;
        if(start < 0){
            end += Math.abs(start);
            start = 0;
        }else if(end > tt.getMaxDuration()){
            //end is too large . move start left. end left
            start -= end - tt.getMaxDuration();
            end = tt.getMaxDuration();
        }
        tt.setStartTime(start);
        tt.setEndTime(end);
    }

    /** adjust the start and end times by expect duration as center.  */
    public static void adjustTimeAsCenter(ITimeTraveller tt,long expectDuration) {
        long startTime = tt.getStartTime();
        long endTime = tt.getEndTime();

        int delta = (int) (tt.getDuration() - expectDuration);
        int diff = Math.abs(delta / 2);
        if(delta > 0){
            // decrease
            endTime -= diff;
            startTime = endTime - expectDuration;
        }else if( delta < 0){
            //increase
            increaseEndTime(tt, diff);
            endTime = tt.getEndTime();
            startTime = endTime - expectDuration;
        }
        //start time is invalid. adjust
        if(startTime < 0){
            endTime += Math.abs(startTime);
            startTime = 0;
            if(endTime > tt.getMaxDuration()){
                throw new IllegalStateException("adjust failed for target/expect duration is too long.");
            }
        }
        tt.setStartTime(startTime);
        tt.setEndTime(endTime);
    }

    public static String toString2(ITimeTraveller tt) {
        return "ITimeTraveller{" +
                "startTime=" + CommonUtils.frameToTime(tt.getStartTime(), TimeUnit.SECONDS) +
                ", endTime=" +  CommonUtils.frameToTime(tt.getEndTime(), TimeUnit.SECONDS) +
                ", maxEndTime=" +  CommonUtils.frameToTime(tt.getMaxDuration(), TimeUnit.SECONDS) +
                '}';
    }
}
