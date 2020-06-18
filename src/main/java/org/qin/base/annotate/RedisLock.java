package org.qin.base.annotate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;


/**
 * @author LiuQin
 */
@Component
public class RedisLock {


    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 获得锁
     */
    public boolean getLock(String lockId, long millisecond) {
        Boolean success = redisTemplate.opsForValue().setIfAbsent(lockId, "lock",
                millisecond, TimeUnit.MILLISECONDS);
        return success != null && success;
    }

    public void releaseLock(String lockId) {
        redisTemplate.delete(lockId);
    }

    /* 检查key是否存在，返回boolean值 */

    @SuppressWarnings("unchecked")
    public Boolean existKey(String key) {
        return redisTemplate.hasKey(key);
    }

    @SuppressWarnings("unchecked")
    public void setKeyAndCacheTime(String key, String value, long timeout) {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        ops.set(key, value, timeout, TimeUnit.SECONDS);
    }

    /**
     * @return
     * @descripttion 从redis中取数据
     * @parms
     * @author liuqin
     * @date 2020/6/5
     */
    @SuppressWarnings("unchecked")
    public String getValue(String key) {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        return ops.get(key);
    }
}
