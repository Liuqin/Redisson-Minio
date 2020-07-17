package org.qin.timewheel;


import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.*;

/**
 * @title: TimerMaker
 * @decription:
 * @author: liuqin
 * @date: 2020/7/17 10:33
 */
public class TimerMaker {
    /**
     * 一个Timer只有一个delayQueue
     */
    private DelayQueue<TimerTaskList> delayQueue;

    /**
     * 底层时间轮
     */
    private TimeWheel timeWheel;

    /**
     * 过期任务执行线程
     */
    private ExecutorService workerThreadPool;

    /**
     * 轮询delayQueue获取过期任务线程
     */
    private ExecutorService bossThreadPool;


    /**
     * 构造函数
     */
    public TimerMaker(long tick, int wheelSize) {

        int nThreads = Runtime.getRuntime().availableProcessors();
        ThreadFactory boosThreadFactory = new ThreadFactoryBuilder().setNameFormat("boos-pool-%d").build();
        ThreadFactory workerThreadFactory = new ThreadFactoryBuilder().setNameFormat("worker-pool-%d").build();
        ExecutorService workerPool = new ThreadPoolExecutor(5, 200, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(1024), workerThreadFactory, new ThreadPoolExecutor.AbortPolicy());
        ExecutorService bossPool = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>(1024), boosThreadFactory, new ThreadPoolExecutor.AbortPolicy());
        this.delayQueue = new DelayQueue<>();
        this.timeWheel = new TimeWheel(tick, wheelSize, System.currentTimeMillis(), delayQueue);
        this.workerThreadPool = workerPool;
        this.bossThreadPool = bossPool;
//        this.workerThreadPool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2 + 2);
//        this.bossThreadPool = Executors.newFixedThreadPool(1);

        this.bossThreadPool.submit(() -> {
            while (true) {
                this.advanceClock(tick * wheelSize);
            }
        });
    }


    /**
     * 添加任务
     */
    public void addTask(TimerTask timerTask) {
        //添加失败任务直接执行
        if (!timeWheel.addTask(timerTask)) {
            workerThreadPool.submit(timerTask.getTask());
        }
    }


    /**
     * 获取过期任务
     */
    private void advanceClock(long timeout) {
        try {
            TimerTaskList timerTaskList = delayQueue.poll(timeout, TimeUnit.MILLISECONDS);
            if (timerTaskList != null) {
                //推进时间
                timeWheel.advanceClock(timerTaskList.getExpiration());
                //执行过期任务（包含降级操作）
                timerTaskList.flush(this::addTask);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
