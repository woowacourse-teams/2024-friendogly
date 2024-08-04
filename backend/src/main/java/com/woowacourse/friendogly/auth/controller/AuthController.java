package com.woowacourse.friendogly.auth.controller;

import com.woowacourse.friendogly.auth.dto.KakaoTokenResponse;
import com.woowacourse.friendogly.auth.dto.KakaoUserResponse;
import com.woowacourse.friendogly.auth.service.KakaoMemberService;
import com.woowacourse.friendogly.auth.service.KakaoOauthService;
import com.woowacourse.friendogly.common.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final KakaoOauthService kakaoOauthService;
    private final KakaoMemberService kakaoMemberService;

    public AuthController(KakaoOauthService kakaoOauthService, KakaoMemberService kakaoMemberService) {
        this.kakaoOauthService = kakaoOauthService;
        this.kakaoMemberService = kakaoMemberService;
    }

    @GetMapping("/kakao/login")
    public ApiResponse<KakaoTokenResponse> login(@RequestParam String code) {
        KakaoTokenResponse token = kakaoOauthService.getToken(code);
        KakaoUserResponse userInfo = kakaoOauthService.getUserInfo(token.access_token());
        kakaoMemberService.save(userInfo.id());

        return ApiResponse.ofSuccess(token);
    }
}
