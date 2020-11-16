package org.qin.shiro.realm;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.HashSet;
import java.util.Set;

/**
 * 公共的realm
 *
 * @author: hyl
 * @date: 2020/02/08
 **/
public class IhrmRealm extends AuthorizingRealm {

    @Override
    public void setName(String name) {
        super.setName("IhrmRealm");
    }


    //授权方法
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        //获取安全数据
        Object primaryPrincipal = principalCollection.getPrimaryPrincipal();
        //获取权限信息
//        Set<String> apisPerms = (Set<String>) primaryPrincipal.getRoles().get("apis");
        //构造权限信息,返回值
        Set<String> apisPerms = new HashSet<>();
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.setStringPermissions(apisPerms);
        return info;
    }


    //认证方法
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        return null;
    }
}
