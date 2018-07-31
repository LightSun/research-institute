package com.heaven7.test.baidu.entity;

import com.google.gson.annotations.SerializedName;
import com.heaven7.java.image.detect.KeyPointData;
import com.heaven7.java.image.detect.LocationF;

import java.util.List;

public class VBodyAnalysis {

    private int person_num;
    @SerializedName("person_info")
    private List<PersonInfo> personInfos;
    private long log_id;

    public List<PersonInfo> getPersonInfos() {
        return personInfos;
    }

    @Override
    public String toString() {
        return "VBodyAnalysis{" +
                "person_num=" + person_num +
                ", personInfos=" + personInfos +
                ", log_id=" + log_id +
                '}';
    }

    public static class PersonInfo implements KeyPointData{
        private Body_parts body_parts;
        private LocationF location;

        @Override
        public String toString() {
            return "PersonInfo{" +
                    "body_parts=" + body_parts +
                    ", location=" + location +
                    '}';
        }
        @Override
        public int getKeyPointCount() {
            return body_parts != null ? body_parts.getKeyPointCount(): 0;
        }
    }

    //身体部位信息
    public static class Body_parts{
        private Position left_ankle; //左脚踝
        private Position left_elbow; //左手肘
        private Position left_hip;   //左髋部.也是胯部
        private Position left_knee;     //左膝
        private Position left_shoulder; //左肩
        private Position left_wrist;    //左手腕
        private Position neck;          //颈
        private Position nose;          //鼻子
        private Position right_ankle;
        private Position right_elbow;
        private Position right_hip;
        private Position right_knee;
        private Position right_shoulder;
        private Position right_wrist;

        private int keyPointCount = -1;

        public int getKeyPointCount() {
            if(keyPointCount == -1){
                int base = 0;
                if(left_ankle !=null && isValid(left_ankle)){
                    base += 1;
                }
                if(left_elbow !=null && isValid(left_elbow)){
                    base += 1;
                }
                if(left_hip !=null && isValid(left_hip)){
                    base += 1;
                }
                if(left_knee !=null && isValid(left_knee)){
                    base += 1;
                }
                if(left_shoulder !=null && isValid(left_shoulder)){
                    base += 1;
                }
                if(left_wrist !=null && isValid(left_wrist)){
                    base += 1;
                }
                //---------
                if(neck !=null && isValid(neck)){
                    base += 1;
                }
                if(nose !=null && isValid(nose)){
                    base += 1;
                }
                //--------------------
                if(right_ankle !=null && isValid(right_ankle)){
                    base += 1;
                }
                if(right_elbow !=null && isValid(right_elbow)){
                    base += 1;
                }
                if(right_hip !=null && isValid(right_hip)){
                    base += 1;
                }
                if(right_knee !=null && isValid(right_knee)){
                    base += 1;
                }
                if(right_shoulder !=null && isValid(right_shoulder)){
                    base += 1;
                }
                if(right_wrist !=null && isValid(right_wrist)){
                    base += 1;
                }
                keyPointCount = base;
            }
            return keyPointCount;
        }

        boolean isValid(Position pos){
            return pos.x !=0 || pos.y != 0;
        }

        @Override
        public String toString() {
            return "Body_parts{" +
                    "left_ankle=" + left_ankle +
                    ", left_elbow=" + left_elbow +
                    ", left_hip=" + left_hip +
                    ", left_knee=" + left_knee +
                    ", left_shoulder=" + left_shoulder +
                    ", left_wrist=" + left_wrist +
                    ", neck=" + neck +
                    ", nose=" + nose +
                    ", right_ankle=" + right_ankle +
                    ", right_elbow=" + right_elbow +
                    ", right_hip=" + right_hip +
                    ", right_knee=" + right_knee +
                    ", right_shoulder=" + right_shoulder +
                    ", right_wrist=" + right_wrist +
                    '}';
        }
    }

    /** 位置。带坐标信息的 */
    public static class Position {
        float x, y;

        @Override
        public String toString() {
            return "Position{" +
                    "x=" + x +
                    ", y=" + y +
                    '}';
        }
    }

}
