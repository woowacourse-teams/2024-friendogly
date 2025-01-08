package com.happy.friendogly.chatroom.dto.response;

import com.happy.friendogly.chatroom.domain.ChatRoom;
import java.time.LocalDateTime;

public record ChatRoomDetailV2(
        Long chatRoomId,
        int memberCount,
        String title,
        String imageUrl,
        String recentMessage,
        LocalDateTime recentMessageCreatedAt
) {

    public ChatRoomDetailV2(ChatRoom chatRoom, String title, String imageUrl,
            String recentMessage, LocalDateTime recentMessageCreatedAt) {
        this(
                chatRoom.getId(),
                chatRoom.countMembers(),
                title,
                imageUrl,
                recentMessage,
                recentMessageCreatedAt
        );
    }
}
