package com.woowacourse.friendogly.auth.dto;

public record RealKakaoTokenRequest(
        String grant_type,
        String client_id,
        String redirect_uri,
        String code
) {

    public RealKakaoTokenRequest(KakaoTokenRequest request) {
        this(
                "authorization_code",
                "3c016b5effca91a6d18c9216bdd9c1a5",
                "http://localhost:8080/auth/kakao/login",
                request.code()
        );
    }
}
