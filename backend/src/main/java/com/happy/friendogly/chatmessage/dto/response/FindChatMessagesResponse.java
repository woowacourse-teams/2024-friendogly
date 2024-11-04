package com.happy.friendogly.chatmessage.dto.response;

import com.happy.friendogly.chatmessage.domain.ChatMessage;
import com.happy.friendogly.chatsocket.domain.MessageType;
import java.time.LocalDateTime;

public record FindChatMessagesResponse(
        MessageType messageType,
        Long senderMemberId,
        String senderName,
        String content,
        LocalDateTime createdAt,
        String profilePictureUrl
) {

    public FindChatMessagesResponse(ChatMessage chatMessage) {
        this(
                chatMessage.getMessageType(),
                chatMessage.getSenderMember().getId(),
                chatMessage.getSenderMember().getName().getValue(),
                chatMessage.getContent(),
                chatMessage.getCreatedAt(),
                chatMessage.getSenderMember().getImageUrl()
        );
    }
}
