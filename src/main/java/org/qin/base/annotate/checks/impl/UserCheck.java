package org.qin.base.annotate.checks.impl;


import org.qin.base.annotate.checkutil.KeyToken;
import org.qin.base.annotate.checkutil.KeyUtil;

/**
 * @title: CheckUser
 * @decription: 手动实现用户幂等
 * @author: liuqin
 * @date: 2020/6/18 09:58
 */
public class UserCheck implements CheckKeys {
    @Override
    public boolean check(String returnVar, Object[] args) {
        returnVar = KeyToken.getToken(args, "clientSeqNo");
        return returnVar != null && "" != returnVar;
    }
}
