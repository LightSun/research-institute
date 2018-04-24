package com.heaven7.vida.research.utils;

/**
 * 线性方程式
 * Created by heaven7 on 2018/4/23 0023.
 */

public class LinearEquation {

    /** y = ax +b */
    private static final byte MODE_Y   = 1;
    /** x = ay +b */
    private static final byte MODE_X   = 2;

    private final float a;
    private final float b;
    private final byte mMode;

    public LinearEquation(float a, float b) {
        this.a = a;
        this.b = b;
        this.mMode = MODE_Y;
    }

    public LinearEquation(float x1, float y1, float x2, float y2) {
        if(x1 == x2){
            a = 0;
            b = x1;
            mMode = MODE_X;
        }else{
            a = (y1 - y2 ) / (x1 - x2);
            b = y1 - a * x1;
            mMode = MODE_Y;
        }
    }

    public float getY(float x){
        if(mMode == MODE_Y) {
            return a * x + b;
        }else{
            if(a == 0){
                throw new IllegalStateException("can't get y");
            }
            return (x - b) / a;
        }
    }
    public float getX(float y){
        if(mMode == MODE_X) {
            return a * y + b;
        }else{
            if(a == 0){
                throw new IllegalStateException("can't get x");
            }
            return (y - b) / a;
        }
    }
}
