package org.qin.threadlocals;

import lombok.SneakyThrows;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @title: ThreadLocals
 * @decription: 公共处理秒杀副本函数
 * @author: liuqin
 * @date: 2020/7/30 22:12
 */
public final class ThreadLocals {
    // private static final ThreadLocal<Object> linkObject = new ThreadLocal<>();

    private static final ConcurrentHashMap<String, ThreadLocal<Object>> linkMap = new ConcurrentHashMap();


    @SneakyThrows
    public static <T extends ThreadHelper> HashMap<String, Object> work(Class<?> clazz, HashMap<String, Object> workMap) {
        HashMap<String, Object> result = new HashMap<>();
        String classname = clazz.getName();
        ThreadLocal<Object> threadLocal = null;
        T link = null;
        if (linkMap.contains(classname)) {
            threadLocal = linkMap.get(classname);
            link = (T) threadLocal.get();
            link = initLink(classname, link);
        } else {
            threadLocal = new ThreadLocal<>();
            link = initLink(classname, null);
            linkMap.put(classname, threadLocal);
        }
        System.out.println("副本内存地址:" + System.identityHashCode(link));
        HashMap<String, Object> dowork = link.doWork(workMap);
        if (dowork != null) {
            result = dowork;
        }
        result.put("instance", link);
        threadLocal.set(link);
        return result;
    }


    private static <T extends ThreadHelper> T initLink(String classname, T link) {
        if (link == null) {
            try {
                T t = (T) ThreadLocalBus.getInstance(classname);
                ThreadHelper threadHelper = t.create();
                link = (T) threadHelper;
            } catch (Exception e) {
                throw e;
            }
        }
        return link;
    }

}
