package org.qin.threadlocals;


import lombok.SneakyThrows;

import java.util.HashMap;

/**
 * @title: ThreadLocals
 * @decription:
 * @author: liuqin
 * @date: 2020/7/30 22:12
 */
public final class ThreadLocals {
    private static final ThreadLocal<Object> linkObject = new ThreadLocal<>();


    @SneakyThrows
    public static <T extends ThreadHelper> HashMap<String, Object> work(Class<?> clazz, HashMap<String, Object> workMap) {
        HashMap<String, Object> result = new HashMap<>();
        T link = (T) linkObject.get();

        if (link == null) {
            try {
                //  System.out.println(clazz.getName());
                T t = (T) ThreadLocalBus.getInstance(clazz.getName());
                ThreadHelper threadHelper = t.create();
                link = (T) threadHelper;
            } catch (Exception e) {
                throw e;
            }
        }
        System.out.println("副本内存地址:" + System.identityHashCode(link));
        HashMap<String, Object> dowork = link.dowork(workMap);
        if (dowork != null) {
            result = dowork;
        }
        result.put("instance", link);
        linkObject.set(link);
        return result;
    }


}
