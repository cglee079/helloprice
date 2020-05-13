package com.podo.helloprice.api.global.security.oauth.value;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;


public class OAuthAuthentication implements Authentication {
    private static final long serialVersionUID = 201307311621L;

    private UserDetails principal;
    private boolean isAuthenticated = true;

    public OAuthAuthentication(UserDetails userInfo) {
        this.principal = userInfo;
    }

    @Override
    public String getName() {
        return this.principal.getUsername();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.principal.getAuthorities();
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    @Override
    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.isAuthenticated = isAuthenticated;
    }

}
