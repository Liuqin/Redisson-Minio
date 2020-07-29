package org.qin.scancode;


import java.util.HashMap;

/**
 * @title: ScanService
 * @decription:
 * @author: liuqin
 * @date: 2020/7/29 09:25
 */
public interface ScanService {
    String getServiceName();

    ScanService getInstance();

    HashMap<String, Object> Hander(HashMap<String, Object> map);
}
