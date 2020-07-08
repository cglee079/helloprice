package com.podo.helloprice.api.global.security;

import com.podo.helloprice.api.global.security.oauth.value.OAuthUserDetails;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Objects;
import java.util.Optional;

public class SecurityUtil extends WebSecurityConfigurerAdapter {

    public static Optional<OAuthUserDetails> getUser() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (Objects.isNull(authentication)){
            return Optional.empty();
        }

        final Object principal = authentication.getPrincipal();

        if (Objects.isNull(principal) || principal instanceof String) {
            return Optional.empty();
        }

        return Optional.of((OAuthUserDetails) principal);
    }

    public static Long getUserId() {
        return SecurityUtil.getUser().map(OAuthUserDetails::getUserId).orElse(null);
    }

    public static String getUsername() {
        return SecurityUtil.getUser().map(OAuthUserDetails::getUsername).orElse(null);
    }

}
