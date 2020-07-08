package com.podo.helloprice.api.global.security.filter;

import com.podo.helloprice.api.global.security.token.SecurityTokenStore;
import com.podo.helloprice.core.enums.RequestHeader;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Objects;

@RequiredArgsConstructor
public class TokenAuthFilter implements Filter {

    public static final String AUTH_HEADER_VALUE_PREFIX = "Bearer";

    private final SecurityTokenStore securityTokenStore;

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain) throws IOException, ServletException {
        final HttpServletRequest httpRequest = (HttpServletRequest) request;

        String accessToken = httpRequest.getHeader(RequestHeader.AUTHORIZATION.name());

        if (Objects.nonNull(accessToken) ) {
            accessToken = accessToken.replace(AUTH_HEADER_VALUE_PREFIX, "");
            accessToken = accessToken.trim();

            Authentication authentication = securityTokenStore.getAuthentication(accessToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        chain.doFilter(request, response);
    }

}
