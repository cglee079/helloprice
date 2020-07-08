package com.podo.helloprice.api.global.security.oauth.value;

import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
public class OAuthUserDetails implements UserDetails {

    private Long userId;
    private OAuthType oAuthType;
    private String userKey;
    private String username;
    private String picture;
    private Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();

    @Builder
    public OAuthUserDetails(OAuthType oAuthType, Long userId, String userKey, String username, String picture, List<SimpleGrantedAuthority> authorities) {
        this.oAuthType = oAuthType;
        this.userId = userId;
        this.userKey = userKey;
        this.username = username;
        this.picture = picture;
        this.authorities.addAll(authorities);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


}
