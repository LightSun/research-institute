package com.vida.common.entity;

import com.heaven7.java.image.detect.IHighLightData;
import com.heaven7.java.image.detect.Location;
import com.heaven7.java.visitor.ResultVisitor;
import com.heaven7.java.visitor.collection.VisitServices;

import java.util.List;

/**
 * @author heaven7
 */
public class MediaData {

    private String filePath;
    private long duration;
    private List<HighLightPair> highLightDataMap;

    public String getFilePath() {
        return filePath;
    }
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public long getDuration() {
        return duration;
    }
    public void setDuration(long duration) {
        this.duration = duration;
    }

    public List<HighLightPair> getHighLightDataMap() {
        return highLightDataMap;
    }
    public void setHighLightDataMap(List<HighLightPair> highLightDataMap) {
        this.highLightDataMap = highLightDataMap;
    }

    public static List<HighLightData> wrapHighLightData(List<IHighLightData> list){
        if(list == null){
            return null;
        }
        return VisitServices.from(list).map(new ResultVisitor<IHighLightData, HighLightData>() {
            @Override
            public HighLightData visit(IHighLightData data, Object param) {
                return new HighLightData(data);
            }
        }).getAsList();
    }

    public static class HighLightPair{
        private int time;
        private List<HighLightData> datas;

        public int getTime() {
            return time;
        }
        public void setTime(int time) {
            this.time = time;
        }

        public List<HighLightData> getDatas() {
            return datas;
        }
        public void setDatas(List<HighLightData> datas) {
            this.datas = datas;
        }
    }

    public static class HighLightData implements IHighLightData{

        private String name;
        private float score;
        private Location location;

        public HighLightData() {
        }

        public HighLightData(IHighLightData  data){
            this.name = data.getName();
            this.score = data.getScore();
            this.location = data.getLocation();
        }

        public void setName(String name) {
            this.name = name;
        }
        public void setScore(float score) {
            this.score = score;
        }
        public void setLocation(Location location) {
            this.location = location;
        }
        @Override
        public String getName() {
            return name;
        }
        @Override
        public float getScore() {
            return score;
        }
        @Override
        public Location getLocation() {
            return location;
        }
    }
}
