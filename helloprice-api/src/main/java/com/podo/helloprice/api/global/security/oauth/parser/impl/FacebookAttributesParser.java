package com.podo.helloprice.api.global.security.oauth.parser.impl;

import com.podo.helloprice.api.global.security.oauth.parser.AttributesParser;
import com.podo.helloprice.api.global.security.oauth.value.OAuthAttributes;
import com.podo.helloprice.api.global.security.oauth.value.OAuthType;
import com.podo.helloprice.api.global.security.oauth.parser.util.ParserUtil;

import java.util.Map;

public class FacebookAttributesParser implements AttributesParser {

    private static final OAuthType OAUTH_TYPE = OAuthType.FACEBOOK;
    private static final String ID_KEY = "id";
    private static final String USERNAME_KEY = "name";
    private static final String PICTURE_FORMAT = "https://graph.facebook.com/%s/picture";

    public OAuthAttributes parse(Map<String, Object> attributes) {
        final String id = (String) attributes.get(ID_KEY);
        final String userKey = ParserUtil.encodeUserKey(OAUTH_TYPE, id);
        final String username = (String) attributes.get(USERNAME_KEY);
        final String picture = String.format(PICTURE_FORMAT, id);

        return OAuthAttributes.builder()
                .oAuthType(OAUTH_TYPE)
                .userKey(userKey)
                .username(username)
                .picture(picture)
                .build();

    }
}
