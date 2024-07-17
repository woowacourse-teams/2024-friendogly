package com.woowacourse.friendogly.member.dto.response;

import com.woowacourse.friendogly.member.domain.Member;

public record SaveMemberResponse(Long id) {

    public static SaveMemberResponse from(Member member) {
        return new SaveMemberResponse(member.getId());
    }
}
