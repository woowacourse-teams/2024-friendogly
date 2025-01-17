package com.happy.friendogly.auth.controller;

import com.happy.friendogly.auth.Auth;
import com.happy.friendogly.auth.dto.KakaoLoginRequest;
import com.happy.friendogly.auth.dto.KakaoLoginResponse;
import com.happy.friendogly.auth.dto.KakaoRefreshRequest;
import com.happy.friendogly.auth.dto.TokenResponse;
import com.happy.friendogly.auth.service.AuthService;
import com.happy.friendogly.auth.service.KakaoMemberService;
import com.happy.friendogly.common.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/auth")
public class AuthController {
    // TODO: KakaoMemberService와 AuthService 통합
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

    @PostMapping("/kakao/logout")
    public ResponseEntity<Void> logout(@Auth Long memberId) {
        kakaoMemberService.logout(memberId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/kakao/refresh")
    public ApiResponse<TokenResponse> refreshToken(@RequestBody KakaoRefreshRequest request) {
        return ApiResponse.ofSuccess(authService.refreshToken(request.refreshToken()));
    }
}
