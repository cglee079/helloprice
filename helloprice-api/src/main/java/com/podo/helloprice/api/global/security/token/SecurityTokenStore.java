package com.podo.helloprice.api.global.security.token;

import com.podo.helloprice.api.domain.user.model.User;
import com.podo.helloprice.api.domain.user.model.UserRole;
import com.podo.helloprice.api.domain.user.model.UserVo;
import com.podo.helloprice.api.global.security.oauth.value.OAuthAuthentication;
import com.podo.helloprice.api.global.security.oauth.value.OAuthType;
import com.podo.helloprice.api.global.security.oauth.value.OAuthUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class SecurityTokenStore {

    private final SecurityTokenManager tokenManager;
    private final Map<String, Authentication> authentications = new HashMap<>();

    //TODO 임시
    private OAuthAuthentication authentication;

    public String login(Authentication authentication, UserVo userVo) {
        final String token = tokenManager.createToken(userVo);
        authentications.put(token, authentication);
        return token;
    }

    public Authentication getAuthentication(String token) {
        return authentication;
        // TODO 임시

//        if (tokenManager.authenticate(token)) {
//            return authentications.get(token);
//        }
//        return null;
    }

    public void logout(String accessToken) {
        authentications.remove(accessToken);
    }


    //TODO 임시
    @PostConstruct
    public void dd() {
        final long userId = 1L;
        final String userKey = "!";
        final OAuthType oAuthType = OAuthType.GITHUB;
        final String username = "bjs";
        final UserRole userRole = UserRole.USER;
        final String picture = "http://no.com";

        final User user = User.builder()
                .userKey(userKey)
                .oAuthType(oAuthType)
                .picture(picture)
                .role(userRole)
                .username(username)
                .build();

        final UserVo userVo = UserVo.createByUser(user);

        final OAuthUserDetails oAuthUserDetails = OAuthUserDetails.builder()
                .userId(userId)
                .oAuthType(oAuthType)
                .userKey(userKey)
                .username(username)
                .picture(picture)
                .authorities(Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + userRole.name())))
                .build();

        this.authentication = new OAuthAuthentication(oAuthUserDetails);
        final String token = this.login(authentication, userVo);

        System.out.println(token);
    }
}

