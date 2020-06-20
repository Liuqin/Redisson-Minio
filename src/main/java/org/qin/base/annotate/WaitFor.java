package org.qin.base.annotate;

import org.qin.base.annotate.checks.impl.ICheckKeyService;
import org.qin.base.annotate.checks.impl.NoCheck;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * @author liuqin
 * @descripttion 幂等注解
 * @parms
 * @return
 * @date 2020/5/23
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface WaitFor {

    int seconds() default 3;// 幂等时间

    LockType lockType() default LockType.RedisLock;// 锁类型 redisLock ,redissonLock

    String[] excludeKeys() default {};// 排除键

    Class<? extends ICheckKeyService> keysCheck() default NoCheck.class; // 检验类
}
