package com.podo.helloprice.api.global.security.oauth.parser.impl;

import com.podo.helloprice.api.global.security.oauth.parser.AttributesParser;
import com.podo.helloprice.api.global.security.oauth.value.OAuthAttributes;
import com.podo.helloprice.api.global.security.oauth.value.OAuthType;
import com.podo.helloprice.api.global.security.oauth.parser.util.ParserUtil;
import com.podo.helloprice.api.global.util.HashUtil;

import java.util.Map;

public class NaverAttriButesParser implements AttributesParser {

    private static final OAuthType OAUTH_TYPE = OAuthType.NAVER;
    private static final String USER_DETAIL_KEY = "response";
    private static final String ID_KEY = "id";
    private static final String USERNAME_KEY = "name";
    private static final String PICTURE_KEY = "profile_image";

    public OAuthAttributes parse(Map<String, Object> attributes) {
        @SuppressWarnings("unchecked")
        final Map<String, String> response = (Map<String, String>) attributes.get(USER_DETAIL_KEY);

        final String id = response.get(ID_KEY);
        final String userKey = HashUtil.hash(ParserUtil.encodeUserKey(OAUTH_TYPE, id));
        final String username = response.get(USERNAME_KEY);
        final String picture = response.get(PICTURE_KEY);

        return OAuthAttributes.builder()
                .oAuthType(OAUTH_TYPE)
                .userKey(userKey)
                .username(username)
                .picture(picture)
                .build();
    }
}
