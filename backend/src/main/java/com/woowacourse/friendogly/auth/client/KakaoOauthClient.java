package com.woowacourse.friendogly.auth.client;

import com.woowacourse.friendogly.auth.dto.KakaoTokenRequest;
import com.woowacourse.friendogly.auth.dto.KakaoTokenResponse;
import com.woowacourse.friendogly.auth.dto.RealKakaoTokenRequest;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class KakaoOauthClient {

    private final RestClient restClient;

    public KakaoOauthClient() {
        this.restClient = RestClient.create();
    }

    public KakaoTokenResponse getToken(KakaoTokenRequest request) {
        RealKakaoTokenRequest realKakaoTokenRequest = new RealKakaoTokenRequest(request);
        
        return restClient.post()
                .uri("https://kauth.kakao.com/oauth/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(realKakaoTokenRequest)
                .retrieve()
                .body(KakaoTokenResponse.class);
    }
}
