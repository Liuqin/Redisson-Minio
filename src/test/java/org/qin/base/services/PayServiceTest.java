package org.qin.base.services;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.qin.base.BaseApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {BaseApplication.class, PayServiceTest.class, MinioService.class})
@Slf4j
public class PayServiceTest {

    @Autowired
    private PayService payService;

    @SneakyThrows
    @Test
    public void pay() {
        UUID uuid = UUID.randomUUID();
        Map<String, Object> map = new HashMap<>();
        map.put("liuq1", "liu");
        map.put("name1", "abc123");
        map.put("clientSeqNo", "max001");
        map.put("uuid", uuid.toString());
        Map<String, Object> map2 = new HashMap<>();
        map2.put("name1", "abc123");
        map2.put("liuq1", "liu");
        map2.put("clientSeqNo", "max001");
        map2.put("uuid", uuid.toString());
        System.out.println(payService.hashCode());
        ExecutorService executorService = Executors.newFixedThreadPool(100);
        for (int i = 0; i < 100; i++) {
            executorService.execute(() -> {
                payService.Redis();
                payService.pay2(map, "123");
                //    payService.pay(map2, "123");
            });

        }

        executorService.shutdown();
        while (!executorService.awaitTermination(1, TimeUnit.SECONDS)) {

        }


    }


//    @SneakyThrows
//    @Test
//    public void testCache() {
//        CacheMap<String, String> miniCache = (CacheMap<String, String>) CacheMap.getDefault();
//
//
//        ExecutorService executorService = Executors.newFixedThreadPool(100);
//        for (int i = 0; i < 5; i++) {
//            executorService.execute(() -> {
//                for (int x = 0; x < 20; x++) {
//                    miniCache.put("123", String.valueOf(x), 1000);
//                    String s = miniCache.get(123);
//                    log.info(s);
//                    log.info(miniCache.toString());
//                    miniCache.put("1234", String.valueOf(x), 1000);
//                    s = miniCache.get(1234);
//                    log.info(s);
//                    log.info(miniCache.toString());
//                    miniCache.put("12345", String.valueOf(x), 1000);
//                    s = miniCache.get(12345);
//                    log.info(s);
//                    log.info(miniCache.toString());
//                }
//            });
//
//        }
//
//        executorService.shutdown();
//        while (!executorService.awaitTermination(1, TimeUnit.SECONDS)) {
//            String s = miniCache.get(123);
//            log.info(s);
//        }
//
//    }

    @Autowired
    private MinioService minioService;

    @Test
    public void minioTest() {
        boolean state = minioService.doMinioService();
        log.info(String.valueOf(state));
    }
}