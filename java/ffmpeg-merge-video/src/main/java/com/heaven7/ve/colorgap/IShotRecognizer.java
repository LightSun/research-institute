package com.heaven7.ve.colorgap;

/**
 * @author heaven7
 */
public interface IShotRecognizer {

    /**
     * indicate shot is environment
     */
    int CATEGORY_ENV = 1;
    /**
     * indicate shot is part
     */
    int CATEGORY_PART = 2;
    /**
     * indicate shot is product
     */
    int CATEGORY_PRODUCT = 3;

    /**
     * recognize the shot category
     * @param item the media part item
     * @return the shot category. often is {@linkplain #CATEGORY_ENV} and etc. 0 means none.
     */
    int recognizeShotCategory(MediaPartItem item);

    /**
     * get shot type for media part item
     * @param item the media part item
     * @return the shot type
     */
    int getShotType(MediaPartItem item);
}
