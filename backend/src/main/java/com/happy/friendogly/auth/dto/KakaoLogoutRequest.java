package com.happy.friendogly.auth.dto;

public record KakaoLogoutRequest(String target_id_type, Long target_id) {

    public KakaoLogoutRequest(Long target_id) {
        this("user_id", target_id);
    }
}
