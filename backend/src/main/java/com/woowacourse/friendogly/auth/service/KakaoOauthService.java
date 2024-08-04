package com.woowacourse.friendogly.auth.service;

import com.woowacourse.friendogly.auth.dto.KakaoProperties;
import com.woowacourse.friendogly.auth.dto.KakaoTokenRequest;
import com.woowacourse.friendogly.auth.dto.KakaoTokenResponse;
import com.woowacourse.friendogly.auth.dto.KakaoUserResponse;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
@EnableConfigurationProperties(KakaoProperties.class)
public class KakaoOauthService {

    private static final String KAKAO_REQUEST_TOKEN_URI = "https://kauth.kakao.com/oauth/token";
    //    private static final String KAKAO_REQUEST_USER_INFO_URI = "https://kapi.kakao.com/v2/user/me?secure_resource=true";
    private static final String KAKAO_REQUEST_USER_INFO_URI = "https://kapi.kakao.com/v2/user/me";

    private final RestClient restClient;
    private final KakaoProperties kakaoProperties;
    private final AuthErrorHandler errorHandler;

    public KakaoOauthService(
            KakaoProperties kakaoProperties,
            AuthErrorHandler errorHandler
    ) {
        this.restClient = RestClient.create();
        this.kakaoProperties = kakaoProperties;
        this.errorHandler = errorHandler;
    }

    public KakaoTokenResponse getToken(String code) {
        KakaoTokenRequest request = new KakaoTokenRequest(kakaoProperties, code);

        return restClient.post()
                .uri(KAKAO_REQUEST_TOKEN_URI)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(request.getBody())
                .retrieve()
                .onStatus(errorHandler)
                .body(KakaoTokenResponse.class);
    }

    public KakaoUserResponse getUserInfo(String accessToken) {
        return restClient.post()
                .uri(KAKAO_REQUEST_USER_INFO_URI)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .retrieve()
                .onStatus(errorHandler)
                .body(KakaoUserResponse.class);
    }
}
