package com.podo.helloprice.product.update.analysis.domain.user.dto;

import com.podo.helloprice.api.domain.user.model.User;
import com.podo.helloprice.api.domain.user.model.UserRole;
import com.podo.helloprice.api.global.security.oauth.value.OAuthType;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserInsert {

    private OAuthType oAuthType;
    private String userKey;
    private String username;
    private String picture;
    private UserRole role;

    @Builder
    public UserInsert(OAuthType oAuthType, String userKey, String name, String picture, UserRole role) {
        this.oAuthType = oAuthType;
        this.userKey = userKey;
        this.username = name;
        this.picture = picture;
        this.role = role;
    }

    public User toEntity() {
        return User.builder()
                .oAuthType(oAuthType)
                .userKey(userKey)
                .username(username)
                .picture(picture)
                .role(role)
                .build();
    }
}
