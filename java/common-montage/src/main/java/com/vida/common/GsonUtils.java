package com.vida.common;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.internal.$Gson$Types;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class GsonUtils {

    public static Type getSuperclassTypeParameter(Class<?> subclass) {
        Type superclass = subclass.getGenericSuperclass();
        if (superclass instanceof Class) {
            throw new RuntimeException("Missing type parameter.");
        }
        ParameterizedType parameterized = (ParameterizedType) superclass;
        return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
    }

    public static Map<String, String> toMap(IRawData data) {
        final Map<String, String> map = new HashMap<String, String>();

        Class<?> clazz = data.getClass();
        populateJsonFromFields(data, clazz, map);
        while ((clazz = clazz.getSuperclass()) != Object.class && clazz != null) {
            populateJsonFromFields(data, clazz, map);
        }
        return map;
    }

    private static void populateJsonFromFields(Object data, Class<?> clazz, Map<String, String> map) {
        final boolean fromSuper = data.getClass() != clazz;
        final Field[] fields = clazz.getDeclaredFields();
        if (fields != null && fields.length > 0) {
            for (Field f : fields) {
                f.setAccessible(true);
                //exclude static
                if(Modifier.isStatic(f.getModifiers())){
                    continue;
                }
                //exclude final
                if(Modifier.isFinal(f.getModifiers())){
                    continue;
                }
                if (fromSuper) {
                    final Expose expose = f.getAnnotation(Expose.class);
                    if (expose == null || !expose.serialize()) {
                        continue;
                    }
                }
                final SerializedName sn = f.getAnnotation(SerializedName.class);
                String key = sn != null ? sn.value() : f.getName();
                try {
                    Object val = f.get(data);
                    //ignore null.
                    if (val != null) {
                        //support single, array, list
                        map.put(key, new GsonBuilder().create().toJson(val));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * the tag interface, and the json fields must have annotation {@link SerializedName}
     * and field from super must have annotation {@link Expose}.
     * Created by heaven7 on 2016/10/9.
     */

    public interface IRawData {
    }
}
