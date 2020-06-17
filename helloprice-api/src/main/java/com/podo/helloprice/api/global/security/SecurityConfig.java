package com.podo.helloprice.api.global.security;

import com.podo.helloprice.api.global.security.filter.CorsFilter;
import com.podo.helloprice.api.global.security.filter.TokenAuthFilter;
import com.podo.helloprice.api.global.security.oauth.OAuth2Service;
import com.podo.helloprice.api.global.security.token.SecurityTokenStore;
import com.podo.helloprice.core.enums.RequestHeader;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.SecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.session.SessionManagementFilter;

import static com.podo.helloprice.api.domain.user.model.UserRole.ADMIN;
import static com.podo.helloprice.api.domain.user.model.UserRole.USER;


@RequiredArgsConstructor
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${security.login.success.url}")
    private String longSuccessUrl;

    private final OAuth2Service oauth2Service;
    private final SecurityTokenStore securityTokenStore;

    @Override
    public void configure(WebSecurity web) {
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().disable();
        http.csrf().disable();
        http.logout().disable();
        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.authorizeRequests().expressionHandler(expressionHandler());

        http.authorizeRequests().antMatchers("/api/v0/**").hasRole(USER.name());

        http.addFilterBefore(new CorsFilter(), SessionManagementFilter.class);
        http.addFilterBefore(new TokenAuthFilter(securityTokenStore), BasicAuthenticationFilter.class);

        http.oauth2Login()
                .successHandler((req, res, auth) -> res.sendRedirect(longSuccessUrl + "?accessToken=" + res.getHeader(RequestHeader.ACCESS_TOKEN.value())))
                .userInfoEndpoint().userService(oauth2Service);
    }

    public SecurityExpressionHandler<FilterInvocation> expressionHandler() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        roleHierarchy.setHierarchy(String.format("ROLE_%s > ROLE_%s", ADMIN.name(), USER.name()));

        DefaultWebSecurityExpressionHandler handler = new DefaultWebSecurityExpressionHandler();

        handler.setRoleHierarchy(roleHierarchy);

        return handler;
    }

}

