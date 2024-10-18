package com.happy.friendogly.playground.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UpdatePlaygroundMemberMessageRequest(

        @NotBlank(message = "상태메세지는 빈 문자열이나 null을 입력할 수 없습니다.")
        String message
) {

}
