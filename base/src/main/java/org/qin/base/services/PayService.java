package org.qin.base.services;

import lombok.extern.slf4j.Slf4j;
import org.qin.base.annotate.IdempotentWaitFor;
import org.qin.base.annotate.LockType;
import org.qin.base.annotate.checks.impl.UserCheck;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author LiuQin
 */
@Service
@Slf4j
public class PayService {

    @IdempotentWaitFor(seconds = 6, lockType = LockType.RedisLock)
    public int pay(Map<String, Object> abc, String xyz) {
        log.info("模拟函数执行时间");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @IdempotentWaitFor(seconds = 6, lockType = LockType.RedissonLock, keysCheck = UserCheck.class,
            excludeKeys = {"name1", "liuq1"})
    public int pay2(Map<String, Object> abc, String xyz) {
        log.info("模拟函数执行时间");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
