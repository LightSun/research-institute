package com.heaven7.utils;

/**
 * @author heaven7
 */
public class ReflectUtils {

    @SuppressWarnings("unchecked")
    public static <T> T newInstance(String className){
        try {
            return (T) Class.forName(className).newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
