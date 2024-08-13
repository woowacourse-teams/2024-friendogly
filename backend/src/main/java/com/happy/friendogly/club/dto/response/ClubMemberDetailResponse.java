package com.happy.friendogly.club.dto.response;

import com.happy.friendogly.member.domain.Member;

public record ClubMemberDetailResponse(
        Long id,
        String name,
        String imageUrl
) {

    public ClubMemberDetailResponse(Member member) {
        this(member.getId(), member.getName().getValue(), member.getImageUrl());
    }
}
