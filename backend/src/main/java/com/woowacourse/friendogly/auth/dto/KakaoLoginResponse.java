package com.woowacourse.friendogly.auth.dto;

public record KakaoLoginResponse(boolean isRegistered, TokenResponse tokens) {

    public static KakaoLoginResponse ofRegistered(TokenResponse tokens) {
        return new KakaoLoginResponse(true, tokens);
    }

    public static KakaoLoginResponse ofNotRegistered() {
        return new KakaoLoginResponse(false, null);
    }
}
