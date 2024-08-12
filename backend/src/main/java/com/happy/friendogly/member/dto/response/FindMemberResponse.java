package com.happy.friendogly.member.dto.response;

import com.happy.friendogly.member.domain.Member;

public record FindMemberResponse(
        Long id,
        String name,
        String tag,
        String email,
        String imageUrl
) {

    public FindMemberResponse(Member member) {
        this(
                member.getId(),
                member.getName().getValue(),
                member.getTag(),
                member.getEmail().getValue(),
                member.getImageUrl()
        );
    }
}
