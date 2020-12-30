package org.opsli.core.security.shiro.realm;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.opsli.common.exception.TokenException;
import org.opsli.core.security.shiro.token.ExternalToken;
import org.springframework.stereotype.Component;


/**
 * 第三方对外接口 认证
 *
 * @author 周鹏程

 * @date 2020-12-29 14:00
 */
@Component
@Slf4j
public class ExternalRealm extends AuthorizingRealm implements FlagRealm {

    /** 账号锁定状态 */
    public static final char LOCK_VAL = '1';

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof ExternalToken;
    }

    /**
     * 授权(验证权限时调用)
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        // TODO 待处理
        return null;
    }

    /**
     * 对外认证(登录时调用)
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)
            throws AuthenticationException, TokenException {
        // TODO 待处理

        return null;
    }

}
