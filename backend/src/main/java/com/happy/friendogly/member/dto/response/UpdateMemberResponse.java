package com.happy.friendogly.member.dto.response;

import com.happy.friendogly.member.domain.Member;

public record UpdateMemberResponse(
        Long id,
        String name,
        String tag,
        String imageUrl
) {

    public UpdateMemberResponse(Member member) {
        this(
                member.getId(),
                member.getName().getValue(),
                member.getTag(),
                member.getImageUrl()
        );
    }
}
