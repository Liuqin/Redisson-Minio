package org.qin.base.annotate.checkImpl;


import org.qin.base.annotate.checkutil.CheckResult;
import org.qin.base.annotate.checkutil.ICheckKeyService;

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
