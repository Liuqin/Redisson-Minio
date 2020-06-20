package org.qin.base.annotate.checks.impl;


/**
 * @title: NoCheck
 * @decription: 默认幂等是不打开的
 * @author: liuqin
 * @date: 2020/6/18 09:22
 */
public class NoCheck implements CheckKeys {

    @Override
    public boolean check(String returnVar, Object[] args) {
        return false;
    }
}


