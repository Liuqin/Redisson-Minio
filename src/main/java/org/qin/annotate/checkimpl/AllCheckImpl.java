package org.qin.annotate.checkimpl;

import org.qin.annotate.checkutil.CheckResult;
import org.qin.annotate.checkutil.ICheckKeyService;

/**
 * @title: AllCheck
 * @decription:
 * @author: liuqin
 * @date: 2020/6/19 13:53
 */
public class AllCheckImpl implements ICheckKeyService {

    @Override
    public CheckResult check(Object[] args) {
        CheckResult checkResult = new CheckResult();
        checkResult.setValid(true);
        return checkResult;
    }
}
