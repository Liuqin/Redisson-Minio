package org.qin.threadlocals;


import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * @title: ScanServiceWare
 * @decription:
 * @author: liuqin
 * @date: 2020/7/29 09:42
 */

@Component
public class TreadLocalWare implements ApplicationContextAware {
    private static ApplicationContext applicationContext;


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        TreadLocalWare.applicationContext = applicationContext;
        TreadLocalWare.getList();
    }


    public static HashMap<String, ThreadHelper> getList() {
        System.out.println("系统开始获取所有副本实现扫描接口的服务:");
        Collection<ThreadHelper> serviceLinkedList = new LinkedList<>(applicationContext.getBeansOfType(ThreadHelper.class).values());
        ThreadLocalBus.threadHelperList = new HashMap<>();
        for (ThreadHelper threadHelper : serviceLinkedList) {
            System.out.println(threadHelper.getClass().getName());
            ThreadLocalBus.threadHelperList.put(threadHelper.getClass().getName(), threadHelper.create());
        }
        return ThreadLocalBus.threadHelperList;
    }
}
