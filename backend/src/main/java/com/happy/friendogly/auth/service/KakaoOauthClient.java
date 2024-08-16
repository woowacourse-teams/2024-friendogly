package com.happy.friendogly.auth.service;

import com.happy.friendogly.auth.dto.KakaoLogoutRequest;
import com.happy.friendogly.auth.dto.KakaoUserResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class KakaoOauthClient {

    private static final String KAKAO_REQUEST_USER_INFO_URI = "https://kapi.kakao.com/v2/user/me";
    private static final String KAKAO_REQUEST_LOGOUT_URI = "https://kapi.kakao.com/v1/user/logout";
    private static final String BEARER = "Bearer ";
    private static final String KAKAO_AK = "KakaoAk ";

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

    // TODO: 타임아웃 설정
    public void logout(KakaoLogoutRequest request) {
        restClient.post()
                .uri(KAKAO_REQUEST_LOGOUT_URI)
                .header(HttpHeaders.AUTHORIZATION, KAKAO_AK + "admin key!!")    // TODO: admin key 주입
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(request)
                .retrieve()
                .onStatus(errorHandler);
    }
}
