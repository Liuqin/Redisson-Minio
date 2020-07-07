package org.qin.base.services;

import lombok.extern.slf4j.Slf4j;
import org.qin.base.annotate.WaitFor;
import org.qin.base.annotate.checkImpl.TokenCheck;
import org.qin.base.annotate.checkutil.LockType;
import org.qin.base.annotate.checkutil.RedisLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author LiuQin
 */
@Service
@Slf4j
public class PayService {

    @WaitFor(seconds = 6, lockType = LockType.RedisLock)
    public int pay(Map<String, Object> abc, String xyz) {
        log.info("模拟函数执行时间1");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @WaitFor(seconds = 6, lockType = LockType.RedissonLock, keysCheck = TokenCheck.class, excludeKeys = {"name1", "liuq1"})
    public int pay2(Map<String, Object> abc, String xyz) {
        log.info("模拟函数执行时间2");
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Autowired
    private RedisLock redisLock;

    public void Redis() {
        long l = System.currentTimeMillis();
        redisLock.setKeyAndCacheTime("123", "1234", 10000);
        redisLock.getValue("123");
        log.info(String.valueOf(System.currentTimeMillis() - l));
    }
}
