package com.heaven7.ve.utils;

import com.heaven7.java.visitor.collection.VisitServices;

/**
 * @author heaven7
 */
public class MarkUtils {

    public static final String marks(String... args){
        return VisitServices.from(args).joinToString(",");
    }
}
