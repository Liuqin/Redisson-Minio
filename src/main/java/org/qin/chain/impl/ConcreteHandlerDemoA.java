package org.qin.chain.impl;

import org.qin.chain.OurChainHandler;
import org.qin.scancode.impl.DemoScanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @title: ConcreteHandlerDemoA
 * @decription:
 * @author: liuqin
 * @date: 2020/8/17 10:26
 */

@Component
public class ConcreteHandlerDemoA implements OurChainHandler {

    @Autowired
    private DemoScanService demoScanService;

    @Override
    public void handleRequest() {

    }


    @Override
    public String groupName() {
        return "DemoA";
    }


    @Override
    public Integer orderNumber() {
        return 1;
    }


    @Override
    public String getServiceName() {
        return "ConcreteHandlerDemoA";
    }


    @Override
    public OurChainHandler getInstance() {
        String serviceName = demoScanService.getServiceName();
        System.out.println("你看到系统成功注入了"+serviceName);
        return new ConcreteHandlerDemoA();
    }

}
