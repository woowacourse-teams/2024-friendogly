package com.happy.friendogly.member.dto.response;

import com.happy.friendogly.auth.dto.TokenResponse;
import com.happy.friendogly.member.domain.Member;

public record SaveMemberResponse(
        Long id,
        String name,
        String tag,
        String imageUrl,
        TokenResponse tokens
) {

    public SaveMemberResponse(Member member, TokenResponse tokens) {
        this(
                member.getId(),
                member.getName().getValue(),
                member.getTag(),
                member.getImageUrl(),
                tokens
        );
    }
}
