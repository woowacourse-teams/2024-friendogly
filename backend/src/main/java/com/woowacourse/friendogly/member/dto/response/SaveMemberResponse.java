package com.woowacourse.friendogly.member.dto.response;

import com.woowacourse.friendogly.member.domain.Member;

public record SaveMemberResponse(Long id, String name, String tag, String email) {

    public SaveMemberResponse(Member member) {
        this(
                member.getId(),
                member.getName().getValue(),
                member.getTag(),
                member.getEmail().getValue()
        );
    }
}
