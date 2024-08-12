package com.happy.friendogly.chat.dto.response;

import com.happy.friendogly.chat.domain.MessageType;
import com.happy.friendogly.member.domain.Member;
import java.time.LocalDateTime;

public record ChatMessageResponse(
        MessageType messageType,
        Long senderMemberId,
        String senderName,
        String content,
        LocalDateTime createdAt,
        String profilePictureUrl
) {

    public ChatMessageResponse(MessageType messageType, String content, Member senderMember, LocalDateTime createdAt) {
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
