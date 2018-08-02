package com.heaven7.utils;

import com.heaven7.java.base.util.SparseArray;
import com.heaven7.java.visitor.util.Map;

import java.util.*;

/**
 * Created by heaven7 on 2018/4/12 0012.
 */

public class CollectionUtils {

    public static <V> void travel(SparseArray<V> map, Map.MapTravelCallback<Integer, V> traveller){
        final int size = map.size();
        for (int i = 0; i < size;  i ++) {
            traveller.onTravel(map.keyAt(i), map.valueAt(i));
        }
    }

    public static float sum(List<Float> vals) {
        float sum = 0;
        for (Float val : vals) {
            sum += val;
        }
        return sum;
    }

    public static <T> Set<T> intersection(Set<T> set1, Collection<T> set2) {
        if(set1 == null || set2 == null || set1.isEmpty() || set2.isEmpty()){
            return Collections.emptySet();
        }
        Set<T> set = new HashSet<>();
        for(T val : set1){
            if(set2.contains(val)){
                set.add(val);
            }
        }
        return set;
    }


}
