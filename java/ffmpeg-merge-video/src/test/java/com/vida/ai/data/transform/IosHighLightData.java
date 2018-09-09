package com.vida.ai.data.transform;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by heaven7 on 2018/9/9.
 */
public class IosHighLightData {

    @SerializedName("assetID")
    private String assertId;

    @SerializedName("frameDomainTags")
    private List<DomainTagData> domainTagData;

    public List<DomainTagData> getDomainTagData() {
        return domainTagData;
    }
    public void setDomainTagData(List<DomainTagData> domainTagData) {
        this.domainTagData = domainTagData;
    }

    public static class DomainTagData{
        private List<Integer> frameSize;
        @SerializedName("domainTags")
        private List<Data> datas;
        @SerializedName("timePoint")
        private int time;

        public List<Integer> getFrameSize() {
            return frameSize;
        }
        public void setFrameSize(List<Integer> frameSize) {
            this.frameSize = frameSize;
        }

        public List<Data> getDatas() {
            return datas;
        }
        public void setDatas(List<Data> datas) {
            this.datas = datas;
        }

        public int getTime() {
            return time;
        }
        public void setTime(int time) {
            this.time = time;
        }
    }

    public static class Data{
        private double x, y, width, height;
        private float confidence;
        @SerializedName("identifier")
        private String name;

        public double getX() {
            return x;
        }
        public void setX(double x) {
            this.x = x;
        }

        public double getY() {
            return y;
        }
        public void setY(double y) {
            this.y = y;
        }

        public double getWidth() {
            return width;
        }
        public void setWidth(double width) {
            this.width = width;
        }

        public double getHeight() {
            return height;
        }
        public void setHeight(double height) {
            this.height = height;
        }

        public float getConfidence() {
            return confidence;
        }
        public void setConfidence(float confidence) {
            this.confidence = confidence;
        }

        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
    }

}
