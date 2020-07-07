package org.qin.base.annotate.checkImpl;


import org.qin.base.annotate.checkImpl.CheckKeys;
import org.qin.base.annotate.checkutil.KeyToken;

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
