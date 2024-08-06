package com.woowacourse.friendogly.auth.dto;

public record TokenResponse(String accessToken, String refreshToken) {
}
