package com.mango.authorize;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * 自定义Token
 */
class StringToken implements AuthenticationToken {

    private final String token;

    public StringToken(String token){
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
