package org.qin.threadlocals;


import java.util.HashMap;

/**
 * @title: ServicesIoc
 * @decription:
 * @author: liuqin
 * @date: 2020/7/29 09:23
 */
public class ThreadLocalBus {
    public static HashMap<String, ThreadHelper> threadHelperList;


    public static ThreadHelper getInstance(String key) {
        return ThreadLocalBus.threadHelperList.get(key);
    }


}
