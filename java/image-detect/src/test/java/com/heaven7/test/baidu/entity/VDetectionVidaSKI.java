package com.heaven7.test.baidu.entity;

import com.heaven7.java.image.detect.IHighLightData;
import com.heaven7.java.image.detect.Location;

import java.util.List;

public class VDetectionVidaSKI {

    private long log_id;
    private List<Results> results;

    public long getLog_id() {
        return log_id;
    }

    public List<Results> getResults() {
        return results;
    }

    @Override
    public String toString() {
        return "VDetectionVidaSKI{" +
                "log_id=" + log_id +
                ", results=" + results +
                '}';
    }

    public static class Results implements IHighLightData{
        private String  name; //高光自定义名称
        private float score; //分数
        private Location location; //坐标

        public String getName() {
            return name;
        }

        public float getScore() {
            return score;
        }

        public Location getLocation() {
            return location;
        }

        @Override
        public String toString() {
            return "results{" +
                    "name='" + name + '\'' +
                    ", score=" + score +
                    ", location=" + location +
                    '}';
        }
    }
}
