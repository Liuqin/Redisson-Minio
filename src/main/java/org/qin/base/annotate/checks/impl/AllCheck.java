package org.qin.base.annotate.checks.impl;


/**
 * @title: AllCheck
 * @decription:
 * @author: liuqin
 * @date: 2020/6/19 13:53
 */
public class AllCheck implements ICheckKeyService {


    @Override
    public CheckResult check(Object[] args) {
        CheckResult checkResult = new CheckResult();
        checkResult.setValid(true);
        return checkResult;
    }
}
