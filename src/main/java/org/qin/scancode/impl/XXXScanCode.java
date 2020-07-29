package org.qin.scancode.impl;


import org.qin.scancode.ScanService;
import org.springframework.stereotype.Component;

import java.util.HashMap;

/**
 * @title: XXXScanCode
 * @decription:
 * @author: liuqin
 * @date: 2020/7/29 09:50
 */

@Component
public class XXXScanCode implements ScanService {
    @Override
    public String getServiceName() {
        return "XXX";
    }


    @Override
    public ScanService getInstance() {
        return this;
    }


    @Override
    public HashMap<String, Object> Hander(HashMap<String, Object> map) {
        return null;
    }
}
