package com.happy.friendogly.chat.dto.response;

import com.happy.friendogly.chat.domain.ChatRoom;

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
