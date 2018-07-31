package com.heaven7.test.baidu.entity;

import com.heaven7.java.image.detect.Location;

public class VObjectDetect {

    private Long log_id;
    private Location result; // 主体检测坐标

    public Long getLog_id() {
        return log_id;
    }

    public void setLog_id(Long log_id) {
        this.log_id = log_id;
    }

    public Location getResult() {
        return result;
    }

    @Override
    public String toString() {
        return "VObjectDetect{" +
                "log_id=" + log_id +
                ", result=" + result +
                '}';
    }

}
