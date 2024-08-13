package com.happy.friendogly.auth.service.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt")
public record JwtProperties(String secretKey, Long accessExpirationTime, Long refreshExpirationTime) {

}
