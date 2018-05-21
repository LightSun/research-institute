package com.heaven7.ve.colorgap.filter;


import com.heaven7.ve.colorgap.GapColorFilter;
import com.heaven7.ve.colorgap.MetaInfo;

import static com.heaven7.ve.colorgap.MetaInfo.*;

/**
 * Created by heaven7 on 2018/3/16 0016.
 */

public class CameraMotionFilter extends GapColorFilter {

    public CameraMotionFilter(CameraMotionCondition condition) {
        super(condition);
    }
    @Override
    public int getFlag() {
        return FLAG_CAMERA_MOTION;
    }

    /**
     * the camera motion filter
     *
     * @author heaven7
     */
    public static class CameraMotionCondition extends GapColorFilter.GapColorCondition {

        public CameraMotionCondition(String cameraMotion) {
            super(MetaInfo.getCameraMotionFrom(cameraMotion));
        }

        @Override
        protected int getCategory(int type) {
            switch (type) {
                case STILL:
                    return CATEGORY_STILL;
                case ZOOM:
                case ZOOM_IN:
                case ZOOM_OUT:
                    return CATEGORY_ZOOM;

                case TILT:
                case TILT_DOWN_UP:
                case TILT_UP_DOWN:
                    return CATEGORY_TILT;

                case PAN:
                case PAN_LEFT_RIGHT:
                case PAN_RIGHT_LEFT:
                    return CATEGORY_PAN;
                default:
                    throw new UnsupportedOperationException();
            }
        }

        @Override
        public boolean isCoreType() {
            switch (type) {
                case STILL:
                case ZOOM:
                case TILT:
                case PAN:
                    return true;
            }
            return false;
        }
    }
}
