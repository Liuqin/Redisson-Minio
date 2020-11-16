package org.qin.timewheel;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;

/**
 * @title: TimerDemo
 * @decription:
 * @author: liuqin
 * @date: 2020/7/21 15:47
 */

/**
 * @author liuqin
 * @descripttion 一个用于反复执行数据的 时间轮
 * @parms
 * @return
 * @date 2020/7/21
 */
@Slf4j
public class TimerCallBackDemo implements TimerBusCallBack {

    @Override
    public Boolean doWork(HashMap<String, Object> hashmap) {
        log.info("system test");
        return false;
    }
}
