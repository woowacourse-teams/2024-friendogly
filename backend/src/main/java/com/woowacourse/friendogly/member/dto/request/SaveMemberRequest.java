package com.woowacourse.friendogly.member.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SaveMemberRequest(
        @NotBlank @Size(max = 15) String name,
        @NotBlank @Email String email
) {

}
