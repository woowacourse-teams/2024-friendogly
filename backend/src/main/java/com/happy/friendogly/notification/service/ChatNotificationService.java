package com.happy.friendogly.notification.service;

import static com.happy.friendogly.notification.domain.NotificationType.CHAT;

import com.happy.friendogly.chatsocket.dto.response.ChatMessageSocketResponse;
import com.happy.friendogly.club.domain.Club;
import com.happy.friendogly.notification.repository.DeviceTokenRepository;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class ChatNotificationService {

    private final DeviceTokenRepository deviceTokenRepository;
    private final NotificationService notificationService;

    public ChatNotificationService(
            DeviceTokenRepository deviceTokenRepository,
            NotificationService notificationService
    ) {
        this.deviceTokenRepository = deviceTokenRepository;
        this.notificationService = notificationService;
    }

    public void sendChatNotification(Long chatRoomId, ChatMessageSocketResponse response, Club club) {
        List<String> receiverTokens = deviceTokenRepository
                .findAllByChatRoomIdWithoutMine(chatRoomId, response.senderMemberId());

        Map<String, String> data = Map.of(
                "chatRoomId", chatRoomId.toString(),
                "messageType", response.messageType().toString(),
                "senderMemberId", response.senderMemberId().toString(),
                "senderName", response.senderName(),
                "content", response.content(),
                "createdAt", response.createdAt().toString(),
                "profilePictureUrl", response.profilePictureUrl(),
                "clubPictureUrl", club.getImageUrl(),
                "clubTitle", club.getTitle().getValue()
        );

        notificationService.sendNotification("채팅", data, CHAT, receiverTokens);
    }
}
