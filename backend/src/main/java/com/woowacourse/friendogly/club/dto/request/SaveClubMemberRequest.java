package com.woowacourse.friendogly.club.dto.request;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record SaveClubMemberRequest(
        @NotEmpty(message = "모임에 참석 할 강아지를 1마리 이상 선택해주세요.")
        List<Long> participatingPetsId
) {

}
