package org.qin.base.annotate.checkImpl;


import java.util.HashMap;
import java.util.Map;

/**
 * @title: ChecksContainer
 * @decription: 容器管理类
 * @author: liuqin
 * @date: 2020/6/18 11:16
 */
public class ChecksContainer {
    private static Map<String, CheckKeys> IOC;

    /**
     * @return
     * @descripttion 容器添加
     * @parms
     * @author liuqin
     * @date 2020/6/18
     */
    public static void add(String classname, CheckKeys checkKeys) {
        if (ChecksContainer.IOC == null) {
            ChecksContainer.IOC = new HashMap<>(10);
        }
        if (!ChecksContainer.IOC.containsKey(classname)) {
            ChecksContainer.IOC.put(classname, checkKeys);
        }
    }

    /**
     * @return
     * @descripttion 容器获取
     * @parms
     * @author liuqin
     * @date 2020/6/18
     */
    public static CheckKeys get(String classname) {
        if (ChecksContainer.IOC == null) {
            ChecksContainer.IOC = new HashMap<>(10);
            return null;
        }
        if (ChecksContainer.IOC.containsKey(classname)) {
            return ChecksContainer.IOC.get(classname);
        }
        return null;
    }
}
