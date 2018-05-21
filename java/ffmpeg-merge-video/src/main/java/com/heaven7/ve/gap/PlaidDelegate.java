package com.heaven7.ve.gap;

import com.heaven7.ve.colorgap.GapColorFilter;

/**
 * Created by heaven7 on 2018/3/17 0017.
 */

public interface PlaidDelegate {

    /** if the plaid is hold by item. true if is hold. */
    boolean isHold();
    void setHold(boolean hold);

    float computeScore(GapColorFilter.GapColorCondition gapColorCondition);

    /**
     * get the duration of this plaid
     * @return the duration
     */
    long getDuration();
}
