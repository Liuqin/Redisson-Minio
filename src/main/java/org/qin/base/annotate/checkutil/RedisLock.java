package org.qin.base.annotate.checkutil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * @author LiuQin
 */
@Component
public class RedisLock {

    @Autowired
    private StringRedisTemplate redisTemplate;
    private static final String newSetIfAbsentScriptStr = " if 1 == redis.call('setnx',KEYS[1],ARGV[1]) then" +
            " redis.call('expire',KEYS[1],ARGV[2])" +
            " return 1;" +
            " else" +
            " return 0;" +
            " end;";

    private static final RedisScript<Boolean> newSetIfAbsentScript = new DefaultRedisScript<Boolean>(newSetIfAbsentScriptStr, Boolean.class);

    public boolean setIfAbsent(String key, String value, Long seconds) {
        List<String> keys = new ArrayList<>();
        keys.add(key);
        Object[] args = {value, seconds.toString()};
        return redisTemplate.execute(newSetIfAbsentScript, keys, args);
    }


    /**
     * 获取锁
     *
     * @param lockKey
     * @param value：单位-秒
     * @param seconds：单位-秒
     * @return
     */
    public boolean getLock(String lockKey, String value, long seconds) {
        try {
            return this.setIfAbsent("lockkey_" + lockKey, value, seconds);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    public boolean getLock(String lockKey, long seconds) {
        try {
            return this.setIfAbsent("lockkey_" + lockKey, lockKey, seconds);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 释放锁
     *
     * @param lockKey
     * @return
     */
    public boolean releaseLock(String lockKey) {
        return this.redisTemplate.delete("lockkey_" + lockKey);
    }

    public boolean existKey(String key) {
        return this.redisTemplate.hasKey(key);
    }

    public void setKeyAndCacheTime(String key, String castString, int seconds) {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        ops.set(key, castString, seconds, TimeUnit.SECONDS);
    }


    public String getValue(String key) {
        return redisTemplate.opsForValue().get(key);
    }
}
