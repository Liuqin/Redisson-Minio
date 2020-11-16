package org.qin.annotate.checkimpl;

import org.qin.annotate.checkutil.CheckResult;
import org.qin.annotate.checkutil.ICheckKeyService;
import org.springframework.stereotype.Component;

/**
 * @title: NoCheck
 * @decription: 默认幂等是不打开的
 * @author: liuqin
 * @date: 2020/6/18 09:22
 */
@Component
public class NoCheckImpl implements ICheckKeyService {

    @Override
    public CheckResult check(Object[] args) {
        CheckResult checkResult = new CheckResult();
        checkResult.setValid(false);
        return checkResult;
    }
}


