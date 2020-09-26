package org.opsli.core.security.shiro.realm;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.opsli.api.wrapper.system.user.UserModel;
import org.opsli.core.msg.TokenMsg;
import org.opsli.core.security.shiro.token.OAuth2Token;
import org.opsli.core.utils.UserTokenUtil;
import org.opsli.core.utils.UserUtil;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 认证
 *
 * @author 孙志强

 * @date 2017-05-20 14:00
 */
@Component
public class OAuth2Realm extends AuthorizingRealm {

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof OAuth2Token;
    }

    /**
     * 授权(验证权限时调用)
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        UserModel user = (UserModel) principals.getPrimaryPrincipal();
        String userId = user.getId();

        //用户权限列表
        List<String> permsSet = UserUtil.getUserAllPermsByUserId(userId);

        //用户角色列表
        List<String> rolesSet = UserUtil.getUserRolesByUserId(userId);

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.addStringPermissions(permsSet);
        info.addRoles(rolesSet);

        return info;
    }

    /**
     * 认证(登录时调用)
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)
            throws AuthenticationException {

        String accessToken = (String) token.getPrincipal();

        // 1. 校验 token 是否有效
        boolean verify = UserTokenUtil.verify(accessToken);
        if(!verify){
            // token失效，请重新登录
            throw new IncorrectCredentialsException(
                    TokenMsg.EXCEPTION_TOKEN_LOSE_EFFICACY.getMessage());
        }

        // 2. 查询 用户信息
        String userId = UserTokenUtil.getUserIdByToken(accessToken);
        UserModel user = UserUtil.getUser(userId);

        // 3. 校验账户是否锁定
        if(user == null || user.getLocked().equals('1')){
            // 账号已被锁定,请联系管理员
            throw new LockedAccountException(
                    TokenMsg.EXCEPTION_LOGIN_ACCOUNT_LOCKED.getMessage());
        }

        return new SimpleAuthenticationInfo(user, accessToken, getName());
    }
}
