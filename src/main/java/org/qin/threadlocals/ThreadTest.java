package org.qin.threadlocals;

import org.springframework.stereotype.Component;

import java.util.HashMap;

/**
 * @title: ThreadTest
 * @decription:
 * @author: liuqin
 * @date: 2020/7/30 23:34
 */

@Component
public class ThreadTest implements ThreadHelper {
    @Override
    public ThreadTest create() {
        System.out.println("新副本，如独立的数据库连接");
        // 这里需要新开副本，不能引导同一个对象
        return new ThreadTest();
    }


    @Override
    public HashMap<String, Object> dowork(HashMap workmap) {
        System.out.println("系统测试独立的线程副本");
        return null;
    }

}
