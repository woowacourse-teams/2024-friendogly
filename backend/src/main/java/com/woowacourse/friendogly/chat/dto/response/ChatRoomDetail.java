package com.woowacourse.friendogly.chat.dto.response;

import com.woowacourse.friendogly.chat.domain.ChatRoom;

public record ChatRoomDetail(
        Long chatRoomId,
        int memberCount,
        String clubName
) {

    public ChatRoomDetail(ChatRoom chatRoom, String clubName) {
        this(
                chatRoom.getId(),
                chatRoom.countMembers(),
                clubName
        );
    }
}
