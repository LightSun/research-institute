package com.heaven7.java.image.utils;

import com.heaven7.java.image.detect.IDataTransformer;
import com.heaven7.java.image.detect.TransformInfo;
import com.heaven7.java.visitor.ResultVisitor;
import com.heaven7.java.visitor.collection.VisitServices;

import java.util.List;

/**
 * @author heaven7
 */
public class TransformUtils {

    public static <T extends IDataTransformer<T>> List<T> transformData(List<T> list, TransformInfo tInfo) {
        if(tInfo == null){
            return list;
        }
        return VisitServices.from(list).map(new ResultVisitor<T, T>() {
            @Override
            public T visit(T data, Object param) {
                return data.transform(tInfo);
            }
        }).getAsList();
    }
}
