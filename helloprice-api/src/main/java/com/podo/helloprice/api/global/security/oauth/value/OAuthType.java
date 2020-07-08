package com.podo.helloprice.api.global.security.oauth.value;

import com.podo.helloprice.api.global.security.oauth.exception.InvalidOAuthTypeException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum OAuthType {

    GOOGLE,
    FACEBOOK,
    KAKAO,
    GITHUB,
    NAVER;

    public static OAuthType valueOfIgnoreCase(String value){
        for (OAuthType oAuthType : OAuthType.values()) {
            if(oAuthType.name().equalsIgnoreCase(value)){
                return oAuthType;
            }
        }

        throw new InvalidOAuthTypeException(value);
    }
}
