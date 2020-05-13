package com.podo.helloprice.api.global.security.token;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.podo.helloprice.api.domain.user.model.UserVo;
import com.podo.helloprice.api.global.util.JsonMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.JacksonSerializer;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Component
public class SecurityTokenManager {

    private static final JacksonSerializer<Map<String, ?>> SERIALIZER = new JacksonSerializer<>(new ObjectMapper());

    @Value("${security.expire.hour}")
    private final Integer expireHour;

    @Value("${security.secret-key}")
    private final String secretKey;

    public String createToken(UserVo userVo) {
        return Jwts.builder()
                .serializeToJsonWith(SERIALIZER)
                .setSubject(JsonMapper.toJson(userVo))
                .setExpiration(new Date(System.currentTimeMillis() + (expireHour * 60 * 60 * 1000)))
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();
    }

    public boolean authenticate(String token) {
        try {

            Jwts.parser()
                    .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                    .parseClaimsJws(token);

            return true;

        } catch (Exception e) {

            return false;
        }
    }
}
