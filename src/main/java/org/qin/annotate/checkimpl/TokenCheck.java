package org.qin.annotate.checkimpl;


import org.qin.annotate.checkutil.CheckResult;
import org.qin.annotate.checkutil.ICheckKeyService;
import org.qin.annotate.checkutil.KeyToken;
import org.springframework.stereotype.Component;

/**
 * @title: CheckUser
 * @decription: 手动实现用户幂等
 * @author: liuqin
 * @date: 2020/6/18 09:58
 */
@Component
public class TokenCheck implements ICheckKeyService {
    @Override
    public CheckResult check(Object[] args) {
        CheckResult checkResult = new CheckResult();
        checkResult.setValid(true);

        String clientSeqNo = KeyToken.getToken(args, "clientSeqNo");
        boolean hasValue = clientSeqNo != null && "" != clientSeqNo;
        if (hasValue) {
            checkResult.setTokenKey(clientSeqNo);
        }
        return checkResult;
    }
}
