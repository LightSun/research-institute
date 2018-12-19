package com.heaven7.java.network;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public class FieldDesc {
    public static final byte TYPE_INT = 1;
    public static final byte TYPE_SHORT = 2;
    public static final byte TYPE_LONG = 3;
    public static final byte TYPE_BYTE = 4;
    public static final byte TYPE_BOOLEAN = 5;
    public static final byte TYPE_FLOAT = 6;
    public static final byte TYPE_DOUBLE = 7;
    public static final byte TYPE_STRING = 8;

    public static final byte TYPE_LIST = 9;
    public static final byte TYPE_ARRAY = 10;
    public static final byte TYPE_SET = 11;
    public static final byte TYPE_OTHER = 12;

    public final byte type;
    public final String name;

    public final Field field;
    private int[] requireTypes;

    public FieldDesc(String name, Field field) {
        this.name = name;
        this.field = field;
        this.type = parseType(field.getType());
    }

    boolean hasRequiredType(int type) {
        if (requireTypes == null) {
            return false;
        }
        for (int val : requireTypes) {
            if (val == type) {
                return true;
            }
        }
        return false;
    }

    public static byte parseType(Class<?> type) {
        if (type.isArray()) {
            return TYPE_ARRAY;
        } else if (List.class.isAssignableFrom(type)) {
            return TYPE_LIST;
        } else if (Set.class.isAssignableFrom(type)) {
            return TYPE_SET;

        } else if (type == Integer.class || type == int.class) {
            return TYPE_INT;
        } else if (type == Short.class || type == short.class) {
            return TYPE_SHORT;
        } else if (type == Long.class || type == long.class) {
            return TYPE_LONG;
        } else if (type == Byte.class || type == byte.class) {
            return TYPE_BYTE;
        } else if (type == Boolean.class || type == boolean.class) {
            return TYPE_BOOLEAN;
        } else if (type == Float.class || type == float.class) {
            return TYPE_FLOAT;
        } else if (type == Double.class || type == double.class) {
            return TYPE_DOUBLE;
        } else if (type == String.class) {
            return TYPE_STRING;
        }
        return TYPE_OTHER;
    }

    public boolean isSimpleType() {
        switch (type) {
            case TYPE_INT:
            case TYPE_LONG:
            case TYPE_SHORT:
            case TYPE_BYTE:
            case TYPE_FLOAT:
            case TYPE_DOUBLE:
            case TYPE_BOOLEAN:
            case TYPE_STRING:
                return true;
        }
        return false;
    }
}
