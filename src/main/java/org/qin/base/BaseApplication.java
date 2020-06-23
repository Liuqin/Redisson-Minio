package org.qin.base;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author liuqin
 * @descripttion 启动类
 * @parms
 * @return
 * @date 2020/6/4
 */
@SpringBootApplication
public class BaseApplication {

    public static void main(String[] args) {
        // ParserConfig.getGlobalInstance().setSafeMode(true);
        SpringApplication.run(BaseApplication.class, args);
    }


}
