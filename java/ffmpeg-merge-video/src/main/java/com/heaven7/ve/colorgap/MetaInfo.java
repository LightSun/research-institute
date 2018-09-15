package com.heaven7.ve.colorgap;

import com.heaven7.java.base.util.Logger;
import com.heaven7.java.base.util.Predicates;
import com.heaven7.java.base.util.SparseArray;
import com.heaven7.java.base.util.Throwables;
import com.heaven7.java.image.detect.HighLightArea;
import com.heaven7.java.image.detect.IHighLightData;
import com.heaven7.java.image.detect.Location;
import com.heaven7.java.visitor.FireVisitor;
import com.heaven7.java.visitor.collection.KeyValuePair;
import com.heaven7.java.visitor.collection.VisitServices;
import com.heaven7.java.visitor.util.Map;
import com.heaven7.utils.CollectionUtils;
import com.heaven7.utils.CommonUtils;
import com.heaven7.ve.PathTimeTraveller;
import com.heaven7.ve.SimpleCopyDelegate;
import com.heaven7.ve.TimeTraveller;
import com.heaven7.ve.utils.HighLightHelper;
import com.vida.common.entity.MediaData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by heaven7 on 2018/3/15 0015.
 */

public interface MetaInfo {

    String DIR_EMPTY = "empty";


    //===========================
    float WEIGHT_CAMERA_MOTION = 1;
    float WEIGHT_LOCATION      = 1;
    float WEIGHT_MEDIA_TYPE    = 1;
    float WEIGHT_SHORT_TYPE    = 1;

    float WEIGHT_MEDIA_DIR     = 3f;
    float WEIGHT_TIME          = 3;
    float WEIGHT_VIDEO_TAG     = 2.5f;
    float WEIGHT_SHOT_KEY      = 5f;
    float WEIGHT_SHOT_CATEGORY = 2.5f;

    int FLAG_TIME       = 0x0001;
    int FLAG_LOCATION   = 0x0002;
    int FLAG_MEDIA_TYPE = 0x0004;
    int FLAG_MEDIA_DIR  = 0x0008;
    int FLAG_SHOT_TYPE         = 0x0010;
    int FLAG_CAMERA_MOTION     = 0x0020;
    int FLAG_VIDEO_TAG         = 0x0040;
    int FLAG_SHOT_KEY          = 0x0080;
    int FLAG_SHOT_CATEGORY     = 0x0100;

    //================= location ==================
    int LOCATION_NEAR_GPS = 1;
    int LOCATION_SAME_COUNTRY = 2;
    int LOCATION_SAME_PROVINCE = 3; //省，市，区
    int LOCATION_SAME_CITY = 4;
    int LOCATION_SAME_REGION = 5;

    //=================== for camera motion ====================
    int STILL = 0;     // 静止, class 0
    int ZOOM = 3;    // 前后移动（zoomIn or zoomOut), class 1
    int ZOOM_IN = 4;
    int ZOOM_OUT = 5;
    int PAN = 6;    // 横向移动(leftRight or rightLeft), class 2
    int PAN_LEFT_RIGHT = 7;
    int PAN_RIGHT_LEFT = 8;
    int TILT = 9;    // 纵向移动(upDown or downUp), class 3
    int TILT_UP_DOWN = 10;
    int TILT_DOWN_UP = 11;
    int CATEGORY_STILL = 1;
    int CATEGORY_ZOOM = 2;
    int CATEGORY_PAN = 3;
    int CATEGORY_TILT = 4;

    //图像类型。视频，图片。 已有

    //============== shooting device =================
    /**
     * the shoot device: cell phone
     */
    int SHOOTING_DEVICE_CELLPHONE = 1;
    /**
     * the shoot device: camera
     */
    int SHOOTING_DEVICE_CAMERA = 2;

    /**
     * the shoot device: drone
     */
    int SHOOTING_DEVICE_DRONE = 3; //无人机


    //====================== shooting mode ===========================
    /**
     * shooting mode: normal
     */
    int SHOOTING_MODE_NORMAL = 1;
    /**
     * shooting mode: slow motion
     */
    int SHOOTING_MODE_SLOW_MOTION = 2;
    /**
     * shooting mode: time lapse
     */
    int SHOOTING_MODE_TIME_LAPSE = 3;

    //=========================== shot type =======================
    /**
     * shot type: big - long- short  ( 大远景)
     */
    int SHOT_TYPE_BIG_LONG_SHORT = 5;
    /**
     * shot type: long short ( 远景)
     */
    int SHOT_TYPE_LONG_SHORT = 4;

    /**  medium long shot. (中远景) */
    int SHOT_TYPE_MEDIUM_LONG_SHOT = 3;
    /**
     * shot type: medium shot(中景)
     */
    int SHOT_TYPE_MEDIUM_SHOT = 2;
    /**
     * shot type: close up - chest ( 特写-胸)
     */
    int SHOT_TYPE_MEDIUM_CLOSE_UP = 1;

