package com.heaven7.util;

import java.util.List;

public class FrameTagInfo {

    private CsvDetail detail;
    private List<Integer> timePoints;

    public CsvDetail getDetail() {
        return detail;
    }
    public void setDetail(CsvDetail detail) {
        this.detail = detail;
    }

    public void setTimePoints(List<Integer> timePoints) {
        this.timePoints = timePoints;
    }
    public String getMediaPath(){
        return detail.getMediaSrc().getFilePath();
    }
    public List<Integer> getTimePoints(){
        return timePoints;
    }

}
