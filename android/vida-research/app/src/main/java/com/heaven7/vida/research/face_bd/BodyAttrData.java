package com.heaven7.vida.research.face_bd;

import java.util.List;

/**
 * Created by heaven7 on 2018/7/5 0005.
 */
public class BodyAttrData {

    private List<PersonAttitudeInfo> person_info;
    int person_num;
    long log_id;

    @Override
    public String toString() {
        return "BodyAttrData{" +
                "person_info=" + person_info +
                ", person_num=" + person_num +
                ", log_id=" + log_id +
                '}';
    }

    public static class PersonAttitudeInfo {
        Attribute attributes;
        FaceInfo.Location location;

        @Override
        public String toString() {
            return "PersonAttitudeInfo{" +
                    "attributes=" + attributes +
                    ", location=" + location +
                    '}';
        }
    }

    public static class Attribute {
        private BaseAttr gender;
        private BaseAttr age;
        private BaseAttr upper_color; //上半身衣着颜色
        private BaseAttr lower_color; //下半身衣着颜色
        private BaseAttr cellphone;   //是否使用手机
        private BaseAttr lower_wear;  //下半身服饰. 长袖、短袖
        private BaseAttr upper_wear;  //长裤、短裤、裙
        private BaseAttr headwear;        //是否戴帽子,  无帽、戴帽
        private BaseAttr glasses;         //是否戴眼镜. 戴眼镜、无眼镜
        private BaseAttr upper_wear_fg;   //上身服饰细分类

        private BaseAttr upper_wear_texture; //上下服饰纹理
        private BaseAttr lower_wear_texture;
        private BaseAttr orientation;
        private BaseAttr umbrella;          //是否称伞

        @Override
        public String toString() {
            return "Attribute{" +
                    "gender=" + gender +
                    ", age=" + age +
                    ", upper_color=" + upper_color +
                    ", lower_color=" + lower_color +
                    ", cellphone=" + cellphone +
                    ", lower_wear=" + lower_wear +
                    ", upper_wear=" + upper_wear +
                    ", headwear=" + headwear +
                    ", glasses=" + glasses +
                    ", upper_wear_fg=" + upper_wear_fg +
                    ", upper_wear_texture=" + upper_wear_texture +
                    ", lower_wear_texture=" + lower_wear_texture +
                    ", orientation=" + orientation +
                    ", umbrella=" + umbrella +
                    '}';
        }
    }


    public static class BaseAttr {
        String name;
        float score;

        @Override
        public String toString() {
            return "BaseAttr{" +
                    "name='" + name + '\'' +
                    ", score=" + score +
                    '}';
        }
    }
}