    /**
     * shot type: close up -head ( 特写-头)
     */
    int SHOT_TYPE_CLOSE_UP = 0;

    int SHOT_TYPE_NONE  = -1;

    int CATEGORY_CLOSE_UP    = 12; //特写/近景
    int CATEGORY_MIDDLE_VIEW = 11; //中景
    int CATEGORY_VISION      = 10; //远景

    //========================== time ==========================
    int[] MORNING_HOURS = {7, 8, 9, 10, 11};
    int[] AFTERNOON_HOURS = {12, 13, 14, 15, 16, 17};
    //int[] NIGHT_HOURS = {18, 19, 20, 21, 22, 24, 1, 2, 3, 4, 5, 6};
    int TIME_SAME_DAY             = 1;
    int TIME_SAME_PERIOD_IN_DAY   = 2; //timeSamePeriodInDay

    class LocationMeta {
        private double longitude, latitude;
        /**
         * 高程精度比水平精度低原因，主要是GPS测出的是WGS-84坐标系下的大地高，而我们工程上，也就是电子地图上采用的高程一般是正常高，
         * 它们之间的转化受到高程异常和大地水准面等误差的制约。
         * 卫星在径向的定轨精度较差，也限制了GPS垂直方向的精度。
         */
        private int gpsHeight; //精度不高
        private String country, province, city, region; //国家， 省， 市， 区

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getRegion() {
            return region;
        }

        public void setRegion(String region) {
            this.region = region;
        }

        public int getGpsHeight() {
            return gpsHeight;
        }

        public void setGpsHeight(int gpsHeight) {
            this.gpsHeight = gpsHeight;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            LocationMeta that = (LocationMeta) o;

            if (Double.compare(that.longitude, longitude) != 0) return false;
            if (Double.compare(that.latitude, latitude) != 0) return false;
            if (gpsHeight != that.gpsHeight) return false;
            if (country != null ? !country.equals(that.country) : that.country != null)
                return false;
            if (province != null ? !province.equals(that.province) : that.province != null)
                return false;
            if (city != null ? !city.equals(that.city) : that.city != null) return false;
            return region != null ? region.equals(that.region) : that.region == null;
        }
    }

    /**
     * the meta data of image/video ,something may from 'AI'.
     */
    class ImageMeta extends SimpleCopyDelegate{
        private String path;
        private int mediaType;
        /** in mills  */
        private long date;

        /** in mill-seconds */
        private long duration;
        private int width, height;

        private LocationMeta location;

        /**
         * frames/second
         */
        private int fps = 30;
        /** see {@linkplain IShotRecognizer#CATEGORY_ENV} and etc. */
        private int shotCategory;
        /**
         * the shot type
         */
        private String shotType;

        /** shot key */
        private String shotKey;
        /**
         * 相机运动
         */
        private String cameraMotion;
        /** video tags */
        private List<List<Integer>> tags;

        /** the whole frame data of video(from analyse , like AI. ), after read should not change */
        private SparseArray<VideoDataLoadUtils.FrameData> frameDataMap;
        /** the high light data. key is the time in seconds. */
        private SparseArray<List<? extends IHighLightData>> highLightMap;
        private HighLightHelper mHighLightHelper;

        /** 主人脸个数 */
        private int mainFaceCount = -1;
        private int mBodyCount = -1;

        /** 通用tag信息 */
        private List<FrameTags> rawVideoTags;
        /** 人脸框信息 */
        private List<FrameFaceRects> rawFaceRects;

        //tag indexes
        private List<Integer> nounTags;
        private List<Integer> domainTags;
        private List<Integer> adjTags;

        private Location subjectLocation;

        //-------------------------- start High-Light ----------------------------
        /** set metadata for high light data. (from load high light) */
        public void setMediaData(MediaData mediaData) {
            List<MediaData.HighLightPair> hlMap = mediaData.getHighLightDataMap();
            if(!Predicates.isEmpty(hlMap)) {
                highLightMap = new SparseArray<>();
                VisitServices.from(hlMap).fire(new FireVisitor<MediaData.HighLightPair>() {
                    @Override
                    public Boolean visit(MediaData.HighLightPair pair, Object param) {
                        List<MediaData.HighLightData> highLightData = VEGapUtils.filterHighLightByScore(pair.getDatas());
                        if(!Predicates.isEmpty(highLightData)){
                            highLightMap.put(pair.getTime(), highLightData);
                        }
                        return null;
                    }
                });
            }
            mHighLightHelper = new HighLightHelper(highLightMap, mediaType == PathTimeTraveller.TYPE_IMAGE);
        }
        @SuppressWarnings("unchecked")
        public KeyValuePair<Integer, List<IHighLightData>> getHighLight(int time){
           return mHighLightHelper.getHighLight(time);
        }
        @SuppressWarnings("unchecked")
        public KeyValuePair<Integer, List<IHighLightData>> getHighLight(ColorGapContext context, TimeTraveller tt){
            return mHighLightHelper.getHighLight(context, tt);
        }

