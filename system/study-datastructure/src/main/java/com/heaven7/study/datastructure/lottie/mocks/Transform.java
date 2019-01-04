package com.heaven7.study.datastructure.lottie.mocks;

import com.heaven7.study.datastructure.lottie.bean.KeyFrame;

import java.util.List;

/**
 * @author heaven7
 */
public class Transform {

    // a 描点， 只关心k元素.
    /*
     a":{
         "a":0,
         "k":[
             0,
             0,
             0
         ],
         "ix":1
     },
     */
    // o: 不透明度 [0,100,11]
    // r: 旋转
    // p: 位置
    // s: 缩放

    List<KeyFrame> anchorPoint;
    //-- position ---
    List<KeyFrame> pathAnims;
    List<Float> xs;
    List<Float> ys;

    // -------- rotate --------
    List<Float> rotates;
}
