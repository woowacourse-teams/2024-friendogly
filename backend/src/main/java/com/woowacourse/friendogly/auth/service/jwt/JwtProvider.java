package com.woowacourse.friendogly.auth.service.jwt;

import com.woowacourse.friendogly.auth.dto.TokenResponse;
import com.woowacourse.friendogly.exception.FriendoglyException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import java.util.UUID;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@EnableConfigurationProperties(JwtProperties.class)
public class JwtProvider {

    private final JwtProperties jwtProperties;

    public JwtProvider(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    public TokenResponse generateTokens(TokenPayload payload) {
        return new TokenResponse(generateAccessToken(payload), generateRefreshToken());
    }

    private String generateAccessToken(TokenPayload payload) {
        Date expirationTime = new Date(System.currentTimeMillis() + jwtProperties.accessExpirationTime());
        return generateToken(payload.memberId().toString(), expirationTime);
    }

    private String generateRefreshToken() {
        Date expirationTime = new Date(System.currentTimeMillis() + jwtProperties.refreshExpirationTime());
        return generateToken(UUID.randomUUID().toString(), expirationTime);
    }

    private String generateToken(String subject, Date expirationTime) {
        return Jwts.builder()
                .subject(subject)
                .expiration(expirationTime)
                .signWith(Keys.hmacShaKeyFor(jwtProperties.secretKey().getBytes()))
                .compact();
    }

    public String validateAndExtract(String token) {
        validateToken(token);
        return getPayload(token).getSubject();
    }

    private void validateToken(String token) {
        Date expirationTime = getPayload(token).getExpiration();

        if (expirationTime.before(new Date())) {
            throw new FriendoglyException("토큰이 만료 되었습니다.", HttpStatus.UNAUTHORIZED);
        }
    }

    private Claims getPayload(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(jwtProperties.secretKey().getBytes()))
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (JwtException | IllegalArgumentException e) {
            throw new FriendoglyException("잘못된 토큰 값입니다. 다시 로그인해주세요", HttpStatus.UNAUTHORIZED);
        }
    }
}
