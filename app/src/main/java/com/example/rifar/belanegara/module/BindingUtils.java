package com.example.rifar.belanegara.module;

/**
 * Created by asus on 8/19/2017.
 */

public class BindingUtils {
    private BindingUtils() { }

    public static <T> void safe(T obj, Action<T> action) {
        if(obj != null)
            action.invoke(obj);
    }

    public static <T> T coelesce(T obj, T orElse) {
        return obj == null? orElse : obj;
    }

    public static Class getPrimitiveType(Class ref) {
        if(ref == Integer.class)
            return int.class;
        if(ref == Boolean.class)
            return boolean.class;
        if(ref == Long.class)
            return long.class;
        if(ref == Double.class)
            return double.class;
        if(ref == Float.class)
            return float.class;
        if(ref == Short.class)
            return short.class;
        if(ref == Character.class)
            return char.class;
        if(ref == Byte.class)
            return byte.class;
        return null;
    }

    public static boolean isPrimitive(Class ref, Class primitive) {
        if(ref == Integer.class && primitive == int.class)
            return true;
        if(ref == Boolean.class && primitive == boolean.class)
            return true;
        if(ref == Long.class && primitive == long.class)
            return true;
        if(ref == Double.class && primitive == double.class)
            return true;
        if(ref == Float.class && primitive == float.class)
            return true;
        if(ref == Short.class && primitive == short.class)
            return true;
        if(ref == Character.class && primitive == char.class)
            return true;
        if(ref == Byte.class && primitive == byte.class)
            return true;

        return false;
    }

    public static boolean hasPrimitive(Class primitive) {
        if (primitive == Integer.class)
            return true;
        if (primitive == Boolean.class)
            return true;
        if (primitive == Long.class)
            return true;
        if (primitive == Double.class)
            return true;
        if (primitive == Float.class)
            return true;
        if (primitive == Short.class)
            return true;
        if (primitive == Character.class)
            return true;
        if (primitive == Byte.class)
            return true;

        return false;
    }
}
