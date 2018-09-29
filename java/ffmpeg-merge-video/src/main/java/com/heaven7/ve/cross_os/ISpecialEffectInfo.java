package com.heaven7.ve.cross_os;

/**
 * @author heaven7
 */
public interface ISpecialEffectInfo extends IEffectInfo {

    //1缓慢缩小2缓慢放大3左平移4右平移5极慢(慢速)
    int TYPE_SLOW_ZOOM_IN = 1;
    int TYPE_SLOW_ZOOM_OUT = 2;
    int TYPE_LEFT_MOVE = 3;
    int TYPE_RIGHT_MOVE = 4;
    int TYPE_VERY_SLOW = 5;

    void setCategory(int category);
    int getCategory();
    void setMultiple(float multiple);
    float getMultiple();

}
