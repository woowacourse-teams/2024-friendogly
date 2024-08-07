package com.woowacourse.friendogly.chat.dto.response;

import com.woowacourse.friendogly.chat.domain.ChatRoom;

public record ChatRoomDetail(
        Long chatRoomId,
        int memberCount,
        String clubName,
        String clubImageUrl
) {

    public ChatRoomDetail(ChatRoom chatRoom, String clubName, String clubImageUrl) {
        this(
                chatRoom.getId(),
                chatRoom.countMembers(),
                clubName,
                clubImageUrl
        );
    }
}
