package org.qin.scancode.impl;

import jdk.nashorn.internal.ir.annotations.Reference;
import org.qin.common.ResultModel;
import org.qin.scancode.ScanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;

/**
 * @title: XXXScanCode
 * @decription:
 * @author: liuqin
 * @date: 2020/7/29 09:50
 */

@Component
public class XXXScanCode implements ScanService {

    @Reference
    private ResultModel<String> resultModel;

    @Override
    public String getServiceName() {
        return "XXX";
    }


    @Override
    public ScanService getInstance() {
        boolean res = this.resultModel == null;
        System.out.println("系统引用验证："+res);
        return this;
    }


    @Override
    public HashMap<String, Object> hander(HashMap<String, Object> map) {
        return null;
    }
}
