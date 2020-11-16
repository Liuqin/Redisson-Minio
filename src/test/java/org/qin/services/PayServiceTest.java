package org.qin.services;

import cn.hutool.core.io.FileUtil;
import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.hutool.system.HostInfo;
import cn.hutool.system.SystemUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.qin.BaseApplication;
import org.qin.algorithm.Coord;
import org.qin.algorithm.Fitting;
import org.qin.simhash.SimHash;
import org.qin.threadlocals.ThreadLocals;
import org.qin.threadlocals.ThreadTest;
import org.qin.threadlocals.ThreadTest2;
import org.qin.timewheel.TimerBus;
import org.qin.timewheel.TimerCallBackDemo;
import org.qin.timewheel.TimerTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
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

        System.out.println(System.currentTimeMillis());
        List<Double> limit = Coord.get4limit(123.11, 121.11, 500);
        System.out.println(limit);

        Fitting fitting = new Fitting();
        double[] data = new double[1000];
        Random ran = new Random(1000);
        for (int i = 0; i < 1000; i++) {
            data[i] = ran.nextDouble();
        }
        fitting.setData(data);

        List<Map.Entry<Integer, Integer>> entries = fitting.fittingResutl();
        for (Map.Entry<Integer, Integer> subdata : entries) {
            System.out.println(subdata.getKey() + " " + subdata.getValue());
        }

        String testStr = "0110000111000101011000111000010101001";
        String[] strs = testStr.split("1");

        String token1 = "123";
        String token2 = "2345";

        SimHash hashtest1 = new SimHash(token1, 64);
        SimHash hashtest2 = new SimHash(token2, 64);
        System.out.println(hashtest1.intSimHash + " " + hashtest2.intSimHash);
        System.out.println(hashtest1.strSimHash + " " + hashtest2.strSimHash);
        int i1 = hashtest1.hammingDistance(hashtest2);
        System.out.println(i1);
        List list = hashtest1.subByDistance(hashtest2, 3);
        System.out.println(list);

        minioService.doMinioService();
        String s = "This is a test string for testing";
        SimHash hash1 = new SimHash(s, 64);
        System.out.println(hash1.intSimHash + "  " + hash1.intSimHash.bitLength());
        hash1.subByDistance(hash1, 3);

        s = "This is a test string for testing, This is a test string for testing abcdef";
        SimHash hash2 = new SimHash(s, 64);
        System.out.println(hash2.intSimHash + " " + hash2.intSimHash.bitCount());
        hash1.subByDistance(hash2, 3);

        QrCodeUtil.generate("https://hutool.cn/", 300, 300, FileUtil.file("d:/qrcode.jpg"));

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
                ThreadLocals.work(ThreadTest2.class, null);
            });
        }
        //等线程全部执行完后关闭线程池
        executorService.shutdown();
        executorService.awaitTermination(Integer.MAX_VALUE, TimeUnit.DAYS);

        for (int i = 0; i < 100; i++) {

        }

    }


    @Test
    public void TimerBusTest() throws InterruptedException {


        TimerCallBackDemo timerCallBackDemo = new TimerCallBackDemo();
        HashMap<String, Object> map= new HashMap<>();

        ExecutorService executorService = Executors.newFixedThreadPool(5);
        for(int i = 0; i<1000; i++) {
            executorService.execute(()->{
                TimerBus.addMoreTimes(timerCallBackDemo, map,3000,5);
            });
        }
        //关闭线程池
        executorService.shutdown();







        System.out.println(myenum.boss);
        HostInfo hostInfo = SystemUtil.getHostInfo();
        log.info(hostInfo.toString());
        //简单用法
        TimerBus.add(new TimerTask(3000, () -> log.info("TimerBusTest 3000")));
        TimerBus.add(new TimerTask(2000, () -> log.info("TimerBusTest 2000")));
        TimerBus.add(new TimerTask(1000, () -> log.info("TimerBusTest 1000 more")));

        // 复杂用法
        TimerBus.add(new TimerTask(1000, () -> {
            log.info("TimerBusTest 1000");
            log.info("第一次进 时间轮");
            TimerBus.add(new TimerTask(1000, () -> {
                log.info("再次进入时间轮");
            }));

        }));

      //  TimerCallBackDemo timerCallBackDemo = new TimerCallBackDemo();
        // 特殊用法
        TimerBus.addMoreTimes(timerCallBackDemo, new HashMap<>(), 1000, 6);
        Thread.sleep(10000);
    }


    public enum myenum {
        worker, boss, agents,
    }

}