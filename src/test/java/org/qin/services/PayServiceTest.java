package org.qin.services;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.qin.BaseApplication;
import org.qin.threadlocals.ThreadLocals;
import org.qin.threadlocals.ThreadTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {BaseApplication.class, PayServiceTest.class, MinioService.class})
@Slf4j
public class PayServiceTest {

    @Autowired
    private PayService payService;


//    @SneakyThrows
//    @Test
//    public void pay() {
//
//    }


    @Autowired
    private MinioService minioService;


    @Test
    public void minioTest() throws InterruptedException {

        System.out.println("测试一下独立的线程副本");
        HashMap<String, Object> stringObjectHashMap = new HashMap<>();

        int threadSize = 100;
        //创建线程池
        ExecutorService executorService = Executors.newFixedThreadPool(threadSize);
        //开始时间
        long start = System.currentTimeMillis();
        //让线程池中的每一个线程都开始工作
        for (int j = 0; j < threadSize; j++) {
            //执行线程
            executorService.execute(() -> {

                ThreadLocals.work(ThreadTest.class, null);
            });
        }
        //等线程全部执行完后关闭线程池
        executorService.shutdown();
        executorService.awaitTermination(Integer.MAX_VALUE, TimeUnit.DAYS);


        for (int i = 0; i < 100; i++) {

        }


    }


//    @Test
//    public void TimerBusTest() throws InterruptedException {
//        System.out.println(myenum.boss);
//        HostInfo hostInfo = SystemUtil.getHostInfo();
//        log.info(hostInfo.toString());
//        //简单用法
//        TimerBus.add(new TimerTask(3000, () -> log.info("TimerBusTest 3000")));
//        TimerBus.add(new TimerTask(2000, () -> log.info("TimerBusTest 2000")));
//        TimerBus.add(new TimerTask(1000, () -> log.info("TimerBusTest 1000 more")));
//
//        // 复杂用法
//        TimerBus.add(new TimerTask(1000, () -> {
//            log.info("TimerBusTest 1000");
//            log.info("第一次进 时间轮");
//            TimerBus.add(new TimerTask(1000, () -> {
//                log.info("再次进入时间轮");
//            }));
//
//        }));
//
//        TimerCallBackDemo timerCallBackDemo = new TimerCallBackDemo();
//        // 特殊用法
//        TimerBus.addMoreTimes(timerCallBackDemo, new HashMap<>(), 1000, 6);
//        Thread.sleep(10000);
//    }
//
//
//    public enum myenum {
//        worker, boss, agents,
//    }


}