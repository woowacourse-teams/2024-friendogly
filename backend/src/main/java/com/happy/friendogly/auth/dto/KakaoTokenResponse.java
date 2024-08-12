package com.happy.friendogly.auth.dto;

public record KakaoTokenResponse(
        String token_type,
        String access_token,
        Integer expires_in,
        String refresh_token,
        Integer refresh_token_expires_in
) {

}
