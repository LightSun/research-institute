package com.heaven7.utils;

import com.heaven7.ve.Copyable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by heaven7 on 2018/3/16 0016.
 */

public class Arrays2 {

    @SuppressWarnings("unchecked")
    public static <T extends Copyable> ArrayList<T> copy(List<T> src) {
        ArrayList<T> result = new ArrayList<>();
        for (T t : src) {
            result.add((T) t.copy());
        }
        return result;
    }

    public static boolean contains(int[] src, int val) {
        for (int a : src) {
            if (a == val) {
                return true;
            }
        }
        return false;
    }
}
