package com.podo.helloprice.api.global.security.oauth.parser.impl;

import com.podo.helloprice.api.global.security.oauth.parser.AttributesParser;
import com.podo.helloprice.api.global.security.oauth.value.OAuthAttributes;
import com.podo.helloprice.api.global.security.oauth.value.OAuthType;
import com.podo.helloprice.api.global.security.oauth.parser.util.ParserUtil;

import java.util.Map;

public class GithubAttributesParser implements AttributesParser {

    private static final OAuthType OAUTH_TYPE = OAuthType.GITHUB;
    private static final String USERNAME_KEY = "login";
    private static final String ID_KEY = "id";
    private static final String PICTURE_KEY = "avatar_url";

    @Override
    public OAuthAttributes parse(Map<String, Object> attributes) {
        final String picture = (String) attributes.get(PICTURE_KEY);
        final String id = ((Integer) attributes.get(ID_KEY)).toString();
        final String username = (String) attributes.get(USERNAME_KEY);

        final String userKey = ParserUtil.encodeUserKey(OAUTH_TYPE, id);

        return OAuthAttributes.builder()
                .oAuthType(OAUTH_TYPE)
                .userKey(userKey)
                .username(username)
                .picture(picture)
                .build();
    }
}
