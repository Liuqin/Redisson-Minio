//package org.qin.base.annotate.checks.impl;
//
//
//import org.qin.base.annotate.RedisLock;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.util.concurrent.ConcurrentHashMap;
//
///**
// * @title: IdempotentResult
// * @decription:
// * @author: liuqin
// * @date: 2020/6/19 14:21
// */
//public class IdempotentResult {
//    public static ConcurrentHashMap<String, Object> Result;
//
//    @Autowired
//    private RedisLock redisLock;
//
//    /**
//     * @return
//     * @descripttion 容器添加
//     * @parms
//     * @author liuqin
//     * @date 2020/6/18
//     */
//    public static void put(String key, Object object) {
//        if (IdempotentResult.Result == null) {
//            IdempotentResult.Result = new ConcurrentHashMap<>();
//        }
//        if (IdempotentResult.Result.containsKey(key)) {
//
//            IdempotentResult.Result.remove(key);
//        }
//        IdempotentResult.Result.put(key, object);
//    }
//
//    /**
//     * @return
//     * @descripttion 容器获取
//     * @parms
//     * @author liuqin
//     * @date 2020/6/18
//     */
//    public static Object get(String key) {
//        if (IdempotentResult.Result == null) {
//            IdempotentResult.Result = new ConcurrentHashMap<>();
//            return null;
//        }
//        if (IdempotentResult.Result.containsKey(key)) {
//            return IdempotentResult.Result.get(key);
//        }
//        return null;
//    }
//}
