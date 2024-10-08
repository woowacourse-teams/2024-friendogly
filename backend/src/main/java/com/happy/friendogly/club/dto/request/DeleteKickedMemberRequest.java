package com.happy.friendogly.club.dto.request;

import jakarta.validation.constraints.Positive;

public record DeleteKickedMemberRequest(
        @Positive(message = "강퇴 대상인 회원ID 필수입니다.")
        Long kickedMemberId
) {

}
