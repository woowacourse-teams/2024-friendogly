package com.woowacourse.friendogly.chat.dto.response;

import com.woowacourse.friendogly.member.domain.Member;

public record FindChatRoomMembersInfoResponse(
        boolean isOwner,
        Long memberId,
        String memberName,
        String memberProfileImageUrl
) {

    public FindChatRoomMembersInfoResponse(boolean isOwner, Member member) {
        this(isOwner, member.getId(), member.getName().getValue(), member.getImageUrl());
    }
}
