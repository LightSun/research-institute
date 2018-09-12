package com.heaven7.ve.colorgap;

import com.heaven7.java.base.util.Predicates;
import com.heaven7.java.base.util.Throwables;
import com.heaven7.utils.FileUtils;
import com.heaven7.utils.TextUtils;
import com.heaven7.ve.colorgap.filter.*;

import static com.heaven7.ve.colorgap.MetaInfo.*;

/**
 * @author heaven7
 */
public class MetaInfoUtils {

   public static String getFlagString(int flag) {
        switch (flag){
            case MetaInfo.FLAG_MEDIA_DIR:
                return "FLAG_MEDIA_DIR";
            case MetaInfo.FLAG_TIME:
                return "FLAG_TIME";
            case MetaInfo.FLAG_LOCATION:
                return "FLAG_LOCATION";
            case MetaInfo.FLAG_MEDIA_TYPE:
                return "FLAG_MEDIA_TYPE";
            case MetaInfo.FLAG_SHOT_TYPE:
                return "FLAG_SHOT_TYPE";
            case MetaInfo.FLAG_CAMERA_MOTION:
                return "FLAG_CAMERA_MOTION";
            case MetaInfo.FLAG_VIDEO_TAG:
                return "FLAG_VIDEO_TAG";
            case MetaInfo.FLAG_SHOT_KEY:
                return "FLAG_SHOT_KEY";
            case MetaInfo.FLAG_SHOT_CATEGORY:
                return "FLAG_SHOT_CATEGORY";

            default:
                throw new UnsupportedOperationException("flag = " + flag);
        }
    }

    public static int getShotTypeFrom(String shotType) {
        if(TextUtils.isEmpty(shotType)){
            return SHOT_TYPE_NONE;
        }
        switch (shotType) {
            case "closeUp":
                return SHOT_TYPE_CLOSE_UP;
            case "mediumCloseUp":
                return SHOT_TYPE_MEDIUM_CLOSE_UP;
            case "mediumShot":
                return SHOT_TYPE_MEDIUM_SHOT;
            case "mediumLongShot":
                return SHOT_TYPE_MEDIUM_LONG_SHOT;
            case "longShot":
                return SHOT_TYPE_LONG_SHORT;
            case "veryLongShot":
                return SHOT_TYPE_BIG_LONG_SHORT;
            default:
                throw new UnsupportedOperationException("wrong shotType " + shotType);
        }
    }
    public static String getShotTypeString(int shotType) {
        switch (shotType) {
            case SHOT_TYPE_CLOSE_UP:
                return "closeUp";
            case SHOT_TYPE_MEDIUM_CLOSE_UP:
                return "mediumCloseUp";
            case SHOT_TYPE_MEDIUM_SHOT:
                return "mediumShot";
            case SHOT_TYPE_MEDIUM_LONG_SHOT:
                return "mediumLongShot";
            case SHOT_TYPE_LONG_SHORT:
                return "longShot";
            case SHOT_TYPE_BIG_LONG_SHORT:
                return "veryLongShot";

            case SHOT_TYPE_NONE:
                return "none";
            default:
                throw new UnsupportedOperationException("wrong shotType " + shotType);
        }
    }

    public static int getCameraMotionFrom(String cameraMotion){
        switch (cameraMotion){
            case "still":
                return STILL;
            case "zoom":
                return ZOOM;
            case "zoomIn":
                return ZOOM_IN;
            case "zoomOut":
                return ZOOM_OUT;
            case "pan":
                return PAN;
            case "leftRight":
                return PAN_LEFT_RIGHT;
            case "rightLeft":
                return PAN_RIGHT_LEFT;
            case "tilt":
                return TILT;
            case "upDown":
                return TILT_UP_DOWN;
            case "downUp":
                return TILT_DOWN_UP;
            default:
                throw new UnsupportedOperationException("wrong cameraMotion " + cameraMotion);
        }
    }

    public static float computeWeight(int flag) {
        switch (flag){
            case FLAG_TIME:
                return WEIGHT_TIME;

            case FLAG_CAMERA_MOTION:
                return WEIGHT_CAMERA_MOTION;

            case FLAG_LOCATION:
                return WEIGHT_LOCATION;

            case FLAG_MEDIA_DIR:
                return WEIGHT_MEDIA_DIR;

            case FLAG_MEDIA_TYPE:
                return WEIGHT_MEDIA_TYPE;

            case FLAG_SHOT_TYPE:
                return WEIGHT_SHORT_TYPE;

            case FLAG_VIDEO_TAG:
                return WEIGHT_VIDEO_TAG;

            case FLAG_SHOT_KEY:
                return WEIGHT_SHOT_KEY;

            case FLAG_SHOT_CATEGORY:
                return WEIGHT_SHOT_CATEGORY;

            case 0:
                return 1;
        }
        throw new UnsupportedOperationException("wrong flag = " + flag);
    }

    //color plan
    static GroupFilter.GroupCondition createColorCondition(ImageMeta meta){
        GroupFilter.GroupCondition gc = new GroupFilter.GroupCondition();
        //location
        if(meta.getLocation() != null){
            gc.addGapColorCondition(new LocationFilter.LocationCondition(meta.getLocation()));
        }
        //dir
        String path = meta.getPath();
        Throwables.checkNull(path);
        String prefix_android = "/storage/emulated/0/";
        if(path.startsWith(prefix_android)){
            path = path.substring(prefix_android.length());
        }
        String fileDir = FileUtils.getFileDir(path, 1, false);
        if(fileDir != null) {
            MediaDirFilter.MediaDirCondition mdc = new MediaDirFilter.MediaDirCondition();
            mdc.addTag(fileDir);
            gc.addGapColorCondition(mdc);
        }
        /*String[] strs = path.split("/"); //last is the file name
        if(strs.length > 1) {
            MediaDirFilter.MediaDirCondition mdc = new MediaDirFilter.MediaDirCondition();
            //the last index is the file name.ignore
            for (int size = strs.length, i = 0; i < size - 1; i++) {
                mdc.addTag(strs[i]);
            }
            gc.addGapColorCondition(mdc);
        }*/
        //date
        if(meta.getDate() > 0) {
            TimeFilter.TimeCondition time = new TimeFilter.TimeCondition(meta.getDate());
            gc.addGapColorCondition(time);
        }
        //media type
        gc.addGapColorCondition(new MediaTypeFilter.MediaTypeCondition(meta.getMediaType()));
        //camera motion
        if(!TextUtils.isEmpty(meta.getCameraMotion())){
            gc.addGapColorCondition(new CameraMotionFilter.CameraMotionCondition(meta.getCameraMotion()));
        }
        // tags
        if(!Predicates.isEmpty(meta.getTags())){
            gc.addGapColorCondition( new VideoTagFilter.VideoTagCondition(meta.getTags()));
        }
        //shot type
        if(!TextUtils.isEmpty(meta.getShotType())){
            gc.addGapColorCondition(new ShotTypeFilter.ShotTypeCondition(meta.getShotType()));
        }
        //shot key
        if(!TextUtils.isEmpty(meta.getShotKey())){
            gc.addGapColorCondition(new ShotKeyFilter.ShotKeyCondition(meta.getShotKey()));
        }
        //shot category
        if(meta.getShotCategory() != 0){
            gc.addGapColorCondition(new ShotCategoryFilter.ShotCategoryCondition(meta.getShotCategory()));
        }
        return gc;
    }

}
