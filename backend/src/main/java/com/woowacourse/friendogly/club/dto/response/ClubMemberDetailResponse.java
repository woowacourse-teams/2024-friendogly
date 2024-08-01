package com.woowacourse.friendogly.club.dto.response;

import com.woowacourse.friendogly.member.domain.Member;

public record ClubMemberDetailResponse(
        Long id,
        String name,
        String imageUrl
) {

    public ClubMemberDetailResponse(Member member) {
        this(member.getId(), member.getName().getValue(), member.getImageUrl());
    }
}
