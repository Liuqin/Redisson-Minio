package org.qin.base.annotate.checkutil;


import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @title: KeyToken
 * @decription:
 * @author: liuqin
 * @date: 2020/6/19 01:01
 */

@Component
public class KeyToken {
    /**
     * @return
     * @descripttion 取出Map中的特定数据
     * @parms
     * @author liuqin
     * @date 2020/6/18
     */
    public static String getToken(Object[] args, String key) {
        for (Object arg : args) {
            if (arg instanceof Map<?, ?>) {
                if (((Map<?, ?>) arg).containsKey(key)) {
                    return (String) ((Map<?, ?>) arg).get(key);
                }
            }
        }
        return "";
    }
}
