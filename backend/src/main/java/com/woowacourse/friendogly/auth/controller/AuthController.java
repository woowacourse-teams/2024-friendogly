package com.woowacourse.friendogly.auth.controller;

import com.woowacourse.friendogly.auth.dto.KakaoTokenResponse;
import com.woowacourse.friendogly.auth.service.KakaoOauthService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final KakaoOauthService kakaoOauthService;

    public AuthController(KakaoOauthService kakaoOauthService) {
        this.kakaoOauthService = kakaoOauthService;
    }

    @GetMapping("/kakao/login")
    public KakaoTokenResponse getToken(@RequestParam String code) {
        return kakaoOauthService.getToken(code);
    }
}
