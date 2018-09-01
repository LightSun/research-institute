package com.heaven7.ve.colorgap;

import java.util.List;

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
    int CATEGORY_PRODUCT = 4;

    /**
     * request recognize key-point
     * @param parts the media parts
     */
    void requestKeyPoint(List<MediaPartItem> parts, Callback callback);

    /**
     * request recognize subject
     * @param parts the media parts
     */
    void requestSubject(List<MediaPartItem> parts, Callback callback);

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


    interface Callback{
        /** called on recognize done . may be key-point or subject */
        void onRecognizeDone(List<MediaPartItem> parts);
    }
}
