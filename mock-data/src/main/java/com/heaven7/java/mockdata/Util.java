package com.heaven7.java.network;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * @author heaven7
 */
public class Util {

    private static List<FieldDesc> getFieldDescs(Class<?> rawClass) {
        Class<?> curClass = rawClass;
        List<FieldDesc> fieldDescs = new ArrayList<>();
        //Number.class.isAssignableFrom(Integer.class)
        while (RequestDescData.class.isAssignableFrom(curClass)) {
            getFieldDescs(rawClass, curClass, fieldDescs);
            curClass = curClass.getSuperclass();
            if (curClass == Object.class || curClass == null) {
                break;
            }
        }
        return fieldDescs;
    }

    private static void getFieldDescs(Class<?> rawClass, Class<?> clazz, List<FieldDesc> out) {
        final boolean fromSuper = rawClass != clazz;
        final Field[] fields = clazz.getDeclaredFields();
        if (Predicates.isEmpty(fields)) {
            return;
        }
        RequiredValidator validator = getValidator(clazz);
        if (validator == null) {
            validator = RequiredValidator.DEFAULT;
        }
        for (Field f : fields) {
            f.setAccessible(true);
            //exclude static
            if (Modifier.isStatic(f.getModifiers())) {
                continue;
            }
            //exclude final
            if (Modifier.isFinal(f.getModifiers())) {
                continue;
            }
            if (fromSuper) {
                final Expose expose = f.getAnnotation(Expose.class);
                if (expose == null || !expose.deserialize()) {
                    continue;
                }
            }
            //require
            Required required = f.getAnnotation(Required.class);
            //not include in param or header when(not required)
            if (required == null && f.getAnnotation(javax.persistence.Transient.class) != null) {
                continue;
            }

            final SerializedName sn = f.getAnnotation(SerializedName.class);
            String name = sn == null ? f.getName() : sn.value();
            FieldDesc desc = new FieldDesc(name, f);
            out.add(desc);

            if (required != null) {
                desc.requireTypes = required.types();
                desc.noticeTemplate = required.msgTemplate();
                desc.validator = validator;
                if (TextUtils.isEmpty(required.msgTemplate())) {
                    throw new IllegalStateException("you must assign the message template for @Required.");
                }
            }
        }
    }
}
