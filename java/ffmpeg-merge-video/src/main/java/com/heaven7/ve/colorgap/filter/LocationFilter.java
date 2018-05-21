package com.heaven7.ve.colorgap.filter;

import com.heaven7.ve.colorgap.GapColorFilter;
import com.heaven7.ve.colorgap.MetaInfo;

import static com.heaven7.ve.colorgap.GapColorFilter.MIN_SCORE;
import static com.heaven7.ve.colorgap.MetaInfo.*;

/**
 * Created by heaven7 on 2018/3/16 0016.
 */

public class LocationFilter extends TypeFilter {

    /**
     * create location filter.
     *
     * @param mCondition the location condition
     * @param cmpType    the compare type of location
     */
    public LocationFilter(LocationCondition mCondition, int cmpType) {
        super(mCondition, cmpType);
    }

    @Override
    public int getFlag() {
        return FLAG_LOCATION;
    }

    @Override
    public float computeScore(GapColorFilter.GapColorCondition condition) {
        if (!mCondition.isSampleTypeCondition(condition, true)) {
            return MIN_SCORE;
        }
        LocationCondition mCondition = this.mCondition.as();
        LocationCondition otherCondition = condition.as();

        switch (type) {
            default:
            case LOCATION_NEAR_GPS:
                return mCondition.gpsNearTo(otherCondition) ? GOOD_SCORE : MIN_SCORE;

            case LOCATION_SAME_REGION:
                if (!mCondition.location.getRegion().equals(otherCondition.location.getRegion())) {
                    return MIN_SCORE;
                }

            case LOCATION_SAME_CITY:
                if (!mCondition.location.getCity().equals(otherCondition.location.getCity())) {
                    return MIN_SCORE;
                }

            case LOCATION_SAME_PROVINCE:
                if (!mCondition.location.getProvince().equals(otherCondition.location.getProvince())) {
                    return MIN_SCORE;
                }

            case LOCATION_SAME_COUNTRY:
                return mCondition.location.getCountry().equals(otherCondition.location.getCountry()) ? MAX_SCORE : MIN_SCORE;
        }

    }

    public static class LocationCondition extends GapColorFilter.GapColorCondition {

        private static final int NEAR_DISTANCE = 10 * 1000;
        private final MetaInfo.LocationMeta location;

        public LocationCondition(MetaInfo.LocationMeta location) {
            super();
            this.location = location;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || !(o instanceof LocationCondition)) {
                return false;
            }
            LocationCondition that = (LocationCondition) o;
            return location.equals(that.location);
        }

        public boolean gpsNearTo(LocationCondition condition) {
            //TODO not support in java
           /* Location la = new Location("LA");
            la.setLongitude(location.getLongitude());
            la.setLatitude(location.getLatitude());

            Location lb = new Location("LB");
            lb.setLongitude(condition.location.getLongitude());
            lb.setLatitude(condition.location.getLatitude());
            return la.distanceTo(lb) <= NEAR_DISTANCE;*/
            return false;
        }
    }
}
