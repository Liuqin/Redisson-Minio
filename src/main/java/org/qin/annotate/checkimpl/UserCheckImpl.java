package org.qin.annotate.checkimpl;


import org.qin.annotate.checkutil.KeyToken;

/**
 * @title: CheckUser
 * @decription: 手动实现用户幂等
 * @author: liuqin
 * @date: 2020/6/18 09:58
 */
public class UserCheckImpl implements CheckKeys {
    @Override
    public boolean check(String returnVar, Object[] args) {
        returnVar = KeyToken.getToken(args, "clientSeqNo");
        return returnVar != null && "" != returnVar;
    }
}
