package com.heaven7.vida.research.face_bd;

import java.util.List;

/**
 * 人身体数据
 * Created by heaven7 on 2018/7/5 0005.
 */
public class BodyData {

    private int person_num;
    private List<PersonInfo> personInfos;
    private long log_id;

    @Override
    public String toString() {
        return "BodyData{" +
                "person_num=" + person_num +
                ", personInfos=" + personInfos +
                ", log_id=" + log_id +
                '}';
    }

    public static class PersonInfo {
        List<BodyPart> body_parts;
        FaceInfo.Location location;

        @Override
        public String toString() {
            return "PersonInfo{" +
                    "body_parts=" + body_parts +
                    ", location=" + location +
                    '}';
        }
    }

    //身体部位信息
    public static class BodyPart{
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

        @Override
        public String toString() {
            return "BodyPart{" +
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
    public static class Position{
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
