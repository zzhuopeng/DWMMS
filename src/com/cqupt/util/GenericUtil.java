package com.cqupt.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 参考资料：dispatcher
 * 学习资料：java反射获得泛型参数GETGENERICSUPERCLASS（）：https://blog.csdn.net/liang5630/article/details/40185591
 */
public class GenericUtil {
    /**
     * 通过反射获取当前类的类对象
     *
     * @param clazz
     * @return
     */
    public static Class<?> getGenericClass(Class<?> clazz) {
        //getGenericSuperclass()获得带有泛型的父类
        //Type是 Java 编程语言中所有类型的公共高级接口。它们包括原始类型、参数化类型、数组类型、类型变量和基本类型。
        Type genType = clazz.getGenericSuperclass();
        //ParameterizedType参数化类型，即泛型
        if (genType instanceof ParameterizedType) {
            Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
            if ((params != null) && (params.length == 1)) {
                return (Class<?>) params[0];
            }
        }
        return null;
    }
}
