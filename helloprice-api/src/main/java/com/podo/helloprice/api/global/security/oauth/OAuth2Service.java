package com.podo.helloprice.api.global.security.oauth;

import com.podo.helloprice.api.domain.user.application.UserReadService;
import com.podo.helloprice.api.domain.user.application.UserPersistService;
import com.podo.helloprice.api.domain.user.dto.UserInsert;
import com.podo.helloprice.api.domain.user.model.UserVo;
import com.podo.helloprice.api.global.security.oauth.parser.AttributesParser;
import com.podo.helloprice.api.global.security.oauth.parser.impl.*;
import com.podo.helloprice.api.global.security.oauth.value.OAuthAttributes;
import com.podo.helloprice.api.global.security.oauth.value.OAuthAuthentication;
import com.podo.helloprice.api.global.security.oauth.value.OAuthType;
import com.podo.helloprice.api.global.security.oauth.value.OAuthUserDetails;
import com.podo.helloprice.api.global.security.token.SecurityTokenStore;
import com.podo.helloprice.core.enums.RequestHeader;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.podo.helloprice.api.domain.user.model.UserRole.USER;
import static com.podo.helloprice.api.global.security.oauth.value.OAuthType.*;


@RequiredArgsConstructor
@Service
public class OAuth2Service implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private static final Map<OAuthType, AttributesParser> OAUTH_TYPE_TO_ATTRIBUTES_PARSER = new HashMap<>();

    static {
        OAUTH_TYPE_TO_ATTRIBUTES_PARSER.put(GITHUB, new GithubAttributesParser());
        OAUTH_TYPE_TO_ATTRIBUTES_PARSER.put(GOOGLE, new GoogleAttributesParser());
        OAUTH_TYPE_TO_ATTRIBUTES_PARSER.put(FACEBOOK, new FacebookAttributesParser());
        OAUTH_TYPE_TO_ATTRIBUTES_PARSER.put(KAKAO, new KakaoAttributeParser());
        OAUTH_TYPE_TO_ATTRIBUTES_PARSER.put(NAVER, new NaverAttriButesParser());
    }

    private final HttpServletResponse httpServletResponse;
    private final UserReadService userReadService;
    private final UserPersistService userPersistService;
    private final SecurityTokenStore securityTokenStore;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        final OAuth2User oAuth2User = new DefaultOAuth2UserService().loadUser(userRequest);

        final String registrationId = userRequest.getClientRegistration().getRegistrationId();

        final OAuthAttributes attributes = getOAuthAttributes(oAuth2User, registrationId);

        final Long userId = userPersistService.persist(createUserInsertDto(attributes));
        final OAuthUserDetails oAuthUserDetails = createOAuthUserDetails(userId, attributes);

        final UserVo userVo = userReadService.getUser(userId);
        final String accessToken = securityTokenStore.login(new OAuthAuthentication(oAuthUserDetails), userVo);

        httpServletResponse.setHeader(RequestHeader.ACCESS_TOKEN.value(), accessToken);

        return new DefaultOAuth2User(attributes.getAuthorities(), Collections.singletonMap("mock", "mock"), "mock");
    }

    private OAuthAttributes getOAuthAttributes(OAuth2User oAuth2User, String registrationId) {
        final OAuthAttributes attributes = OAUTH_TYPE_TO_ATTRIBUTES_PARSER.get(OAuthType.valueOfIgnoreCase(registrationId)).parse(oAuth2User.getAttributes());

        attributes.setRole(userReadService.getRoleByKey(attributes.getUserKey()).orElse(USER));

        return attributes;
    }

    private UserInsert createUserInsertDto(OAuthAttributes attributes) {
        return UserInsert.builder()
                .oAuthType(attributes.getOAuthType())
                .userKey(attributes.getUserKey())
                .name(attributes.getUsername())
                .picture(attributes.getPicture())
                .role(attributes.getRole())
                .build();
    }

    private OAuthUserDetails createOAuthUserDetails(Long userId, OAuthAttributes attributes) {
        return OAuthUserDetails.builder()
                .userId(userId)
                .oAuthType(attributes.getOAuthType())
                .userKey(attributes.getUserKey())
                .username(attributes.getUsername())
                .picture(attributes.getPicture())
                .authorities(attributes.getAuthorities())
                .build();
    }
}
