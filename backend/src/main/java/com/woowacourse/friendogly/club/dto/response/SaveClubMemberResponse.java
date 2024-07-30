package com.woowacourse.friendogly.club.dto.response;

import com.woowacourse.friendogly.member.domain.Member;

public record SaveClubMemberResponse(
        Long memberId
) {

    public SaveClubMemberResponse(Member member) {
        this(member.getId());
    }
}
