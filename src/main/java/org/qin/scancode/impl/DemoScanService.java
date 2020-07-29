package org.qin.scancode.impl;


import org.qin.scancode.ScanService;
import org.springframework.stereotype.Component;

import java.util.HashMap;

/**
 * @title: DemoScanService
 * @decription:
 * @author: liuqin
 * @date: 2020/7/29 10:30
 */

@Component
public class DemoScanService implements ScanService {
    @Override
    public String getServiceName() {
        return "demo";
    }
    @Override
    public ScanService getInstance() {
        return this;
    }
    @Override
    public HashMap<String, Object> hander(HashMap<String, Object> map) {
        return null;
    }
}
