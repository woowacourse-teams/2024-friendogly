package com.happy.friendogly.chatroom.dto.response;

import com.happy.friendogly.member.domain.Member;

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
