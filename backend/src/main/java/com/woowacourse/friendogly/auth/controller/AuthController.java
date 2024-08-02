package com.woowacourse.friendogly.auth.controller;

import com.woowacourse.friendogly.auth.client.KakaoOauthClient;
import com.woowacourse.friendogly.auth.dto.KakaoTokenRequest;
import com.woowacourse.friendogly.auth.dto.KakaoTokenResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final KakaoOauthClient client;

    public AuthController(KakaoOauthClient client) {
        this.client = client;
    }

    @GetMapping("/kakao/login")
    public KakaoTokenResponse getToken(KakaoTokenRequest request) {
        return client.getToken(request);
    }
}
