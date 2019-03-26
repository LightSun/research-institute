package com.heaven7.vida.research.utils;

import android.graphics.Rect;
import androidx.annotation.Nullable;

import com.heaven7.vida.research.face_bd.FaceInfo;

/**
 * 矩形坐标计算器
 * Created by heaven7 on 2018/4/28 0028.
 */

public class FaceRectCalculator {

    public static Rect calculate(FaceInfo.Location location, float rotateAngle, @Nullable Rect out){
        return calculate(location.left, location.top, location.width, location.height, rotateAngle, out);
    }

    public static Rect calculate(int left, int top, int width, int height ,float rotateAngle, @Nullable Rect out){
        //rotateAngle < 0 ： offset left or else right
        if(out == null){
            out = new Rect();
        }
        float angle = Math.abs(rotateAngle);
        if(angle < 1){
       // if(rotateAngle < 1){
            out.left = left;
            out.top = top;
            out.bottom = top + height;
            out.right = left + width;
            return out;
        }

        double radians = Math.toRadians(angle);
        if(rotateAngle < 0){
            //for bottom-left triangle
            double dx_bottom = Math.sin(radians) * height;
            double dy_bottom = Math.cos(radians) * height;
            //for top-left triangle
            double dy_top = Math.sin(radians) * width;
            double dx_top = Math.cos(radians) * width;

           /* out.left = (int) (left - dx_bottom);
            out.right = out.left + width;
            //out.bottom = (int) (top - dy_top - dy_bottom);
            //out.top = out.bottom - height;
            out.top = top;
            out.bottom = top + height;*/

            out.left = left;
            out.top = top;
            out.bottom = top + height;
            out.right = left + width;
        }else{
            //for right-top triangle
            double dx_top = Math.cos(radians) * width;
            double dy_top = Math.sin(radians) * width;
            //for right-bottom triangle
            double dx_bottom = Math.sin(radians) * height;
            double dy_bottom = Math.cos(radians) * height;

           /* out.bottom = (int) (top - dy_top - dy_bottom);
            out.top = out.bottom - height;
            out.right = (int) (left + dx_bottom + dx_top);
            out.left = out.right - width;*/

            out.left = left;
            out.top = top;
            out.bottom = top + height;
            out.right = left + width;
            return out;
        }
        return out;
    }
}
