package org.qin.scancode;

import java.util.HashMap;

/**
 * @title: ServicesIoc
 * @decription:
 * @author: liuqin
 * @date: 2020/7/29 09:23
 */
public class ScanBus {

    public static HashMap<String, ScanService> ScanServiceList;


    public static ScanService getInstance(String key) {
        return ScanBus.ScanServiceList.get(key);
    }


    public HashMap<String, Object> hander(String key, HashMap<String, Object> map) {
        ScanService instance = ScanBus.getInstance(key);
        if (instance == null) {
            return null;
        } else {
            return instance.hander(map);
        }
    }


    //验证扫码的合法性
    public HashMap<String, Object> checkCode(String qrCodeStr, HashMap<String, Object> map) {
        return null;
    }

}
