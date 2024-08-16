package com.happy.friendogly.notification.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateDeviceTokenRequest(
        @NotBlank(message = "빈 문자열이나 null을 입력할 수 없습니다.")
        String deviceToken
) {

}
