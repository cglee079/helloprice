package com.podo.helloprice.api.global.security.oauth.value;

import com.podo.helloprice.api.domain.user.model.UserRole;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;

@Getter
public class OAuthAttributes {

    private OAuthType oAuthType;
    private String userKey;
    private String username;
    private String picture;
    private UserRole role;

    @Builder
    public OAuthAttributes(OAuthType oAuthType, String userKey, String username, String picture) {
        this.oAuthType = oAuthType;
        this.userKey = userKey;
        this.username = username;
        this.picture = picture;
    }

    public void setRole(UserRole role){
        this.role = role;
    }

    public List<SimpleGrantedAuthority> getAuthorities(){
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role));
    }
}
