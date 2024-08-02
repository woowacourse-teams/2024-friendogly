package com.woowacourse.friendogly.chat.dto.response;

import com.woowacourse.friendogly.member.domain.Member;

public record FindChatRoomMembersInfoResponse(
        Long memberId,
        String memberName,
        String memberProfileImageUrl
) {

    public FindChatRoomMembersInfoResponse(Member member) {
        this(member.getId(), member.getName().getValue(), member.getImageUrl());
    }
}
