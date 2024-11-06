package com.happy.friendogly.chatroom.dto.response;

import com.happy.friendogly.chatroom.domain.ChatRoom;
import java.time.LocalDateTime;

public record ChatRoomDetailV2(
        Long chatRoomId,
        int memberCount,
        String clubName,
        String clubImageUrl,
        String recentMessage,
        LocalDateTime recentMessageCreatedAt
) {

    public ChatRoomDetailV2(ChatRoom chatRoom, String clubName, String clubImageUrl,
            String recentMessage, LocalDateTime recentMessageCreatedAt) {
        this(
                chatRoom.getId(),
                chatRoom.countMembers(),
                clubName,
                clubImageUrl,
                recentMessage,
                recentMessageCreatedAt
        );
    }
}
