package com.woowacourse.friendogly.member.dto.response;

import com.woowacourse.friendogly.member.domain.Member;

public record SaveMemberResponse(Long id, String name, String email) {

    public SaveMemberResponse(Member member) {
        this(member.getId(), member.getName().getValue(), member.getEmail().getValue());
    }
}
