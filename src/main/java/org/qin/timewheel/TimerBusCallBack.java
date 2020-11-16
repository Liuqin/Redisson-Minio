package org.qin.timewheel;

import java.util.HashMap;

/**
 * @title: TimerBusCallBack
 * @decription:
 * @author: liuqin
 * @date: 2020/7/21 15:44
 */
public interface TimerBusCallBack {

    Boolean doWork(HashMap<String, Object> hashmap);
}
