package org.qin.timewheel;


import java.util.concurrent.ConcurrentHashMap;

/**
 * @title: TimerBus
 * @decription:
 * @author: liuqin
 * @date: 2020/7/17 10:36
 */
public class TimerBus {
    private static ConcurrentHashMap<String, TimerMaker> timerMakerHashMap;


    public static void Add(int tick, int wheelSize, TimerTask timeTask) {
        if (TimerBus.timerMakerHashMap == null) {
            TimerBus.timerMakerHashMap = new ConcurrentHashMap<>();
        }
        String key = String.valueOf(tick) + "," + String.valueOf(wheelSize);
        if (TimerBus.timerMakerHashMap.size() == 0 || TimerBus.timerMakerHashMap.contains(key)) {
            TimerMaker timerMaker = new TimerMaker(tick, wheelSize);
            TimerBus.timerMakerHashMap.put(key, timerMaker);
        }
        TimerBus.timerMakerHashMap.get(key).addTask(timeTask);
    }


    public static void Add(TimerTask timerTask) {
        TimerBus.Add(50, 120, timerTask);
    }
}

