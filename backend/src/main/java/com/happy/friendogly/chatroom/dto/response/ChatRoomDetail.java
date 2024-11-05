package com.happy.friendogly.chatroom.dto.response;

import com.happy.friendogly.chatroom.domain.ChatRoom;

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
