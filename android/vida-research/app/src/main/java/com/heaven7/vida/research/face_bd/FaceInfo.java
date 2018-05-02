package com.heaven7.vida.research.face_bd;

import android.graphics.Rect;

/**
 * face info of https://ai.baidu.com/docs#/Face-Detect/top
 * Created by heaven7 on 2018/4/27 0027.
 */

public class FaceInfo {

    private Location location;
    /**
     * 人脸置信度
     */
    private double face_probability;
    /**
     * 人脸框相对于竖直方向的顺时针旋转角，[-180,180]
     */
    private int rotation_angle;

    /**
     * 年龄。face_fields包含age时返回
     */
    private double age;
    /**
     * 美丑打分，范围0-100，越大表示越美。face_fields包含beauty时返回
     */
    private double beauty;

    /**
     * 三维旋转之左右旋转角[-90(左), 90(右)]
     */
    private double yaw;
    /**
     * 三维旋转之俯仰角度[-90(上), 90(下)]
     */
    private double pitch;
    /**
     * 平面内旋转角[-180(逆时针), 180(顺时针)]
     */
    private double roll;

    /**
     * 表情，0，不笑；1，微笑；2，大笑。face_fields包含expression时返回
     */
    private int expression;
    /**
     * 表情置信度，范围0~1。face_fields包含expression时返回
     */
    private double expression_probability;
    /**
     * 性别
     */
    private String gender;
    /**
     * 性别置信度，范围[0~1]，face_fields包含gender时返回
     */
    private double gender_probability;
    /**
     * 是否带眼镜，0-无眼镜，1-普通眼镜，2-墨镜。face_fields包含glasses时返回
     */
    private int glasses;
    /**
     * 眼镜置信度，范围[0~1]，face_fields包含glasses时返回
     */
    private double glasses_probability;

    /**
     * 人种 yellow、white、black、arabs。face_fields包含race时返回
     */
    private String race;
    /**
     * 人种置信度，范围[0~1]，face_fields包含race时返回
     */
    private double race_probability;

    //landmark , faceshape, landmark72


    public Location getLocation() {
        return location;
    }

    public double getFace_probability() {
        return face_probability;
    }

    public int getRotation_angle() {
        return rotation_angle;
    }

    public double getAge() {
        return age;
    }

    public double getBeauty() {
        return beauty;
    }

    public double getYaw() {
        return yaw;
    }

    public double getPitch() {
        return pitch;
    }

    public double getRoll() {
        return roll;
    }

    public int getExpression() {
        return expression;
    }

    public double getExpression_probability() {
        return expression_probability;
    }

    public String getGender() {
        return gender;
    }

    public double getGender_probability() {
        return gender_probability;
    }

    public int getGlasses() {
        return glasses;
    }

    public double getGlasses_probability() {
        return glasses_probability;
    }

    public String getRace() {
        return race;
    }

    public double getRace_probability() {
        return race_probability;
    }

    @Override
    public String toString() {
        return "FaceInfo{" +
                "location=" + location +
                ", face_probability=" + face_probability +
                ", rotation_angle=" + rotation_angle +
                ", age=" + age +
                ", beauty=" + beauty +
                ", yaw=" + yaw +
                ", pitch=" + pitch +
                ", roll=" + roll +
                ", expression=" + expression +
                ", expression_probability=" + expression_probability +
                ", gender='" + gender + '\'' +
                ", gender_probability=" + gender_probability +
                ", glasses=" + glasses +
                ", glasses_probability=" + glasses_probability +
                ", race='" + race + '\'' +
                ", race_probability=" + race_probability +
                '}';
    }

    /**
     * 人脸在图片中的位置
     */
    public static class Location {

        public int left, top, width, height;

        public Rect toRect(){
            return new Rect(left, top, left + width, top + height);
        }

        @Override
        public String toString() {
            return "Location{" +
                    "left=" + left +
                    ", top=" + top +
                    ", width=" + width +
                    ", height=" + height +
                    '}';
        }
    }

    /**
     * 人脸质量信息。face_fields包含qualities时返回
     */
    public static class Qualities {
        /**
         * 人脸模糊程度，范围[0~1]，0表示清晰，1表示模糊
         */
        public double blur;
        /**
         * 取值范围在[0~255],表示脸部区域的光照程度
         */
        public int illumination;
        /**
         * 人脸完整度，0或1, 0为人脸溢出图像边界，1为人脸都在图像边界内
         */
        public int completeness;
        /**
         * 真实人脸/卡通人脸置信度
         */
        public Type type;

        @Override
        public String toString() {
            return "Qualities{" +
                    "blur=" + blur +
                    ", illumination=" + illumination +
                    ", completeness=" + completeness +
                    ", type=" + type +
                    '}';
        }
    }

    /**
     * 人脸各部分遮挡的概率，范围[0~1]，0表示完整，1表示不完整
     */
    public static class Occlusion {
        /**
         * 左眼遮挡比例
         */
        public double left_eye;
        public double right_eye;
        /**
         * 鼻子遮挡比例
         */
        public double nose;
        /**
         * 嘴巴遮挡比例
         */
        public double mouth;
        /**
         * 左脸颊遮挡比例
         */
        public double left_cheek;
        public double right_cheek;
        /**
         * 下巴遮挡比例
         */
        public double chin;

        @Override
        public String toString() {
            return "Occlusion{" +
                    "left_eye=" + left_eye +
                    ", right_eye=" + right_eye +
                    ", nose=" + nose +
                    ", mouth=" + mouth +
                    ", left_cheek=" + left_cheek +
                    ", right_cheek=" + right_cheek +
                    ", chin=" + chin +
                    '}';
        }
    }

    /**
     * 人脸置信度信息
     */
    public static class Type {
        /**
         * 真实人脸置信度，[0~1]，大于0.5可以判断为人脸
         */
        public double human;
        /**
         * 卡通人脸置信度，[0~1]
         */
        public double cartoon;

        @Override
        public String toString() {
            return "Type{" +
                    "human=" + human +
                    ", cartoon=" + cartoon +
                    '}';
        }
    }
}
