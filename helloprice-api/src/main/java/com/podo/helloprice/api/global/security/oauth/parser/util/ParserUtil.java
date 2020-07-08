package com.podo.helloprice.api.global.security.oauth.parser.util;

import com.podo.helloprice.api.global.security.oauth.value.OAuthType;
import com.podo.helloprice.api.global.util.HashUtil;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ParserUtil {

    public String encodeUserKey(OAuthType oAuthType, String id){
        return HashUtil.hash(oAuthType.name(), id);
    }
}
