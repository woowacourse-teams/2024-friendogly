package com.woowacourse.friendogly.club.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record SaveClubMemberRequest(

        @NotNull(message = "모임 정보는 필수입니다.")
        @Positive(message = "올바르지 않는 모임 정보 입니다.")
        Long clubId
) {
}
