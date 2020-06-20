package org.qin.base.annotate.checks.impl;


import lombok.Data;

/**
 * @title: CheckResult
 * @decription:
 * @author: liuqin
 * @date: 2020/6/19 16:36
 */

@Data
public class CheckResult {
    /**
     * 唯一标识
     */
    private String tokenKey;
    /**
     * 验证是否幂等
     */
    private Boolean valid;


}
