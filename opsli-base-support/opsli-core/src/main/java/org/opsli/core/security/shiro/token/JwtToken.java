package org.opsli.core.security.shiro.token;


import org.apache.shiro.authc.AuthenticationToken;

/**
 *  OAuth2 token
 *
 * @author Parker
 * @date 2017-05-20 13:22
 */
public class JwtToken implements AuthenticationToken {

    private final String token;

    public JwtToken(String token){
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
