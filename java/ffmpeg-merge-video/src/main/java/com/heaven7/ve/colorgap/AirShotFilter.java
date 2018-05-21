package com.heaven7.ve.colorgap;

import com.heaven7.java.base.anno.Nullable;

import java.util.List;

/**
 * the air-shot filter. which may locate between two chapters. or in chapter.
 * Created by heaven7 on 2018/5/15 0015.
 */

public interface AirShotFilter {
    /**
     * filter air shot between two chapters.
     * @param plaid the music plaid
     * @param left the left chapter
     * @param right the right chapter
     * @param airShots the air shots
     * @return the filtered air shot to fill the music plaid. or null if air-shots is empty.
     */
    @Nullable
    MediaPartItem filter(CutInfo.PlaidInfo plaid, Chapter left, Chapter right, List<MediaPartItem> airShots);

    /**
     * filter air shot between two stories.
     * @param plaid the music plaid
     * @param biasShot the bias shot
     * @param airShots the air shots, may be hold . care about this.
     * @return the filtered air shot to fill the music plaid. or null if air-shots is empty.
     */
   @Nullable MediaPartItem filter(CutInfo.PlaidInfo plaid, MediaPartItem biasShot, List<MediaPartItem> airShots);

}
