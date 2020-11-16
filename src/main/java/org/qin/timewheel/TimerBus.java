package org.qin.timewheel;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
/**
 * @title: TimerBus
 * @decription:
 * @author: liuqin
 * @date: 2020/7/17 10:36
 */
public class TimerBus {

    private static ConcurrentHashMap<String, TimerMaker> timerMakerHashMap;

    public static void add(int tick, int wheelSize, TimerTask timeTask) {
        if (TimerBus.timerMakerHashMap == null) {
            TimerBus.timerMakerHashMap = new ConcurrentHashMap<>();
        }
        String key = tick + "," + wheelSize;
        if (TimerBus.timerMakerHashMap.size() == 0 || !TimerBus.timerMakerHashMap.containsKey(key)) {
            TimerMaker timerMaker = new TimerMaker(tick, wheelSize);
            TimerBus.timerMakerHashMap.put(key, timerMaker);
        }
        TimerBus.timerMakerHashMap.get(key).addTask(timeTask);
    }

    public static void add(TimerTask timerTask) {
        TimerBus.add(50, 120, timerTask);
    }

    /**
     * @return
     * @descripttion 间隔相同秒数，尝试执行逻辑成功直到尝试到达最大次数。
     * @parms timerBusCallBack 执行逻辑,hashmap 参数
     * @author liuqin
     * @date 2020/7/21
     */
    public static void addMoreTimes(TimerBusCallBack timerBusCallBack, HashMap<String, Object> hashmap, int Interval, int maxTimes) {
        AtomicInteger count = new AtomicInteger();
        if (maxTimes > count.get()) {
            TimerBus.add(new TimerTask(Interval, () -> {
                count.getAndIncrement();
                if (count.get() <= maxTimes && !timerBusCallBack.doWork(hashmap) && maxTimes > 0) {
                    addMoreTimes(timerBusCallBack, hashmap, Interval, maxTimes - 1);
                }
            }));
        }
    }

    /**
     * @return
     * @descripttion 间隔时间逐渐增大的请求，在达到指定尝试次数前，一直尝试请求
     * @parms
     * @author liuqin
     * @date 2020/7/21
     */
    public static void intervalMoreTimes(TimerBusCallBack timerBusCallBack, HashMap<String, Object> hashmap, int maxTimes, int Interval, int moreInterval) {
        AtomicInteger count = new AtomicInteger();
        int intervalTime = Interval + moreInterval;
        TimerBus.add(new TimerTask(Interval, () -> {
            count.getAndIncrement();
            if (count.get() <= maxTimes && !timerBusCallBack.doWork(hashmap) && maxTimes > 0) {
                intervalMoreTimes(timerBusCallBack, hashmap, maxTimes - 1, intervalTime, moreInterval);
            }
        }));
    }
}

