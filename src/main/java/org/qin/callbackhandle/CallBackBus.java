package org.qin.callbackhandle;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;

import java.util.HashMap;

/**
 * @author liuqin
 * @descripttion CallBackBus
 * @parms
 * @return
 * @date 2020/8/5
 */
public class CallBackBus {

    public static void businessHandle(CallBackHander callBackHander, HashMap<String, Object> hashMap) {
        boolean result = callBackHander.done(hashMap);
        String callBackUrl = callBackHander.callBackUrl();
        if (result && null != callBackUrl && StrUtil.isNotBlank(callBackUrl)) {
            HttpUtil.post(callBackUrl, hashMap);
        }
    }
}
