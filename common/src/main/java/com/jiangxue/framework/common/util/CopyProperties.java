package com.jiangxue.framework.common.util;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.PropertyDescriptor;
import java.util.Arrays;

public class CopyProperties {
    /**
     * 复制对象属性，忽略源对象中的null值
     * @param source 源对象
     * @param target 目标对象
     */
    public static void copyNonNullProperties(Object source, Object target) {
        if (source == null || target == null) {
            return;
        }

        BeanUtils.copyProperties(source, target, getNullPropertyNames(source));
    }

    /**
     * 获取对象中所有值为null的属性名
     * @param source 源对象
     * @return 包含所有null值属性名的数组
     */
    private static String[] getNullPropertyNames(Object source) {
        final BeanWrapper wrappedSource = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = wrappedSource.getPropertyDescriptors();

        return Arrays.stream(pds)
                .map(PropertyDescriptor::getName)
                .filter(name -> {
                    // 跳过class属性
                    if ("class".equals(name)) return false;
                    // 检查属性值是否为null
                    return wrappedSource.getPropertyValue(name) == null;
                })
                .toArray(String[]::new);
    }
}
