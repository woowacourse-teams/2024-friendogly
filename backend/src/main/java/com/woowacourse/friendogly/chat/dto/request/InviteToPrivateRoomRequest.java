package com.woowacourse.friendogly.chat.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record InviteToPrivateRoomRequest(
        @NotNull(message = "Member ID는 null일 수 없습니다.")
        @Positive(message = "Member ID는 양수만 입력 가능합니다.")
        Long receiverMemberId
) {

}
