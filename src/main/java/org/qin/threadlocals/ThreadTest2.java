package org.qin.threadlocals;


import org.springframework.stereotype.Component;

import java.util.HashMap;

/**
 * @title: threadTest2
 * @decription:
 * @author: liuqin
 * @date: 2020/7/31 13:49
 */

@Component
public class ThreadTest2 implements ThreadHelper {
    @Override
    public <T extends ThreadHelper> T create() {
        System.out.println("业务2副本");
        return (T) new ThreadTest2();
    }


    @Override
    public HashMap<String, Object> dowork(HashMap<String, Object> workmap) {
        System.out.println("业务2副本地址:");
        return null;
    }
}
