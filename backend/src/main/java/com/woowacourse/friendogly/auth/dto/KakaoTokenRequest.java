package com.woowacourse.friendogly.auth.dto;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public class KakaoTokenRequest {

    private final MultiValueMap<String, String> body;

    public KakaoTokenRequest(KakaoProperties properties, String code) {
        body = new LinkedMultiValueMap<>();
        body.add("grant_type", properties.grantType());
        body.add("client_id", properties.clientId());
        body.add("redirect_uri", properties.redirectUri());
        body.add("code", code);
    }

    public MultiValueMap<String, String> getBody() {
        return body;
    }
}
