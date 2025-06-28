package com.jiangxue.framework.common.util;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.util.UUID;

public class CustomUUIDGenerator implements IdentifierGenerator {

    @Override
    public Object generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        // 生成标准UUID并处理格式
        UUID uuid = UUID.randomUUID();
        return uuid.toString()
                .replace("-", "")  // 移除连字符
                .toUpperCase();    // 转为大写
    }
}
