package org.qin.annotate.checkutil;


import java.util.concurrent.ConcurrentHashMap;

/**
 * @title: ChecksContainer
 * @decription: 容器管理类
 * @author: liuqin
 * @date: 2020/6/18 11:16
 */
public class ChecksContainer {
    private static ConcurrentHashMap<String, ICheckKeyService> IOC;


    /**
     * @return
     * @descripttion 容器添加
     * @parms
     * @author liuqin
     * @date 2020/6/18
     */
    public static void add(String classname, ICheckKeyService checkKeys) {
        if (ChecksContainer.IOC == null) {
            ChecksContainer.IOC = new ConcurrentHashMap<>(16);
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
    public static ICheckKeyService get(String classname) {
        if (ChecksContainer.IOC == null) {
            ChecksContainer.IOC = new ConcurrentHashMap<>(16);
            return null;
        }
        if (ChecksContainer.IOC.containsKey(classname)) {
            return ChecksContainer.IOC.get(classname);
        }
        return null;
    }
}
