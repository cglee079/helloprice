package com.podo.helloprice.api.domain.user.model;

import com.podo.helloprice.api.domain.BaseEntity;
import com.podo.helloprice.api.global.security.oauth.value.OAuthType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user")
@Entity
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userKey;

    private String username;

    private String picture;

    private UserRole role;

    private OAuthType oAuthType;

    @Builder
    public User(String userKey, String username, String picture, UserRole role, OAuthType oAuthType) {
        this.userKey = userKey;
        this.username = username;
        this.picture = picture;
        this.role = role;
        this.oAuthType = oAuthType;
    }
}
