package org.opsli.core.security.shiro.token;


import org.apache.shiro.authc.AuthenticationToken;

/**
 *  第三方对外接口 token
 *
 * @author Parker
 * @date 2017-05-20 13:22
 */
public class ExternalToken implements AuthenticationToken {

    private final String token;

    public ExternalToken(String token){
        this.token = token;
    }

    @Override
    public String getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }

}
