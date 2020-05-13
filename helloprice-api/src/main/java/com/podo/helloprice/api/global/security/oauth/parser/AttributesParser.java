package com.podo.helloprice.api.global.security.oauth.parser;

import com.podo.helloprice.api.global.security.oauth.value.OAuthAttributes;

import java.util.Map;

public interface AttributesParser {

    OAuthAttributes parse(Map<String, Object> attributes);
}