        public List<KeyValuePair<Integer, List<IHighLightData>>> getHighLights(ColorGapContext context, TimeTraveller tt){
            return mHighLightHelper.getHighLights(context, tt);
        }
        @SuppressWarnings("unchecked")
        public HighLightArea getHighLightArea(ColorGapContext context, TimeTraveller tt){
            return mHighLightHelper.getHighLightArea(context, tt);
        }

        public void setHighLightMap(SparseArray<List<? extends IHighLightData>> highLightMap) {
            this.highLightMap = highLightMap;
            this.mHighLightHelper = new HighLightHelper(highLightMap, mediaType == PathTimeTraveller.TYPE_IMAGE);
        }

        //-------------------------- end High-Light ----------------------------
        public SparseArray<VideoDataLoadUtils.FrameData> getFrameDataMap() {
            if(frameDataMap == null){
                frameDataMap = new SparseArray<>();
            }
            return frameDataMap;
        }
        public void setFrameDataMap(SparseArray<VideoDataLoadUtils.FrameData> frameDataMap) {
            this.frameDataMap = frameDataMap;
        }
        public void travelAllFrameDatas(Map.MapTravelCallback<Integer, VideoDataLoadUtils.FrameData> traveller){
            Throwables.checkNull(frameDataMap);
            CollectionUtils.travel(frameDataMap, traveller);
        }

        public List<Integer> getNounTags() {
            return nounTags != null ? nounTags : Collections.emptyList();
        }
        public void setNounTags(List<Integer> nounTags) {
            this.nounTags = nounTags;
        }

        public List<Integer> getDomainTags() {
            return domainTags != null ? domainTags : Collections.emptyList();
        }
        public void setDomainTags(List<Integer> domainTags) {
            this.domainTags = domainTags;
        }

        public List<Integer> getAdjTags() {
            return adjTags != null ? adjTags : Collections.emptyList();
        }
        public void setAdjTags(List<Integer> adjTags) {
            this.adjTags = adjTags;
        }
        public void setShotCategory(int shotCategory) {
            this.shotCategory = shotCategory;
        }

        public int getShotCategory() {
            return shotCategory;
        }
        public String getShotKey() {
            return shotKey;
        }
        public void setShotKey(String shotKey) {
            this.shotKey = shotKey;
        }

