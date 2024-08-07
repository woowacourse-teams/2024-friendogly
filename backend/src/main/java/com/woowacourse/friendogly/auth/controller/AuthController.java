package com.woowacourse.friendogly.auth.controller;

import com.woowacourse.friendogly.auth.dto.KakaoLoginRequest;
import com.woowacourse.friendogly.auth.dto.KakaoLoginResponse;
import com.woowacourse.friendogly.auth.dto.KakaoRefreshRequest;
import com.woowacourse.friendogly.auth.dto.TokenResponse;
import com.woowacourse.friendogly.auth.service.AuthService;
import com.woowacourse.friendogly.auth.service.KakaoMemberService;
import com.woowacourse.friendogly.common.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final KakaoMemberService kakaoMemberService;
    private final AuthService authService;

    public AuthController(
            KakaoMemberService kakaoMemberService,
            AuthService authService
    ) {
        this.kakaoMemberService = kakaoMemberService;
        this.authService = authService;
    }

    @PostMapping("/kakao/login")
    public ApiResponse<KakaoLoginResponse> login(@RequestBody KakaoLoginRequest request) {
        return ApiResponse.ofSuccess(kakaoMemberService.login(request));
    }

    @GetMapping("/kakao/refresh")
    public ApiResponse<TokenResponse> refreshToken(@RequestBody KakaoRefreshRequest request) {
        return ApiResponse.ofSuccess(authService.refreshToken(request.refreshToken()));
    }
}
