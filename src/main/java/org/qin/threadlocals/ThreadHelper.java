package org.qin.threadlocals;


import java.util.HashMap;

/**
 * @title: ThreadServices
 * @decription:
 * @author: liuqin
 * @date: 2020/7/30 22:21
 */
public interface ThreadHelper {

    <T extends ThreadHelper> T create();

    HashMap<String, Object> dowork(HashMap<String, Object> workmap);
}
