package com.woowacourse.friendogly.auth.dto;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "kakao")
public record KakaoProperties(
        String grantType,
        String clientId,
        String redirectUri
) {

}
