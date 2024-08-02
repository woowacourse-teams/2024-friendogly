package com.woowacourse.friendogly.auth.dto;

public record KakaoTokenRequest(
        String code,
        String error,
        String error_description,
        String state
) {
}
