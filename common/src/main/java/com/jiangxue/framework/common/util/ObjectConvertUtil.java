package com.jiangxue.framework.common.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
public class ObjectConvertUtil {


    public static <T, U> List<U> convertToList(List<T> source, Class<U> targetRecordClass) {
        if (CollectionUtils.isEmpty(source)) {
            return Collections.emptyList();
        }
        List<U> result = new ArrayList<>();
        for (T t : source) {
            try {
                U u = targetRecordClass.getDeclaredConstructor().newInstance();
                BeanUtils.copyProperties(t, u);
                result.add(u);
            } catch (NoSuchMethodException e1) {
                log.error("convertTo error1", e1);
            } catch (Exception e2) {
                log.error("convertTo error2", e2);
            }
        }
        return result;
    }

    public static <T, U> U convertTo(T source, Class<U> targetClass) {
        try {
            U target = targetClass.getDeclaredConstructor().newInstance();
            BeanUtils.copyProperties(source, target);
            return target;
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
