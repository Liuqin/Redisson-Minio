package org.qin.scancode;

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
public class ScanMiddleWare implements ApplicationContextAware {

    private static ApplicationContext applicationContext;


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ScanMiddleWare.applicationContext = applicationContext;
        ScanMiddleWare.getList();
    }


    public static HashMap<String, ScanService> getList() {
        System.out.println("系统开始获取所有实现扫描接口的服务:");
        Collection<ScanService> serviceLinkedList = new LinkedList<>(applicationContext.getBeansOfType(ScanService.class).values());
        ScanBus.ScanServiceList = new HashMap<>();
        for (ScanService scanService : serviceLinkedList) {
            System.out.println(scanService.getClass().getName());
            ScanBus.ScanServiceList.put(scanService.getServiceName(), scanService.getInstance());
        }

        return ScanBus.ScanServiceList;
    }
}
