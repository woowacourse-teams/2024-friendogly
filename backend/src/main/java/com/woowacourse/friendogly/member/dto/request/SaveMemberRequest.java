package com.woowacourse.friendogly.member.dto.request;

import jakarta.validation.constraints.NotBlank;

public record SaveMemberRequest(@NotBlank String name) {

}
