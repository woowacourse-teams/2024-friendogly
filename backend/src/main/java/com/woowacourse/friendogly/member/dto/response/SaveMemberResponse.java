package com.woowacourse.friendogly.member.dto.response;

import com.woowacourse.friendogly.auth.dto.TokenResponse;
import com.woowacourse.friendogly.member.domain.Member;

public record SaveMemberResponse(
        Long id,
        String name,
        String tag,
        String email,
        String imageUrl,
        TokenResponse tokens
) {

    public SaveMemberResponse(Member member, TokenResponse tokens) {
        this(
                member.getId(),
                member.getName().getValue(),
                member.getTag(),
                member.getEmail().getValue(),
                member.getImageUrl(),
                tokens
        );
    }
}
