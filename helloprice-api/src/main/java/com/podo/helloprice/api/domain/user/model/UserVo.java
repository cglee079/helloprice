package com.podo.helloprice.api.domain.user.model;

import com.podo.helloprice.api.global.security.oauth.value.OAuthType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.podo.helloprice.api.domain.user.model.UserRole.ADMIN;


@NoArgsConstructor
@Getter
@EqualsAndHashCode
public class UserVo {

    private OAuthType oAuthType;
    private String username;
    private String picture;
    private Boolean isAdmin;

    public static UserVo createByUser(User user) {
        final UserVo userVo = new UserVo();
        userVo.oAuthType = user.getOAuthType();
        userVo.username = user.getUsername();
        userVo.picture = user.getPicture();
        userVo.isAdmin = ADMIN.equals(user.getRole());
        return userVo;
    }
}
