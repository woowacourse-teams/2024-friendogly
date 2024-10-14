package com.happy.friendogly.club.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record DeleteKickedMemberRequest(
        @NotNull(message = "강퇴 대상인 회원ID 필수입니다.")
        @Positive(message = "회원ID는 양수 입니다.")
        Long kickedMemberId
) {

}
