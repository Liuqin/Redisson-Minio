package org.qin.base.annotate.checkUtil;


/**
 * @title: CheckKeys
 * @decription: 幂等公共检查接口
 * @author: liuqin
 * @date: 2020/6/18 09:18
 */

public interface ICheckKeyService {
    /**
     * @return CheckResult 是否开启幂等
     * @author liuqin
     * @date 2020/6/18
     * @descripttion 在这里做幂等验证，
     * 把幂等唯一标识返回在 returnVar中,
     * 使得幂等逻辑可以有唯一标识
     * @parms args args 参数
     */
    CheckResult check(Object[] args);

}