        public int getMainFaceCount() {
            return mainFaceCount;
        }
        public void setMainFaceCount(int mainFaceCount) {
            this.mainFaceCount = mainFaceCount;
        }
        public int getMediaType() {
            return mediaType;
        }
        public void setMediaType(int mediaType) {
            this.mediaType = mediaType;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        /** date in mills */
        public long getDate() {
            return date;
        }
        /** date in mills */
        public void setDate(long date) {
            this.date = date;
        }

        public long getDuration() {
            return duration;
        }

        /** set duration in mill-seconds */
        public void setDuration(long duration) {
            this.duration = duration;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public int getFps() {
            return fps;
        }

        public void setFps(int fps) {
            this.fps = fps;
        }

        public String getShotType() {
            return shotType;
        }

        public void setShotType(String shotType) {
            this.shotType = shotType;
        }

        public String getCameraMotion() {
            return cameraMotion;
        }

        public void setCameraMotion(String cameraMotion) {
            this.cameraMotion = cameraMotion;
        }

        public List<List<Integer>> getTags() {
            return tags;
        }
        public void setTags(List<List<Integer>> tags) {
            this.tags = tags;
        }
        public LocationMeta getLocation() {
            return location;
        }
        public void setLocation(LocationMeta location) {
            this.location = location;
        }
        public void setBodyCount(int size) {
            this.mBodyCount = size;
        }
        public int getBodyCount(){
            return mBodyCount;
        }
        public int getPersonCount() {
            return Math.max(mBodyCount, mainFaceCount);
        }

        public Location getSubjectLocation() {
            return subjectLocation;
        }
        public void setSubjectLocation(Location subjectLocation) {
            this.subjectLocation = subjectLocation;
        }

        //============================================================
        public List<FrameFaceRects> getAllFaceRects() {
            if(frameDataMap == null){
                return Collections.emptyList();
            }
            List<FrameFaceRects> result = new ArrayList<>();
            final int size = frameDataMap.size();
            for (int i = 0; i < size ; i++) {
                FrameFaceRects faceRects = frameDataMap.valueAt(i).getFaceRects();
                if(faceRects != null) {
                    result.add(faceRects);
                }else{
                    //no face we just add a mock
                }
            }
            return result;
        }
        public List<FrameTags> getVideoTags(TimeTraveller part) {
            return getVideoTags(part.getStartTime(), part.getEndTime());
        }

        /** get all video tags.  startTime and endTime in frames */
        public List<FrameTags> getVideoTags(long startTime, long endTime) {
            if(frameDataMap == null){
                return Collections.emptyList();
            }
            List<FrameTags> result = new ArrayList<>();
            final int size = frameDataMap.size();
            for (int i = 0; i < size ; i++) {
                long key = CommonUtils.timeToFrame(frameDataMap.keyAt(i), TimeUnit.SECONDS);
                if(key >= startTime && key <= endTime){
                    FrameTags tag = frameDataMap.valueAt(i).getTag();
                    if(tag != null) {
                        result.add(tag);
                    }
                }
            }
            return result;
        }
        public List<FrameTags> getAllVideoTags() {
            if(frameDataMap == null){
                return Collections.emptyList();
            }
            List<FrameTags> result = new ArrayList<>();
            final int size = frameDataMap.size();
            for (int i = 0; i < size ; i++) {
                //long key = CommonUtils.timeToFrame(frameDataMap.keyAt(i), TimeUnit.SECONDS);
                FrameTags tag = frameDataMap.valueAt(i).getTag();
                if(tag != null) {
                    result.add(tag);
                }
            }
            return result;
        }
        public List<FrameFaceRects> getFaceRects(TimeTraveller part) {
            return getFaceRects(part.getStartTime(), part.getEndTime());
        }
        /** get all face rects.  startTime and endTime in frames */
        public List<FrameFaceRects> getFaceRects(long startTime, long endTime) {
            if(frameDataMap == null){
                return Collections.emptyList();
            }
            List<FrameFaceRects> result = new ArrayList<>();
            final int size = frameDataMap.size();
            for (int i = 0; i < size ; i++) {
                long key = CommonUtils.timeToFrame(frameDataMap.keyAt(i), TimeUnit.SECONDS);
                if(key >= startTime && key <= endTime){
                    FrameFaceRects faceRects = frameDataMap.valueAt(i).getFaceRects();
                    if(faceRects != null) {
                        result.add(faceRects);
                    }
                }
            }
            return result;
        }

        public void setRawVideoTags(List<FrameTags> tags) {
             rawVideoTags = tags;
        }
        public List<FrameTags> getRawVideoTags() {
            return rawVideoTags;
        }

        public List<FrameFaceRects> getRawFaceRects() {
            return rawFaceRects;
        }
        public void setRawFaceRects(List<FrameFaceRects> rawFaceRects) {
            this.rawFaceRects = rawFaceRects;
        }
        /**  判断这段原始视频内容是否是“人脸为主”. work before cut */
        public boolean containsFaces(){
            if(rawFaceRects == null){
                if(frameDataMap == null){
                    return false;
                }
                rawFaceRects = getAllFaceRects();
            }
            if(Predicates.isEmpty(rawFaceRects)){
                return false;
            }
            List<FrameFaceRects> tempList = new ArrayList<>();
            VisitServices.from(rawFaceRects).visitForQueryList((ffr, param) -> ffr.hasRect(), tempList);
            float result = tempList.size() * 1f / rawFaceRects.size();
            Logger.d("ImageMeta", "isHumanContent", "human.percent = "
                    + result + " ,path = " + path);
            return result > 0.55f;
        }

        /** 判断这段原始视频是否被标记原始tag（人脸 or 通用） . work before cut */
        public boolean hasRawTags(){
            return frameDataMap != null && frameDataMap.size() > 0;
        }

        @Override
        public void setFrom(SimpleCopyDelegate sc) {
             if(sc instanceof ImageMeta){
                 ImageMeta src = (ImageMeta) sc;
                 setShotType(src.getShotType());
                 setShotCategory(src.getShotCategory());
                 setShotKey(src.getShotKey());

                 setMainFaceCount(src.getMainFaceCount());
                 setDuration(src.getDuration());
                 setMediaType(src.getMediaType());
                 setPath(src.getPath());
                 setCameraMotion(src.getCameraMotion());
                 setDate(src.getDate());
                 setFps(src.getFps());
                 setHeight(src.getHeight());
                 setWidth(src.getWidth());
                 //not deep copy
                 setTags(src.tags);
                 setAdjTags(src.adjTags);
                 setNounTags(src.nounTags);
                 setDomainTags(src.domainTags);

                 setLocation(src.getLocation());
                 setRawFaceRects(src.getRawFaceRects());
                 setRawVideoTags(src.getRawVideoTags());
                 setFrameDataMap(src.frameDataMap);
                 setHighLightMap(src.highLightMap);
             }
        }
    }

}
