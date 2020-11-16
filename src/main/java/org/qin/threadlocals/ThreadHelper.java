package org.qin.threadlocals;

import java.util.HashMap;

/**
 * @title: ThreadServices
 * @decription: 副本线程通用接口
 * @author: liuqin
 * @date: 2020/7/30 22:21
 */
public interface ThreadHelper {

    <T> T create();

    HashMap<String, Object> doWork(HashMap<String, Object> workmap);
}
