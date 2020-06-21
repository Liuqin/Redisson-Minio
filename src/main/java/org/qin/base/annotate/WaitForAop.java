package org.qin.base.annotate;


import com.alibaba.fastjson.JSON;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.qin.base.annotate.checkUtil.*;
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
public class WaitForAop {
    private final String KEY_TEMPLATE = "waitfor_%s";
    @Autowired
    private RedisLock redisLock;
    @Autowired
    private Redisson redissonLock;
    @Autowired
    private KeyUtil keyUtil;


    /**
     * @return
     * @descripttion 环绕通知逻辑
     * 基于redis lock 和redisison lock 的幂等锁
     * 对Waitfor 注解实现幂等
     * @parms
     * @author liuqin
     * @date 2020/6/3
     */
    @SneakyThrows
    @Around("@annotation(idempotentWaitFor)")
    public Object around(ProceedingJoinPoint joinPoint, WaitFor idempotentWaitFor) {

        long l = System.currentTimeMillis();
        int seconds = idempotentWaitFor.seconds();
        Class<? extends ICheckKeyService> cls = idempotentWaitFor.keysCheck();
        LockType lockType = idempotentWaitFor.lockType();
        int waitTime = seconds + 3;
        String keyLock = "";
        String key = "";
        //  Class<?> returnType = method.getReturnType();
        Object objectResult = null;

        try {
            Object[] args = joinPoint.getArgs();
            String className = cls.getCanonicalName();
            ICheckKeyService instance = ChecksContainer.get(className);
            instance = doCheckKeyFromIoc(joinPoint, cls, className, instance);
            // 触发后端幂等检查，符合条件幂等
            CheckResult checkResult = instance.check(args);
            if (!checkResult.getValid()) {
                // Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
                // 不符合幂等要求，直接切面返回
                log.warn("重要提示，没有命中幂等函数：");
                return joinPoint.proceed();
            }
            String checkReturnVar = "";
            if (checkResult.getTokenKey() != null && !"".equals(checkResult.getTokenKey())) {
                checkReturnVar += checkResult.getTokenKey();
                checkReturnVar += "_";
            }


            Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
            // 试图在Map中去掉部分key,排除其幂等影响,生成加密key
            String[] ex = idempotentWaitFor.excludeKeys();
            String genKey = keyUtil.generate(method, ex, args);
            key = String.format(KEY_TEMPLATE, checkReturnVar + genKey);
            keyLock = key + "@lock";
            log.info("key:" + key);
            if (redisLock.existKey(key)) {
                objectResult = readFromRedis(joinPoint, waitTime, key, keyLock);
                return objectResult;
            }
            log.info("尝试加锁:" + keyLock);
            // 试图加锁机制
            // 锁机制上锁成功
            if (lockType.equals(LockType.RedisLock)) {
                if (redisLock.getLock(keyLock, seconds * 1000)) {
                    if (!redisLock.existKey(key)) {
                        objectResult = lockAndExecute(joinPoint, lockType, seconds, key, keyLock);
                    } else {
                        objectResult = readFromRedis(joinPoint, waitTime, key, keyLock);
                    }

                } else {
                    objectResult = readFromRedis(joinPoint, waitTime, key, keyLock);
                }
            } else {
                //. 尝试加锁，最多等待3秒，上锁以后idempotentWaitFor.seconds()秒自动解锁
                if (redissonLock.getLock(keyLock).tryLock(30, seconds * 1000, TimeUnit.MILLISECONDS)) {
                    if (redisLock.existKey(key)) {
                        objectResult = readFromRedis(joinPoint, waitTime, key, keyLock);
                    } else {
                        objectResult = lockAndExecute(joinPoint, lockType, seconds, key, keyLock);
                    }
                } else {
                    objectResult = readFromRedis(joinPoint, waitTime, key, keyLock);
                }
            }


            long l1 = System.currentTimeMillis();
            log.info("time:" + String.valueOf(l1 - l));
            return objectResult;
        } catch (Throwable throwable) {
            // RemoveCache(key);
            deleteLock(lockType, keyLock);
            log.error(throwable.getMessage());
            throw throwable;
        }
    }

    /**
     * @return
     * @descripttion 负责检查是否符合幂等条件
     * 同时帮助反射对象加入自定义容器
     * @parms
     * @author liuqin
     * @date 2020/6/20
     */
    private ICheckKeyService doCheckKeyFromIoc(ProceedingJoinPoint joinPoint, Class<? extends ICheckKeyService> cls, String className, ICheckKeyService instance) throws Exception {
        if (instance == null) {
            // 静态
            instance = cls.getDeclaredConstructor().newInstance();
            // 标准写法，防止子类实例出现空对象
            //  instance = SpringContextUtils.getBeanByClass(cls);
            if (instance != null) {
                ChecksContainer.add(className, instance);
            } else {
                Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
                log.warn("重要提示，没有注解成功：" + method.toString());
                throw new Exception("重要提示，没有注解成功");
            }
        }
        return instance;
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
    private Object lockAndExecute(ProceedingJoinPoint joinPoint, LockType lockType, int seconds, String key, String keyLock) throws Throwable {
//        RemoveCache(key);
        Object proceed = joinPoint.proceed();
//        IdempotentResult.put(key, proceed);
        redisLock.setKeyAndCacheTime(key, keyUtil.castString(proceed), seconds);
//        log.info("系统正常返回结果:" + KeyUtil.toString(proceed));
//        log.info("当前正在解锁：" + keyLock);
//         redisLock.releaseLock(keyLock);
        deleteLock(lockType, keyLock);
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
    private Object readFromRedis(ProceedingJoinPoint proceedingJoinPoint, int waitTime, String key, String keyLock) throws Exception {
//        log.info("尝试缓存" + keyLock);

        int tryCount = 0;
        boolean take = true;

        int maxcount = 100 * waitTime;
        String redisResult = "";

        while (take) {
            redisResult = redisLock.getValue(key);
            tryCount++;
            take = ("".equals(redisResult) || redisResult == null) && maxcount > tryCount;
            Thread.sleep(10);
        }
        if (!"".equals(redisResult) && redisResult != null) {
            //    log.info("系统尝试次数:" + tryCount);
            log.info("系统获取到了缓存结果:" + redisResult + ",key=>" + key);
            return JSON.parse(redisResult);
        } else {
            log.info("redisResult:" + redisResult + ",key=>" + key);
            log.error("获取幂等失败");
            throw new Exception("获取幂等失败");
        }
    }
}

