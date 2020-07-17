package org.qin.services;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.qin.BaseApplication;
import org.qin.timewheel.TimerBus;
import org.qin.timewheel.TimerTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {BaseApplication.class, PayServiceTest.class, MinioService.class})
@Slf4j
public class PayServiceTest {

    @Autowired
    private PayService payService;


    @SneakyThrows
    @Test
    public void pay() {
//        UUID uuid = UUID.randomUUID();
//        Map<String, Object> map = new HashMap<>();
//        map.put("liuq1", "liu");
//        map.put("name1", "abc123");
//        map.put("clientSeqNo", "max001");
//        map.put("uuid", uuid.toString());
//        Map<String, Object> map2 = new HashMap<>();
//        map2.put("name1", "abc123");
//        map2.put("liuq1", "liu");
//        map2.put("clientSeqNo", "max001");
//        map2.put("uuid", uuid.toString());
//        System.out.println(payService.hashCode());
//        ExecutorService executorService = Executors.newFixedThreadPool(100);
//        for (int i = 0; i < 100; i++) {
//            executorService.execute(() -> {
//                payService.pay2(map, "123");
//                //    payService.pay(map2, "123");
//            });
//
//        }
//        executorService.shutdown();
//        while (!executorService.awaitTermination(1, TimeUnit.SECONDS)) {
//
//        }
    }


//    @SneakyThrows
//    @Test
//    public void testNode() {
//        payService.testMultipleNode();
//        boolean x = true;
//        Assert.isTrue(x);
//    }


    @Autowired
    private MinioService minioService;


    @Test
    public void minioTest() {
        boolean state = minioService.doMinioService();
        log.info(String.valueOf(state));
    }


    @Test
    public void TimerBusTest() {
        TimerBus.Add(new TimerTask(1000, () -> {
            log.info("TimerBusTest TimerBusTest TimerBusTest");
        }));


    }


}