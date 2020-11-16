package org.qin.callbackhandle;

import java.util.HashMap;

/**
 * @title: CallBackHander
 * @decription:
 * @author: liuqin
 * @date: 2020/8/4 17:15
 */
public interface CallBackHander {

    boolean done(HashMap<String, Object> hashMap);

    /**
     * @return void
     * @descripttion 回调地址，使用post作为请求方式
     * @author liuqin
     * @date 2020/8/5
     */
    String callBackUrl();
}
