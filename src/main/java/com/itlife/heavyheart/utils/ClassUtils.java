package com.itlife.heavyheart.utils;

import org.springframework.core.GenericTypeResolver;

import java.lang.reflect.Type;

/**
 * @Author kex
 * @Create 2020/8/14 10:28
 * @Description
 */
public class ClassUtils extends org.springframework.util.ClassUtils {
    public ClassUtils() {
    }

    public static Type getGenericType(Class<?> clz, Type type) {
        return (Type) GenericTypeResolver.getTypeVariableMap(clz).get(type);
    }
}
