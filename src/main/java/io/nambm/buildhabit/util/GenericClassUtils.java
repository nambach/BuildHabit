package io.nambm.buildhabit.util;

import java.lang.reflect.ParameterizedType;

public class GenericClassUtils {
    public static <T> Class<T> getGenericClass(Class clazz, int...indexes) {
        int index = indexes.length == 0 ? 0 : indexes[0];
        return (Class)((ParameterizedType) clazz.getGenericSuperclass())
                .getActualTypeArguments()[index];
    }
}
