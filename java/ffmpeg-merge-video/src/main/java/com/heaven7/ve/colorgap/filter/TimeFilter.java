package com.heaven7.ve.colorgap.filter;

import com.heaven7.ve.colorgap.MetaInfo;
import com.heaven7.ve.utils.Arrays2;

import java.util.Calendar;
import java.util.Date;

import static com.heaven7.ve.colorgap.MetaInfo.FLAG_TIME;

/**
 * Created by heaven7 on 2018/3/16 0016.
 */

public class TimeFilter extends TypeFilter {

    public TimeFilter(TimeCondition mCondition, int cmpType) {
        super(mCondition, cmpType);
    }

    @Override
    public int getFlag() {
        return FLAG_TIME;
    }

    @Override
    public float computeScore(GapColorCondition condition) {
        if (!mCondition.isSampleTypeCondition(condition, true)) {
            return MIN_SCORE;
        }
        TimeCondition mCondition = this.mCondition.as();
        TimeCondition otherCondition = condition.as();
        switch (type){
            default:
            case MetaInfo.TIME_SAME_DAY:
                return mCondition.isSameDay(otherCondition) ? MAX_SCORE : MIN_SCORE;

            case MetaInfo.TIME_SAME_PERIOD_IN_DAY:
                return mCondition.isSamePeriodInDay(otherCondition) ? GOOD_SCORE : MIN_SCORE;
        }
    }

    public static class TimeCondition extends GapColorCondition{

        private static final int PERIOD_DAY_MORNING    = 1;
        private static final int PERIOD_DAY_AFTERNOON  = 2;
        private static final int PERIOD_DAY_NIGHT      = 3;
        private final long time;

        public TimeCondition(long time) {
            super();
            this.time = time;
        }

        public boolean isSameDay(TimeCondition condition) {
            Calendar c = Calendar.getInstance();
            c.setTime(new Date(time));
            int day = c.get(Calendar.DAY_OF_YEAR);
            int year = c.get(Calendar.YEAR);

            c = Calendar.getInstance();
            c.setTime(new Date(condition.time));
            int day2 = c.get(Calendar.DAY_OF_YEAR);
            int year2 = c.get(Calendar.YEAR);
            return year == year2 && day == day2;
        }

        public boolean isSamePeriodInDay(TimeCondition condition) {
            return isSameDay(condition) && getPeriodOfDay() == condition.getPeriodOfDay();
        }

        private int getPeriodOfDay() {
            Calendar c = Calendar.getInstance();
            c.setTime(new Date(time));
            int hour = c.get(Calendar.HOUR_OF_DAY);
            if(Arrays2.contains(MetaInfo.MORNING_HOURS, hour)){
                return PERIOD_DAY_MORNING;
            }
            if(Arrays2.contains(MetaInfo.AFTERNOON_HOURS, hour)){
                return PERIOD_DAY_AFTERNOON;
            }
            return PERIOD_DAY_NIGHT;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            if (!super.equals(o)) return false;

            TimeCondition that = (TimeCondition) o;

            return time == that.time;
        }
    }
}
