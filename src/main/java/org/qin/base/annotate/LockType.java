package org.qin.base.annotate;

/**
 * @title: LockType
 * @decription: 锁的类型
 * @author: liuqin
 * @date: 2020/6/18 14:13
 */
public enum LockType {
    /**
     * 普通Redis锁
     */
    RedisLock,
    /**
     * Redis锁加强版
     */
    RedissonLock
}
