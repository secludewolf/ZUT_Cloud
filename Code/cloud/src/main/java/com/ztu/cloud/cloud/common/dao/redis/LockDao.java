package com.ztu.cloud.cloud.common.dao.redis;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author Jager
 * @description 互斥锁
 * @date 2020/7/17-17:11
 **/
@Component
public class LockDao {
    RedisTemplate<String, String> redisTemplate;

    private String prefix = "lock_";

    public LockDao(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public Boolean insert(String id) {
        return insert(id, 10, TimeUnit.SECONDS);
    }

    public Boolean insert(String id, long timeout, TimeUnit unit) {
        return this.redisTemplate.opsForValue().setIfAbsent(this.prefix + id, id, timeout, unit);
    }

    public Boolean delete(String id) {
        return this.redisTemplate.delete(this.prefix + id);
    }
}
