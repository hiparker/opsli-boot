package org.opsli.core.security.shiro.token;


import org.apache.shiro.authc.AuthenticationToken;

/**
 *  jwt token
 *
 * @author parker

 * @date 2017-05-20 13:22
 */
public class JwtToken implements AuthenticationToken {
    private String token;

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
