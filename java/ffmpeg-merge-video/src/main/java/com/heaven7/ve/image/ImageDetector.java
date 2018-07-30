package com.heaven7.ve.image;

import java.util.List;

public interface ImageDetector {

    void detectHighLight(byte[] imageData, HighLightCallback callback);

    /** detect the key points */
    void detectKeyPoints();

    interface HighLightCallback {

        void onFailed(int code, String msg);

        void onSuccess(List<? extends IHighLightData> dataList);
    }
    interface KeyPointCallback {

        void onFailed(int code, String msg);

        void onSuccess(KeyPointData data);
    }

}
