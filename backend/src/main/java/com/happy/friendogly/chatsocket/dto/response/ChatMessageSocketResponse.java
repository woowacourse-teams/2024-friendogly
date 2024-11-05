package com.happy.friendogly.chatsocket.dto.response;

import com.happy.friendogly.chatsocket.domain.MessageType;
import com.happy.friendogly.member.domain.Member;
import java.time.LocalDateTime;

public record ChatMessageSocketResponse(
        MessageType messageType,
        Long senderMemberId,
        String senderName,
        String content,
        LocalDateTime createdAt,
        String profilePictureUrl
) {

    public ChatMessageSocketResponse(
            MessageType messageType,
            String content,
            Member senderMember,
            LocalDateTime createdAt
    ) {
        this(
                messageType,
                senderMember.getId(),
                senderMember.getName().getValue(),
                content,
                createdAt,
                senderMember.getImageUrl()
        );
    }
}
