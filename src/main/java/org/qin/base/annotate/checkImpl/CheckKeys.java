package org.qin.base.annotate.checkImpl;


/**
 * @title: CheckKeys
 * @decription:
 * @author: liuqin
 * @date: 2020/6/18 09:18
 */
public interface CheckKeys {
    /**
     * @return 是否开启幂等
     * @author liuqin
     * @date 2020/6/18
     * @descripttion 在这里做幂等验证，
     * 把幂等唯一标识返回在 returnVar中,
     * 使得幂等逻辑可以有唯一标识
     * @parms returnVar args 返回给方法的标识
     * @parms args args 参数
     */
    boolean check(String returnVar, Object[] args);
}


