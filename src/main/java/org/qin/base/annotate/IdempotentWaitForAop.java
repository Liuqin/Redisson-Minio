package org.qin.base.annotate;


import com.alibaba.fastjson.JSON;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.qin.base.annotate.checks.impl.CheckKeys;
import org.qin.base.annotate.checks.impl.ChecksContainer;
import org.redisson.Redisson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * @author liuqin
 * @descripttion 幂等Aop
 * @parms
 * @return
 * @date 2020/6/3
 */
@SuppressWarnings("ALL")
@Aspect
@Component
@Slf4j
public class IdempotentWaitForAop {
    private final String KEY_TEMPLATE = "idempotent_%s";
    @Autowired
    private RedisLock redisLock;
    @Autowired
    private Redisson redissonLock;
    @Autowired
    private KeyUtil keyUtil;

    /**
     * @return
     * @descripttion 环绕通知逻辑
     * @parms
     * @author liuqin
     * @date 2020/6/3
     */
    @SneakyThrows
    @Around("@annotation(idempotentWaitFor)")
    public Object around(ProceedingJoinPoint joinPoint, IdempotentWaitFor idempotentWaitFor) {

        long l = System.currentTimeMillis();

        String keyLock = "";
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        //  Class<?> returnType = method.getReturnType();
        Object objectResult = null;
        String checkReturnVar = "";
        try {
            Object[] args = joinPoint.getArgs();
            String className = idempotentWaitFor.keysCheck().getCanonicalName();
            CheckKeys instance = ChecksContainer.get(className);
            if (instance == null) {
                instance = idempotentWaitFor.keysCheck().newInstance();
                ChecksContainer.add(className, instance);
            }
            // 触发后端幂等检查，符合条件幂等
            String[] checkArr = {checkReturnVar};
            boolean check = instance.check(checkArr, args);
            if (!"".equals(checkArr[0]) && checkArr != null && checkArr[0] != null) {
                checkReturnVar = checkArr[0] + "_";
            }

            if (!check) {
                // 不符合幂等要求，直接切面返回
                log.warn("重要提示，没有命中幂等函数：" + method.toString());
                return joinPoint.proceed();
            }
            // 试图在Map中去掉部分key,排除其幂等影响,生成加密key
            String genKey = keyUtil.generate(method, idempotentWaitFor.excludeKeys(), args);
            String key = String.format(KEY_TEMPLATE, checkReturnVar + genKey);
            keyLock = key + "@lock";
            log.info("key:" + key);
            Boolean hasKey = redisLock.existKey(key);
            if (hasKey) {
                objectResult = readFromRedis(joinPoint, idempotentWaitFor, key, keyLock);
                return objectResult;
            }
            log.info("尝试加锁:" + keyLock);
            // 试图加锁机制
            boolean lock = false;
            if (idempotentWaitFor.lockType().equals(LockType.RedisLock)) {
                lock = redisLock.getLock(keyLock, idempotentWaitFor.seconds());
            } else {
                //. 尝试加锁，最多等待3秒，上锁以后idempotentWaitFor.seconds()秒自动解锁
                lock = redissonLock.getLock(keyLock).tryLock(10, idempotentWaitFor.seconds()*1000, TimeUnit.MILLISECONDS);
            }
            // 锁机制上锁成功
            if (lock) {
                if (hasKey) {
                    objectResult = readFromRedis(joinPoint, idempotentWaitFor, key, keyLock);
                } else {
                    objectResult = lockAndExecute(joinPoint, idempotentWaitFor, key, keyLock);
                }
            } else {
                objectResult = readFromRedis(joinPoint, idempotentWaitFor, key, keyLock);
            }

            long l1 = System.currentTimeMillis();
            log.info("time:"+String.valueOf(l1-l));
            return objectResult;
        } catch (Throwable throwable) {
            deleteLock(idempotentWaitFor.lockType(), keyLock);
            log.error(throwable.getMessage());
            throw throwable;
        }
    }

    /**
     * @return 删除锁逻辑
     * @descripttion
     * @parms lockType keyLock
     * @author liuqin
     * @date 2020/6/17
     */
    private void deleteLock(LockType lockType, String keyLock) {

        if (lockType.equals(LockType.RedissonLock)) {
            redissonLock.getLock(keyLock).unlock();
        } else {
            if (!"".equals(keyLock)) {
                Boolean hasLock = redisLock.existKey(keyLock);
                if (hasLock) {
                    redisLock.releaseLock(keyLock);
                }
            }
        }
    }


    /**
     * @return
     * @descripttion 获取锁，进入业务逻辑执行区域
     * @parms
     * @author liuqin
     * @date 2020/6/3
     */
    private Object lockAndExecute(ProceedingJoinPoint joinPoint, IdempotentWaitFor idempotentWaitFor, String key, String keyLock) throws Throwable {
        Object proceed = joinPoint.proceed();
        redisLock.setKeyAndCacheTime(key, keyUtil.castString(proceed), idempotentWaitFor.seconds());
//        log.info("系统正常返回结果:" + KeyUtil.toString(proceed));
//        log.info("当前正在解锁：" + keyLock);
        // redisLock.releaseLock(keyLock);
        deleteLock(idempotentWaitFor.lockType(), keyLock);
        return proceed;
    }


    /**
     * @return
     * @descripttion 业务不需要进入逻辑区域，直接等待缓存结果
     * @parms
     * @author liuqin
     * @date 2020/6/3
     */
    @SuppressWarnings("AlibabaLowerCamelCaseVariableNaming")
    private Object readFromRedis(ProceedingJoinPoint proceedingJoinPoint, IdempotentWaitFor idempotentWaitFor, String key, String keyLock) throws Exception {
//        log.info("尝试缓存" + keyLock);

        int tryCount = 0;
        boolean take = true;
        int waitTime = idempotentWaitFor.seconds() + 3;
        int maxcount = 20*waitTime;
        String redisResult = "";
        while (take) {
            redisResult = redisLock.getValue(key);
            tryCount++;
            take = ("".equals(redisResult) || redisResult == null) && maxcount > tryCount;
            Thread.sleep(50);
        }
        if (!"".equals(redisResult) && redisResult != null) {
            log.info("系统尝试次数:" + tryCount);
            log.info("系统获取到了缓存结果:" + redisResult + ",key=>" + key);
            return JSON.parse(redisResult);
        } else {
            log.info("redisResult:" + redisResult + ",key=>" + key);
            log.error("获取幂等失败");
            throw new Exception("获取幂等失败");
        }
    }
}

