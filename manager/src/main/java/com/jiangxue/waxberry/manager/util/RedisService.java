package com.jiangxue.waxberry.manager.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisService {

    private static final String REDIS_KEY = "waxberry:";
    private static final String REDIS_KEY_APPROVAL = "auth:approval:generator:";

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    // 存储键值对（默认过期时间：1小时）
    public void set(String key, String value) {
        set(key, value, 1, TimeUnit.HOURS);
    }

    // 存储键值对并设置过期时间
    public void set(String key, String value, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value, timeout, unit);
    }

    // 获取值
    public String get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    // 删除键
    public boolean delete(String key) {
        return Boolean.TRUE.equals(redisTemplate.delete(key));
    }

    // 判断键是否存在
    public boolean exists(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    // 获取键的剩余过期时间（指定时间单位）
    public Long getExpire(String key, TimeUnit timeUnit) {
        return redisTemplate.getExpire(key, timeUnit);
    }

    public void incrementClicks(String key,String value ,int increment,TimeUnit timeUnit) {
        redisTemplate.opsForZSet().incrementScore(key, value, increment);
        // 设置过期时间
        redisTemplate.expire(key, 1, TimeUnit.HOURS);
    }

    public Double getClickScore(String key, String value) {
        return redisTemplate.opsForZSet().score(key, value);
    }

    /**
     * 生成业务编号
     * @param key 业务键前缀
     * @return 生成的编号
     */
    public String generateCode(String serviceName,String key) {
        String redisKey = REDIS_KEY + serviceName + ":" + REDIS_KEY_APPROVAL + key;
        RedisAtomicLong counter = new RedisAtomicLong(redisKey, redisTemplate.getConnectionFactory());
        long count = counter.incrementAndGet();

        // 设置过期时间（可选）
        counter.expire(1, TimeUnit.DAYS);

        return key + "-" + String.format("%07d", count);
    }
}
