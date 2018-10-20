package com.heaven7.ve.cross_os;

/**
 * @author heaven7
 */
public interface ISpecialEffectInfo extends IEffectInfo {

    //1缓慢缩小2缓慢放大3左平移4右平移5极慢(慢速)
    /** 无 */
    int TYPE_NONE = 0;

    int TYPE_ZOOM_OUT = 1;
    int TYPE_ZOOM_IN  = 2;
    int TYPE_LEFT_TRANSLATION = 3;
    int TYPE_RIGHT_TRANSLATION = 4;

    int TYPE_SPEED_SLOW      = 5;
    int TYPE_SPEED_VERY_SLOW = 6;
    int TYPE_SPEED_FAST      = 7;
    int TYPE_SPEED_VERY_FAST = 8;

    int TYPE_SPEED_SLOW_TO_FAST   = 9;
    int TYPE_SPEED_FAST_TO_SLOW   = 10;
    int TYPE_SPEED_NORMAL_TO_SLOW = 11;
    int TYPE_SPEED_SLOW_TO_NORMAL = 12;

    /** 变焦 */
    int TYPE_DOUBLE_ZOOM_IN = 21;
    /** 分屏 */
    int TYPE_SPLIT_SCREEN      = 30;
    /** 分屏: 竖直 2分 */
    int TYPE_SPLIT_SCREEN_V2   = 31;
    /** 分屏: 竖直 3分 */
    int TYPE_SPLIT_SCREEN_V3   = 32;
    /** 分屏: 水平 2分 */
    int TYPE_SPLIT_SCREEN_H2   = 33;
    /** 分屏: 水平 3分 */
    int TYPE_SPLIT_SCREEN_H3   = 34;

    /* the position of split-screens */
    int SPLIT_SCREEN_H2_LEFT  = 1;
    int SPLIT_SCREEN_H2_RIGHT = 2;
    int SPLIT_SCREEN_H3_LEFT  = 3;
    int SPLIT_SCREEN_H3_RIGHT = 4;
    int SPLIT_SCREEN_H3_CENTER = 5;

    int SPLIT_SCREEN_V2_TOP    = 6;
    int SPLIT_SCREEN_V2_BOTTOM = 7;
    int SPLIT_SCREEN_V3_BOTTOM = 8;
    int SPLIT_SCREEN_V3_TOP    = 9;
    int SPLIT_SCREEN_V3_CENTER = 10;

    void setCategory(int category);

    int getCategory();

    void setMultiple(float multiple);

    float getMultiple();

}
