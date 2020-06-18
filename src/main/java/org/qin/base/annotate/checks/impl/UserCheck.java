package org.qin.base.annotate.checks.impl;


import org.springframework.stereotype.Component;

/**
 * @title: CheckUser
 * @decription: 手动实现用户幂等
 * @author: liuqin
 * @date: 2020/6/18 09:58
 */
@Component

public class UserCheck implements CheckKeys {


    @Override
    public boolean check(String[] returnVar, Object[] args) {
        String clientSeqNo = KeyToken.getToken(args, "clientSeqNo");
        returnVar[0] = clientSeqNo;
        return clientSeqNo != null && "" != clientSeqNo;
    }


}
