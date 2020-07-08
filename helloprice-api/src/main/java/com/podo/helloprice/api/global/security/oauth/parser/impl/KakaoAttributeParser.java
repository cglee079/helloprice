package com.podo.helloprice.api.global.security.oauth.parser.impl;

import com.podo.helloprice.api.global.security.oauth.parser.AttributesParser;
import com.podo.helloprice.api.global.security.oauth.value.OAuthAttributes;
import com.podo.helloprice.api.global.security.oauth.value.OAuthType;
import com.podo.helloprice.api.global.security.oauth.parser.util.ParserUtil;
import org.springframework.util.StringUtils;

import java.util.Map;

public class KakaoAttributeParser implements AttributesParser {

    private static final OAuthType OAUTH_TYPE = OAuthType.KAKAO;
    private static final String ID_KEY = "id";
    private static final String USER_DETAIL_WRAP_KEY = "kakao_account";
    private static final String USER_DETAIL_KEY = "profile";
    private static final String USERNAME_KEY = "nickname";
    private static final String PICTURE_KEY = "profile_image_url";
    private static final String DEFAULT_PICTURE = "https://www.podo-dev.com/user/default-kakao.png";

    @SuppressWarnings("unchecked")
    public OAuthAttributes parse(Map<String, Object> attributes) {
        final Map<String, Object> userDetailWrap = (Map<String, Object>) attributes.get(USER_DETAIL_WRAP_KEY);
        final Map<String, String> userDetail = (Map<String, String>) userDetailWrap.get(USER_DETAIL_KEY);

        final String id = ((Integer) attributes.get(ID_KEY)).toString();
        final String userKey = ParserUtil.encodeUserKey(OAUTH_TYPE, id);
        final String username = userDetail.get(USERNAME_KEY);
        final String picture = getPicture(userDetail.get(PICTURE_KEY), DEFAULT_PICTURE);

        return OAuthAttributes.builder()
                .oAuthType(OAUTH_TYPE)
                .userKey(userKey)
                .username(username)
                .picture(picture)
                .build();
    }

    private String getPicture(String pictureUrl, String defaultPicture) {
        if(StringUtils.hasText(pictureUrl)){
            return pictureUrl;
        }

        return defaultPicture;
    }
}
