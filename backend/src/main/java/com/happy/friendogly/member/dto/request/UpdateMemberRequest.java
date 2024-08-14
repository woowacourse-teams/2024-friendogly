package com.happy.friendogly.member.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateMemberRequest(
        @NotBlank(message = "name은 빈 문자열이나 null을 입력할 수 없습니다.")
        @Size(max = 15, message = "닉네임은 1글자 이상 8글자 이하여야 합니다.")
        String name,

        @NotBlank(message = "imageUpdateType은 빈 문자열이나 null을 입력할 수 없습니다.")
        String imageUpdateType
) {

}
