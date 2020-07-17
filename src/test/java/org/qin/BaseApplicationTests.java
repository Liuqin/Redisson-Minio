package org.qin;


import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.qin.services.MinioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {BaseApplication.class, BaseApplicationTests.class})
@EnableAutoConfiguration
@ComponentScan
@Slf4j
class BaseApplicationTests {

    @Autowired
    private MinioService minioService;


    @Test
    void contextLoads() {
        log.info(minioService.toString());
    }

}
