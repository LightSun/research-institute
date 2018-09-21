package com.heaven7.java.study.antlr;

import com.heaven7.java.base.anno.Nullable;
import com.heaven7.java.base.util.Predicates;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectUtils {

    public static Object callVirtualMethod(Object receiver, String methodName, Object... args){
        Method[] methods = receiver.getClass().getMethods();
        if(Predicates.isEmpty(methods)){
            throw new EvaluateException("can't evaluate expression caused by can't find method (" +
                    methodName + ") for object("+ receiver + ")");
        }
        for(Method m : methods){
            try {
                return m.invoke(receiver, args);
            } catch (IllegalAccessException | InvocationTargetException e) {
                //ignore
            }
        }
        throw new EvaluateException("can't evaluate expression caused by invoke virtual method (" +
                methodName + ") error for object("+ receiver + ")");
    }
    public static Object callStaticMethod(Class<?> clazz, String methodName, Object... args){
        Method[] methods = clazz.getMethods();
        if(Predicates.isEmpty(methods)){
            throw new EvaluateException("can't evaluate expression caused by can't find static method (" +
                    methodName + ") for class("+ clazz.getName() + ")");
        }
        for(Method m : methods){
            try {
                return m.invoke(null, args);
            } catch (IllegalAccessException | InvocationTargetException e) {
                //ignore
            }
        }
        throw new EvaluateException("can't evaluate expression caused by invoke static method (" +
                methodName + ") error for class("+ clazz.getName() + ")");
    }

    public static Object getVirtualFieldValue(@Nullable Object receiver, String fieldName) {
        Field f = getField(receiver.getClass(), fieldName);
        if(f == null){
            throw new EvaluateException("can't evaluate expression caused by can't find field (" +
                    fieldName + ") for object("+ receiver + ")");
        }
        try {
            f.setAccessible(true);
            return f.get(receiver);
        } catch (Exception e) {
            throw new EvaluateException(e);
        }
    }
    public static Object getStaticFieldValue(Class<?> clazz, String fieldName) {
        Field f = getField(clazz, fieldName);
        if(f == null){
            throw new EvaluateException("can't evaluate expression caused by can't find field (" +
                    fieldName + ") for class("+ clazz.getName() + ")");
        }
        try {
            f.setAccessible(true);
            return f.get(null);
        } catch (Exception e) {
            throw new EvaluateException(e);
        }
    }
    public static void setVirtualFieldValue(String fieldName, Object val,@Nullable Object receiver) {
        Field f = getField(receiver.getClass(), fieldName);
        if(f == null){
            throw new EvaluateException("can't evaluate expression caused by can't find field (" +
                    fieldName + ") for object("+ receiver + ")");
        }
        try {
            f.setAccessible(true);
            f.set(receiver, val);
        } catch (Exception e) {
            throw new EvaluateException(e);
        }
    }
    public static void setStaticFieldValue(Class<?> clazz, String fieldName, Object val) {
        Field f = getField(clazz, fieldName);
        if(f == null){
            throw new EvaluateException("can't evaluate expression caused by can't find field (" +
                    fieldName + ") for class("+ clazz.getName() + ")");
        }
        try {
            f.setAccessible(true);
            f.set(null, val);
        } catch (Exception e) {
            throw new EvaluateException(e);
        }
    }

    private static Field getField(Class<?> clazz, String fieldName) {
        Class<?> target = clazz;
        do{
            try {
                return target.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                target = target.getSuperclass();
                if(target == null){
                    return null;
                }
            }
        }while (true);
    }
}
