package org.opsli.core.security.shiro.realm;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.IoUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.opsli.api.wrapper.system.user.UserModel;
import org.opsli.common.api.TokenThreadLocal;
import org.opsli.common.exception.TokenException;
import org.opsli.core.msg.TokenMsg;
import org.opsli.core.security.shiro.token.JwtToken;
import org.opsli.core.utils.UserTokenUtil;
import org.opsli.core.utils.UserUtil;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.List;

/**
 * 认证
 *
 * @author 孙志强

 * @date 2017-05-20 14:00
 */
@Component
@Slf4j
public class JwtRealm extends AuthorizingRealm implements FlagRealm {

    /** 账号锁定状态 */
    public static final String LOCK_VAL = "1";

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    /**
     * 授权(验证权限时调用)
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

        UserModel user = (UserModel) principals.getPrimaryPrincipal();
        String userId = user.getId();

        //用户权限列表
        List<String> permsSet = UserUtil.getUserAllPermsByUserId(userId);
        if(CollUtil.isNotEmpty(permsSet)){
            info.addStringPermissions(permsSet);
        }

        //用户角色列表
        List<String> rolesSet = UserUtil.getUserRolesByUserId(userId);
        if(CollUtil.isNotEmpty(rolesSet)){
            info.addRoles(rolesSet);
        }

        return info;
    }

    /**
     * 认证(登录时调用)
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)
            throws AuthenticationException,TokenException {

        String accessToken = (String) token.getPrincipal();

        // 1. 校验 token 是否有效
        boolean verify = UserTokenUtil.verify(accessToken);
        if(!verify){
            // token失效，请重新登录
            throw new TokenException(
                    TokenMsg.EXCEPTION_TOKEN_LOSE_EFFICACY);
        }

        // 2. 查询 用户信息
        String userId = UserTokenUtil.getUserIdByToken(accessToken);
        UserModel user = UserUtil.getUser(userId);

        // 3. 校验账户是否锁定
        if(user == null || user.getLocked().equals(LOCK_VAL)){
            // 账号已被锁定,请联系管理员
            // token失效，请重新登录
            throw new TokenException(
                    TokenMsg.EXCEPTION_LOGIN_ACCOUNT_LOCKED);
        }

        return new SimpleAuthenticationInfo(user, accessToken, getName());
    }


    /**
     * Token 认证
     */
    public static void authToken()
            throws TokenException {

        String accessToken = TokenThreadLocal.get();

        // 1. 校验 token 是否有效
        boolean verify = UserTokenUtil.verify(accessToken);
        if(!verify){
            // token失效，请重新登录
            throw new TokenException(
                    TokenMsg.EXCEPTION_TOKEN_LOSE_EFFICACY);
        }

        // 2. 查询 用户信息
        String userId = UserTokenUtil.getUserIdByToken(accessToken);
        UserModel user = UserUtil.getUser(userId);

        // 3. 校验账户是否锁定
        if(user == null || user.getLocked().equals(JwtRealm.LOCK_VAL)){
            // 账号已被锁定,请联系管理员
            // token失效，请重新登录
            throw new TokenException(
                    TokenMsg.EXCEPTION_TOKEN_LOSE_EFFICACY);
        }
    }

    /**
     * 按钮权限 认证
     */
    public static void authPerms(String[] currPerms)
            throws TokenException {
        if(currPerms == null ){
            return;
        }

        String accessToken = TokenThreadLocal.get();

        // 查询 用户信息
        String userId = UserTokenUtil.getUserIdByToken(accessToken);

        //用户权限列表
        List<String> permsSet = UserUtil.getUserAllPermsByUserId(userId);

        if(CollUtil.isEmpty(permsSet)){
            // 无权访问该方法
            throw new TokenException(
                    TokenMsg.EXCEPTION_NOT_AUTH);
        }

        for (String currPerm : currPerms) {
            if(!permsSet.contains(currPerm)){
                // 无权访问该方法
                throw new TokenException(
                        TokenMsg.EXCEPTION_NOT_AUTH);
            }
        }
    }

}
