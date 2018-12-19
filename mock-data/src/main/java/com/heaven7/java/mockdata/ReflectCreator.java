package com.heaven7.java.network;

/**
 * @author heaven7
 */
public class ReflectCreator<T> {

    private final Class<T> clazz;

    private ReflectCreator(Class<T> clazz) {
        this.clazz = clazz;
    }

    public static <T> ReflectCreator<T> ofClass(Class<T> clazz){
        return new ReflectCreator<>(clazz);
    }

    public T lightCreate(){
        try {
            T t = clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private static class FieldDesc{
    }
}
