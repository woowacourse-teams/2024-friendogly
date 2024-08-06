package com.woowacourse.friendogly.auth.controller;

import com.woowacourse.friendogly.auth.dto.KakaoLoginResponse;
import com.woowacourse.friendogly.auth.dto.KakaoTokenResponse;
import com.woowacourse.friendogly.auth.dto.KakaoUserResponse;
import com.woowacourse.friendogly.auth.service.KakaoMemberService;
import com.woowacourse.friendogly.auth.service.KakaoOauthClient;
import com.woowacourse.friendogly.common.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final KakaoOauthClient kakaoOauthClient;
    private final KakaoMemberService kakaoMemberService;

    public AuthController(KakaoOauthClient kakaoOauthClient, KakaoMemberService kakaoMemberService) {
        this.kakaoOauthClient = kakaoOauthClient;
        this.kakaoMemberService = kakaoMemberService;
    }

    @GetMapping("/kakao/login")
    public ApiResponse<KakaoLoginResponse> login(@RequestParam String code) {
        KakaoTokenResponse token = kakaoOauthClient.getToken(code);
        KakaoUserResponse userInfo = kakaoOauthClient.getUserInfo(token.access_token());

        return ApiResponse.ofSuccess(kakaoMemberService.login(userInfo.id()));
    }
}
