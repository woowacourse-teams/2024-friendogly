package com.woowacourse.friendogly.auth.service;

import com.woowacourse.friendogly.auth.dto.KakaoUserResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class KakaoOauthClient {

    private static final String KAKAO_REQUEST_USER_INFO_URI = "https://kapi.kakao.com/v2/user/me";
    private static final String BEARER = "Bearer ";

    private final RestClient restClient;
    private final AuthErrorHandler errorHandler;

    public KakaoOauthClient(AuthErrorHandler errorHandler) {
        this.restClient = RestClient.create();
        this.errorHandler = errorHandler;
    }

    // TODO: 타임아웃 설정
    public KakaoUserResponse getUserInfo(String accessToken) {
        return restClient.post()
                .uri(KAKAO_REQUEST_USER_INFO_URI)
                .header(HttpHeaders.AUTHORIZATION, BEARER + accessToken)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .retrieve()
                .onStatus(errorHandler)
                .body(KakaoUserResponse.class);
    }
}
