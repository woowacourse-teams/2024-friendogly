package com.happy.friendogly.footprint.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateWalkStatusManualRequest(

        @NotBlank(message = "walkStatus는 빈 문자열이나 null을 입력할 수 없습니다.")
        String walkStatus
) {
}
