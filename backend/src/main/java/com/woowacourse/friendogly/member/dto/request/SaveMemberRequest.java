package com.woowacourse.friendogly.member.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SaveMemberRequest(
        @NotBlank(message = "빈 문자열이나 null을 입력할 수 없습니다.")
        @Size(max = 15, message = "닉네임은 1글자 이상 15글자 이하여야 합니다.")
        String name,

        @NotBlank(message = "빈 문자열이나 null을 입력할 수 없습니다.")
        @Email(message = "올바른 email 형식이 아닙니다.")
        String email
) {

}
