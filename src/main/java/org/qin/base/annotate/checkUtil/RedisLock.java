package org.qin.base.annotate.checkUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * @author LiuQin
 */
@Component
public class RedisLock {


    private static final String SUCCESS = "SUCCESS";
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


    String newSetIfAbsentScriptStr = " if 1 == redis.call('setnx',KEYS[1],ARGV[1]) then" +
            " redis.call('expire',KEYS[1],ARGV[2])" +
            " return 1;" +
            " else" +
            " return 0;" +
            " end;";

    public RedisScript<Boolean> newSetIfAbsentScript = new DefaultRedisScript<Boolean>
            (newSetIfAbsentScriptStr, Boolean.class);

    /**
     * 获取锁
     * @param lockKey
     * @param value
     * @param expireTime：单位-秒
     * @return
     */
    public boolean getLock(String lockKey, String value, int expireTime){
        try{
            String script = "if redis.call('setNx',KEYS[1],ARGV[1]) then if redis.call('get',KEYS[1])==ARGV[1] then return redis.call('expire',KEYS[1],ARGV[2]) else return 0 end end";

            RedisScript<Long> redisScript = new DefaultRedisScript<>(script, Long.class);

            Object result = redisTemplate.execute(redisScript, (List<String>) new StringRedisSerializer(),new StringRedisSerializer(), Collections.singletonList(lockKey),value,expireTime + "");
            System.out.println(result + "-----------");
            //Object result = redisTemplate.execute(redisScript, Collections.singletonList(lockKey),value,expireTime + "");

            if(SUCCESS.equals(result)){
                return true;
            }

        }catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 释放锁
     * @param lockKey
     * @param value
     * @return
     */
    public boolean releaseLock(String lockKey, String value){

        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";

        RedisScript<Long> redisScript = new DefaultRedisScript<>(script, Long.class);

        Object result = redisTemplate.execute(redisScript, (List<String>) new StringRedisSerializer(),new StringRedisSerializer(), Collections.singletonList(lockKey),value);
        if(SUCCESS.equals(result)) {
            return true;
        }

        return false;
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
