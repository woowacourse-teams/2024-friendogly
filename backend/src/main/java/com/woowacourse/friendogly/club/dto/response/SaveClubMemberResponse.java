package com.woowacourse.friendogly.club.dto.response;

import com.woowacourse.friendogly.club.domain.ClubMember;

public record SaveClubMemberResponse(
        Long clubMemberId
) {

    public SaveClubMemberResponse(ClubMember clubMember) {
        this(clubMember.getId());
    }
}
