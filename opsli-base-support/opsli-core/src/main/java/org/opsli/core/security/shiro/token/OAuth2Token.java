package org.opsli.core.security.shiro.token;


import org.apache.shiro.authc.AuthenticationToken;

/**
 *  OAuth2 token
 *
 * @author 孙志强

 * @date 2017-05-20 13:22
 */
public class OAuth2Token implements AuthenticationToken {

    private final String token;

    public OAuth2Token(String token){
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
